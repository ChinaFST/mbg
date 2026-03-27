package com.dy.colony.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.R;
import com.dy.colony.app.utils.FileUtils;
import com.dy.colony.di.component.DaggerHomeComponent;
import com.dy.colony.mvp.contract.HomeContract;
import com.dy.colony.mvp.contract.IBackPressed;
import com.dy.colony.mvp.presenter.HomePresenter;
import com.dy.colony.mvp.ui.fragment.MainFragment;
import com.dy.colony.mvp.ui.fragment.SettingFragment;
import com.dy.colony.mvp.ui.fragment.TestRecordFragment;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.nav_main)
    LinearLayout mNavMain;
    @BindView(R.id.nav_record)
    LinearLayout mNavRecord;
    @BindView(R.id.nav_settings)
    LinearLayout mNavSettings;
    @BindView(R.id.nav_sync)
    LinearLayout mNavSync;

    private FragmentManager mFragmentManager;
    private MainFragment mMainFragment;
    private SettingFragment mSettingFragment;
    private TestRecordFragment mTestRecordFragment;
    private Fragment mCurrentFragment;
    private long mExitTime = 0;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_home; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        checkLocationPermission();
        mFragmentManager = getSupportFragmentManager();
        showFragment(0);
        //GpioUtils.writeGpioDirectly("/sys/class/gpiocontrol/gpiocontrol/gpiocontrol150",1+"");
        // GpioUtils.writeGpioWithSu("/sys/class/gpiocontrol/gpiocontrol/gpiocontrol150",1+"");
        checkAndRequestManageStoragePermission();
    }

    List<String> permissionDeniedList = new ArrayList<>();
    private static final int LOCATION_PERMISSION_CODE = 123;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    private void checkLocationPermission() {
        permissionDeniedList.add(Manifest.permission.WAKE_LOCK);
        permissionDeniedList.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissionDeniedList.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
        permissionDeniedList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionDeniedList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionDeniedList.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
        permissionDeniedList.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        permissionDeniedList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionDeniedList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionDeniedList.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissionDeniedList.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissionDeniedList.add(Manifest.permission.REORDER_TASKS);
        permissionDeniedList.add(Manifest.permission.VIBRATE);
        permissionDeniedList.add(Manifest.permission.READ_PHONE_STATE);
        permissionDeniedList.add(Manifest.permission.INTERNET);
        permissionDeniedList.add(Manifest.permission.CAMERA);

        //检查权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, LOCATION_PERMISSION_CODE);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            LogUtils.d("权限请求");
            String[] deniedPermissions = permissionDeniedList.toArray(new String[0]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }

    }


    private static final int REQUEST_CODE_MANAGE_STORAGE = 2001;

    // 核心黑魔法：利用反射调用 isExternalStorageManager
    private boolean isExternalStorageManager() {
        if (Build.VERSION.SDK_INT >= 30) { // 直接硬编码 30 代替 Build.VERSION_CODES.R
            try {
                // 动态查找并调用 Environment 类的 isExternalStorageManager 方法
                Method method = Environment.class.getMethod("isExternalStorageManager");
                return (Boolean) method.invoke(null);
            } catch (Exception e) {
                Log.e("Permission", "反射检测权限失败", e);
            }
        }
        return false;
    }

    // 检查并申请权限
    private void checkAndRequestManageStoragePermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!isExternalStorageManager()) {
                // 没有权限，强行使用字符串常量跳转设置页
                try {
                    Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_CODE_MANAGE_STORAGE);
                } catch (Exception e) {
                    // 兜底方案：如果找不到具体应用的页面，就跳到总的全局文件权限管理页
                    Intent intent = new Intent("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
                    startActivityForResult(intent, REQUEST_CODE_MANAGE_STORAGE);
                }
            } else {
                // 已经有权限了，去建文件夹吧！
                FileUtils.initPathlevel1();
                FileUtils.initPathlevel2();
            }
        } else {
            // Android 10 及以下，走传统的 ActivityCompat.requestPermissions 读写权限申请
            // ... 你原有的低版本权限申请逻辑 ...
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.d("onRequestPermissionsResult");
        boolean grantedLocation = true;
        if (requestCode == LOCATION_PERMISSION_CODE) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    grantedLocation = false;
                    break;
                }
            }
        }

        LogUtils.d(grantedLocation);
        if (!grantedLocation) {
            ArmsUtils.snackbarText("Permission error !!!");
            finish();
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

    @OnClick({R.id.nav_main, R.id.nav_record, R.id.nav_settings, R.id.nav_sync})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nav_main:
                showFragment(0);
                break;
            case R.id.nav_record:
                showFragment(1);
                break;
            case R.id.nav_settings:
                showFragment(2);
                break;
            case R.id.nav_sync:
                ArmsUtils.snackbarText("Syncing data...");
                break;
            default:
                break;
        }
    }


    private void showFragment(int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        mNavMain.setSelected(false);
        mNavRecord.setSelected(false);
        mNavSettings.setSelected(false);
        mNavSync.setSelected(false);
        switch (index) {
            case 0:
                mNavMain.setSelected(true);
                if (mMainFragment == null) {
                    mMainFragment = MainFragment.newInstance();
                    transaction.add(R.id.fragment_container, mMainFragment);
                } else {
                    transaction.show(mMainFragment);
                }
                mCurrentFragment = mMainFragment;
                break;

            case 1:
                mNavRecord.setSelected(true);
                if (mTestRecordFragment == null) {
                    mTestRecordFragment = TestRecordFragment.newInstance();
                    transaction.add(R.id.fragment_container, mTestRecordFragment);
                } else {
                    transaction.show(mTestRecordFragment);
                }
                mCurrentFragment = mTestRecordFragment;
                break;
            case 2:
                mNavSettings.setSelected(true);
                if (mSettingFragment == null) {
                    mSettingFragment = SettingFragment.newInstance();
                    transaction.add(R.id.fragment_container, mSettingFragment);
                } else {
                    transaction.show(mSettingFragment);
                }
                mCurrentFragment = mSettingFragment;
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mMainFragment != null) transaction.hide(mMainFragment);
        if (mTestRecordFragment != null) transaction.hide(mTestRecordFragment);
        if (mSettingFragment != null) transaction.hide(mSettingFragment);
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment instanceof TestRecordFragment) {
            boolean consumed = ((IBackPressed) mCurrentFragment).onBackPressed();
            if (consumed) {
                return;
            }
        }
        _exit();


    }


    /**
     * 退出
     */
    private void _exit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, getString(R.string.hint_exit_app), Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            ArmsUtils.exitApp();
        }
    }


}