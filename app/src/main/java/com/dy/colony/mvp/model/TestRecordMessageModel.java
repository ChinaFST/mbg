package com.dy.colony.mvp.model;

import android.app.Application;

import com.dy.colony.app.utils.FileUtils;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.TestRecordMessageContract;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

@ActivityScope
public class TestRecordMessageModel extends BaseModel implements TestRecordMessageContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public TestRecordMessageModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<File>> getFiles(String path) {
        return Observable.just(getfileslist(path)).subscribeOn(Schedulers.io());
    }

    public List<File> getfileslist(String path) {
        return FileUtils.listFilesInDir(path);
    }
}