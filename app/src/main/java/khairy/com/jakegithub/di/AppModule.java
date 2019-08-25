package khairy.com.jakegithub.di;


import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import khairy.com.jakegithub.database.dao.GithubDao;
import khairy.com.jakegithub.database.GithubDatabase;
import khairy.com.jakegithub.network.JakeApi;
import khairy.com.jakegithub.repository.Repository;
import khairy.com.jakegithub.util.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {


    @Singleton
    @Provides
    static GithubDatabase githubDatabaseInstance(Application application) {
        return GithubDatabase.getInstance(application);
    }

    @Singleton
    @Provides
    GithubDao githubDaoProvides(GithubDatabase githubDatabase) {
        return githubDatabase.githubDao();
    }


    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Singleton
    @Provides
    static Repository provideRepository(JakeApi jakeApi, GithubDao githubDao, Application application) {
        return new Repository(jakeApi, githubDao, application);
    }

}
