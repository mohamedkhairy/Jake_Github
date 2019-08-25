package khairy.com.jakegithub.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import khairy.com.jakegithub.database.entity.GithubModel;
import khairy.com.jakegithub.repository.Repository;
import khairy.com.jakegithub.resource.ApiResource;

public class JakeGithubViewModel extends ViewModel {


    private MediatorLiveData<ApiResource<List<GithubModel>>> githubList = new MediatorLiveData<>();
    private Repository repository;

    @Inject
    public JakeGithubViewModel(Repository repository) {
        this.repository = repository;
    }


    public LiveData<ApiResource<List<GithubModel>>> observeUser() {
        return githubList;
    }


    public void getJakeRepo(int page, Boolean onError) {

        githubList.setValue(ApiResource.loading(null));

        LiveData<ApiResource<List<GithubModel>>> model = repository.getData(page, onError);

        githubList.addSource(model, githubModels -> {
            githubList.setValue(githubModels);
            githubList.removeSource(model);

        });


    }

}
