package khairy.com.jakegithub.repository;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import khairy.com.jakegithub.database.dao.GithubDao;
import khairy.com.jakegithub.database.entity.GithubModel;
import khairy.com.jakegithub.network.JakeApi;
import khairy.com.jakegithub.resource.ApiResource;

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

    public LiveData<ApiResource<List<GithubModel>>> getData(int page, Boolean onError) {
        if (isNetworkAvailable() && !onError) {
            return getGithubData(page);
        } else {
            return getDataLocal(page);
        }
    }

    private LiveData<ApiResource<List<GithubModel>>> getGithubData(int page) {


        return LiveDataReactiveStreams.fromPublisher(
                jakeApi.getRepo(page, 15)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map((List<GithubModel> data) -> ApiResource.success(data, false))
                        .onErrorReturn(msg -> ApiResource.error(msg.getMessage(), null))
                        .doAfterNext(listApiResource -> {
                            if (listApiResource.data != null) {
                                githubDao.insertAll(listApiResource.data);
                            }
                        })
                        .subscribeOn(Schedulers.io()));

    }


    private LiveData<ApiResource<List<GithubModel>>> getDataLocal(int page) {

        return LiveDataReactiveStreams.fromPublisher(
                githubDao.getAllRepo()
                        .observeOn(AndroidSchedulers.mainThread())
                        .map((List<GithubModel> data) -> ApiResource.success(data, true))
                        .onErrorReturn(msg -> ApiResource.error(msg.getMessage(), null))
                        .subscribeOn(Schedulers.io()));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
