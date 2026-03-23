package com.dy.colony.mvp.model;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.MyAppLocation;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Company_Point;
import com.dy.colony.greendao.beans.Company_Point_Unit;
import com.dy.colony.greendao.daos.Company_PointDao;
import com.dy.colony.greendao.daos.Company_Point_UnitDao;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.ChoseUnitContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

@ActivityScope
public class ChoseUnitModel extends BaseModel implements ChoseUnitContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ChoseUnitModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<Company_Point>> loadMore(int page, int page_num) {
        return Observable.create(new ObservableOnSubscribe<List<Company_Point>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Company_Point>> emitter) throws Exception {
                List<Company_Point> res = new ArrayList<>();
                List<Company_Point> downs = DBHelper.getCompany_PointDao(MyAppLocation.myAppLocation)
                        .queryBuilder().offset(page * page_num).limit(page_num)
                        .orderDesc(Company_PointDao.Properties.Priority).list();
                for (int i = 0; i < downs.size(); i++) {
                    Company_Point down = downs.get(i);
                    List<Company_Point_Unit> list = DBHelper.getCompany_Point_UnitDao(MyAppLocation.myAppLocation).queryBuilder()
                            .where(Company_Point_UnitDao.Properties.RegId.like(down.getRegId())).
                                    orderDesc(Company_Point_UnitDao.Properties.Priority).list();
                    down.setSubItems(list);
                    res.add(down);
                }
                emitter.onNext(res);
                emitter.onComplete();
            }
        });
    }

    @Override
    public Observable<List<Company_Point>> seachData(int page, int page_num, String keyword) {
        return Observable.create(new ObservableOnSubscribe<List<Company_Point>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Company_Point>> emitter) throws Exception {
                List<Company_Point> res = new ArrayList<>();
                //先找被检单位有没有包含关键字 有则加载到list
                List<Company_Point> downs = DBHelper.getCompany_PointDao(MyAppLocation.myAppLocation).queryBuilder()
                        .whereOr(Company_PointDao.Properties.ContactMan.like("%" + keyword + "%")
                                , Company_PointDao.Properties.ContactPhone.like("%" + keyword + "%")
                                , Company_PointDao.Properties.RegName.like("%" + keyword + "%")
                                , Company_PointDao.Properties.RegAddress.like("%" + keyword + "%")
                                , Company_PointDao.Properties.RegCorpName.like("%" + keyword + "%")
                                , Company_PointDao.Properties.ContactMan.like("%" + keyword + "%")
                        )
                        .offset(page * page_num).limit(page_num)
                        .list();
                for (int i = 0; i < downs.size(); i++) {
                    Company_Point down = downs.get(i);
                    List<Company_Point_Unit> list = DBHelper.getCompany_Point_UnitDao(MyAppLocation.myAppLocation).queryBuilder().where(Company_Point_UnitDao.Properties.RegId.like(down.getRegId())).list();
                    down.setSubItems(list);
                    res.add(down);
                }

                /*List<Company_Point_Unit> down = DBHelper.getCompany_Point_UnitDao(MyAppLocation.myAppLocation).queryBuilder()
                        .whereOr(Company_Point_UnitDao.Properties.ContactMan.like("%"+keyword+"%")
                                ,Company_Point_UnitDao.Properties.ContactPhone.like("%"+keyword+"%")
                                ,Company_Point_UnitDao.Properties.RegName.like("%"+keyword+"%")
                                ,Company_Point_UnitDao.Properties.RegAddress.like("%"+keyword+"%")
                                ,Company_Point_UnitDao.Properties.RegCorpName.like("%"+keyword+"%")
                                ,Company_Point_UnitDao.Properties.ContactMan.like("%"+keyword+"%")
                        ).list();

                for (int i = 0; i < down.size(); i++) {
                    String id = down.get(i).getRegId();
                    List<Company_Point> where = DBHelper.getCompany_PointDao(MyAppLocation.myAppLocation).queryBuilder().where(Company_PointDao.Properties.RegId.like(id)).list();
                    if (where.size()==0){
                        continue;
                    }
                    Company_Point point = where.get(0);
                    List<Company_Point_Unit> list = DBHelper.getCompany_Point_UnitDao(MyAppLocation.myAppLocation).queryBuilder().where(Company_Point_UnitDao.Properties.RegId.like(id)).list();
                    point.setSubItems(list);

                    boolean contains =constans(res,id);
                    if (!contains){
                        res.add(point);
                    }
                }*/
                //找经营户有没有包含关键字 有则找到该经营户所属的被检单位并加入list
                LogUtils.d(res);
                emitter.onNext(res);
                emitter.onComplete();
            }
        });
    }
}