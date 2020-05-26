package com.meadetechnologies.unittestingexample.di;

import android.app.Application;

import com.meadetechnologies.unittestingexample.BaseApplication;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Component(
        modules = {AndroidInjectionModule.class, AppModule.class, ActivityBuildersModule.class, ViewModelFactoryModule.class}
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
