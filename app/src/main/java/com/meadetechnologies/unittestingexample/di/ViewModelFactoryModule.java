package com.meadetechnologies.unittestingexample.di;

import androidx.lifecycle.ViewModelProvider;

import com.meadetechnologies.unittestingexample.viewmodels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);
}
