package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.FGGD_TestModule;
import com.dy.colony.mvp.contract.FGGD_TestContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.FGGD_TestActivity;;

@ActivityScope
@Component(modules = FGGD_TestModule.class, dependencies = AppComponent.class)
public interface FGGD_TestComponent {
    void inject(FGGD_TestActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FGGD_TestComponent.Builder view(FGGD_TestContract.View view);

        FGGD_TestComponent.Builder appComponent(AppComponent appComponent);

        FGGD_TestComponent build();
    }
}