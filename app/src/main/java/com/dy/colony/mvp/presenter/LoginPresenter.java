package com.dy.colony.mvp.presenter;

import android.app.Application;
import android.content.Intent;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;
import com.dy.colony.R;
import com.dy.colony.app.utils.MethodUtil;
import com.dy.colony.app.utils.SPUtils;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.User;
import com.dy.colony.greendao.daos.UserDao;
import com.dy.colony.mvp.model.api.service.Platform_Service;
import com.dy.colony.mvp.model.entity.ObjUserData;
import com.dy.colony.mvp.model.entity.Platform_LoginBack;
import com.dy.colony.mvp.ui.activity.HomeActivity;
import com.google.gson.Gson;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.LoginContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void login(String username, String password) {
        mModel.login(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<Integer>(mErrorHandler) {
                    /**
                     * @param integer 0成功 1失败，用户名不存在 2密码不正确
                     */
                    @Override
                    public void onNext(Integer integer) {
                        switch (integer) {
                            case 0:
                                List<User> list = DBHelper.getUserDao(mApplication).queryBuilder().where(UserDao.Properties.Username.eq(username))
                                        .where(UserDao.Properties.Password.eq(password)).list();


                                mRootView.loginSuccess(mApplication.getString(R.string.hint_loginsuccess), list.get(0));

                                break;
                            case 1:
                                mRootView.loginFail(mApplication.getString(R.string.hint_loginfail1));
                                break;
                            case 2:
                                mRootView.loginFail(mApplication.getString(R.string.hint_loginfail2));

                                break;
                            case 3:
                                break;
                        }
                    }

                });


    }

    public void login_platform(String username, String password, boolean remember) {
        LogUtils.d(remember);
        RetrofitUrlManager.getInstance().putDomain("xxx", Platform_Service.URL);
        mModel.login_Platform(username, MethodUtil.MD5(MethodUtil.MD5(password)), Constants.DEVICENUM, BuildConfig.VERSION_NAME, Constants.LATITUDE + "," + Constants.LONTITUDE, "")
                .retryWhen(new RetryWithDelay(2, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                // .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<Platform_LoginBack>(mErrorHandler) {
                    @Override
                    public void onNext(@NonNull Platform_LoginBack loginBack) {
                        if (loginBack.getResultCode().equals(Platform_Service.SUCCESS)) {
                            ObjUserData objUserData = loginBack.getObj();
                            if (remember) {
                                SPUtils.put(mRootView.getActivity(), Constants.KEY_USERINFOR_JSON, new Gson().toJson(objUserData));
                            } else {
                                SPUtils.remove(mRootView.getActivity(), Constants.KEY_USERINFOR_JSON);
                            }
                            Constants.ISREMBERUSERNAME = remember;
                            SPUtils.put(mRootView.getActivity(), Constants.KEY_REMBERUSERNAME, Constants.ISREMBERUSERNAME);
                            Constants.USER_PLATFORM = objUserData;
                            Constants.IS_OFFLINE_MODE = false;
                            mRootView.launchActivity(new Intent(mRootView.getActivity(), HomeActivity.class));
                            mRootView.killMyself();
                        } else {
                            ArmsUtils.snackbarText(loginBack.getMsg());
                        }

                    }
                });
    }
}