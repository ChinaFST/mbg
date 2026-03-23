package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.FGGDAdjustingModule;
import com.dy.colony.mvp.contract.FGGDAdjustingContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.FGGDAdjustingActivity;;

@ActivityScope
@Component(modules = FGGDAdjustingModule.class, dependencies = AppComponent.class)
public interface FGGDAdjustingComponent {
    void inject(FGGDAdjustingActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FGGDAdjustingComponent.Builder view(FGGDAdjustingContract.View view);

        FGGDAdjustingComponent.Builder appComponent(AppComponent appComponent);

        FGGDAdjustingComponent build();
    }
}