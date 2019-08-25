package khairy.com.jakegithub.di;

import androidx.lifecycle.ViewModelProvider;


import dagger.Binds;
import dagger.Module;
import khairy.com.jakegithub.viewmodels.ViewModelProviderFactory;


@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelFactory);

}
