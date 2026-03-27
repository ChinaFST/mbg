package com.dy.colony.di.module;

import dagger.Binds;
import dagger.Module;

import com.dy.colony.mvp.contract.JTJ_NewTestItemContract;
import com.dy.colony.mvp.model.JTJ_NewTestItemModel;

@Module
public abstract class JTJ_NewTestItemModule {

    @Binds
    abstract JTJ_NewTestItemContract.Model bindJTJ_NewTestItemModel(JTJ_NewTestItemModel model);
}