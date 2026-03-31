package com.dy.colony.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.di.component.DaggerFGGD_TestComponent;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.mvp.contract.FGGD_TestContract;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.model.entity.eventbus.FGTestMessageBean;
import com.dy.colony.mvp.presenter.FGGD_TestPresenter;
import com.dy.colony.mvp.ui.adapter.FGGDAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class FGGD_TestActivity extends BaseActivity<FGGD_TestPresenter> implements FGGD_TestContract.View {
    @Inject
    List<GalleryBean> mFGGDGalleryBeanList;
    @Inject
    FGGDAdapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.yzlf_contro_value)
    TextView mYzlfControValue;
    @BindView(R.id.bzqxf_contro_value)
    TextView mBzqxfControValue;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.contro_value_show)
    TextView mControValueShow;
    @BindView(R.id.contro_value_parent)
    LinearLayout mControValueParent;
    @BindView(R.id.message_gallnum)
    TextView mMessageGallnum;
    @BindView(R.id.btn_change_sampleplace)
    AutoCompleteTextView mBtnChangeSampleplace;


    @BindView(R.id.message_project)
    AutoCompleteTextView mMessageProject;
    @BindView(R.id.message_method)
    AutoCompleteTextView mMessageMethod;
    @BindView(R.id.message_di_dro)
    AutoCompleteTextView mMessageDiDro;
    @BindView(R.id.btn_change_di_dro)
    Button mBtnChangeDiDro;
    @BindView(R.id.message_di_dro_parent)
    LinearLayout mMessageDiDroParent;
    @BindView(R.id.message_searinumber)
    AutoCompleteTextView mMessageSearinumber;
    @BindView(R.id.message_samplename)
    AutoCompleteTextView mMessageSamplename;
    @BindView(R.id.message_standnum)
    AutoCompleteTextView mMessageStandnum;
    @BindView(R.id.message_limitvalue)
    AutoCompleteTextView mMessageLimitvalue;
    @BindView(R.id.message_result)
    AutoCompleteTextView mMessageResult;
    @BindView(R.id.message_jujement)
    AutoCompleteTextView mMessageJujement;
    @BindView(R.id.message_beuntils)
    AutoCompleteTextView mMessageBeuntils;
    @BindView(R.id.message_testtime)
    AutoCompleteTextView mMessageTesttime;
    @BindView(R.id.message_testsite)
    AutoCompleteTextView mMessageTestsite;
    @BindView(R.id.message_testpeople)
    AutoCompleteTextView mMessageTestpeople;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.btn_clear)
    Button mBtnClear;
    @BindView(R.id.btn_sampletest)
    Button mBtnSampletest;
    @BindView(R.id.btn_controtest)
    Button mBtnControtest;


    @BindView(R.id.btn_adjust)
    Button mBtnAdjust;
    @BindView(R.id.btn_query)
    Button mBtnQuery;
    @BindView(R.id.linearLayout5)
    LinearLayout mLinearLayout5;

    private Detection_Record_FGGD_NC nowShowbean;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FGTestMessageBean tags) {
        LogUtils.d(tags);
        Intent intent = new Intent();
        int index = tags.index;
        GalleryBean bean = mFGGDGalleryBeanList.get(index);
        switch (tags.tag) { //1 选择检测项目 2选择样品 3选择被检单位 4选择检测任务 5显示通道详细信息
            case 0:
                if (null != nowShowbean) {
                    GalleryBean bean1 = mFGGDGalleryBeanList.get(nowShowbean.getGalleryNum() - 1);
                    setDetailMessage((Detection_Record_FGGD_NC) bean1);
                } else { //默认选择第一通道，显示第一通道信息
                    nowShowbean = (Detection_Record_FGGD_NC) bean;
                    setDetailMessage(nowShowbean);
                    LogUtils.d(nowShowbean);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case 1:
                intent.setClass(getActivity(), ChoseProjectActivity.class);
                intent.putExtra("from", "fggd");
                intent.putExtra("index", bean.getGalleryNum());
                launchActivity(intent);
                break;
            case 2:
                if (null == bean.getProjectMessage()) {
                    ArmsUtils.snackbarText(getString(R.string.choseprojectFirst));
                    return;
                }
                intent.setClass(getActivity(), ChoseSampleActivity.class);
                intent.putExtra("from", "fggd");
                intent.putExtra("index", bean.getGalleryNum());
                launchActivity(intent);
                break;
            case 3:
                /*intent.setClass(getActivity(), ChoseUnitActivity.class);
                intent.putExtra("from", "fggd");
                intent.putExtra("index", bean.getGalleryNum());
                launchActivity(intent);*/
                break;

            case 5:
                nowShowbean = (Detection_Record_FGGD_NC) bean;
                setDetailMessage(nowShowbean);
                LogUtils.d(nowShowbean);
                break;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFGGD_TestComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_fggd_test; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecyclerView();

        if (mFGGDGalleryBeanList.size() != 0) {
            setDetailMessage((Detection_Record_FGGD_NC) mFGGDGalleryBeanList.get(0));
        } else {
            setDetailMessage(null);
        }
        mBtnClear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPresenter.longCleanValue();
                return false;
            }
        });

        initClickListener();
        initOnTouchListener();
        //mock();
    }

    private void mock() {
        mFGGDGalleryBeanList.clear();
        for (int i = 0; i < 2; i++) {
            GalleryBean e = new Detection_Record_FGGD_NC();
            e.setGalleryNum(i + 1);
            e.setTestMoudle(1 + "");
            FGGDTestItem baseProjectMessage = new FGGDTestItem();
            baseProjectMessage.setProject_name("农药残留");
            e.setProjectMessage(baseProjectMessage);
            FoodItemAndStandard sampleMessage = new FoodItemAndStandard();
            sampleMessage.setSampleName("紫蜜桃");
            e.setSampleMessage(sampleMessage);
            ((Detection_Record_FGGD_NC) e).setTestresult("1.52g/ml");
            ((Detection_Record_FGGD_NC) e).setWave1(256);
            ((Detection_Record_FGGD_NC) e).setWave2(256);
            ((Detection_Record_FGGD_NC) e).setWave3(256);
            ((Detection_Record_FGGD_NC) e).setWave4(256);
            ((Detection_Record_FGGD_NC) e).setWave1_start(100);
            ((Detection_Record_FGGD_NC) e).setWave2_start(100);
            ((Detection_Record_FGGD_NC) e).setWave3_start(100);
            ((Detection_Record_FGGD_NC) e).setWave4_start(100);
            ((Detection_Record_FGGD_NC) e).setLuminousness1(200);
            ((Detection_Record_FGGD_NC) e).setLuminousness2(200);
            ((Detection_Record_FGGD_NC) e).setLuminousness3(200);
            ((Detection_Record_FGGD_NC) e).setLuminousness4(200);
            ((Detection_Record_FGGD_NC) e).setAbsorbance1(1);
            ((Detection_Record_FGGD_NC) e).setAbsorbance2(1);
            ((Detection_Record_FGGD_NC) e).setAbsorbance3(1);
            ((Detection_Record_FGGD_NC) e).setAbsorbance4(1);
            e.setRemainingtime(-1);
            mFGGDGalleryBeanList.add(e);
        }

    }

    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mAdapter.setEmptyView(R.layout.recycle_empty_layout, (ViewGroup) mRecyclerView.getParent());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initClickListener() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.bt_chose_units) {
                    Detection_Record_FGGD_NC item = (Detection_Record_FGGD_NC) mAdapter.getData().get(position);
                    if (item.getState() == 1) {
                        ArmsUtils.snackbarText(getString(R.string.testinng));
                        return;
                    }

                    showEditUnitsDialog(position, item.getProsecutedunits());
                }
            }
        });
    }

    private void showEditUnitsDialog(int position, String currentContent) {
        // 1. 创建一个容器，用来给输入框加边距（Margin），直接放 EditText 会贴边很难看
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dimen_20dp);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dimen_20dp);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);

        final EditText editText = new EditText(this);
        editText.setLayoutParams(params);
        editText.setText(currentContent);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        editText.setHint(getString(R.string.enter_inspect_unit));
        editText.setSingleLine(true);
        // 自动把光标移到末尾
        if (currentContent != null) {
            editText.setSelection(currentContent.length());
        }

        container.addView(editText);

        // 2. 构建对话框
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.enter_inspect_unit)
                .setView(container)
                .setPositiveButton(R.string.sure, (dialogInterface, i) -> {
                    String inputText = editText.getText().toString().trim();
                    Detection_Record_FGGD_NC galleryBean = (Detection_Record_FGGD_NC) mAdapter.getData().get(position);
                    galleryBean.setProsecutedunits(inputText);
                    mAdapter.notifyItemChanged(position);
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        editText.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                // 3. 显式调用 showSoftInput，并传入强制显示的标志
                // 注意：第一个参数必须是当前获得焦点的 View
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 160);
        dialog.show();
    }

    private void initOnTouchListener() {
        mBtnChangeSampleplace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (null == nowShowbean) {
                        return true;
                    }
                    mPresenter.makeChoseSampleplace(nowShowbean);
                }
                return true;
            }
        });

    }


    @OnClick({R.id.btn_adjust, R.id.btn_clear, R.id.btn_sampletest, R.id.btn_controtest,  R.id.btn_query, R.id.btn_change_di_dro})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_adjust:
                startActivity(new Intent(FGGD_TestActivity.this, FGGDAdjustingActivity.class));
                break;
            case R.id.btn_clear:
                mPresenter.cleanValue();
                break;

            case R.id.btn_sampletest:
                mPresenter.sampleTest();
                break;
            case R.id.btn_controtest:
                mPresenter.controlTest();
                break;

            case R.id.btn_query:
                startActivity(new Intent(getActivity(), TestRecordNewActivity.class));
                break;
            case R.id.btn_change_di_dro:
                mPresenter.makeChoseDi_Dro(nowShowbean);
                break;

        }
    }

    @SuppressLint({"StringFormatMatches", "SetTextI18n"})
    public void setDetailMessage(Detection_Record_FGGD_NC fggd_nc) {
        if (fggd_nc == null) {
            mMessageGallnum.setText(R.string.titledetailnomore);
            mMessageProject.setText("");
            mMessageMethod.setText("");
            mMessageSearinumber.setText("");
            mMessageSamplename.setText("");
            mMessageStandnum.setText("");
            mMessageLimitvalue.setText("");
            mMessageResult.setText("");
            mMessageJujement.setText("");
            mMessageBeuntils.setText("");
            mMessageTesttime.setText("");
            mMessageTestsite.setText("");
            mMessageTestpeople.setText("");

        } else {
            if (fggd_nc.getDowhat() == 2) {
                mControValueParent.setVisibility(View.GONE);
                mScrollView.setVisibility(View.GONE);
                BaseProjectMessage message = fggd_nc.getProjectMessage();
                if (null != message) {
                    String method = message.getMethod();
                    //LogUtils.d(method);
                    if ("0".equals(method) || "1".equals(method)) {
                        mControValueShow.setText(getString(R.string.controlvalue) + message.getControValue());
                    }
                }

                return;
            }
            mControValueParent.setVisibility(View.GONE);
            mScrollView.setVisibility(View.GONE);
            BaseProjectMessage message = fggd_nc.getProjectMessage();
            String method1 = fggd_nc.getTest_method();

            if (null != message) {
                String method = message.getMethod();
                //LogUtils.d(method);
                mMessageMethod.setText(method1);
                if ("0".equals(method)) {
                    mMessageMethod.setText(method1 + getString(R.string.controlvalue) + message.getControValue());

                } else if ("1".equals(method)) {   //标准曲线法 稀释倍数
                    mMessageMethod.setText(method1 + getString(R.string.controlvalue) + message.getControValue());

                    mMessageDiDroParent.setVisibility(View.VISIBLE);
                    mMessageDiDro.setText(fggd_nc.getDilutionratio() + "");
                } else if ("3".equals(method)) { //系数法 反应液滴数
                    mMessageDiDroParent.setVisibility(View.VISIBLE);
                    mMessageDiDro.setText(fggd_nc.getEveryresponse() + "");
                } else {
                    mMessageDiDroParent.setVisibility(View.GONE);
                }
            } else {
                mMessageMethod.setText(method1);
                mMessageDiDroParent.setVisibility(View.GONE);
            }

            int num = fggd_nc.getGalleryNum();
            mMessageGallnum.setText(String.format(getString(R.string.channeldetails), num));

            mMessageProject.setText(fggd_nc.getTest_project());
            mMessageSearinumber.setText(fggd_nc.getSerialNumber());
            mMessageSamplename.setText(fggd_nc.getSamplename());
            mBtnChangeSampleplace.setText(fggd_nc.getSampleplace());

            mMessageStandnum.setText(fggd_nc.getStand_num());
            mMessageLimitvalue.setText(fggd_nc.getSymbol() + fggd_nc.getCov() + fggd_nc.getCov_unit());
            mMessageResult.setText(fggd_nc.getTestresult());
            mMessageJujement.setText(fggd_nc.getDecisionoutcome());
            mMessageBeuntils.setText(fggd_nc.getProsecutedunits());
            mMessageTesttime.setText(fggd_nc.getdfTestingtimeyy_mm_dd_hh_mm_ss());
            mMessageTestsite.setText(fggd_nc.getTestsite());
            mMessageTestpeople.setText(fggd_nc.getInspector());


        }

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
        LogUtils.d("分光暂停");
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