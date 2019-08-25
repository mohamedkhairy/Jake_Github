package khairy.com.jakegithub.di;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import khairy.com.jakegithub.MainActivity;
import khairy.com.jakegithub.di.fragment.FragmentViewModelModule;
import khairy.com.jakegithub.di.fragment.JakeFragmentModule;

@Module
public abstract class ActivityBuilderModule {


    @ContributesAndroidInjector(modules = {JakeFragmentModule.class, FragmentViewModelModule.class})
    abstract MainActivity contributesMainActivity();
}
