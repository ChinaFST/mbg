package com.dy.colony.di.module;

import dagger.Binds;
import dagger.Module;

import com.dy.colony.mvp.contract.CountContract;
import com.dy.colony.mvp.model.CountModel;

@Module
public abstract class CountModule {

    @Binds
    abstract CountContract.Model bindCountModel(CountModel model);
}