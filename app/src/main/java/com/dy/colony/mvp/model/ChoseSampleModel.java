package com.dy.colony.mvp.model;

import android.app.Application;

import com.dy.colony.greendao.daos.FoodItemAndStandardDao;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.ChoseSampleContract;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

@ActivityScope
public class ChoseSampleModel extends BaseModel implements ChoseSampleContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ChoseSampleModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }


    @Override
    public Observable<List<? extends BaseSampleMessage>> getSample(int lastpage, int pagenum, QueryBuilder<? extends BaseSampleMessage> builder, String from) {
        return Observable.create(new ObservableOnSubscribe<List<? extends BaseSampleMessage>>() {
            @Override
            public void subscribe(ObservableEmitter<List<? extends BaseSampleMessage>> emitter) throws Exception {
                List<? extends BaseSampleMessage> list = builder.offset(lastpage * pagenum).limit(pagenum).orderDesc(FoodItemAndStandardDao.Properties.Priority).list();
                emitter.onNext(list);
                emitter.onComplete();
            }
        });
    }

}