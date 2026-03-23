package com.dy.colony.mvp.model;

import android.app.Application;

import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.greendao.daos.Detection_Record_FGGD_NCDao;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.TestRecordNewContract;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

@ActivityScope
public class TestRecordNewModel extends BaseModel implements TestRecordNewContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public TestRecordNewModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<Detection_Record_FGGD_NC>> getDetection_Record_FGGD_NC_LoadMore(int lastpage, int pagenum) {

        return Observable.create(new ObservableOnSubscribe<List<Detection_Record_FGGD_NC>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Detection_Record_FGGD_NC>> emitter) throws Exception {
                QueryBuilder<Detection_Record_FGGD_NC> builder = DBHelper.getDetection_Record_FGGD_NCDao(MyAppLocation.myAppLocation).queryBuilder();
                // LogUtils.d("subscribe");

                List<Detection_Record_FGGD_NC> list = builder.orderDesc(Detection_Record_FGGD_NCDao.Properties.Testingtime).offset(lastpage * pagenum).limit(pagenum).list();
                emitter.onNext(list);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<List<Detection_Record_FGGD_NC>> getDetection_Record_FGGD_NC_Seach(QueryBuilder<Detection_Record_FGGD_NC> builder, int lastpage, int pagenum) {

        return Observable.create(new ObservableOnSubscribe<List<Detection_Record_FGGD_NC>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Detection_Record_FGGD_NC>> emitter) throws Exception {
                QueryBuilder<Detection_Record_FGGD_NC> where;

                where = builder;

                // LogUtils.d("subscribe");
                List<Detection_Record_FGGD_NC> list = where.offset(lastpage * pagenum).limit(pagenum).orderDesc(Detection_Record_FGGD_NCDao.Properties.Testingtime).list();

                emitter.onNext(list);
                emitter.onComplete();
            }
        });
    }
}