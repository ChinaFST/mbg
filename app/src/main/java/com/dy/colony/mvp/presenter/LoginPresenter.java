package com.dy.colony.mvp.presenter;

import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.R;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.User;
import com.dy.colony.greendao.daos.UserDao;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.LoginContract;

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
}