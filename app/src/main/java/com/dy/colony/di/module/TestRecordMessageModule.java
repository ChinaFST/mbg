package com.dy.colony.di.module;

import android.app.AlertDialog;

import androidx.fragment.app.FragmentActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dmax.dialog.SpotsDialog;

import com.dy.colony.mvp.contract.TestRecordMessageContract;
import com.dy.colony.mvp.model.TestRecordMessageModel;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

@Module
public abstract class TestRecordMessageModule {

    @Binds
    abstract TestRecordMessageContract.Model bindTestRecordMessageModel(TestRecordMessageModel model);

    @ActivityScope
    @Provides
    static RxPermissions providesrxpermissions(TestRecordMessageContract.View view){
        return new RxPermissions((FragmentActivity) view.getActivity());
    }
    @ActivityScope
    @Provides
    static AlertDialog getSportDialog(TestRecordMessageContract.View view) {
        return new SpotsDialog.Builder().setContext(view.getActivity()).setCancelable(true).build();
    }
}