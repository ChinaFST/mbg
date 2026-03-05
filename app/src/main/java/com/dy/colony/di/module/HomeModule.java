package com.dy.colony.di.module;

import dagger.Binds;
import dagger.Module;

import com.dy.colony.mvp.contract.HomeContract;
import com.dy.colony.mvp.model.HomeModel;

@Module
public abstract class HomeModule {

    @Binds
    abstract HomeContract.Model bindHomeModel(HomeModel model);
}