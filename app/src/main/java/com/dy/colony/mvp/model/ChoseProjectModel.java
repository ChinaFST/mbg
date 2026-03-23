package com.dy.colony.mvp.model;

import android.app.Application;

import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.greendao.daos.FGGDTestItemDao;
import com.dy.colony.greendao.daos.JTJTestItemDao;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.ChoseProjectContract;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@ActivityScope
public class ChoseProjectModel extends BaseModel implements ChoseProjectContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;
    @Inject
    JTJTestItemDao mJTJTestItemDao;
    @Inject
    FGGDTestItemDao mFGGDTestItemDao;

    @Inject
    public ChoseProjectModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
        this.mJTJTestItemDao = null;
        this.mFGGDTestItemDao = null;
    }

    @Override
    public Observable<List<? extends BaseProjectMessage>> loadJTJitem(int i, String... keyowrd) {

        return Observable.create(new ObservableOnSubscribe<List<? extends BaseProjectMessage>>() {
            @Override
            public void subscribe(ObservableEmitter<List<? extends BaseProjectMessage>> emitter) throws Exception {
                QueryBuilder<JTJTestItem> builder = mJTJTestItemDao.queryBuilder();
                if (keyowrd.length > 0) {
                    builder = builder.where(JTJTestItemDao.Properties.ProjectName.like("%" + keyowrd[0] + "%"));
                }
                emitter.onNext(builder.where(JTJTestItemDao.Properties.Item_type.eq(String.valueOf(i)))
                        .orderDesc(JTJTestItemDao.Properties.Priority).list());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<? extends BaseProjectMessage>> loadFGGDitem(int i, String... keyowrd) {

        return Observable.create(new ObservableOnSubscribe<List<? extends BaseProjectMessage>>() {
            @Override
            public void subscribe(ObservableEmitter<List<? extends BaseProjectMessage>> emitter) throws Exception {
                QueryBuilder<FGGDTestItem> builder = mFGGDTestItemDao.queryBuilder();
                if (keyowrd.length > 0) {
                    builder = builder.where(FGGDTestItemDao.Properties.Project_name.like("%" + keyowrd[0] + "%"));
                }

                List<FGGDTestItem> list = builder.orderDesc(FGGDTestItemDao.Properties.Priority).list();
                emitter.onNext(list);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());

    }
}