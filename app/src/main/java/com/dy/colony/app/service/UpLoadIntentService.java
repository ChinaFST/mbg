package com.dy.colony.app.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.apkfuns.logutils.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;
import com.dy.colony.R;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.mvp.model.api.service.Platform_Service;
import com.dy.colony.mvp.model.entity.Platform_UploadBack;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

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


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_KJFW.equals(action)) {
                final Bundle param1 = intent.getBundleExtra(EXTRA_PARAM1);
                Detection_Record_FGGD_NC data = (Detection_Record_FGGD_NC) param1.get("data");
                handleActionUpLoad(data);
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


    private void uploadData(Detection_Record_FGGD_NC nc) {
        if (nc.getIsupload() == 1) {
            ArmsUtils.snackbarText(getString(R.string.record_has_uploaded));
            return;
        }

        String decisionoutcome = nc.getDecisionoutcome();
        if (!getString(R.string.ok).equals(decisionoutcome) && !getString(R.string.ng).equals(decisionoutcome)) {
            ArmsUtils.snackbarText(getString(R.string.testresult) + "：" + decisionoutcome + getString(R.string.Upload_not_supported));
            return;
        }


        RxErrorHandler handler = ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler();
        RetrofitUrlManager instance = RetrofitUrlManager.getInstance();
        instance.putDomain("xxx", Platform_Service.URL);
        ArmsUtils.obtainAppComponentFromContext(this).repositoryManager()
                .obtainRetrofitService(Platform_Service.class)
                .upload(getcjBody(nc))
                .subscribe(new ErrorHandleSubscriber<Platform_UploadBack>(handler) {
                    @Override
                    public void onNext(@NonNull Platform_UploadBack back) {
                        LogUtils.d(back);
                        Platform_UploadBack.DataBean data = back.getData();
                        if (data == null) {
                            ArmsUtils.snackbarText(back.getMsg());
                            return;
                        }
                        if (data.getSuccess() == 1) {
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

    private RequestBody getcjBody(Detection_Record_FGGD_NC nc) {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("token", Constants.NOWUSER.getEmail());
        com.alibaba.fastjson.JSONArray value = new com.alibaba.fastjson.JSONArray();
        com.alibaba.fastjson.JSONObject e = new com.alibaba.fastjson.JSONObject();

        String limit = nc.getSymbol() + nc.getCov();
        String testresult = nc.getTestresult();
        String cov_unit = nc.getCov_unit();
        String decisionoutcome = nc.getDecisionoutcome();
        String samplename = nc.getSamplename();
        String test_project = nc.getTest_project();
        String unique_sample = nc.getUnique_sample();
        String unique_testproject = nc.getUnique_testproject();


        StringBuilder builder = new StringBuilder(test_project);

        String itemResult = builder.toString();
        System.out.println(itemResult);

        //唯一 id
        e.put("checkCode", nc.getSysCode());
        //抽样单 id
        e.put("sampleId", nc.getUnique_task());
        //样品 id
        e.put("foodId", unique_sample);
        //样品名称
        e.put("foodName", samplename);
        //检测项目 id
        e.put("itemId", unique_testproject);
        //检测项目名称
        e.put("itemName", itemResult);
        e.put("regName", nc.getProsecutedunits());
        e.put("checkDate", nc.getdfTestingtimeyy_mm_dd_hh_mm_ss());
        e.put("checkAccord", nc.getStand_num());
        e.put("limitValue", limit);
        e.put("checkResult", testresult);
        e.put("checkUnit", cov_unit);
        e.put("conclusion", decisionoutcome);
        //e.put("deviceName", BuildConfig.DEVICE_MODEL_NAME);
        e.put("deviceModel", nc.getTest_Moudle());
        e.put("deviceMethod", nc.getTest_method());
        e.put("position", nc.getLongitude() + "," + nc.getLatitude());

        value.add(e);
        jsonObject.put("records", value);

        MediaType mediaType = MediaType.parse("application/json");
        String content = jsonObject.toJSONString();
        LogUtils.d(content);
        RequestBody body = RequestBody.create(mediaType, content);
        return body;
    }


}
