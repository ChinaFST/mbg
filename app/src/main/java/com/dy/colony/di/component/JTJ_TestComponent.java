package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.JTJ_TestModule;
import com.dy.colony.mvp.contract.JTJ_TestContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.JTJ_TestActivity;;

@ActivityScope
@Component(modules = JTJ_TestModule.class, dependencies = AppComponent.class)
public interface JTJ_TestComponent {
    void inject(JTJ_TestActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        JTJ_TestComponent.Builder view(JTJ_TestContract.View view);

        JTJ_TestComponent.Builder appComponent(AppComponent appComponent);

        JTJ_TestComponent build();
    }
}