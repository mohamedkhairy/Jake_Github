package khairy.com.jakegithub.di.fragment;


import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import khairy.com.jakegithub.di.key.ViewModelKey;
import khairy.com.jakegithub.fragment.JakeGithubViewModel;

@Module
public abstract class FragmentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(JakeGithubViewModel.class)
    public abstract ViewModel bindFragmentViewModel(JakeGithubViewModel viewModel);
}
