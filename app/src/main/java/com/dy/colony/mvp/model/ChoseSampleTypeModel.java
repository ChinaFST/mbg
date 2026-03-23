package com.dy.colony.mvp.model;

import android.app.Application;

import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Simple33;
import com.dy.colony.greendao.daos.Simple33Dao;
import com.dy.colony.mvp.model.entity.base.BaseSimple33Message;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.ChoseSampleTypeContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

@ActivityScope
public class ChoseSampleTypeModel extends BaseModel implements ChoseSampleTypeContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ChoseSampleTypeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    String s = "";

    @Override
    public String checkPcode(BaseSimple33Message next) {
        if (next instanceof Simple33) {
            String code = ((Simple33) next).getFoodPCode();
            if ("00001".equals(code)) {
                return "食品种类->" + s;
            } else {
                List<Simple33> list = DBHelper.getSimple33Dao(MyAppLocation.myAppLocation).queryBuilder().where(Simple33Dao.Properties.FoodCode.eq(code)).list();
                Simple33 simple33 = list.get(0);
                String name = simple33.getFoodName() + "->";
                s = name + s;
                checkPcode(simple33);
            }

        }

        return s;
    }

    private String getfoodname(BaseSimple33Message baseSimple33Message) {
        String name = null;

        if (baseSimple33Message instanceof Simple33) {
            Simple33 message = (Simple33) baseSimple33Message;
            name = message.getFoodName();
        }
        return name;
    }

    @Override
    public Observable<List<? extends BaseSimple33Message>> load() {

        return Observable.create(new ObservableOnSubscribe<List<? extends BaseSimple33Message>>() {
            @Override
            public void subscribe(ObservableEmitter<List<? extends BaseSimple33Message>> emitter) throws Exception {
                // LogUtils.d("subscribe");
                List<? extends BaseSimple33Message> baseSimple33MessageList = null;


                baseSimple33MessageList = DBHelper.getSimple33Dao(MyAppLocation.myAppLocation).queryBuilder().where(Simple33Dao.Properties.FoodPCode.eq("00001")).list();


                emitter.onNext(baseSimple33MessageList);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<List<? extends BaseSimple33Message>> checkData(BaseSimple33Message message) {
        return Observable.create(new ObservableOnSubscribe<List<? extends BaseSimple33Message>>() {
            @Override
            public void subscribe(ObservableEmitter<List<? extends BaseSimple33Message>> emitter) throws Exception {
                // LogUtils.d("subscribe");
                List<? extends BaseSimple33Message> where = null;
                if (message instanceof Simple33) {
                    Simple33 down = (Simple33) message;
                    String code = down.getFoodCode() + "";
                    where = DBHelper.getSimple33Dao(MyAppLocation.myAppLocation).queryBuilder().where(Simple33Dao.Properties.FoodPCode.eq(code)).list();

                }
                emitter.onNext(where);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<List<? extends BaseSimple33Message>> checkDatabackleve(BaseSimple33Message message) {
        return Observable.create(new ObservableOnSubscribe<List<? extends BaseSimple33Message>>() {
            @Override
            public void subscribe(ObservableEmitter<List<? extends BaseSimple33Message>> emitter) throws Exception {
                // LogUtils.d("subscribe");
                List<? extends BaseSimple33Message> where = new ArrayList<>();
                if (message instanceof Simple33) {

                    Simple33 down = (Simple33) message;
                    String code = down.getFoodPCode() + "";
                    if (!"00001".equals(code)) {
                        List<Simple33> list = DBHelper.getSimple33Dao(MyAppLocation.myAppLocation).queryBuilder().where(Simple33Dao.Properties.FoodCode.eq(code)).list();
                        String code1 = list.get(0).getFoodPCode();
                        where = DBHelper.getSimple33Dao(MyAppLocation.myAppLocation).queryBuilder().where(Simple33Dao.Properties.FoodPCode.eq(code1)).list();
                    }

                }
                emitter.onNext(where);
                emitter.onComplete();
            }
        });
    }

    @Override
    public void tonull() {
        s = "";
    }
}