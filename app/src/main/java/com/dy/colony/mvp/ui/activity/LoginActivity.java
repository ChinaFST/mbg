package com.dy.colony.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;
import com.dy.colony.R;
import com.dy.colony.app.utils.SPUtils;
import com.dy.colony.di.component.DaggerLoginComponent;
import com.dy.colony.greendao.beans.User;
import com.dy.colony.mvp.contract.LoginContract;
import com.dy.colony.mvp.presenter.LoginPresenter;
import com.google.gson.Gson;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CancelAdapt;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View, CancelAdapt {

    @BindView(R.id.usename)
    EditText mUsename;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.cb)
    CheckBox mCb;
    @BindView(R.id.login)
    Button mLogin;
    @BindView(R.id.callours)
    TextView mCallours;
    @BindView(R.id.online_register)
    TextView mOnlineRegister;
    @BindView(R.id.version)
    TextView mVersion;
    @BindView(R.id.activity_login)
    RelativeLayout mActivityLogin;
    @BindView(R.id.appname)
    TextView mAppname;
    @BindView(R.id.test)
    TextView mTest;
    @Inject
    Gson mGson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Object userinfo = extras.get("userinfo");
            if (userinfo != null) {
                if (userinfo instanceof User) {
                    mUsename.setText(((User) userinfo).getUsername());
                    mPassword.setText(((User) userinfo).getPassword());
                }
            }
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initCb();
        initUserInfor();
        initVersion();

        /*if (Constants.ISREMBERUSERNAME) {
            login();
        }*/
        if (BuildConfig.DEBUG) {
            mUsename.setText("admin");
            mPassword.setText("123456");
            mCb.setChecked(true);
        }
    }


    @SuppressLint("SetTextI18n")
    private void initVersion() {
        LogUtils.d(mVersion);
        mVersion.setText("V" + BuildConfig.VERSION_NAME);
    }

    private void initCb() {
        mCb.setChecked(Constants.ISREMBERUSERNAME);
        mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.put(getActivity(), Constants.KEY_REMBERUSERNAME, isChecked);
            }
        });
    }

    private void initUserInfor() {
        if (Constants.ISREMBERUSERNAME) {
            String userJson = (String) SPUtils.get(getActivity(), Constants.KEY_USERINFOR_JSON, mGson.toJson(new User()));
            User user = mGson.fromJson(userJson, User.class);
            mUsename.setText(user.getUsername());
            mPassword.setText(user.getPassword());
        }
    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
        // killMyself();
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
    public void loginSuccess(String s, User user) {
        SPUtils.put(this, "remenberpassword", Constants.ISREMBERUSERNAME);
        if (Constants.ISREMBERUSERNAME) {
            SPUtils.put(getActivity(), Constants.KEY_USERINFOR_JSON, mGson.toJson(user));
        }
        Constants.NOWUSER = user;
        launchActivity(new Intent(getActivity(), HomeActivity.class));
        killMyself();
    }


    @Override
    public void loginFail(String s) {
        ArmsUtils.snackbarText(s);
    }


    @OnClick({R.id.cb, R.id.login, R.id.callours, R.id.online_register, R.id.offline})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb:
                if (Constants.ISREMBERUSERNAME) {
                    Constants.ISREMBERUSERNAME = false;
                } else {
                    Constants.ISREMBERUSERNAME = true;
                }
                break;
            case R.id.login:
                login();
                break;
            case R.id.callours:
                //launchActivity(new Intent(getActivity(), CallUsActivity.class));
                break;
            case R.id.online_register:
                //launchActivity(new Intent(getActivity(), RegisterOnLineActivity.class));
                break;

            case R.id.offline:
                launchActivity(new Intent(getActivity(), HomeActivity.class));
                killMyself();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        ArmsUtils.exitApp();
        super.onBackPressed();
    }


    private void login() {
        String username = mUsename.getText().toString();
        String password = mPassword.getText().toString();
        if (username.isEmpty()) {
            ArmsUtils.snackbarText(this.getString(R.string.login_toast_nousername));
        } else if (password.isEmpty()) {
            ArmsUtils.snackbarText(this.getString(R.string.login_toast_nopassword));
        } else {
            mPresenter.login(username, password);
        }
    }
}