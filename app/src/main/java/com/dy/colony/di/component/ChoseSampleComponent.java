package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.ChoseSampleModule;
import com.dy.colony.mvp.contract.ChoseSampleContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.ChoseSampleActivity;;

@ActivityScope
@Component(modules = ChoseSampleModule.class, dependencies = AppComponent.class)
public interface ChoseSampleComponent {
    void inject(ChoseSampleActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChoseSampleComponent.Builder view(ChoseSampleContract.View view);

        ChoseSampleComponent.Builder appComponent(AppComponent appComponent);

        ChoseSampleComponent build();
    }
}