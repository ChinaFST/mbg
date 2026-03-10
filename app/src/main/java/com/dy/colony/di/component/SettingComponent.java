package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.SettingModule;
import com.dy.colony.mvp.contract.SettingContract;

import com.jess.arms.di.scope.FragmentScope;
import com.dy.colony.mvp.ui.fragment.SettingFragment;;

@FragmentScope
@Component(modules = SettingModule.class, dependencies = AppComponent.class)
public interface SettingComponent {
    void inject(SettingFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SettingComponent.Builder view(SettingContract.View view);

        SettingComponent.Builder appComponent(AppComponent appComponent);

        SettingComponent build();
    }
}