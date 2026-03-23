package com.dy.colony.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.app.utils.DataUtils;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.greendao.beans.Simple33;
import com.dy.colony.greendao.daos.Simple33Dao;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.dy.colony.di.component.DaggerEdtorSampleComponent;
import com.dy.colony.mvp.contract.EdtorSampleContract;
import com.dy.colony.mvp.presenter.EdtorSamplePresenter;

import com.dy.colony.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class EdtorSampleActivity extends BaseActivity<EdtorSamplePresenter> implements EdtorSampleContract.View {
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.projectname)
    AutoCompleteTextView mProjectname;
    @BindView(R.id.sample_type)
    AutoCompleteTextView mSampleType;
    @BindView(R.id.samplename)
    AutoCompleteTextView mSamplename;
    @BindView(R.id.samplenum)
    AutoCompleteTextView mSamplenum;
    @BindView(R.id.standnum)
    AutoCompleteTextView mStandnum;
    @BindView(R.id.Demarcate)
    Spinner mDemarcate;
    @BindView(R.id.StandarValue)
    AutoCompleteTextView mStandarValue;
    @BindView(R.id.Unit)
    AutoCompleteTextView mUnit;
    @BindView(R.id.btn_cancle)
    Button mBtnCancle;
    @BindView(R.id.btn_determine)
    Button mBtnDetermine;
    @BindView(R.id.linearLayout35)
    LinearLayout mLinearLayout35;
    @BindView(R.id.sample_type_name)
    AutoCompleteTextView mSampleTypeName;
    private Intent mIntent;
    private BaseSampleMessage mBaseSampleMessage;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerEdtorSampleComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_edtorsample; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String projectName = intent.getStringExtra("ProjectName");
        String sampleName = intent.getStringExtra("SampleName");
        if (null != projectName) {
            mProjectname.setText(projectName);
        }
        if (null != sampleName) {
            mSamplename.setText(sampleName);
        }
        if (Constants.PLATFORM_TAG == 5) {
            mSamplenum.setText("zj" + System.currentTimeMillis());
        }
        List<String> mItems = new ArrayList<>();
        mItems.add("<");
        mItems.add(">");
        mItems.add("≥");
        mItems.add("≤");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mItems);
        mDemarcate.setAdapter(adapter);
        mIntent = getIntent();
        int intExtra = mIntent.getIntExtra("data", -1);
        switch (intExtra) {
            case -1:
                ArmsUtils.snackbarText(getString(R.string.parerror) + intExtra);
                killMyself();
                break;
            case 1:
                setTitle(getString(R.string.newsample));
                break;
            case 2:
                setTitle(getString(R.string.editersample));
                Bundle extras = mIntent.getExtras();
                if (null == extras) {
                    ArmsUtils.snackbarText("Bundle 为空");
                    killMyself();
                }
                mBaseSampleMessage = ((BaseSampleMessage) extras.get("databean"));
                if (mBaseSampleMessage instanceof FoodItemAndStandard) {
                    setMessage_FoodItemAndStandard_(((FoodItemAndStandard) mBaseSampleMessage));
                }
                break;
        }
        mSampleType.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivityForResult(new Intent(getActivity(), ChoseSampleTypeActivity.class), 1);
                return false;
            }
        });

        mProjectname.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPresenter.makeDialogShowproject();
                return false;
            }
        });

        mStandnum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String projectName = mProjectname.getText().toString();
                if (projectName.isEmpty()) {
                    ArmsUtils.snackbarText(getString(R.string.enter_or_select_item));
                    return false;
                }
                mPresenter.loadLocaStandNumber(projectName);
                return false;
            }
        });
        if (!mProjectname.getText().toString().isEmpty()) {
            mPresenter.loadLocaStandNumber(projectName);
        }
    }

    private void setMessage_FoodItemAndStandard_(FoodItemAndStandard message) {
        mProjectname.setText(message.getItemName());
        String code = message.getFoodPCode();
        mSampleType.setText(code);
        mSamplenum.setText(message.getSampleNum());
        mSamplename.setText(message.getSampleName());
        mStandnum.setText(message.getStandardName());
        String sign = message.getCheckSign();
        LogUtils.d(sign);
        if ("<".equals(sign)) {
            mDemarcate.setSelection(0);
        } else if (">".equals(sign)) {
            mDemarcate.setSelection(1);
        } else if ("≥".equals(sign)) {
            mDemarcate.setSelection(2);
        } else if ("≤".equals(sign)) {
            mDemarcate.setSelection(3);
        }
        mStandarValue.setText(message.getStandardValue());
        mUnit.setText(message.getCheckValueUnit());
        List<Simple33> list = DBHelper.getSimple33Dao(getActivity()).queryBuilder().where(Simple33Dao.Properties.FoodPCode.eq(code)).list();
        if (list.size() > 0) {
            mSampleTypeName.setText(list.get(0).getFoodName());
        } else {
            //ArmsUtils.snackbarText(getString(R.string.sample_type_query_failed));
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

    @Override
    public void setChosedProject(String string) {
        mProjectname.setText(string);
        mPresenter.loadLocaStandNumber(string);
    }

    @Override
    public void setChosedStandNumber(BaseSampleMessage message) {
        if (message instanceof FoodItemAndStandard) {
            FoodItemAndStandard standard = (FoodItemAndStandard) message;
            SpinnerAdapter adapter = mDemarcate.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++) {
                String item = adapter.getItem(i).toString();
                if (item.equals(standard.getCheckSign())) {
                    mDemarcate.setSelection(i);
                    break;
                }
            }

            mStandnum.setText(standard.getStandardName());
            mStandarValue.setText(standard.getStandardValue());
            mUnit.setText(standard.getCheckValueUnit());
        }

    }


    @OnClick({R.id.btn_cancle, R.id.btn_determine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancle:
                killMyself();
                break;
            case R.id.btn_determine:
                saveOrUpData();
                break;
        }
    }

    private void saveOrUpData() {
        //目前只做了本地内置的样品的增删改查
        String samplename = mSamplename.getText().toString();
        String code = mSampleType.getText().toString();
        String projectname = mProjectname.getText().toString();
        String samplenumber = mSamplenum.getText().toString();
        String standnumber = mStandnum.getText().toString();
        String sign = mDemarcate.getSelectedItem().toString();
        String standvalue = mStandarValue.getText().toString();
        String standvalueunit = mUnit.getText().toString();
        String nowtimeyyymmddhhmmss = DataUtils.getNowtimeyyymmddhhmmss();


        if ("".equals(samplename) || "".equals(code) || "".equals(projectname) || "".equals(samplenumber) ||
                "".equals(standnumber) || "".equals(sign) || "".equals(standvalue) || "".equals(standvalueunit) || "".equals(nowtimeyyymmddhhmmss)) {
            ArmsUtils.snackbarText(getString(R.string.hintmessage_requiredfieldscannotbeempty));
            return;
        }

        if (null != mBaseSampleMessage) {
            //UPDATA
            FoodItemAndStandard message = (FoodItemAndStandard) mBaseSampleMessage;
            message.setSampleName(samplename);
            message.setFoodPCode(code);
            message.setItemName(projectname);
            message.setSampleNum(samplenumber);
            message.setStandardName(standnumber);
            message.setCheckSign(sign);
            message.setStandardValue(standvalue);
            message.setCheckValueUnit(standvalueunit);
            message.setuDate(nowtimeyyymmddhhmmss);
            message.setFlag(2);
            DBHelper.getFoodItemAndStandardDao(getActivity()).update(message);
        } else {
            //NEW
            FoodItemAndStandard message = new FoodItemAndStandard();
            message.setSampleName(samplename);
            message.setFoodPCode(code);
            message.setItemName(projectname);
            message.setSampleNum(samplenumber);
            message.setStandardName(standnumber);
            message.setCheckSign(sign);
            message.setStandardValue(standvalue);
            message.setCheckValueUnit(standvalueunit);
            message.setuDate(nowtimeyyymmddhhmmss);
            message.setFlag(2);
            message.setId(null);
            message.setCheckId(UUID.randomUUID().toString());
            DBHelper.getFoodItemAndStandardDao(getActivity()).insert(message);
        }


        killMyself();
    }
}