package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.TestRecordNewModule;
import com.dy.colony.mvp.contract.TestRecordNewContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.TestRecordNewActivity;;

@ActivityScope
@Component(modules = TestRecordNewModule.class, dependencies = AppComponent.class)
public interface TestRecordNewComponent {
    void inject(TestRecordNewActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TestRecordNewComponent.Builder view(TestRecordNewContract.View view);

        TestRecordNewComponent.Builder appComponent(AppComponent appComponent);

        TestRecordNewComponent build();
    }
}