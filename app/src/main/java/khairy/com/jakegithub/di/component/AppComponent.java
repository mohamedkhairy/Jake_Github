package khairy.com.jakegithub.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import khairy.com.jakegithub.BaseApplication;
import khairy.com.jakegithub.di.ActivityBuilderModule;
import khairy.com.jakegithub.di.AppModule;
import khairy.com.jakegithub.di.ViewModelFactoryModule;
import khairy.com.jakegithub.di.fragment.JakeApiModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivityBuilderModule.class,
        AppModule.class,
        ViewModelFactoryModule.class,
        JakeApiModule.class
})
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
