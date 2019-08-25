package khairy.com.jakegithub.di.fragment;

import dagger.Module;
import dagger.Provides;
import khairy.com.jakegithub.network.JakeApi;
import retrofit2.Retrofit;

@Module
public class JakeApiModule {

    @Provides
    static JakeApi provideApi(Retrofit retrofit) {
        return retrofit.create(JakeApi.class);
    }
}
