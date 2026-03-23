package com.dy.colony.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dy.colony.Constants;
import com.dy.colony.R;
import com.dy.colony.app.utils.SPUtils;
import com.dy.colony.di.component.DaggerSettingComponent;
import com.dy.colony.greendao.beans.User;
import com.dy.colony.mvp.contract.SettingContract;
import com.dy.colony.mvp.presenter.SettingPresenter;
import com.dy.colony.mvp.ui.activity.LoginActivity;
import com.jess.arms.base.BaseFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment<SettingPresenter> implements SettingContract.View {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_user_info)
    TextView tvUserInfo;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.switch_auto_upload)
    CheckBox switchAutoUpload;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerSettingComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        User user = Constants.NOWUSER;
        if (user != null) {
            String name = user.getName();
            if (name == null || name.isEmpty()) {
                name = user.getUsername();
            }
            tvUsername.setText(name);

            String info = user.getUnit();
            if (info == null || info.isEmpty()) {
                info = user.getJobname();
            }
            if (info == null || info.isEmpty()) {
                info = user.getPhone();
            }
            tvUserInfo.setText(info != null ? info : "");
        }

        // 初始化开关状态
        boolean isAutoUpload = (boolean) SPUtils.get(getActivity(), Constants.KEY_AUTO_UPLOAD_ENABLED, false);
        switchAutoUpload.setChecked(isAutoUpload);
        switchAutoUpload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.put(getActivity(), Constants.KEY_AUTO_UPLOAD_ENABLED, isChecked);
                if (isChecked) {
                    showMessage(getString(R.string.auto_upload_enabled));
                } else {
                    showMessage(getString(R.string.autoupload_disabled));
                }
            }
        });
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        Constants.NOWUSER = null;
        // 如果需要清除持久化的用户信息，可以根据需求添加
        // SPUtils.remove(getActivity(), Constants.KEY_USERINFOR_JSON);

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }


    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {
        ArmsUtils.snackbarText(message);
    }
}