package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.ChoseSampleTypeModule;
import com.dy.colony.mvp.contract.ChoseSampleTypeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.ChoseSampleTypeActivity;;

@ActivityScope
@Component(modules = ChoseSampleTypeModule.class, dependencies = AppComponent.class)
public interface ChoseSampleTypeComponent {
    void inject(ChoseSampleTypeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChoseSampleTypeComponent.Builder view(ChoseSampleTypeContract.View view);

        ChoseSampleTypeComponent.Builder appComponent(AppComponent appComponent);

        ChoseSampleTypeComponent build();
    }
}