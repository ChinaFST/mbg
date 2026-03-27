package com.dy.colony.di.module;

import dagger.Binds;
import dagger.Module;

import com.dy.colony.mvp.contract.FGGD_NewTestItemContract;
import com.dy.colony.mvp.model.FGGD_NewTestItemModel;

@Module
public abstract class FGGD_NewTestItemModule {

    @Binds
    abstract FGGD_NewTestItemContract.Model bindFGGD_NewTestItemModel(FGGD_NewTestItemModel model);
}