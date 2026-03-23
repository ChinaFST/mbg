package com.dy.colony.mvp.presenter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.model.entity.eventbus.FGTestMessageBean;
import com.dy.colony.mvp.ui.adapter.MatchDialogAdapter;
import com.google.gson.Gson;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.dy.colony.mvp.contract.FGGD_TestContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ActivityScope
public class FGGD_TestPresenter extends BasePresenter<FGGD_TestContract.Model, FGGD_TestContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<GalleryBean> mFGGDGalleryBeanList;

    @Inject
    public FGGD_TestPresenter(FGGD_TestContract.Model model, FGGD_TestContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mFGGDGalleryBeanList = null;
    }

    public void cleanValue() {
        for (int i = 0; i < mFGGDGalleryBeanList.size(); i++) {
            GalleryBean bean = mFGGDGalleryBeanList.get(i);
            int state = bean.getState();
            if (state != 1) {
                bean.cleanValue();
            }
        }
        ArmsUtils.snackbarText(mApplication.getString(R.string.cleansuccess));
    }

    public void longCleanValue() {
        for (int i = 0; i < mFGGDGalleryBeanList.size(); i++) {
            GalleryBean bean = mFGGDGalleryBeanList.get(i);
            int state = bean.getState();
            if (state != 1) {
                bean.cleanValueMore();
            }
        }
        ArmsUtils.snackbarText(mApplication.getString(R.string.cleansuccess));
    }


    public void sampleTest() {
        ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).executorService().submit(new Runnable() {
            @Override
            public void run() {
                //1.找出没在检测的通道
                //2.找出已经选择了检测项目的通道 ，并且检测项目对应的波长段AD值必须要小于97%
                List<GalleryBean> gallery = findReStartTestGallery();
                LogUtils.d(gallery);
                List<GalleryBean> gallery1 = new ArrayList<>();
                //做样品时必须选择样品
                for (int i = 0; i < gallery.size(); i++) {
                    GalleryBean bean = gallery.get(i);

                    if (TextUtils.isEmpty(((Detection_Record_FGGD_NC) bean).getSamplename())) { //样品
                        continue;
                    }
                    gallery1.add(bean);
                }
                if (gallery1.size() == 0) {
                    showSampleHintDialog();
                    return;
                }
                //3.按照检测方法是否需要对照值进行分类
                List<List<GalleryBean>> method = findByMethod(gallery1, 1);
                for (int i = 0; i < method.size(); i++) {
                    List<GalleryBean> beans = method.get(i);
                    for (int i1 = 0; i1 < beans.size(); i1++) {
                        GalleryBean bean = beans.get(i1);
                        GalleryBean bean1 = mFGGDGalleryBeanList.get(bean.getGalleryNum() - 1);
                        bean1.startFGGDTest(1);
                    }
                }
            }
        });


    }

    public void controlTest() {
        ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).executorService().submit(new Runnable() {
            @Override
            public void run() {
                //1.找出没在检测的通道
                //2.找出已经选择了检测项目的通道 ，并且对于的波长段AD值必须要小于97%
                List<GalleryBean> gallery = findReStartControGallery();

                if (gallery.size() == 0) {
                    showControlHintDialog();
                    return;
                }
                //3.找出需要做对照的通道并分类
                List<List<GalleryBean>> method = findByMethod(gallery, 2);
                for (int i = 0; i < method.size(); i++) {
                    List<GalleryBean> beans = method.get(i);
                    for (int i1 = 0; i1 < beans.size(); i1++) {
                        GalleryBean bean = beans.get(i1);
                        GalleryBean bean1 = mFGGDGalleryBeanList.get(bean.getGalleryNum() - 1);
                        if (i1 == 0 && (i == 0 || i == 1)) { //需要做对照的检测方法在第一通道做对照
                            bean1.startFGGDTest(2); //
                        } else {
                            if (TextUtils.isEmpty(((Detection_Record_FGGD_NC) bean).getSamplename())) {
                                continue;
                            }
                            bean1.startFGGDTest(1);
                        }
                    }
                }
            }
        });

    }

    /**
     * @param gallery
     * @param dowhat  1样品按钮 2对照按钮
     * @return
     */
    private List<List<GalleryBean>> findByMethod(List<GalleryBean> gallery, int dowhat) {
        List<GalleryBean> method_0_gallery = new ArrayList<>();
        List<GalleryBean> method_1_gallery = new ArrayList<>();
        List<GalleryBean> method_2_gallery = new ArrayList<>();
        List<GalleryBean> method_3_gallery = new ArrayList<>();
        List<List<GalleryBean>> g = new ArrayList<>();
        for (int i = 0; i < gallery.size(); i++) {
            GalleryBean bean = gallery.get(i);
            BaseProjectMessage message = bean.getProjectMessage();
            String method = message.getMethod();
            switch (method) {
                case "0":
                    method_0_gallery.add(bean);
                    break;
                case "1":
                    method_1_gallery.add(bean);
                    break;
                case "2":
                    method_2_gallery.add(bean);
                    break;
                case "3":
                    method_3_gallery.add(bean);
                    break;
            }

        }

        g.add(method_0_gallery);
        g.add(method_1_gallery);
        g.add(method_2_gallery);
        g.add(method_3_gallery);
        String project_name = "";
        if (dowhat == 1 && method_0_gallery.size() != 0) {
            //不允许在没有做对照的时候直接开始样品倒计时，试剂浪费
            BaseProjectMessage projectMessage = method_0_gallery.get(0).getProjectMessage();
            if (projectMessage.getControValue() == 0f) {
                g.remove(method_0_gallery);
                project_name = project_name + projectMessage.getProjectName() + " ";

            }
        }
        if (dowhat == 1 && method_1_gallery.size() != 0) {
            //不允许在没有做对照的时候直接开始样品倒计时，试剂浪费
            BaseProjectMessage projectMessage = method_1_gallery.get(0).getProjectMessage();
            if (projectMessage.getControValue() == 0f) {
                g.remove(method_1_gallery);
                project_name = project_name + projectMessage.getProjectName() + " ";
            }
        }
        if (!project_name.isEmpty()) {
            ArmsUtils.snackbarText(project_name + mApplication.getString(R.string.controlled_needed_first));
        }
        return g;


    }

    private void showControlHintDialog() {
        String message = mApplication.getString(R.string.hint_howtousermessage1);

        if (isAllUsed()) {
            message = mApplication.getString(R.string.allgalleryistesting);
        }
        String finalMessage = message;
        mRootView.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(mRootView.getActivity())
                        .setTitle(MyAppLocation.myAppLocation.getString(R.string.hint))
                        .setMessage(finalMessage)
                        .setNegativeButton(MyAppLocation.myAppLocation.getString(R.string.sure), null).show();
            }
        });

    }

    private void showSampleHintDialog() {
        String message = mApplication.getString(R.string.hint_howtousermessage2);
        if (isAllUsed()) {
            message = mApplication.getString(R.string.allgalleryistesting);
        }
        String finalMessage = message;
        mRootView.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(mRootView.getActivity())
                        .setTitle(mApplication.getString(R.string.hint))
                        .setMessage(finalMessage)
                        .setNegativeButton(mApplication.getString(R.string.sure), null).show();
            }
        });

    }

    /**
     * @return 判断通道是否都在检测
     */
    private boolean isAllUsed() {
        for (int i = 0; i < mFGGDGalleryBeanList.size(); i++) {
            GalleryBean bean = mFGGDGalleryBeanList.get(i);
            if (bean.getState() != 1) {
                return false;
            }
        }
        return true;
    }

    private List<GalleryBean> findReStartTestGallery() {
        List<GalleryBean> freegallery = new ArrayList<>();
        for (int i = 0; i < mFGGDGalleryBeanList.size(); i++) {
            GalleryBean bean = mFGGDGalleryBeanList.get(i);
            int state = bean.getState();

            BaseProjectMessage message = bean.getProjectMessage();
            if (null == message) {
                continue;
            }

            int wavelength = message.getWavelength();

            if (state != 1) {
                double luminousness = 0;
                switch (wavelength) {
                    case 410:
                        luminousness = bean.getLuminousness1();
                        break;
                    case 536:
                        luminousness = bean.getLuminousness2();
                        break;
                    case 595:
                        luminousness = bean.getLuminousness3();
                        break;
                    case 620:
                        luminousness = bean.getLuminousness4();
                        break;
                }
                if (luminousness <= Constants.FGLIMITVALUE_LOW) {
                    freegallery.add(bean);
                }

            }
        }

        return freegallery;
    }

    private List<GalleryBean> findReStartControGallery() {
        List<GalleryBean> freegallery = new ArrayList<>();
        for (int i = 0; i < mFGGDGalleryBeanList.size(); i++) {
            GalleryBean bean = mFGGDGalleryBeanList.get(i);
            int state = bean.getState();
            int wavelength = 0;
            BaseProjectMessage message = bean.getProjectMessage();

            if (state != 1 && null != message) {
                wavelength = message.getWavelength();
                double luminousness = 0;
                switch (wavelength) {
                    case 410:
                        luminousness = bean.getLuminousness1();
                        break;
                    case 536:
                        luminousness = bean.getLuminousness2();
                        break;
                    case 595:
                        luminousness = bean.getLuminousness3();
                        break;
                    case 620:
                        luminousness = bean.getLuminousness4();
                        break;
                }
                if (luminousness <= 0.97) {
                    freegallery.add(bean);
                }

            }
        }

        return freegallery;
    }


    public void makeChoseStandard(Detection_Record_FGGD_NC showbean) {
        String name = showbean.getProjectMessage().getProjectName();
        mModel.loadStandard(name).subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(2, 1))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<? extends BaseSampleMessage>>(mErrorHandler) {
                    @Override
                    public void onNext(List<? extends BaseSampleMessage> messages) {
                        if (messages.size() == 0) {
                            ArmsUtils.snackbarText(mRootView.getActivity().getString(R.string.no_standard_found));
                            return;
                        }
                        //去重，去掉重复的标准和限量值
                        Set set = new HashSet();
                        List newList = new ArrayList();
                        for (Object next : messages) {
                            String element = ((BaseSampleMessage) next).getStandardAndLimitalues();
                            if (set.add(element)) {
                                newList.add(next);
                            }
                        }
                        messages.clear();
                        messages.addAll(newList);
                        mRootView.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                makeChoseDialog(messages, showbean);
                            }
                        });
                    }
                });
    }

    private <T> void makeChoseDialog(List<T> messages, Detection_Record_FGGD_NC detectionRecordFggdNc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mRootView.getActivity());
        View inflate = mRootView.getActivity().getLayoutInflater().inflate(R.layout.chosestandard_dialog_layout, null);
        TextView title1 = (TextView) inflate.findViewById(R.id.title1);
        TextView title_sampleseg = (TextView) inflate.findViewById(R.id.title_sampleseg);
        TextView title2 = (TextView) inflate.findViewById(R.id.title2);
        TextView title3 = (TextView) inflate.findViewById(R.id.title3);
        TextView title4 = (TextView) inflate.findViewById(R.id.title4);
        title1.setText(getString(R.string.testproject));
        title_sampleseg.setVisibility(View.GONE);
        T t = messages.get(0);
        if (t instanceof BaseSampleMessage) {
            title_sampleseg.setVisibility(View.VISIBLE);
            builder.setTitle(R.string.select_standard_limit);
            title1.setText(R.string.testing_standards);
            title2.setText(R.string.contrast_symbols);
            title3.setText(getString(R.string.limitvalue));
            title4.setText(R.string.limit_unit);
        }

        LinearLayout checkbos_parent = (LinearLayout) inflate.findViewById(R.id.checkbox_parent);
        Map<Integer, CheckBox> mMap = new HashMap<>();
        for (int i = 0; i < MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList.size(); i++) {
            GalleryBean bean = MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList.get(i);
            if (!(bean instanceof Detection_Record_FGGD_NC)) {
                return;
            }
            if (null == bean.getProjectMessage()) {
                continue;
            }
            if (!((Detection_Record_FGGD_NC) bean).getTest_project().equals(detectionRecordFggdNc.getTest_project())) {
                continue;
            }
            CheckBox box = new CheckBox(inflate.getContext());
            int gallery = detectionRecordFggdNc.getGalleryNum();
            int gallery1 = bean.getGalleryNum();
            LogUtils.d(gallery1);
            if (gallery1 == gallery) {
                box.setChecked(true);
                box.setEnabled(false);
            }
            box.setText(mRootView.getActivity().getString(R.string.channel_label, bean.getGalleryNum()));
            checkbos_parent.addView(box);
            mMap.put(bean.getGalleryNum(), box);

        }
        LogUtils.d(mMap);

        CheckBox checkall = (CheckBox) inflate.findViewById(R.id.checkall);
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
        inflate.invalidate();

        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        ArmsUtils.configRecyclerView(recyclerView, new GridLayoutManager(mRootView.getActivity(), 1));
        MatchDialogAdapter adapter = new MatchDialogAdapter(R.layout.matchprojectdialog_layout_item, messages);

        recyclerView.setAdapter(adapter);

        builder.setView(inflate);


        AlertDialog dialog = builder.create();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                T t1 = messages.get(position);
                for (int key : mMap.keySet()) {
                    CheckBox box = mMap.get(key);
                    if (box.isChecked()) {
                        ((Detection_Record_FGGD_NC) MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList.get(key - 1)).setStandard(t1);
                    }
                }
                EventBus.getDefault().post(new FGTestMessageBean(0));
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }

    private String getString(@StringRes int resId) {
        return mRootView.getActivity().getString(resId);
    }

    double number = 0;

    public void makeChoseDi_Dro(Detection_Record_FGGD_NC detectionRecordFggdNc) {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mRootView.getActivity());
        if (null == detectionRecordFggdNc) {
            return;
        }
        LogUtils.d(detectionRecordFggdNc.getGallery());
        View view2 = View.inflate(mRootView.getActivity(), R.layout.chose_di_eve_layout, null);
        EditText mNumber = (EditText) view2.findViewById(R.id.numbers);
        BaseProjectMessage message2 = detectionRecordFggdNc.getProjectMessage();
        String method = message2.getMethod();
        String name = message2.getProjectName();

        LinearLayout checkbos_parent = (LinearLayout) view2.findViewById(R.id.checkbox_parent);
        Map<Integer, CheckBox> mMap = new HashMap<>();
        if ("1".equals(method) || mApplication.getString(R.string.mothod2).equals(method)) {  //1 di
            number = detectionRecordFggdNc.getDilutionratio();
            mNumber.setText(String.format("%s", number));
            mAlertDialog.setTitle(R.string.inputdirnumber);
        } else if ("3".equals(method) || mApplication.getString(R.string.mothod4).equals(method)) { //3 eve
            number = detectionRecordFggdNc.getEveryresponse();
            mNumber.setText(String.format("%s", number));
            mAlertDialog.setTitle(R.string.number_reaction_drops);
        }
        view2.findViewById(R.id.sub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = Double.parseDouble(mNumber.getText().toString());
                if (number > 1) {
                    number--;
                    mNumber.setText(String.format("%s", number));
                }
            }
        });
        view2.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = Double.parseDouble(mNumber.getText().toString());
                if (number < 9999) {
                    number++;
                    mNumber.setText(String.format("%s", number));
                }
            }
        });


        for (int i = 0; i < MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList.size(); i++) {
            GalleryBean bean = MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList.get(i);
            if (!(bean instanceof Detection_Record_FGGD_NC)) {
                return;
            }
            Detection_Record_FGGD_NC bean1 = (Detection_Record_FGGD_NC) bean;
            BaseProjectMessage message = bean1.getProjectMessage();
            LogUtils.d(message);
            if (null != message) {
                String method1 = null;
                String name1 = null;
                method1 = message.getMethod();
                name1 = message.getProjectName();

                LogUtils.d(method1 + "," + method + "  " + name1 + "," + name);
                if (method1.equals(method) && name1.equals(name)) {
                    CheckBox box = new CheckBox(view2.getContext());
                    int gallery = detectionRecordFggdNc.getGalleryNum();
                    int gallery1 = bean.getGalleryNum();
                    LogUtils.d(gallery1);
                    if (gallery1 == gallery) {
                        box.setChecked(true);
                        box.setEnabled(false);
                    }
                    box.setText(mRootView.getActivity().getString(R.string.channel_label, bean.getGalleryNum()));
                    checkbos_parent.addView(box);
                    mMap.put(bean.getGalleryNum(), box);
                }
            }
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
        } else {
            checkall.setVisibility(View.VISIBLE);
        }
        view2.invalidate();
        mAlertDialog.setView(view2);
        String finalMethod = method;
        mAlertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                number = 0;
            }
        }).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = mNumber.getText().toString();
                if (s.equals("")) {
                    ArmsUtils.snackbarText(mApplication.getString(R.string.hintnotnull));
                    return;
                }
                number = Double.parseDouble(s);
                for (int key : mMap.keySet()) {
                    CheckBox box = mMap.get(key);
                    if (box.isChecked()) {
                        if ("1".equals(finalMethod) || mApplication.getString(R.string.mothod2).equals(finalMethod)) {
                            ((Detection_Record_FGGD_NC) MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList.get(key - 1)).setDilutionratio(number);
                        } else if ("3".equals(finalMethod) || mApplication.getString(R.string.mothod4).equals(finalMethod)) {
                            ((Detection_Record_FGGD_NC) MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList.get(key - 1)).setEveryresponse(number);
                        }
                    }
                }
                number = 0;
            }
        });
        mAlertDialog.show();
    }

    public void makeChoseSampleplace(Detection_Record_FGGD_NC detectionRecordFggdNc) {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mRootView.getActivity());
        if (null == detectionRecordFggdNc) {
            ArmsUtils.snackbarText(mApplication.getString(R.string.hint_pleaceselectgallery));
            return;
        }
        LogUtils.d(detectionRecordFggdNc.getGallery());
        View view2 = View.inflate(mRootView.getActivity(), R.layout.chose_sampleplace_layout, null);
        EditText mPlace = (EditText) view2.findViewById(R.id.place);
        mPlace.setText(detectionRecordFggdNc.getSampleplace());
        // LogUtils.d(method);
        LinearLayout checkbos_parent = (LinearLayout) view2.findViewById(R.id.checkbox_parent);
        Map<Integer, CheckBox> mMap = new HashMap<>();
        for (int i = 0; i < MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList.size(); i++) {
            GalleryBean bean = MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList.get(i);
            if (!(bean instanceof Detection_Record_FGGD_NC)) {
                return;
            }

            CheckBox box = new CheckBox(view2.getContext());
            int gallery = detectionRecordFggdNc.getGalleryNum();
            int gallery1 = bean.getGalleryNum();
            LogUtils.d(gallery1);
            if (gallery1 == gallery) {
                box.setChecked(true);
                box.setEnabled(false);
            }
            box.setText(mApplication.getString(R.string.gallery) + bean.getGalleryNum());
            checkbos_parent.addView(box);
            mMap.put(bean.getGalleryNum(), box);

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
        mAlertDialog.setNegativeButton(mApplication.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(mApplication.getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = mPlace.getText().toString();
                for (int key : mMap.keySet()) {
                    CheckBox box = mMap.get(key);
                    if (box.isChecked()) {
                        ((Detection_Record_FGGD_NC) MyAppLocation.myAppLocation.mSerialDataService.mFGGDGalleryBeanList.get(key - 1)).setSampleplace(s);
                    }
                }

            }
        });
        mAlertDialog.show();
    }
}