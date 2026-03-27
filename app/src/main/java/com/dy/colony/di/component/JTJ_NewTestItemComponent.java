package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.JTJ_NewTestItemModule;
import com.dy.colony.mvp.contract.JTJ_NewTestItemContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.JTJ_NewTestItemActivity;;

@ActivityScope
@Component(modules = JTJ_NewTestItemModule.class, dependencies = AppComponent.class)
public interface JTJ_NewTestItemComponent {
    void inject(JTJ_NewTestItemActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        JTJ_NewTestItemComponent.Builder view(JTJ_NewTestItemContract.View view);

        JTJ_NewTestItemComponent.Builder appComponent(AppComponent appComponent);

        JTJ_NewTestItemComponent build();
    }
}