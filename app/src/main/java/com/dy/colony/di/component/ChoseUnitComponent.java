package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.ChoseUnitModule;
import com.dy.colony.mvp.contract.ChoseUnitContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.ChoseUnitActivity;;

@ActivityScope
@Component(modules = ChoseUnitModule.class, dependencies = AppComponent.class)
public interface ChoseUnitComponent {
    void inject(ChoseUnitActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ChoseUnitComponent.Builder view(ChoseUnitContract.View view);

        ChoseUnitComponent.Builder appComponent(AppComponent appComponent);

        ChoseUnitComponent build();
    }
}