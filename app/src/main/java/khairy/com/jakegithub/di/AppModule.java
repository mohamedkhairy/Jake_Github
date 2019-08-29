package khairy.com.jakegithub.di;


import android.app.Application;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import khairy.com.jakegithub.database.dao.GithubDao;
import khairy.com.jakegithub.database.GithubDatabase;
import khairy.com.jakegithub.util.Constants;
import khairy.com.jakegithub.util.LiveDataCallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static khairy.com.jakegithub.util.Constants.CONNECTION_TIMEOUT;
import static khairy.com.jakegithub.util.Constants.READ_TIMEOUT;
import static khairy.com.jakegithub.util.Constants.WRITE_TIMEOUT;

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
    static OkHttpClient okHttpClientProvides() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }


    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}
