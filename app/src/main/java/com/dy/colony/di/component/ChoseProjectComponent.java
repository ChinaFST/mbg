package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.ChoseProjectModule;
import com.dy.colony.mvp.contract.ChoseProjectContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.ChoseProjectActivity;;

@ActivityScope
@Component(modules = ChoseProjectModule.class, dependencies = AppComponent.class)
public interface ChoseProjectComponent {
    void inject(ChoseProjectActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChoseProjectComponent.Builder view(ChoseProjectContract.View view);

        ChoseProjectComponent.Builder appComponent(AppComponent appComponent);

        ChoseProjectComponent build();
    }
}