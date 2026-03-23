package com.dy.colony.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;

import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.di.component.DaggerFGGDAdjustingComponent;
import com.dy.colony.mvp.contract.FGGDAdjustingContract;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.model.entity.eventbus.FGTestMessageBean;
import com.dy.colony.mvp.presenter.FGGDAdjustingPresenter;
import com.dy.colony.mvp.ui.adapter.FGGDAdjustAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class FGGDAdjustingActivity extends BaseActivity<FGGDAdjustingPresenter> implements FGGDAdjustingContract.View {
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.ll)
    LinearLayout mLl;
    @BindView(R.id.ad_value)
    RecyclerView mAdValue;
    @BindView(R.id.correct_1)
    Button mCorrect1;
    @BindView(R.id.correct_2)
    Button mCorrect2;
    @BindView(R.id.btn_clean)
    Button mBtnClean;
    @Inject
    List<GalleryBean> mList;
    @Inject
    FGGDAdjustAdapter mFGGDAdjustAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFGGDAdjustingComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_fggdadjusting; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        super.onStart();
        MyAppLocation.myAppLocation.mSerialDataService.startFGGDSendthread();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        super.onPause();
        MyAppLocation.myAppLocation.mSerialDataService.stopsFGGDSendthread();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FGTestMessageBean tags) {
        if (tags.tag == 0) {
            mFGGDAdjustAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mCorrect1.setVisibility(View.GONE);
        mCorrect2.setText(R.string.str_adjust);
        initRecycleview();

    }

    private void initRecycleview() {
        ArmsUtils.configRecyclerView(mAdValue, mLayoutManager);
        mAdValue.setAdapter(mFGGDAdjustAdapter);
    }

    @OnClick({R.id.correct_1, R.id.correct_2, R.id.btn_clean})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.correct_1:
                if (MyAppLocation.myAppLocation.mSerialDataService.RUNFLAG_FGGD_HASSTART) {
                    ArmsUtils.snackbarText(getString(R.string.adjust_message1));
                    return;
                }

                MyAppLocation.myAppLocation.mSerialDataService.mData_SerialControl.send(Constants.SPECTRAL_AD_CALIBRATION_REQUEST_1); //AD值校准请求 1

                break;
            case R.id.correct_2:
                if (MyAppLocation.myAppLocation.mSerialDataService.RUNFLAG_FGGD_HASSTART) {
                    ArmsUtils.snackbarText(getString(R.string.adjust_message1));
                    return;
                }

                MyAppLocation.myAppLocation.mSerialDataService.mData_SerialControl.send(Constants.SPECTRAL_AD_CALIBRATION_REQUEST_2); //AD值校准请求 1

                break;
            case R.id.btn_clean:
                //清零
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        for (int i = 0; i < mList.size(); i++) {
                            GalleryBean bean = mList.get(i);
                            int state = bean.getState();
                            if (state != 1) {
                                bean.setClearn(true);
                            }
                        }
                        ArmsUtils.snackbarText(getString(R.string.cleansuccess));
                    }
                }, 0);
                break;
        }
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

    @Override
    public Activity getActivity() {
        return this;
    }
}