package khairy.com.jakegithub.repository;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import khairy.com.jakegithub.database.dao.GithubDao;
import khairy.com.jakegithub.database.entity.GithubModel;
import khairy.com.jakegithub.network.JakeApi;
import khairy.com.jakegithub.resource.Resource;
import khairy.com.jakegithub.util.ApiResponse;
import khairy.com.jakegithub.util.AppExecutors;
import khairy.com.jakegithub.util.NetworkBoundResource;

public class Repository {

    private JakeApi jakeApi;
    private GithubDao githubDao;
    private Application application;

    @Inject
    public Repository(JakeApi jakeApi, GithubDao githubDao, Application app) {
        this.jakeApi = jakeApi;
        this.githubDao = githubDao;
        this.application = app;

    }


    public LiveData<Resource<List<GithubModel>>> getData(final int page, final Boolean refresh) {
        return new NetworkBoundResource<List<GithubModel>, List<GithubModel>>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull List<GithubModel> item) {
                GithubModel[] arr = new GithubModel[item.size()];
                item.toArray(arr);
                githubDao.insertAll(arr);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<GithubModel> data) {

                if (isNetworkAvailable()) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected boolean shouldRefresh() {
                return refresh;
            }

            @NonNull
            @Override
            protected void refreshDb() {
                githubDao.deleteAll();
            }

            @NonNull
            @Override
            protected LiveData<List<GithubModel>> loadFromDb() {
                return githubDao.getAllRepo();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<GithubModel>>> createCall() {
                return jakeApi.getRepo(page, 15);
            }
        }.getAsLiveData();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
