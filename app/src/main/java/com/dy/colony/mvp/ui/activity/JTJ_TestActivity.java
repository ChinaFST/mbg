package com.dy.colony.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.app.utils.DiaLogUtils;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.model.entity.base.BaseUntilMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.model.entity.eventbus.ExternTestMessageBean;
import com.dy.colony.mvp.model.entity.eventbus.JTJTestMessageBean;
import com.dy.colony.mvp.ui.widget.BaseJTJTestView;
import com.dy.colony.mvp.ui.widget.MyJTJ_TestView_External;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.dy.colony.di.component.DaggerJTJ_TestComponent;
import com.dy.colony.mvp.contract.JTJ_TestContract;
import com.dy.colony.mvp.presenter.JTJ_TestPresenter;

import com.dy.colony.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.jess.arms.utils.Preconditions.checkNotNull;

public class JTJ_TestActivity extends BaseActivity<JTJ_TestPresenter> implements JTJ_TestContract.View {
    @Inject
    List<GalleryBean> mList;
    @Inject
    AlertDialog mSportDialog;
    @Inject
    UsbManager mUsbManager;
    @Inject
    Dialog mDialog;
    @Inject
    View mView;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.group_chart)
    LinearLayout mGroupChart;
    @BindView(R.id.message_gallnum)
    TextView mMessageGallnum;
    @BindView(R.id.choseproject)
    Button mChoseproject;
    @BindView(R.id.samplename_btn)
    Button mSamplenameBtn;
    @BindView(R.id.samplename)
    AutoCompleteTextView mSamplename;
    @BindView(R.id.samplename_layout)
    TextInputLayout mSamplenameLayout;
    @BindView(R.id.samplenum)
    AutoCompleteTextView mSamplenum;
    @BindView(R.id.samplenum_layout)
    TextInputLayout mSamplenumLayout;
    @BindView(R.id.samplenum_btn)
    Button mSamplenumBtn;
    @BindView(R.id.unit_btn)
    Button mUnitBtn;
    @BindView(R.id.btn_change_sampleplace)
    Button mBtnChangeSampleplace;
    @BindView(R.id.message_method)
    AutoCompleteTextView mMessageMethod;
    @BindView(R.id.message_standnum)
    AutoCompleteTextView mMessageStandnum;
    @BindView(R.id.message_limitvalue)
    AutoCompleteTextView mMessageLimitvalue;
    @BindView(R.id.message_result)
    AutoCompleteTextView mMessageResult;
    @BindView(R.id.message_jujement)
    AutoCompleteTextView mMessageJujement;
    @BindView(R.id.message_testtime)
    AutoCompleteTextView mMessageTesttime;
    @BindView(R.id.message_testsite)
    AutoCompleteTextView mMessageTestsite;
    @BindView(R.id.message_testpeople)
    AutoCompleteTextView mMessageTestpeople;
    @BindView(R.id.start_test)
    FloatingActionButton mStartTest;

    private List<BaseJTJTestView> mJTJTestViews = new ArrayList<>();
    private GalleryBean nowShowBean;
    private int mIndex;

    private int type;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerJTJ_TestComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_jtj_test; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    private boolean isShow = false;

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (getIntent() != null) {
            type = getIntent().getIntExtra(Constants.JUMP_PARAM_TYPE, 0);
        }

        initGallery();
        setLinstenter();
        initLongClickListener();
        initOnTouchListener();
        mToolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isShow) {
                    ArmsUtils.snackbarText(getString(R.string.hideresult));
                    isShow = false;
                } else {
                    ArmsUtils.snackbarText(getString(R.string.showresult));
                    isShow = true;
                }
                for (int i = 0; i < mJTJTestViews.size(); i++) {
                    mJTJTestViews.get(i).showTestResultNumber(isShow);
                }
                return true;
            }
        });
    }

    private void initGallery() {
        mGroupChart.removeAllViews();
        mJTJTestViews.clear();
        MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.clear();

        for (int i = 0; i < 2; i++) {
            GalleryBean e = new Detection_Record_FGGD_NC();
            e.setGalleryNum(i + 1);
            e.setTestMoudle(2 + "");
            e.setJTJModel(1);
            e.setJTJCardModel(0);
            if (type == 1) {
                ((Detection_Record_FGGD_NC) e).setTest_project(getString(R.string.pork_testing));
            }
            MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.add(e);
            MyJTJ_TestView_External p = new MyJTJ_TestView_External(this, i); //新建通道，将baan与该自定义view绑定
            mJTJTestViews.add(p);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            mGroupChart.addView(p, layoutParams);//添加到视图容器
            LogUtils.d(e);
            LogUtils.d(mGroupChart.getChildCount());
        }

        EventBus.getDefault().post(new ExternTestMessageBean(0, mIndex));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EventBus.getDefault().post(new ExternTestMessageBean(0, mIndex));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ExternTestMessageBean bean) {
        LogUtils.d(bean);
        if (bean.tag == 0) {
            if (bean.index != -1) {
                mIndex = bean.index;
            }
            LogUtils.d(mIndex);
            nowShowBean = MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(mIndex);
            initMessage(nowShowBean);
        }

    }

    @SuppressLint("SetTextI18n")
    private void initMessage(GalleryBean nowShowBean) {
        mMessageGallnum.setText(getString(R.string.gallery) + nowShowBean.getJTJ_MAC() + getString(R.string.detail_msg));
        Detection_Record_FGGD_NC bean = (Detection_Record_FGGD_NC) nowShowBean;

        mChoseproject.setText(bean.getTest_project());
        mChoseproject.setHint(getString(R.string.testproject));
        mSamplenameBtn.setText(bean.getSamplename());
        mSamplenumBtn.setText(bean.getSerialNumber());
        mUnitBtn.setText(bean.getProsecutedunits());
        mBtnChangeSampleplace.setText(bean.getSampleplace());
        mMessageMethod.setText(bean.getTest_method());
        mMessageStandnum.setText(bean.getStand_num());
        mMessageLimitvalue.setText(bean.getSymbol() + bean.getCov() + bean.getCov_unit());
        mMessageResult.setText(bean.getTestresult());
        mMessageJujement.setText(bean.getDecisionoutcome());
        mMessageTesttime.setText(bean.getdfTestingtimeyy_mm_dd_hh_mm_ss());
        mMessageTestsite.setText(bean.getTestsite());
        mMessageTestpeople.setText(bean.getInspector());
    }


    private void setLinstenter() {
        mSamplename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && mSamplenameLayout.getVisibility() == VISIBLE) {
                    ((Detection_Record_FGGD_NC) MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(mIndex)).setSamplenameByself(s.toString());
                }

            }
        });
        mSamplenum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && mSamplenumLayout.getVisibility() == VISIBLE) {
                    ((Detection_Record_FGGD_NC) MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(mIndex)).setSerialNumber(s.toString());
                }

            }
        });

    }


    private void initLongClickListener() {
        mSamplenameBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (nowShowBean.getState() == 1) {
                    ArmsUtils.snackbarText(getString(R.string.testing));
                    return true;
                }
                if (null == nowShowBean.getProjectMessage()) {
                    ArmsUtils.snackbarText(getString(R.string.choseprojectFirst));
                    return true;
                }
                nowShowBean.removeSampleMessage();
                mSamplenameBtn.setVisibility(GONE);
                mSamplenameLayout.setVisibility(VISIBLE);
                return true;
            }
        });

        mSamplename.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (nowShowBean.getState() == 1) {
                    ArmsUtils.snackbarText(getString(R.string.testing));
                    return true;
                }
                mSamplenameLayout.setVisibility(GONE);
                mSamplenameBtn.setVisibility(VISIBLE);
                return true;
            }
        });

    }

    private void initOnTouchListener() {
        mBtnChangeSampleplace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (null == nowShowBean) {
                        return true;
                    }
                    mPresenter.makeChoseSampleplace(mIndex);
                }
                return true;
            }
        });


    }


    @OnClick({R.id.choseproject, R.id.samplename_btn, R.id.samplenum_btn, R.id.unit_btn, R.id.start_test})
    public void onClick(View view) {
        int state;
        Intent intent;
        switch (view.getId()) {
            case R.id.choseproject:
                //选择检测项目
                state = nowShowBean.getState();
                if (state == 1) {
                    ArmsUtils.snackbarText(getString(R.string.testing_wait));
                    return;
                }

                intent = new Intent(getActivity(), ChoseProjectActivity.class);
                intent.putExtra("from", "jtj_1");
                intent.putExtra("index", mIndex);
                startActivity(intent);

                break;
            case R.id.samplename_btn:
                //选择样品名称
                state = nowShowBean.getState();
                if (state == 1) {
                    ArmsUtils.snackbarText(getString(R.string.testing_wait));
                    return;
                }
                if (null == nowShowBean.getProjectMessage()) {
                    ArmsUtils.snackbarText(getString(R.string.place_choseproject));
                    return;
                }
                intent = new Intent(getActivity(), ChoseSampleActivity.class);
                intent.putExtra("from", "jtj");
                intent.putExtra("index", mIndex);
                startActivity(intent);

                break;
            case R.id.samplenum_btn:

                break;
            case R.id.unit_btn:
                //选择被检单位
                state = nowShowBean.getState();
                if (state == 1) {
                    ArmsUtils.snackbarText(getString(R.string.testing_wait));
                    return;
                }
                intent = new Intent(getActivity(), ChoseUnitActivity.class);
                intent.putExtra("from", "jtj");
                intent.putExtra("index", mIndex);
                startActivity(intent);
                break;

            case R.id.start_test:
                mPresenter.makeChoseSeachMethodDialog(mJTJTestViews);
                break;


        }
    }

    @Override
    public void showSportDialog(String message) {
        mSportDialog.setMessage(message);
        if (!mSportDialog.isShowing()) {
            mSportDialog.show();
        }

    }

    @Override
    public void hideSportDialog() {
        if (mSportDialog.isShowing()) {
            mSportDialog.dismiss();
        }
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
}