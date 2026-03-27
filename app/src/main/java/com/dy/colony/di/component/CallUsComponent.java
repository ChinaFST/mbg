package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.CallUsModule;
import com.dy.colony.mvp.contract.CallUsContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.CallUsActivity;;

@ActivityScope
@Component(modules = CallUsModule.class, dependencies = AppComponent.class)
public interface CallUsComponent {
    void inject(CallUsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CallUsComponent.Builder view(CallUsContract.View view);

        CallUsComponent.Builder appComponent(AppComponent appComponent);

        CallUsComponent build();
    }
}