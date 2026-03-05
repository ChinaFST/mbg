package com.dy.colony.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.R;
import com.dy.colony.app.utils.ByteUtils;
import com.dy.colony.di.component.DaggerHomeComponent;
import com.dy.colony.mvp.contract.HomeContract;
import com.dy.colony.mvp.presenter.HomePresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {

    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.btn_color)
    Button mBtnColor;

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
        byte[] array = ByteUtils.getBooleanArray((byte) 0x40);
        LogUtils.d(array);

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
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
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
                }
            }
        }

        LogUtils.d(grantedLocation);
        if (!grantedLocation) {
            ArmsUtils.snackbarText("Permission error !!!");
            finish();
            return;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_start, R.id.btn_color})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                ArmsUtils.startActivity(new Intent(HomeActivity.this, CountActivity.class));
                break;
            case R.id.btn_color:


                break;
        }
    }


}