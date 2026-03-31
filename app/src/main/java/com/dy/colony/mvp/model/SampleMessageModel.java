package com.dy.colony.mvp.model;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.greendao.daos.FoodItemAndStandardDao;
import com.dy.colony.mvp.model.api.service.YQWLW_Service;
import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.model.entity.UpdateFileMessage;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.SampleMessageContract;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

@ActivityScope
public class SampleMessageModel extends BaseModel implements SampleMessageContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public SampleMessageModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<? extends BaseSampleMessage>> getFoodItemAndStandard(int lastpage, int pagenum, QueryBuilder<? extends BaseSampleMessage> builder) {
        return Observable.create(new ObservableOnSubscribe<List<? extends BaseSampleMessage>>() {
            @Override
            public void subscribe(ObservableEmitter<List<? extends BaseSampleMessage>> emitter) throws Exception {
                List<? extends BaseSampleMessage> list = builder.offset(lastpage * pagenum).limit(pagenum).orderDesc(FoodItemAndStandardDao.Properties.Id).orderDesc(FoodItemAndStandardDao.Properties.UDate).list();
                emitter.onNext(list);
                emitter.onComplete();
            }

            /*@Override
            public void subscribe(ObservableEmitter<List<FoodItemAndStandard_KJFW>> emitter) throws Exception {

            }*/
        });
    }

    @Override
    public Observable<List<OutMoudle>> getJXLs() {
        return Observable.create(new ObservableOnSubscribe<List<OutMoudle>>() {
            @Override
            public void subscribe(ObservableEmitter<List<OutMoudle>> emitter) throws Exception {
                List<OutMoudle> list = new ArrayList<>();
                List<FoodItemAndStandard> s = DBHelper.getFoodItemAndStandardDao(MyAppLocation.myAppLocation).loadAll();
                OutMoudle<String> jxlTitle = new FoodItemAndStandard().toJxlTitle();
                list.add(jxlTitle);
                for (int i = 0; i < s.size(); i++) {
                    FoodItemAndStandard simple33 = s.get(i);
                    OutMoudle<String> jxlString = simple33.toJxlString();
                    LogUtils.d("lyl==" + jxlString.getKey());
                    list.add(jxlString);
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