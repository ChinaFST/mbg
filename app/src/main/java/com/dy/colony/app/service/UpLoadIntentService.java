package com.dy.colony.app.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.apkfuns.logutils.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.app.utils.BeanUtils;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.model.api.service.Platform_Service;
import com.dy.colony.mvp.model.entity.ObjUserData;
import com.dy.colony.mvp.model.entity.Platform_UploadBack;
import com.dy.colony.mvp.model.entity.UpLoadBean;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.EventBus;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.annotations.NonNull;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class UpLoadIntentService extends IntentService {
    private static final String ACTION_KJFW = "action.ACTION_KJFW";
    private static final String EXTRA_PARAM1 = "extra.PARAM1";
    private static final String ACTION_KJFW_LISTR = "com.wangzx.dy.sample.app.service.action.ACTION_KJFW_LIST";



    public UpLoadIntentService() {
        super("UpLoadIntentService");
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startUpLoad(Context context, Long index) {
        Intent intent = new Intent(context, UpLoadIntentService.class);
        intent.setAction(ACTION_KJFW);
        Detection_Record_FGGD_NC load = DBHelper.getDetection_Record_FGGD_NCDao(context).load(index);
        Bundle value = new Bundle();
        value.putParcelable("data", load);
        intent.putExtra(EXTRA_PARAM1, value);
        context.startService(intent);
    }

    public static void startUpLoadList(Context context, ArrayList<Detection_Record_FGGD_NC> data) {
        Intent intent = new Intent(context, UpLoadIntentService.class);
        intent.setAction(ACTION_KJFW_LISTR);
        //Detection_Record_FGGD_NC load = DBHelper.getDetection_Record_FGGD_NCDao(context).load(index);
        Bundle value = new Bundle();
        value.putParcelableArrayList("data", data);
        intent.putExtra(EXTRA_PARAM1, value);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_KJFW.equals(action)) {
                final Bundle param1 = intent.getBundleExtra(EXTRA_PARAM1);
                Detection_Record_FGGD_NC data = (Detection_Record_FGGD_NC) param1.get("data");
                handleActionUpLoad(data);
            }else if (ACTION_KJFW_LISTR.equals(action)) {
                final Bundle param1 = intent.getBundleExtra(EXTRA_PARAM1);
                ArrayList<Detection_Record_FGGD_NC> data = param1.getParcelableArrayList("data");
                handleActionUpLoad_list(data);
            }
        }
    }


    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpLoad(Detection_Record_FGGD_NC data) {
        Detection_Record_FGGD_NC detection_record_fggd_nc = DBHelper.getDetection_Record_FGGD_NCDao(this).load(data.getId());
        uploadData(detection_record_fggd_nc);
    }
    private void handleActionUpLoad_list(ArrayList<Detection_Record_FGGD_NC> data) {

    }

    private void uploadData(Detection_Record_FGGD_NC nc) {
        if (Constants.IS_OFFLINE_MODE) {
            ArmsUtils.snackbarText(getString(R.string.offline_mode_hint));
            return;
        }
        if (nc.getIsupload() == 1) {
            ArmsUtils.snackbarText(getString(R.string.record_has_uploaded));
            return;
        }

        String decisionoutcome = nc.getDecisionoutcome();
        if (!getString(R.string.ok).equals(decisionoutcome) && !getString(R.string.ng).equals(decisionoutcome)) {
            ArmsUtils.snackbarText(getString(R.string.testresult) + "：" + decisionoutcome + getString(R.string.Upload_not_supported));
            return;
        }
        StringBuilder builder = new StringBuilder();
        List<Detection_Record_FGGD_NC> list = new ArrayList<>();
        list.add(nc);
        ObjUserData userPlatform = Constants.USER_PLATFORM;
        ObjUserData.UserDTO user = userPlatform.getUser();
        String bean = getUploadBean(list, user);
        builder.append("results=").append(bean).append("&userToken=").append(userPlatform.getToken());
        LogUtils.d(builder.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), String.valueOf(builder));
        RxErrorHandler handler = ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler();
        RetrofitUrlManager instance = RetrofitUrlManager.getInstance();
        instance.putDomain("xxx", Platform_Service.URL);
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager()
                .obtainRetrofitService(Platform_Service.class)
                .uploadUcData(body)
                .subscribe(new ErrorHandleSubscriber<Platform_UploadBack>(handler) {
                    @Override
                    public void onNext(@NonNull Platform_UploadBack back) {
                        LogUtils.d(back);
                        Platform_UploadBack.ObjBean data = back.getObj();
                        if (data == null) {
                            ArmsUtils.snackbarText(back.getMsg());
                            return;
                        }
                        if (data.getSuccessNum() != 0) {
                            nc.setIsupload(1);
                            DBHelper.getDetection_Record_FGGD_NCDao(UpLoadIntentService.this).update(nc);
                            //通知更新UI
                            EventBus.getDefault().post(nc);
                        } else {
                            ArmsUtils.snackbarText(back.getMsg());
                        }
                    }
                });

    }

    private String getUploadBean(List<Detection_Record_FGGD_NC> data, ObjUserData.UserDTO user) {

        try {
            List<UpLoadBean> upLoadBeans = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                Detection_Record_FGGD_NC nc = data.get(i);

                UpLoadBean bean = new UpLoadBean();
                bean.setId(nc.getSysCode());
                //任务相关信息

                //bean.setTaskName(nc.getPlanName());
                //样品相关信息

                bean.setFoodName(nc.getSamplename());
                //检测项目相关信息   因为没有用平台的检测项目，这里需要通过本地检测项目名称去匹配平台的检测项目

                bean.setItemName(nc.getTest_project());
                //检测值与限量值相关信息

                String symbol = nc.getSymbol();
                String cov = nc.getCov();
                bean.setLimitValue(symbol + cov);

                bean.setCheckAccord(nc.getStand_num());
                String cov_unit = nc.getCov_unit();
                bean.setCheckUnit(cov_unit);
                String decisionoutcome = nc.getDecisionoutcome();

                String testresult = nc.getTestresult();
                bean.setCheckResult(testresult);
                bean.setConclusion(decisionoutcome);
                //被检单位相关信息
                bean.setRegName(nc.getProsecutedunits());

                //检测人员相关信息
                bean.setCheckUserid(user.getId());
                bean.setCheckUsername(user.getRealname());
                bean.setCheckDate(nc.getdfTestingtimeyy_mm_dd_hh_mm_ss());
                bean.setUploadId(user.getId());
                bean.setUploadName(user.getRealname());
                //bean.setUploadDate();
                //检测仪器相关信息
                //bean.setStatusFalg(1 + "");

                bean.setDeviceName(getString(R.string.device_name));
                String moudle = nc.getTest_Moudle();
                if ("1".equals(moudle)) {
                    bean.setDeviceModel(getString(R.string.fggd_module));
                } else if ("2".equals(moudle)) {
                    bean.setDeviceModel(getString(R.string.jtj_module));
                } else {
                    bean.setDeviceModel(moudle);
                }

                bean.setDeviceMethod(nc.getTest_method());
                bean.setDeviceCompany(getString(R.string.login_company_name));

                bean.setDataSource(1 + "");
                bean.setDataType(1 + "");
                /*if (nc.getUnique_task().equals("")){//送检
                    bean.setDataType(1 + "");
                }else {//抽样单
                    bean.setDataType(0 + "");
                }*/

                bean.setParam2(null);
                bean.setParam3(null);
                bean.setParam8(nc.getLongitude() + "," + nc.getLatitude() + "," + nc.getTestsite());

                upLoadBeans.add(bean);


            }
            LogUtils.d(upLoadBeans);
            String s = JSON.toJSONString(upLoadBeans);
            LogUtils.d(s);
            return URLEncoder.encode(s);
        } catch (Exception e) {
            ArmsUtils.snackbarText(e.getMessage());
        }
        return "";
    }


}
