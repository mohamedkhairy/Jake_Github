package khairy.com.jakegithub.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import khairy.com.jakegithub.database.entity.GithubModel;
import khairy.com.jakegithub.repository.Repository;
import khairy.com.jakegithub.resource.Resource;


public class JakeGithubViewModel extends ViewModel {


    private MediatorLiveData<Resource<List<GithubModel>>> githubList = new MediatorLiveData<>();
    private Repository repository;

    @Inject
    public JakeGithubViewModel(Repository repository) {
        this.repository = repository;
    }


    public LiveData<Resource<List<GithubModel>>> observeUser() {
        return githubList;
    }

    public void getJakeRepo(int page, Boolean refresh) {


        LiveData<Resource<List<GithubModel>>> model = repository.getData(page, refresh);

        githubList.addSource(model, githubModels -> {
            githubList.setValue(githubModels);
            switch (githubModels.status) {
                case SUCCESS: {
                    githubList.removeSource(model);
                    break;
                }
                case ERROR: {
                    githubList.removeSource(model);
                    break;
                }
            }

        });


    }

}
