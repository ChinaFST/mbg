package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.CountModule;
import com.dy.colony.mvp.contract.CountContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.CountActivity;;

@ActivityScope
@Component(modules = CountModule.class, dependencies = AppComponent.class)
public interface CountComponent {
    void inject(CountActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CountComponent.Builder view(CountContract.View view);

        CountComponent.Builder appComponent(AppComponent appComponent);

        CountComponent build();
    }
}