package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.SampleMessageModule;
import com.dy.colony.mvp.contract.SampleMessageContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.SampleMessageActivity;;

@ActivityScope
@Component(modules = SampleMessageModule.class, dependencies = AppComponent.class)
public interface SampleMessageComponent {
    void inject(SampleMessageActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SampleMessageComponent.Builder view(SampleMessageContract.View view);

        SampleMessageComponent.Builder appComponent(AppComponent appComponent);

        SampleMessageComponent build();
    }
}