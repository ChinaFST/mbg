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
    private List<UsbDevice> mDeviceList;


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

    public void makeChoseSeachMethodDialog(List<BaseJTJTestView> views) {
        List<BaseJTJTestView> reStartviews = new ArrayList<>();
        List<BaseJTJTestView> startviews = new ArrayList<>();

        View view = View.inflate(mRootView.getActivity(), R.layout.chose_scanner_method_layout_jtj_s_p, null);
        LinearLayout group = (LinearLayout) view.findViewById(R.id.group);
        for (int i = 0; i < views.size(); i++) {
            BaseJTJTestView bean = views.get(i);
            int state = bean.getGallery().getState();
            if (state == 1) {
                startviews.add(bean);
            }
            if (state != 1 && bean.checkMessageState()) {
                View gallery = View.inflate(mRootView.getActivity(), R.layout.chose_timer_layout, null);
                ((TextView) gallery.findViewById(R.id.jtj_gallerynum)).setText(mApplication.getResources().getString(R.string.gallery) + bean.getGallery().getJTJ_MAC() + mApplication.getResources().getString(R.string.will_be));
                EditText times = (EditText) gallery.findViewById(R.id.tiemrs);
                BaseProjectMessage message = bean.getGallery().getProjectMessage();
                times.setText(message.getTestTime() + "");
                gallery.setTag(i);
                group.addView(gallery);
                reStartviews.add(bean);
            }
        }
        if (startviews.size() == MyAppLocation.myAppLocation.mSerialDataService.mJTJGalleryBeanList.size()) {
            ArmsUtils.snackbarText(mApplication.getResources().getString(R.string.no_free_channel));
            return;
        }
        if (reStartviews.size() == 0) {
            ArmsUtils.snackbarText(mApplication.getString(R.string.pleacechoseitemfirst));
            return;
        }

        mAlertDialog.setTitle(mApplication.getResources().getString(R.string.select_detection_mode)).setView(view);
        mAlertDialog.setPositiveButton(mApplication.getText(R.string.start_countdown), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    View at = group.getChildAt(i);
                    int tag = (int) at.getTag();
                    BaseJTJTestView view1 = views.get(tag);
                    String s = ((EditText) at.findViewById(R.id.tiemrs)).getText().toString();
                    if ("".equals(s)) {
                        ArmsUtils.snackbarText(mApplication.getResources().getString(R.string.invalid_input_value));
                        continue;
                    }
                    int integer = Integer.parseInt(s);
                    view1.getGallery().startCountdown_JTJ(integer);
                }

            }
        }).setNegativeButton(mApplication.getText(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNeutralButton(R.string.start_detection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < reStartviews.size(); i++) {
                    BaseJTJTestView view1 = reStartviews.get(i);
                    view1.startTest();
                }
            }
        });
        mAlertDialog.show();

    }
}