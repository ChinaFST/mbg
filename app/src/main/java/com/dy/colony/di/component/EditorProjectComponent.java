package com.dy.colony.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.dy.colony.di.module.EditorProjectModule;
import com.dy.colony.mvp.contract.EditorProjectContract;

import com.jess.arms.di.scope.ActivityScope;
import com.dy.colony.mvp.ui.activity.EditorProjectActivity;;

@ActivityScope
@Component(modules = EditorProjectModule.class, dependencies = AppComponent.class)
public interface EditorProjectComponent {
    void inject(EditorProjectActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        EditorProjectComponent.Builder view(EditorProjectContract.View view);

        EditorProjectComponent.Builder appComponent(AppComponent appComponent);

        EditorProjectComponent build();
    }
}