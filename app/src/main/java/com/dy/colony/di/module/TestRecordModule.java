package com.dy.colony.di.module;

import dagger.Binds;
import dagger.Module;

import com.dy.colony.mvp.contract.TestRecordContract;
import com.dy.colony.mvp.model.TestRecordModel;

@Module
public abstract class TestRecordModule {

    @Binds
    abstract TestRecordContract.Model bindTestRecordModel(TestRecordModel model);
}