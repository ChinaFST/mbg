package com.dy.colony.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dy.colony.Constants;
import com.dy.colony.app.utils.SPUtils;
import com.dy.colony.greendao.beans.User;
import com.google.gson.Gson;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.dy.colony.di.component.DaggerSplashComponent;
import com.dy.colony.mvp.contract.SplashContract;
import com.dy.colony.mvp.presenter.SplashPresenter;

import com.dy.colony.R;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {
    private Handler mHandler = new Handler();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSplashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_splash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Constants.ISREMBERUSERNAME) {
                    String userJson = (String) SPUtils.get(SplashActivity.this, Constants.KEY_USERINFOR_JSON, new Gson().toJson(new User()));
                    User user = new Gson().fromJson(userJson, User.class);
                    if (null != user) {
                        mPresenter.login(user.getUsername(), user.getPassword());
                    } else {
                        killMyself();
                        launchActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                } else {
                    killMyself();
                    launchActivity(new Intent(SplashActivity.this, LoginActivity.class));

                }
            }
        }, 100);

    }

    @Override
    public void loginSuccess(String s, User user) {
        Constants.NOWUSER = user;
        launchActivity(new Intent(getActivity(), HomeActivity.class));
        killMyself();
    }

    @Override
    public void loginFail(String s) {
        killMyself();
        launchActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


}