package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.FGGD_NewTestItemModule;
import com.dy.colony.mvp.contract.FGGD_NewTestItemContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.FGGD_NewTestItemActivity;;

@ActivityScope
@Component(modules = FGGD_NewTestItemModule.class, dependencies = AppComponent.class)
public interface FGGD_NewTestItemComponent {
    void inject(FGGD_NewTestItemActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FGGD_NewTestItemComponent.Builder view(FGGD_NewTestItemContract.View view);

        FGGD_NewTestItemComponent.Builder appComponent(AppComponent appComponent);

        FGGD_NewTestItemComponent build();
    }
}