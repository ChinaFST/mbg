package com.dy.colony.mvp.model;

import android.app.Application;

import com.dy.colony.MyAppLocation;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.daos.FoodItemAndStandardDao;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.JTJ_TestContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

@ActivityScope
public class JTJ_TestModel extends BaseModel implements JTJ_TestContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public JTJ_TestModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<? extends BaseSampleMessage>> loadStandard(String project) {
        return Observable.create(new ObservableOnSubscribe<List<? extends BaseSampleMessage>>() {
            @Override
            public void subscribe(ObservableEmitter<List<? extends BaseSampleMessage>> emitter) throws Exception {
                emitter.onNext(DBHelper.getFoodItemAndStandardDao(MyAppLocation.myAppLocation).queryBuilder()
                        .where(FoodItemAndStandardDao.Properties.ItemName.eq(project)).list());
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }
}