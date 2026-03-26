package com.dy.colony.di.module;

import dagger.Binds;
import dagger.Module;

import com.dy.colony.mvp.contract.EditorProjectContract;
import com.dy.colony.mvp.model.EditorProjectModel;

@Module
public abstract class EditorProjectModule {

    @Binds
    abstract EditorProjectContract.Model bindEditorProjectModel(EditorProjectModel model);
}