package com.dy.colony.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.R;
import com.dy.colony.app.utils.DataUtils;
import com.dy.colony.di.component.DaggerJTJ_NewTestItemComponent;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.mvp.contract.JTJ_NewTestItemContract;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.presenter.JTJ_NewTestItemPresenter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class JTJ_NewTestItemActivity extends BaseActivity<JTJ_NewTestItemPresenter> implements JTJ_NewTestItemContract.View {
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.projectName)
    AutoCompleteTextView mProjectName;
    @BindView(R.id.textInputLayout2)
    TextInputLayout mTextInputLayout2;
    @BindView(R.id.testMethod)
    AutoCompleteTextView mTestMethod;
    @BindView(R.id.textInputLayout5)
    TextInputLayout mTextInputLayout5;
    @BindView(R.id.c1)
    AutoCompleteTextView mC1;
    @BindView(R.id.t1A)
    AutoCompleteTextView mT1A;
    @BindView(R.id.t1B)
    AutoCompleteTextView mT1B;
    @BindView(R.id.c1_t1A)
    AutoCompleteTextView mC1T1A;
    @BindView(R.id.c1_t1B)
    AutoCompleteTextView mC1T1B;
    @BindView(R.id.linearLayout32)
    LinearLayout mLinearLayout32;
    @BindView(R.id.c2)
    AutoCompleteTextView mC2;
    @BindView(R.id.t2A)
    AutoCompleteTextView mT2A;
    @BindView(R.id.t2B)
    AutoCompleteTextView mT2B;
    @BindView(R.id.c2_t2A)
    AutoCompleteTextView mC2T2A;
    @BindView(R.id.c2_t2B)
    AutoCompleteTextView mC2T2B;
    @BindView(R.id.linearLayout33)
    LinearLayout mLinearLayout33;
    @BindView(R.id.testTime)
    AutoCompleteTextView mTestTime;
    @BindView(R.id.textInputLayout9)
    TextInputLayout mTextInputLayout9;
    @BindView(R.id.password)
    AutoCompleteTextView mPassword;
    @BindView(R.id.textInputLayout10)
    TextInputLayout mTextInputLayout10;
    @BindView(R.id.save)
    Button mSave;
    @BindView(R.id.cancle)
    Button mCancle;


    private Intent mIntent;
    private Bundle mExtras;
    private int flag = 0;
    private JTJTestItem mJTJTestItem;
    private String mProjectName_s;
    private String mTestMethod_s;
    private String mC1_s;
    private String mT1_sA;
    private String mT1_sB;
    private String mC1T1_sA;
    private String mC1T1_sB;
    private String mC2_s;
    private String mT2_sA;
    private String mT2_sB;
    private String mC2T2_sA;
    private String mC2T2_sB;
    private String mTestTime_s;
    private String mPassWord_s;
    private BaseProjectMessage data;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerJTJ_NewTestItemComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_jtj_newtestitem; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mIntent = getIntent();
        LogUtils.d(mIntent);
        mExtras = mIntent.getExtras();
        LogUtils.d(mExtras);
        if (null != mExtras) {
            flag = 1;

            data = (BaseProjectMessage) mExtras.get("data");
            if (data instanceof JTJTestItem) {
                this.setTitle(R.string.EditorProjectActivity);
                mJTJTestItem = ((JTJTestItem) data);
            }
            initJTJTestItem();

        } else {
            flag = 2;
            this.setTitle(R.string.new_testing_items);
            mJTJTestItem = new JTJTestItem();
        }

    }

    @SuppressLint("SetTextI18n")
    private void initJTJTestItem() {
        if (data != null) {
            LogUtils.d(data);
            mProjectName.setText(data.getProjectName());
            mTestMethod.setText(data.getTestMethod() + "");
            mC1.setText(data.getC1() + "");
            mT1A.setText(data.getT1A() + "");
            mT1B.setText(data.getT1B() + "");
            mC1T1A.setText(data.getC1_t1A() + "");
            mC1T1B.setText(data.getC1_t1B() + "");
            mC2.setText(data.getC2() + "");
            mT2A.setText(data.getT2A() + "");
            mT2B.setText(data.getT2B() + "");
            mC2T2A.setText(data.getC2_t2A() + "");
            mC2T2B.setText(data.getC2_t2B() + "");
            mTestTime.setText(data.getTestTime() + "");
            mPassword.setText(data.getPassword());

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

    @OnClick({R.id.save, R.id.cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                getInputMessage();
                if ("".equals(mProjectName_s) || "".equals(mTestMethod_s) || "".equals(mC1_s) || "".equals(mT1_sA) || "".equals(mT1_sB) || "".equals(mC1T1_sA) || "".equals(mC1T1_sB) ||
                        "".equals(mC2_s) || "".equals(mT2_sA) || "".equals(mT2_sB) || "".equals(mC2T2_sA) || "".equals(mC2T2_sB) || "".equals(mTestTime_s)) {
                    Toast.makeText(this, getString(R.string.hintmessage_requiredfieldscannotbeempty), Toast.LENGTH_LONG).show();
                } else {
                    mJTJTestItem.setItem_type(1 + "");
                    mJTJTestItem.setProjectName(mProjectName_s);
                    mJTJTestItem.setTestMethod(Integer.parseInt(mTestMethod_s.trim()));
                    mJTJTestItem.setC1(Double.parseDouble(mC1_s.trim()));
                    mJTJTestItem.setT1A(Double.parseDouble(mT1_sA.trim()));
                    mJTJTestItem.setT1B(Double.parseDouble(mT1_sB.trim()));
                    mJTJTestItem.setC1_t1A(Double.parseDouble(mC1T1_sA.trim()));
                    mJTJTestItem.setC1_t1B(Double.parseDouble(mC1T1_sB.trim()));
                    mJTJTestItem.setC2(Double.parseDouble(mC2_s.trim()));
                    mJTJTestItem.setT2A(Double.parseDouble(mT2_sA.trim()));
                    mJTJTestItem.setT2B(Double.parseDouble(mT2_sB.trim()));
                    mJTJTestItem.setC2_t2A(Double.parseDouble(mC2T2_sA.trim()));
                    mJTJTestItem.setC2_t2B(Double.parseDouble(mC2T2_sB.trim()));
                    mJTJTestItem.setTestTime(Integer.valueOf(mTestTime_s.trim()));
                    mJTJTestItem.setPassword(mPassWord_s);
                    mJTJTestItem.setVersion(DataUtils.getNowtimeyyymmddhhmmss());
                    LogUtils.d(flag);
                    LogUtils.d(mJTJTestItem);
                    if (flag == 1) {
                        DBHelper.getJTJTestItemDao(getActivity()).insertOrReplace(mJTJTestItem);
                    } else if (flag == 2) {
                        mJTJTestItem.setId(null);
                        mJTJTestItem.setUnique_testproject(UUID.randomUUID().toString());
                        DBHelper.getJTJTestItemDao(getActivity()).insert(mJTJTestItem);
                    }
                    finish();
                }

                break;
            case R.id.cancle:
                finish();
                break;
        }
    }

    private void getInputMessage() {
        mProjectName_s = mProjectName.getText().toString();
        mTestMethod_s = mTestMethod.getText().toString();
        mC1_s = mC1.getText().toString();
        mT1_sA = mT1A.getText().toString();
        mT1_sB = mT1B.getText().toString();
        mC1T1_sA = mC1T1A.getText().toString();
        mC1T1_sB = mC1T1B.getText().toString();
        mC2_s = mC2.getText().toString();
        mT2_sA = mT2A.getText().toString();
        mT2_sB = mT2B.getText().toString();
        mC2T2_sA = mC2T2A.getText().toString();
        mC2T2_sB = mC2T2B.getText().toString();
        mTestTime_s = mTestTime.getText().toString();
        mPassWord_s = mPassword.getText().toString();
    }
}