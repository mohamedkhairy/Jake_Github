package khairy.com.jakegithub.di.fragment;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import khairy.com.jakegithub.fragment.JakeGithubFragment;

@Module
public abstract class JakeFragmentModule {

    @ContributesAndroidInjector
    abstract JakeGithubFragment contributesJakeFragment();
}
