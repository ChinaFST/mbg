package com.dy.colony.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.hardware.usb.UsbDevice;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.model.entity.eventbus.ExternTestMessageBean;
import com.dy.colony.mvp.model.entity.eventbus.JTJTestMessageBean;
import com.dy.colony.mvp.ui.widget.BaseJTJTestView;
import com.dy.colony.usbhelp.UsbControl;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.JTJ_TestContract;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@ActivityScope
public class JTJ_TestPresenter extends BasePresenter<JTJ_TestContract.Model, JTJ_TestContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    AlertDialog.Builder mAlertDialog;


    @Inject
    public JTJ_TestPresenter(JTJ_TestContract.Model model, JTJ_TestContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }


    @SuppressLint("SetTextI18n")
    public void makeChoseSampleplace(int index) {
        Detection_Record_FGGD_NC detectionRecordFggdNc = (Detection_Record_FGGD_NC) MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(index);
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mRootView.getActivity());
        if (null == detectionRecordFggdNc) {
            ArmsUtils.snackbarText(mApplication.getString(R.string.select_appropriate_channel));
            return;
        }
        LogUtils.d(detectionRecordFggdNc.getGallery());
        View view2 = View.inflate(mRootView.getActivity(), R.layout.chose_sampleplace_layout, null);
        EditText mPlace = (EditText) view2.findViewById(R.id.place);
        mPlace.setText(detectionRecordFggdNc.getSampleplace());
        // LogUtils.d(method);
        LinearLayout checkbos_parent = (LinearLayout) view2.findViewById(R.id.checkbox_parent);
        Map<Integer, CheckBox> mMap = new HashMap<>();
        for (int i = 0; i < MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.size(); i++) {
            GalleryBean bean = MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(i);
            if (!(bean instanceof Detection_Record_FGGD_NC)) {
                return;
            }

            CheckBox box = new CheckBox(view2.getContext());

            if (index == i) {
                box.setChecked(true);
                box.setEnabled(false);
            }
            box.setText(mApplication.getString(R.string.gallery) + bean.getJTJ_MAC());
            checkbos_parent.addView(box);
            mMap.put(i, box);

        }
        LogUtils.d(mMap);

        CheckBox checkall = (CheckBox) view2.findViewById(R.id.checkall);
        checkall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int key : mMap.keySet()) {
                    CheckBox box = mMap.get(key);
                    box.setChecked(isChecked);
                }
            }
        });
        if (mMap.size() == 1) {
            checkall.setVisibility(View.GONE);
        }
        view2.invalidate();
        mAlertDialog.setTitle(mRootView.getActivity().getString(R.string.hint_inputsampleplace));
        mAlertDialog.setView(view2);
        mAlertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = mPlace.getText().toString();
                for (int key : mMap.keySet()) {
                    CheckBox box = mMap.get(key);
                    if (box.isChecked()) {
                        ((Detection_Record_FGGD_NC) MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.get(key)).setSampleplace(s);
                    }
                }
                EventBus.getDefault().post(new ExternTestMessageBean(0, index));

            }
        });
        mAlertDialog.show();
    }

}