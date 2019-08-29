package khairy.com.jakegithub.util;


import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import khairy.com.jakegithub.resource.Resource;


// CacheObject: Type for the Resource data. (database cache)
// RequestObject: Type for the API response. (network request)
public abstract class NetworkBoundResource<CacheObject, RequestObject> {


    private AppExecutors appExecutors;
    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();


    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }

    @MainThread
    private void init() {


        // update LiveData for loading status
        results.setValue(Resource.<CacheObject>loading(null, true));

        // observe LiveData source from local db
        final LiveData<CacheObject> dbSource = loadFromDb();

        results.addSource(dbSource, cacheObject -> {
            results.removeSource(dbSource);

            if (shouldFetch(cacheObject)) {
                // get data from the network
                fetchFromNetwork(dbSource);
            } else {
                results.addSource(dbSource, cacheObject1 ->
                        setValue(Resource.success(cacheObject1, false, true)));
            }
        });


    }

    /**
     * 1) observe local db
     * 2) if <condition/> query the network
     * 3) stop observing the local db
     * 4) insert new data into local db
     * 5) begin observing local db again to see the refreshed data from network
     *
     * @param dbSource
     */
    private void fetchFromNetwork(final LiveData<CacheObject> dbSource) {

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();


        // update LiveData for loading status
        results.addSource(dbSource, cacheObject ->
                setValue(Resource.loading(cacheObject, true)));


        results.addSource(apiResponse, requestObjectApiResponse -> {
            results.removeSource(dbSource);
            results.removeSource(apiResponse);


            /*
                3 cases:
                   1) ApiSuccessResponse
                   2) ApiErrorResponse
                   3) ApiEmptyResponse
             */

            if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {

                appExecutors.diskIO().execute(() -> {

                    // refresh checking
                    if (shouldRefresh()) {
                        refreshDb();
                    }
                    // save the response to the local db
                    saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse) requestObjectApiResponse));

                    appExecutors.mainThread().execute(() ->
                            results.addSource(loadFromDb(), cacheObject ->
                                    setValue(Resource.success(cacheObject, true, false))));
                });
            }

            else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {
                appExecutors.mainThread().execute(() ->
                        results.addSource(loadFromDb(), cacheObject ->
                                setValue(Resource.success(cacheObject, false, true))));
            }

            else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {
                results.addSource(dbSource, cacheObject ->
                        setValue(Resource.error(
                        ((ApiResponse.ApiErrorResponse) requestObjectApiResponse).getErrorMessage(),
                        cacheObject
                        )
                ));
            }
        });
    }

    private CacheObject processResponse(ApiResponse.ApiSuccessResponse response) {
        return (CacheObject) response.getBody();
    }

    private void setValue(Resource<CacheObject> newValue) {
        if (results.getValue() != newValue) {
            results.setValue(newValue);
        }
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    @MainThread
    protected abstract boolean shouldRefresh();

    // Called to Refresh the cached data from the database.
    @NonNull
    @MainThread
    protected abstract void refreshDb();

    // Called to get the cached data from the database.
    @NonNull
    @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData() {
        return results;
    }

    ;
}
