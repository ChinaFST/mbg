package com.dy.colony.di.module;

import dagger.Binds;
import dagger.Module;

import com.dy.colony.mvp.contract.CallUsContract;
import com.dy.colony.mvp.model.CallUsModel;

@Module
public abstract class CallUsModule {

    @Binds
    abstract CallUsContract.Model bindCallUsModel(CallUsModel model);
}