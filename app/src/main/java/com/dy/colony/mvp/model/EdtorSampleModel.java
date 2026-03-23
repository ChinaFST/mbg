package com.dy.colony.mvp.model;

import android.app.Application;

import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.greendao.daos.FGGDTestItemDao;
import com.dy.colony.greendao.daos.FoodItemAndStandardDao;
import com.dy.colony.greendao.daos.JTJTestItemDao;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.EdtorSampleContract;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@ActivityScope
public class EdtorSampleModel extends BaseModel implements EdtorSampleContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public EdtorSampleModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<String>> loadLocaProject(String keyword) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {


                TreeSet<String> set = new TreeSet<String>();
                QueryBuilder<JTJTestItem> jtjTestItemQueryBuilder = DBHelper.getJTJTestItemDao(MyAppLocation.myAppLocation).queryBuilder();
                if (!keyword.isEmpty()) {
                    jtjTestItemQueryBuilder = jtjTestItemQueryBuilder.where(JTJTestItemDao.Properties.ProjectName.like("%" + keyword + "%"));
                }
                List<JTJTestItem> items = jtjTestItemQueryBuilder.list();
                for (int i = 0; i < items.size(); i++) {
                    JTJTestItem e = items.get(i);
                    set.add(e.getProjectName());
                }
                QueryBuilder<FGGDTestItem> fggdTestItemQueryBuilder = DBHelper.getFGGDTestItemDao(MyAppLocation.myAppLocation).queryBuilder();
                if (!keyword.isEmpty()) {
                    fggdTestItemQueryBuilder = fggdTestItemQueryBuilder.where(FGGDTestItemDao.Properties.Project_name.like("%" + keyword + "%"));
                }
                List<FGGDTestItem> value = fggdTestItemQueryBuilder.list();
                for (int i = 0; i < value.size(); i++) {
                    FGGDTestItem e = value.get(i);
                    set.add(e.getProject_name());
                }
                emitter.onNext(new ArrayList<>(set));


                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    private List<String> fuzzySearch(List<String> originalList, String query) {
        List<String> results = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            return results; // 如果查询为空，返回空列表
        }

        // 遍历原始列表进行模糊搜索
        for (String item : originalList) {
            if (item.toLowerCase().contains(query.toLowerCase())) {
                results.add(item); // 如果匹配，添加到结果列表
            }
        }

        return results; // 返回搜索结果
    }

    @Override
    public Observable<List<FoodItemAndStandard>> loadLocaStandNumber(String projectname) {

        return Observable.create(new ObservableOnSubscribe<List<FoodItemAndStandard>>() {
            @Override
            public void subscribe(ObservableEmitter<List<FoodItemAndStandard>> emitter) throws Exception {
                List<FoodItemAndStandard> list = DBHelper.getFoodItemAndStandardDao(mApplication).queryBuilder().where(FoodItemAndStandardDao.Properties.ItemName.eq(projectname)).list();
                Map<String, FoodItemAndStandard> map = new HashMap();
                for (int i = 0; i < list.size(); i++) {
                    FoodItemAndStandard e = list.get(i);
                    map.put(e.getStandardAndLimitalues(), e);
                }
                Collection<FoodItemAndStandard> values = map.values();
                emitter.onNext(new ArrayList<>(values));
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}