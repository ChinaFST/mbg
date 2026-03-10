package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.TestRecordModule;
import com.dy.colony.mvp.contract.TestRecordContract;

import com.jess.arms.di.scope.FragmentScope;
import com.dy.colony.mvp.ui.fragment.TestRecordFragment;;

@FragmentScope
@Component(modules = TestRecordModule.class, dependencies = AppComponent.class)
public interface TestRecordComponent {
    void inject(TestRecordFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TestRecordComponent.Builder view(TestRecordContract.View view);

        TestRecordComponent.Builder appComponent(AppComponent appComponent);

        TestRecordComponent build();
    }
}