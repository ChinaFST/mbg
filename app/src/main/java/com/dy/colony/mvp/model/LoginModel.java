package com.dy.colony.mvp.model;

import android.app.Application;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;
import com.dy.colony.app.utils.MD5Utils_kjc;
import com.dy.colony.app.utils.MethodUtil;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.User;
import com.dy.colony.greendao.daos.UserDao;
import com.dy.colony.mvp.model.api.service.Platform_Service;
import com.dy.colony.mvp.model.entity.Platform_LoginBack;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.LoginContract;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.Query;

@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    /**
     * @param username
     * @param password
     * @return 0成功 1失败，用户名不存在 2密码不正确
     */
    @Override
    public Observable<Integer> login(String username, String password) {

        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                UserDao userDao = DBHelper.getUserDao(mApplication);
                List<User> users = userDao.loadAll();
                LogUtils.d("获取到用户数量：" + users.size());
                if (users.size() == 0) {
                    User user = new User();
                    user.setUsername("admin");
                    user.setPassword("123456");
                    userDao.insertOrReplace(user);
                }
                WhereCondition equals = UserDao.Properties.Username.eq(username);
                List<User> list = userDao.queryBuilder().where(equals).list();
                if (list.size() == 1) {
                    User user = list.get(0);
                    if (user.getPassword().equals(password)) {
                        emitter.onNext(0);
                    } else {
                        emitter.onNext(2);
                    }
                } else if (list.size() == 0) {
                    emitter.onNext(1);
                }
            }
        })
                //指定了被观察者执行的线程环境
                .subscribeOn(Schedulers.io())
                //将接下来执行的线程环境指定为io线程
                .observeOn(Schedulers.io());

    }

    @Override
    public Observable<Platform_LoginBack> login_Platform(String username, String password, String devicecode, String softWareVersion, String place, String ip) {
        return mRepositoryManager.obtainRetrofitService(Platform_Service.class)
                .login(username, password, devicecode, softWareVersion, place, ip)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }


}