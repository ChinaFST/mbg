package com.dy.colony.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.MyAppLocation;
import com.dy.colony.app.utils.CheckNullUtils;
import com.dy.colony.app.utils.DataUtils;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.ui.adapter.GuidePageAdapter_h;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.dy.colony.di.component.DaggerFGGD_NewTestItemComponent;
import com.dy.colony.mvp.contract.FGGD_NewTestItemContract;
import com.dy.colony.mvp.presenter.FGGD_NewTestItemPresenter;

import com.dy.colony.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class FGGD_NewTestItemActivity extends BaseActivity<FGGD_NewTestItemPresenter> implements FGGD_NewTestItemContract.View {
    private int method_checked_flag = 0;
    private int wavalength_checked_flag = 0;
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.testprojectname)
    AutoCompleteTextView mTestprojectname;
    @BindView(R.id.password)
    AutoCompleteTextView mPassword;
    @BindView(R.id.seariernum)
    AutoCompleteTextView mSeariernum;
    @BindView(R.id.chosemethod)
    Spinner mChosemethod;

    @BindView(R.id.vp)
    ViewPager mVp;

    @BindView(R.id.chosewavalength)
    Spinner mChosewavalength;
    @BindView(R.id.save)
    Button mSave;
    @BindView(R.id.cancle)
    Button mCancle;


    private View methoed_a_page;
    private View methoed_b_page;
    private View methoed_c_page;
    private View methoed_d_page;
    private List<View> mViewList = new ArrayList<>();
    private GuidePageAdapter_h mAdapter;
    private ViewHoldera mViewHoldera;
    private ViewHolderb mViewHolderb;
    private ViewHolderc mViewHolderc;
    private ViewHolderd mViewHolderd;
    private Intent mIntent;
    private int flag;
    private FGGDTestItem mFGGDTestItem;
    private BaseProjectMessage data;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFGGD_NewTestItemComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_fggd_newtestitem; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mIntent = getIntent();
        Bundle mExtras = mIntent.getExtras();

        initpages();
        mAdapter = new GuidePageAdapter_h(mViewList);
        mVp.setAdapter(mAdapter);
        customSpinner();
        mChosemethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                method_checked_flag = position;
                LogUtils.d(method_checked_flag);
                mVp.setCurrentItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mChosewavalength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wavalength_checked_flag = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //禁止viewpage左右滑动
        mVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        if (null != mExtras) {
            flag = 1;
            data = (BaseProjectMessage) mExtras.get("data");
            if (data instanceof FGGDTestItem) {
                this.setTitle(R.string.EditorProjectActivity);
                mFGGDTestItem = (FGGDTestItem) data;
            }
            initFGGDTestItem();

        } else {
            flag = 2;
            this.setTitle(R.string.new_testing_items);
            mFGGDTestItem = new FGGDTestItem();
            mVp.setCurrentItem(method_checked_flag);
        }
    }

    private void customSpinner() {
        String[] moudles = getResources().getStringArray(R.array.methods);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item_main_14sp, moudles);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChosemethod.setAdapter(arrayAdapter1);
    }

    private void initFGGDTestItem() {
        String method = data.getMethod();
        mVp.setCurrentItem(Integer.valueOf(method));
        mChosemethod.setSelection(Integer.valueOf(method));
        mPassword.setText(data.getPassword());
        mTestprojectname.setText(data.getProjectName());
        mSeariernum.setText(data.getSerialNumber() + "");
        int wavelength = data.getWavelength();
        switch (wavelength) {
            case 410:
                mChosewavalength.setSelection(0);
                break;
            case 536:
                mChosewavalength.setSelection(1);
                break;
            case 595:
                mChosewavalength.setSelection(2);
                break;
            case 620:
                mChosewavalength.setSelection(3);
                break;
        }

        switch (method) {
            case "0":
                mViewHoldera.mUnitInput.setText(data.getUnit_input());
                mViewHoldera.mYrsj.setText(data.getYuretime() + "");
                mViewHoldera.mJcsj.setText(data.getJiancetime() + "");
                mViewHoldera.mYinXfwa.setText(data.getYin_a() + "");
                mViewHoldera.mYinXfwb.setText(data.getYin_b() + "");
                mViewHoldera.mYangXfwa.setText(data.getYang_a() + "");
                mViewHoldera.mYangXfwb.setText(data.getYang_b() + "");
                mViewHoldera.mKya.setText(data.getKeyi_a() + "");
                mViewHoldera.mKyb.setText(data.getKeyi_b() + "");
                break;
            case "1":
                mViewHolderb.mYrsj.setText(data.getYuretime() + "");
                mViewHolderb.mJcsj.setText(data.getJiancetime() + "");
                mViewHolderb.mUnitInput.setText(data.getUnit_input());
                mViewHolderb.mAx0.setText(data.getBiaozhun_a0());
                mViewHolderb.mAx1.setText(data.getBiaozhun_b0());
                mViewHolderb.mAx2.setText(data.getBiaozhun_c0());
                mViewHolderb.mAx3.setText(data.getBiaozhun_d0());
                mViewHolderb.mAfrom.setText(data.getBiaozhun_from0());
                mViewHolderb.mAto.setText(data.getBiaozhun_to0());
                mViewHolderb.mBx0.setText(data.getBiaozhun_a1());
                mViewHolderb.mBx1.setText(data.getBiaozhun_b1());
                mViewHolderb.mBx2.setText(data.getBiaozhun_c1());
                mViewHolderb.mBx3.setText(data.getBiaozhun_d1());
                mViewHolderb.mBfrom.setText(data.getBiaozhun_from1());
                mViewHolderb.mBto.setText(data.getBiaozhun_to1());
                mViewHolderb.mJzqxa.setText(data.getJiaozhen_a() + "");
                mViewHolderb.mJzqxb.setText(data.getJiaozhen_b() + "");
                mViewHolderb.mCheckuserTuise.setChecked(data.isUsetuise());
                break;
            case "2":
                mViewHolderc.mYrsj.setText(data.getYuretime() + "");
                mViewHolderc.mUnitInput.setText(data.getUnit_input());
                mViewHolderc.mJcsj.setText(data.getJiancetime() + "");
                mViewHolderc.mJzqxa.setText(data.getJiaozhen_a() + "");
                mViewHolderc.mJzqxb.setText(data.getJiaozhen_b() + "");
                mViewHolderc.mYinxfwa.setText(data.getYin_a() + "");
                mViewHolderc.mYinxfwb.setText(data.getYin_b() + "");
                mViewHolderc.mYangxfwa.setText(data.getYang_a() + "");
                mViewHolderc.mYangxfwb.setText(data.getYang_b() + "");
                mViewHolderc.mKyfwa.setText(data.getKeyi_a() + "");
                mViewHolderc.mKyfwb.setText(data.getKeyi_b() + "");
                mViewHolderc.mCheckuserTuise.setChecked(data.isUsetuise());
                break;
            case "3":
                mViewHolderd.mUnitInput.setText(data.getUnit_input());
                mViewHolderd.mJzqxa.setText(data.getJiaozhen_a() + "");
                mViewHolderd.mJzqxb.setText(data.getJiaozhen_b() + "");
                mViewHolderd.mJzqxc.setText(data.getJiaozhen_c() + "");
                mViewHolderd.mJzqxd.setText(data.getJiaozhen_d() + "");
                break;
        }
    }

    private void initpages() {
        LayoutInflater inflater = getLayoutInflater();
        methoed_a_page = inflater.inflate(R.layout.method_a, null);
        mViewHoldera = new ViewHoldera(methoed_a_page);

        methoed_b_page = inflater.inflate(R.layout.method_b, null);
        mViewHolderb = new ViewHolderb(methoed_b_page);

        methoed_c_page = inflater.inflate(R.layout.method_c, null);
        mViewHolderc = new ViewHolderc(methoed_c_page);

        methoed_d_page = inflater.inflate(R.layout.method_d, null);
        mViewHolderd = new ViewHolderd(methoed_d_page);
        mViewList.add(methoed_a_page);
        mViewList.add(methoed_b_page);
        mViewList.add(methoed_c_page);
        mViewList.add(methoed_d_page);

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

    @OnClick({R.id.save, R.id.cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                if (saveNewTestItem()) {
                    if (flag == 1) {
                        ArmsUtils.snackbarText(getString(R.string.fggditem_message1));
                    } else if (flag == 2) {
                        ArmsUtils.snackbarText(getString(R.string.fggditem_message2));
                    }
                    killMyself();
                }
                break;
            case R.id.cancle:
                killMyself();
                break;
        }
    }

    private boolean saveNewTestItem() {

        if (CheckNullUtils.checkNull(mTestprojectname)) {
            ArmsUtils.snackbarText(getString(R.string.fggditem_message3));
            return false;
        }
        if (CheckNullUtils.checkNull(mPassword)) {
            ArmsUtils.snackbarText(getString(R.string.fggditem_message4));
            return false;
        }
        if (CheckNullUtils.checkNull(mSeariernum)) {
            ArmsUtils.snackbarText(getString(R.string.fggditem_message5));
            return false;
        }
        switch (method_checked_flag) {
            case 0:

                if (CheckNullUtils.checkNull(mViewHoldera.mYrsj)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yure_time));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHoldera.mJcsj)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_detect_time));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHoldera.mUnitInput)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_unit_value));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHoldera.mYinXfwa)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yin_range));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHoldera.mYinXfwb)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yin_range));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHoldera.mYangXfwa)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yang_range));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHoldera.mYangXfwb)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yang_range));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHoldera.mKya)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_keyi_range));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHoldera.mKyb)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_keyi_range));
                    return false;
                }


                break;
            case 1:
                if (CheckNullUtils.checkNull(mViewHolderb.mYrsj)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yure_time));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mJcsj)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_detect_time));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mUnitInput)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_unit_value));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mAx0)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message18));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mAx1)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message19));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mAx2)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message20));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mAx3)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message21));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mAfrom)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message22));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mAto)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message23));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mBx0)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message24));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mBx1)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message25));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mBx2)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message26));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mBx3)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message27));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mBfrom)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message28));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mBto)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message29));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mJzqxa)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_jz_curve_a));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderb.mJzqxb)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_jz_curve_b));
                    return false;
                }

                //褪色法
                break;
            case 2:

                if (CheckNullUtils.checkNull(mViewHolderc.mYrsj)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yure_time));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderc.mJcsj)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_detect_time));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderc.mUnitInput)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_unit_value));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderc.mJzqxa)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message35));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderc.mJzqxb)) {
                    ArmsUtils.snackbarText(getString(R.string.fggditem_message36));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderc.mYinxfwa)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yin_range));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderc.mYinxfwb)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yin_range));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderc.mYangxfwa)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yang_range));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderc.mYangxfwb)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_yang_range));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderc.mKyfwa)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_keyi_range));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderc.mKyfwb)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_keyi_range));
                    return false;
                }


                break;
            case 3:
                if (CheckNullUtils.checkNull(mViewHolderd.mUnitInput)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_unit_value));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderd.mJzqxa)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_jz_curve_a));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderd.mJzqxb)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_jz_curve_b));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderd.mJzqxc)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_jz_curve_c));
                    return false;
                }
                if (CheckNullUtils.checkNull(mViewHolderd.mJzqxd)) {
                    ArmsUtils.snackbarText(getString(R.string.enter_jz_curve_d));
                    return false;
                }

                break;
        }
        mFGGDTestItem.setProject_name(getText(mTestprojectname));
        mFGGDTestItem.setPassword(getText(mPassword));
        if (wavalength_checked_flag == 0) {
            mFGGDTestItem.setWavelength(410);
        } else if (wavalength_checked_flag == 1) {
            mFGGDTestItem.setWavelength(536);
        } else if (wavalength_checked_flag == 2) {
            mFGGDTestItem.setWavelength(595);
        } else if (wavalength_checked_flag == 3) {
            mFGGDTestItem.setWavelength(620);
        }
        mFGGDTestItem.setSerialNumber(getInteger(mSeariernum));
        //mFGGDTestItem.setShowhint(""); //2.0后舍弃

        switch (method_checked_flag) {
            case 0:

                mFGGDTestItem.setMethod(0 + "");
                mFGGDTestItem.setUnit_input(getText(mViewHoldera.mUnitInput));
                mFGGDTestItem.setYuretime(getInteger(mViewHoldera.mYrsj));
                mFGGDTestItem.setJiancetime(getInteger(mViewHoldera.mJcsj));
                mFGGDTestItem.setYin_a(getDouble(mViewHoldera.mYinXfwa));
                mFGGDTestItem.setYin_b(getDouble(mViewHoldera.mYinXfwb));
                mFGGDTestItem.setYang_a(getDouble(mViewHoldera.mYangXfwa));
                mFGGDTestItem.setYang_b(getDouble(mViewHoldera.mYangXfwb));
                mFGGDTestItem.setKeyi_a(getDouble(mViewHoldera.mKya));
                mFGGDTestItem.setKeyi_b(getDouble(mViewHoldera.mKyb));

                break;
            case 1:
                mFGGDTestItem.setMethod(1 + "");
                mFGGDTestItem.setYuretime(getInteger(mViewHolderb.mYrsj));
                mFGGDTestItem.setJiancetime(getInteger(mViewHolderb.mJcsj));
                mFGGDTestItem.setUnit_input(getText(mViewHolderb.mUnitInput));

                mFGGDTestItem.setBiaozhun_a0(getDouble(mViewHolderb.mAx0) + "");
                mFGGDTestItem.setBiaozhun_b0(getDouble(mViewHolderb.mAx1) + "");
                mFGGDTestItem.setBiaozhun_c0(getDouble(mViewHolderb.mAx2) + "");
                mFGGDTestItem.setBiaozhun_d0(getDouble(mViewHolderb.mAx3) + "");
                mFGGDTestItem.setBiaozhun_from0(getDouble(mViewHolderb.mAfrom) + "");
                mFGGDTestItem.setBiaozhun_to0(getDouble(mViewHolderb.mAto) + "");

                mFGGDTestItem.setBiaozhun_a1(getDouble(mViewHolderb.mBx0) + "");
                mFGGDTestItem.setBiaozhun_b1(getDouble(mViewHolderb.mBx1) + "");
                mFGGDTestItem.setBiaozhun_c1(getDouble(mViewHolderb.mBx2) + "");
                mFGGDTestItem.setBiaozhun_d1(getDouble(mViewHolderb.mBx3) + "");
                mFGGDTestItem.setBiaozhun_from1(getDouble(mViewHolderb.mBfrom) + "");
                mFGGDTestItem.setBiaozhun_to1(getDouble(mViewHolderb.mBto) + "");

                mFGGDTestItem.setJiaozhen_a(getDouble(mViewHolderb.mJzqxa));
                mFGGDTestItem.setJiaozhen_b(getDouble(mViewHolderb.mJzqxb));
                mFGGDTestItem.setUsetuise(mViewHolderb.mCheckuserTuise.isChecked());
                break;
            case 2:

                mFGGDTestItem.setMethod(2 + "");
                mFGGDTestItem.setYuretime(getInteger(mViewHolderc.mYrsj));
                mFGGDTestItem.setUnit_input(getText(mViewHolderc.mUnitInput));
                mFGGDTestItem.setJiancetime(getInteger(mViewHolderc.mJcsj));
                mFGGDTestItem.setJiaozhen_a(getDouble(mViewHolderc.mJzqxa));
                mFGGDTestItem.setJiaozhen_b(getDouble(mViewHolderc.mJzqxb));
                mFGGDTestItem.setYin_a(getDouble(mViewHolderc.mYinxfwa));
                mFGGDTestItem.setYin_b(getDouble(mViewHolderc.mYinxfwb));
                mFGGDTestItem.setYang_a(getDouble(mViewHolderc.mYangxfwa));
                mFGGDTestItem.setYang_b(getDouble(mViewHolderc.mYangxfwb));
                mFGGDTestItem.setKeyi_a(getDouble(mViewHolderc.mKyfwa));
                mFGGDTestItem.setKeyi_b(getDouble(mViewHolderc.mKyfwb));
                mFGGDTestItem.setUsetuise(mViewHolderc.mCheckuserTuise.isChecked());
                break;
            case 3:
                mFGGDTestItem.setMethod(3 + "");
                mFGGDTestItem.setUnit_input(getText(mViewHolderd.mUnitInput));
                mFGGDTestItem.setJiaozhen_a(getDouble(mViewHolderd.mJzqxa));
                mFGGDTestItem.setJiaozhen_b(getDouble(mViewHolderd.mJzqxb));
                mFGGDTestItem.setJiaozhen_c(getDouble(mViewHolderd.mJzqxc));
                mFGGDTestItem.setJiaozhen_d(getDouble(mViewHolderd.mJzqxd));
                break;

        }
        mFGGDTestItem.setVersion(DataUtils.getNowtimeyyymmddhhmmss());
        if (flag == 1) {
            DBHelper.getFGGDTestItemDao(getActivity()).update(mFGGDTestItem);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    List<GalleryBean> list = MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList;
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setProjectChange(true);
                    }
                }
            }, 0);
        } else if (flag == 2) {
            mFGGDTestItem.setId(null);
            mFGGDTestItem.setUnique_testproject(UUID.randomUUID().toString());
            DBHelper.getFGGDTestItemDao(getActivity()).insert(mFGGDTestItem);
        }

        return true;
    }

    public String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public String getText(AutoCompleteTextView editText) {
        return editText.getText().toString().trim();
    }

    public double getDouble(AutoCompleteTextView editText) {
        return Double.valueOf(editText.getText().toString().trim());
    }

    public double getDouble(EditText editText) {
        return Double.valueOf(editText.getText().toString().trim());
    }

    public int getInteger(AutoCompleteTextView editText) {
        return Integer.valueOf(editText.getText().toString().trim());
    }

    public int getInteger(EditText editText) {
        return Integer.valueOf(editText.getText().toString().trim());
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    static class ViewHoldera {
        @BindView(R.id.yrsj)
        AutoCompleteTextView mYrsj;
        @BindView(R.id.jcsj)
        AutoCompleteTextView mJcsj;
        @BindView(R.id.unit_input)
        AutoCompleteTextView mUnitInput;
        @BindView(R.id.yin_xfwa)
        AutoCompleteTextView mYinXfwa;
        @BindView(R.id.yin_xfwb)
        AutoCompleteTextView mYinXfwb;
        @BindView(R.id.yang_xfwa)
        AutoCompleteTextView mYangXfwa;
        @BindView(R.id.yang_xfwb)
        AutoCompleteTextView mYangXfwb;
        @BindView(R.id.kya)
        AutoCompleteTextView mKya;
        @BindView(R.id.kyb)
        AutoCompleteTextView mKyb;

        ViewHoldera(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static
    class ViewHolderb {
        @BindView(R.id.yrsj)
        AutoCompleteTextView mYrsj;
        @BindView(R.id.jcsj)
        AutoCompleteTextView mJcsj;
        @BindView(R.id.unit_input)
        AutoCompleteTextView mUnitInput;
        @BindView(R.id.ax0)
        AutoCompleteTextView mAx0;
        @BindView(R.id.ax1)
        AutoCompleteTextView mAx1;
        @BindView(R.id.ax2)
        AutoCompleteTextView mAx2;
        @BindView(R.id.ax3)
        AutoCompleteTextView mAx3;
        @BindView(R.id.afrom)
        AutoCompleteTextView mAfrom;
        @BindView(R.id.ato)
        AutoCompleteTextView mAto;
        @BindView(R.id.bx0)
        AutoCompleteTextView mBx0;
        @BindView(R.id.bx1)
        AutoCompleteTextView mBx1;
        @BindView(R.id.bx2)
        AutoCompleteTextView mBx2;
        @BindView(R.id.bx3)
        AutoCompleteTextView mBx3;
        @BindView(R.id.bfrom)
        AutoCompleteTextView mBfrom;
        @BindView(R.id.bto)
        AutoCompleteTextView mBto;
        @BindView(R.id.jzqxa)
        AutoCompleteTextView mJzqxa;
        @BindView(R.id.jzqxb)
        AutoCompleteTextView mJzqxb;
        @BindView(R.id.checkuser_tuise)
        Switch mCheckuserTuise;

        ViewHolderb(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static
    class ViewHolderc {
        @BindView(R.id.yrsj)
        AutoCompleteTextView mYrsj;
        @BindView(R.id.jcsj)
        AutoCompleteTextView mJcsj;
        @BindView(R.id.unit_input)
        AutoCompleteTextView mUnitInput;
        @BindView(R.id.jzqxa)
        AutoCompleteTextView mJzqxa;
        @BindView(R.id.jzqxb)
        AutoCompleteTextView mJzqxb;
        @BindView(R.id.yinxfwa)
        AutoCompleteTextView mYinxfwa;
        @BindView(R.id.yinxfwb)
        AutoCompleteTextView mYinxfwb;
        @BindView(R.id.yangxfwa)
        AutoCompleteTextView mYangxfwa;
        @BindView(R.id.yangxfwb)
        AutoCompleteTextView mYangxfwb;
        @BindView(R.id.kyfwa)
        AutoCompleteTextView mKyfwa;
        @BindView(R.id.kyfwb)
        AutoCompleteTextView mKyfwb;
        @BindView(R.id.checkuser_tuise)
        Switch mCheckuserTuise;

        ViewHolderc(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static
    class ViewHolderd {
        @BindView(R.id.unit_input)
        AutoCompleteTextView mUnitInput;
        @BindView(R.id.jzqxa)
        AutoCompleteTextView mJzqxa;
        @BindView(R.id.jzqxb)
        AutoCompleteTextView mJzqxb;
        @BindView(R.id.jzqxc)
        AutoCompleteTextView mJzqxc;
        @BindView(R.id.jzqxd)
        AutoCompleteTextView mJzqxd;

        ViewHolderd(View view) {
            ButterKnife.bind(this, view);
        }
    }
}