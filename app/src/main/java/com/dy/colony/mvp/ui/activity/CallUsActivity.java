package com.dy.colony.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dy.colony.R;
import com.dy.colony.di.component.DaggerCallUsComponent;
import com.dy.colony.mvp.contract.CallUsContract;
import com.dy.colony.mvp.presenter.CallUsPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class CallUsActivity extends BaseActivity<CallUsPresenter> implements CallUsContract.View {
    @BindView(R.id.weichart)
    RelativeLayout mWeichart;
    @BindView(R.id.qq)
    RelativeLayout mQq;
    @BindView(R.id.emeail)
    RelativeLayout mEmeail;
    @BindView(R.id.phone)
    RelativeLayout mPhone;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCallUsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_callus; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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

    @OnClick({R.id.weichart, R.id.qq, R.id.emeail, R.id.phone})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.weichart:
                break;
            case R.id.qq:
                try {
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                    startActivity(intent);
                } catch (Exception e) {
                    ArmsUtils.snackbarText(getString(R.string.check_QQ_installed));
                }
                break;
            case R.id.emeail:
                break;
            case R.id.phone:
                break;
        }
    }
}