package com.dy.colony.mvp.model;

import android.app.Application;

import com.dy.colony.MyAppLocation;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.mvp.model.api.service.YQWLW_Service;
import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.model.entity.UpdateFileMessage;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.EditorProjectContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

@ActivityScope
public class EditorProjectModel extends BaseModel implements EditorProjectContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public EditorProjectModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<OutMoudle>> getFGGDJXLs() {
        return Observable.create(new ObservableOnSubscribe<List<OutMoudle>>() {
            @Override
            public void subscribe(ObservableEmitter<List<OutMoudle>> emitter) throws Exception {
                List<OutMoudle> list = new ArrayList<>();
                List<FGGDTestItem> s = DBHelper.getFGGDTestItemDao(MyAppLocation.myAppLocation).loadAll();
                list.add(new FGGDTestItem().toJxlTitle());
                for (int i = 0; i < s.size(); i++) {
                    FGGDTestItem simple33 = s.get(i);
                    list.add(simple33.toJxlString());
                }
                emitter.onNext(list);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<List<OutMoudle>> getJTJJXLs() {
        return Observable.create(new ObservableOnSubscribe<List<OutMoudle>>() {
            @Override
            public void subscribe(ObservableEmitter<List<OutMoudle>> emitter) throws Exception {
                List<OutMoudle> list = new ArrayList<>();
                List<JTJTestItem> s = DBHelper.getJTJTestItemDao(MyAppLocation.myAppLocation).loadAll();
                list.add(new JTJTestItem().toJxlTitle());
                for (int i = 0; i < s.size(); i++) {
                    JTJTestItem simple33 = s.get(i);
                    list.add(simple33.toJxlString());
                }
                emitter.onNext(list);
                emitter.onComplete();
            }
        });
    }


    @Override
    public Observable<UpdateFileMessage> upgradeFile(String appName) {
        return mRepositoryManager
                .obtainRetrofitService(YQWLW_Service.class)
                .upgradeFile(appName)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }
}