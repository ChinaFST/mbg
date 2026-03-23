package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.EdtorSampleModule;
import com.dy.colony.mvp.contract.EdtorSampleContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.EdtorSampleActivity;;

@ActivityScope
@Component(modules = EdtorSampleModule.class, dependencies = AppComponent.class)
public interface EdtorSampleComponent {
    void inject(EdtorSampleActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        EdtorSampleComponent.Builder view(EdtorSampleContract.View view);

        EdtorSampleComponent.Builder appComponent(AppComponent appComponent);

        EdtorSampleComponent build();
    }
}