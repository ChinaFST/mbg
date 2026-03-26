package com.dy.colony.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dy.colony.R;
import com.dy.colony.di.component.DaggerMainComponent;
import com.dy.colony.mvp.contract.MainContract;
import com.dy.colony.mvp.presenter.MainPresenter;
import com.dy.colony.mvp.ui.activity.FGGD_TestActivity;
import com.dy.colony.mvp.ui.activity.JTJ_TestActivity;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;

import butterknife.OnClick;

public class MainFragment extends BaseFragment<MainPresenter> implements MainContract.View {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @OnClick({R.id.card_1, R.id.card_2, R.id.card_3, R.id.card_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_1:
                launchActivity(new Intent(getActivity(), FGGD_TestActivity.class));
                break;
            case R.id.card_2:
                launchActivity(new Intent(getActivity(), FGGD_TestActivity.class));
                break;
            case R.id.card_3:
                launchActivity(new Intent(getActivity(), JTJ_TestActivity.class));
                break;
            case R.id.card_4:
                launchActivity(new Intent(getActivity(), JTJ_TestActivity.class).putExtra("type",1));
                break;
        }
    }
}