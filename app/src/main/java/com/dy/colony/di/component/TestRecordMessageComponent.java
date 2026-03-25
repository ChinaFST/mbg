package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.TestRecordMessageModule;
import com.dy.colony.mvp.contract.TestRecordMessageContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.TestRecordMessageActivity;;

@ActivityScope
@Component(modules = TestRecordMessageModule.class, dependencies = AppComponent.class)
public interface TestRecordMessageComponent {
    void inject(TestRecordMessageActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TestRecordMessageComponent.Builder view(TestRecordMessageContract.View view);

        TestRecordMessageComponent.Builder appComponent(AppComponent appComponent);

        TestRecordMessageComponent build();
    }
}