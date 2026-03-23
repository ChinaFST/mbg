package com.dy.colony.di.module;

import dagger.Binds;
import dagger.Module;

import com.dy.colony.mvp.contract.EdtorSampleContract;
import com.dy.colony.mvp.model.EdtorSampleModel;

@Module
public abstract class EdtorSampleModule {

    @Binds
    abstract EdtorSampleContract.Model bindEdtorSampleModel(EdtorSampleModel model);
}