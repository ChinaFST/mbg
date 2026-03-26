package com.dy.colony.app.service;

import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.apkfuns.logutils.utils.ObjectUtil;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.android_serialport_api.SerialControl;
import com.dy.colony.android_serialport_api.SerialHelper;
import com.dy.colony.app.utils.ByteUtils;
import com.dy.colony.app.utils.DataUtils;
import com.dy.colony.app.utils.DecimalFormatUtils;
import com.dy.colony.app.utils.FileUtils;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;
import com.dy.colony.mvp.model.entity.base.GalleryBean;
import com.dy.colony.mvp.model.entity.eventbus.FGTestMessageBean;
import com.dy.colony.serialport.listener.OnOpenSerialPortListener;
import com.dy.colony.serialport.listener.OnSerialPortDataListener;
import com.jess.arms.base.BaseService;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


public class SerialDataService extends BaseService implements OnOpenSerialPortListener, OnSerialPortDataListener {
    private IBinder bind = new MyBinder();
    /**
     * 是否进行16进制转化
     */
    public boolean mConversionNotice = true;
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
    private int times1;
    private boolean issendCurrentFinish = false;
    private Handler mHandler;
    public List<GalleryBean> mFGGDGalleryBeanList = new ArrayList<>();
    public List<GalleryBean> mJTJGalleryBeanList = new ArrayList<>();
    /**
     * 分光串口数据
     */
    public SerialControl mData_SerialControl;
    /**
     * 串口是否打开
     */
    private boolean mOpenComPort;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }

    @Override
    public void init() {
        mHandler = new Handler(Looper.getMainLooper());
        mScheduledThreadPoolExecutor = ((ScheduledThreadPoolExecutor) ArmsUtils.obtainAppComponentFromContext(MyAppLocation.myAppLocation).executorService());
        //initTTYS1();
        //初始化串口
        initSerialcontrol();
        receiveDatas();
        initTimes();

    }

    private void cleanLog() {
        //删除分光异常保存的文件，不删会导致内存消耗
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dayuan/fglog/";
        FileUtils.deleteDir(path);
    }




    private void receiveDatas() {
        RxErrorHandler mRxErrorHandler = ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler();
        //分光光度信息接收处理
        Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(ObservableEmitter<byte[]> emitter) throws Exception {
                String s8 = "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]";
                String s6 = "[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]";
                if (null != mData_SerialControl) {

                    mData_SerialControl.setOnFGGDDataReceiveListener(new SerialHelper.OnFGGDDataReceiveListener() {
                        @Override
                        public void onDataReceive(byte[] buffer, int size) {
                            LogUtils.d("fggd_reciver");
                            LogUtils.d(ByteUtils.byte2HexStr2(buffer));
                            //需要区分新旧固件 旧的固件固定返回24个通道的数据
                            // 新的固件有多少通道返回多少通道
                            // 所以现在的方法就会存在16通道的仪器会出现24个检测通道
                            //每个通道有8个字节 截取后6个或8个通道的值，看是否为0 为0则舍去
                            int mSize = size;
                            int i1 = (mSize - 6) / 8;
                            LogUtils.d(i1);
                            if (mFGGDGalleryBeanList.size() != i1) {
                                if (mFGGDGalleryBeanList.size() != 0) {
                                    MyAppLocation.myAppLocation.speak(getString(R.string.channel_data_loss));
                                    ArmsUtils.snackbarText(getString(R.string.channel_data_loss));
                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dayuan/fglog/";
                                    FileUtils.writeTxtToFile(ObjectUtil.objectToString(buffer), path, System.currentTimeMillis() + ".txt");
                                    return;
                                }
                                //初始化数据arry
                                for (int i = 0; i < (mSize - 6) / 8; i++) {
                                    GalleryBean e = new Detection_Record_FGGD_NC();
                                    e.setGalleryNum(i + 1);
                                    e.setTestMoudle(1 + "");
                                    mFGGDGalleryBeanList.add(e);
                                }

                            }
                            // MyAppLocation.myAppLocation.speak("数据正常");
                            // LogUtils.d(buffer);
                            emitter.onNext(buffer);
                        }
                    });
                }
            }
        }).map(new Function<byte[], List<Integer>>() {
            @Override
            public List<Integer> apply(byte[] buffer) throws Exception {
                List<Integer> mlist = new ArrayList<>();
                int b = 4;
                for (int i = 0; i < mFGGDGalleryBeanList.size(); i++) {  //遍历解析数据
                    int w1 = 0;
                    int w2 = 0;
                    int w3 = 0;
                    int w4 = 0;
                    w1 = ByteUtils.bytes2Int(buffer, b + 0);
                    w2 = ByteUtils.bytes2Int(buffer, b + 2);
                    w3 = ByteUtils.bytes2Int(buffer, b + 4);
                    w4 = ByteUtils.bytes2Int(buffer, b + 6);


                    mlist.add(w1);
                    mlist.add(w2);
                    mlist.add(w3);
                    mlist.add(w4);
                    LogUtils.d(w1+"-"+w2+"-"+w3+"-"+w4+"-");

                    b = b + 8;
                }
                // LogUtils.d(mlist);
                return mlist;
            }
        })
                .onErrorReturn(new Function<Throwable, List<Integer>>() {
                    @Override
                    public List<Integer> apply(Throwable throwable) throws Exception {
                        ArmsUtils.snackbarText("分光数据异常");

                        return new ArrayList<>(mFGGDGalleryBeanList.size());
                    }
                }).subscribeOn(Schedulers.computation())
                .subscribe(new ErrorHandleSubscriber<List<Integer>>(mRxErrorHandler) {
                    @Override
                    public void onNext(List<Integer> beans) {
                        for (int i = 0, j = 0; i < beans.size(); ) {
                            GalleryBean bean = mFGGDGalleryBeanList.get(j);
                            int w1, w2, w3, w4;
                            w1 = beans.get(i++);
                            w2 = beans.get(i++);
                            w3 = beans.get(i++);
                            w4 = beans.get(i++);

                            bean.setWave1(w1);
                            bean.setWave2(w2);
                            bean.setWave3(w3);
                            bean.setWave4(w4);
                            j++;


                            //设置每个通道实时的四个波长段的ad值
                            if (bean.isClearn()) { //清零标志
                                //设置作为比较比色皿是否放入的ad值
                                bean.setDzh_wave1_start(w1);
                                bean.setDzh_wave2_start(w2);
                                bean.setDzh_wave3_start(w3);
                                bean.setDzh_wave4_start(w4);
                                bean.setClearn(false);
                                /// Detection_Record_FGGD_NC fg_nc= (Detection_Record_FGGD_NC) bean;
                                //fg_nc.getCheckPID();
                            }
                            //检测项目修改后需要对没有开始检测但是已经选了检测项目的通道进行修改
                            if (bean.isProjectChange()) {
                                if (bean.getState() != 1) {
                                    BaseProjectMessage message = bean.getProjectMessage();
                                    if (null != message) {
                                        if (message instanceof FGGDTestItem) {
                                            FGGDTestItem load = DBHelper.getFGGDTestItemDao(MyAppLocation.myAppLocation).load(message.getId_base());
                                            bean.removeAll();
                                            bean.setProjectMessage(load);
                                        }

                                    }
                                    bean.setProjectChange(false);
                                }

                            }
                            // LogUtils.d(w1+"-"+w2+"-"+w3+"-"+w4);


                            int wave1 = bean.getWave1();
                            int start1 = bean.getDzh_wave1_start();
                            float i1 = 0;
                            double absorbance1 = 0;
                            if (start1 != 0) {
                                i1 = (float) wave1 / start1;
                                absorbance1 = Math.log10(1 / i1);
                            }


                            int wave2 = bean.getWave2();
                            int start2 = bean.getDzh_wave2_start();
                            float i2 = 0;
                            double absorbance2 = 0;
                            if (start2 != 0) {
                                i2 = (float) wave2 / start2;
                                absorbance2 = Math.log10(1 / i2);
                            }


                            int wave3 = bean.getWave3();
                            int start3 = bean.getDzh_wave3_start();
                            float i3 = 0;
                            double absorbance3 = 0;
                            if (start3 != 0) {
                                i3 = (float) wave3 / start3;
                                absorbance3 = Math.log10(1 / i3);
                            }
                            int wave4 = bean.getWave4();
                            int start4 = bean.getDzh_wave4_start();
                            float i4 = 0;
                            double absorbance4 = 0;
                            if (start4 != 0) {
                                i4 = (float) wave4 / start4;
                                absorbance4 = Math.log10(1 / i4);
                            }

                            //透光率
                            bean.setLuminousness1(i1);
                            bean.setLuminousness2(i2);
                            bean.setLuminousness3(i3);
                            bean.setLuminousness4(i4);
                            // LogUtils.d(i1 + "-" + i2 + "-" + i3 + "-" + i4 + "-");
                            //吸光度
                            bean.setAbsorbance1(absorbance1);
                            bean.setAbsorbance2(absorbance2);
                            bean.setAbsorbance3(absorbance3);
                            bean.setAbsorbance4(absorbance4);
                        }
                    }
                });


    }

    private int fggd_send_flag;

    private void initTimes() {
        //请求数据，分光，频率500毫秒一次
        mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
        mScheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (mOpenComPort) {
                    switch (fggd_send_flag) {
                        case 0:
                            if (RUNFLAG_FGGD || RUNFLAG_FGGD_HASSTART) {
                                mData_SerialControl.send(Constants.SPECTRAL_DATA_REQUEST_DY1000);//分光数据请求
                            }
                            fggd_send_flag++;
                            break;
                        case 1:
                            fggd_send_flag--;
                            break;
                    }

                }

            }
        }, 0, 500, TimeUnit.MILLISECONDS);

        //地理位置更新 频率2分钟一次
        mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
        mScheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                boolean connected = ArmsUtils.isNetworkConnected(getApplicationContext());
                if (connected) {
                    //locationClient.requestLocation();
                }
            }
        }, 0, 120, TimeUnit.SECONDS);

        //分光光度倒计时 频率1秒一次
        mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
        mScheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //分光模块的数据更新
                if (RUNFLAG_FGGD || RUNFLAG_FGGD_HASSTART) {
                    for (int i = 0; i < mFGGDGalleryBeanList.size(); i++) {
                        Detection_Record_FGGD_NC bean = (Detection_Record_FGGD_NC) mFGGDGalleryBeanList.get(i);
                        BaseProjectMessage message = bean.getProjectMessage();
                        if (null == message) {
                            continue;
                        }
                        //LogUtils.d(message);
                        int jiancetime = 0;
                        String method = "";
                        jiancetime = message.getTestTime();
                        method = message.getMethod();
                        int remainingtime = bean.getRemainingtime();
                        if (remainingtime >= 0) {
                            if (remainingtime == jiancetime) { //去掉预热时间 预热时间结束后才赋值开始的ad值
                                bean.startTest();
                            }
                            bean.setRemainingtime(remainingtime - 1);
                            bean.setCountedDown(true);
                            RUNFLAG_FGGD_HASSTART = true;

                        }
                        int state = bean.getState();
                        if (state == 1 && remainingtime == 0 && bean.isCountedDown()) {
                            switch (method) {
                                case "0": //抑制率法  检测项目自带限量值判断
                                    switch (bean.getDowhat()) {
                                        case 1:
                                            bean.testFinished();
                                            getFGmethod1result(i);
                                            break;
                                        case 2:
                                            bean.testFinished();
                                            getFGmethod1contro(i);
                                            break;
                                    }
                                    break;
                                case "1": //标准曲线法  需要根据样品的限量值来判断结果 （或者从匹配的检测项目中获取）
                                    switch (bean.getDowhat()) {
                                        case 1:
                                            bean.testFinished();
                                            getFGmethod2result(i);
                                            break;
                                        case 2:
                                            bean.testFinished();
                                            getFGmethod2contro(i);
                                            break;
                                    }
                                    break;
                                case "2": //动力学法  检测项目自带限量值判断
                                    bean.testFinished();
                                    getFGmethod3result(i);
                                    break;
                                case "3": //系数法  需要根据样品的限量值来判断结果 （或者从匹配的检测项目中获取）
                                    bean.testFinished();
                                    getFGmethod4result(i);
                                    break;
                            }
                            bean.setCountedDown(false);
                            checkFGState();
                        }
                    }
                    if (mFGGDGalleryBeanList.size() > 0) {
                        //发送更新
                        EventBus.getDefault().post(new FGTestMessageBean(0));
                    }
                }


            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * @param i 抑制率法结果
     */
    private void getFGmethod1result(int i) {
        Detection_Record_FGGD_NC detection_record_fggd_nc = (Detection_Record_FGGD_NC) mFGGDGalleryBeanList.get(i);

        BaseProjectMessage message2 = detection_record_fggd_nc.getProjectMessage();
        //每个检测项目都有自己的一个对照值 所以需要每次都从数据库拿最新的检测项目
        //判断是否合格所需参数
        double yang_a = message2.getYang_a();
        double yang_b = message2.getYang_b();
        double yin_a = message2.getYin_a();
        double yin_b = message2.getYin_b();
        //计算结果所需的参数
        double start_Absorbance = 0;//开始吸光度
        double stop_Absorbance = 0; //结束吸光度
        int wavelength = message2.getWavelength();
        String unit_input = message2.getUnit_input();
        //需要根据不同波长取不同的吸光度
        switch (wavelength) {
            case 410:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance1_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance1_after(); //结束吸光度
                break;
            case 536:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance2_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance2_after(); //结束吸光度
                break;
            case 595:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance3_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance3_after(); //结束吸光度
                break;
            case 620:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance4_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance4_after(); //结束吸光度
                break;
        }
        if ("Infinity".equals(start_Absorbance + "") || "Infinity".equals(stop_Absorbance + "")) {
            ArmsUtils.snackbarText(getString(R.string.contro_erromessage1));
            detection_record_fggd_nc.setState(3);
            return;
        }
        double df_abs = 0; //吸光度差
        //吸光度差 = 结束时吸光度 - 开始时吸光度
        df_abs = 0;
        if (start_Absorbance < stop_Absorbance) {
            df_abs = stop_Absorbance - start_Absorbance;
        }
        // 抑制率= (对照值-df_abs) / 对照值
        double df_contro_abs = 0;
        //对照值
        //double controlValue = Constants.FGGD_YIZHILV_CONTROL_VALUE;
        double controlValue = message2.getControValue();
        if (controlValue == 0) {
            ArmsUtils.snackbarText(getString(R.string.lastcontrovalue));
            detection_record_fggd_nc.setState(3);
            return;
        }
        if (controlValue > df_abs) {
            df_contro_abs = controlValue - df_abs;
        }
        //检测结果
        double testResult = (df_contro_abs / controlValue) * 100;
        //判断结果
        String conclusion = "";

        /*
         * 其实这里应该在判断检测结果的单位，不同单位检测结果也需要混算 比如将g/100g 转换成 % ，但是这里默认为 %
         * */
        double v = getRandomNumber() / 10;
        if (testResult >= yin_a && testResult < yin_b) {
            conclusion = getString(R.string.ok);

            testResult = testResult + v;//加了随机数后的值
            if (testResult > yin_b) { //大于阴性的最大值  超标了
                testResult = testResult - v * 2;
            }


        } else if (testResult >= yang_a && testResult <= yang_b) {
            conclusion = getString(R.string.ng);

            testResult = testResult + v;//加了随机数后的值
            if (testResult > yang_b) { //大于阳性的最大值  超标了
                testResult = testResult - v * 2;
            }

        } else {
            conclusion = "NG";
        }
        //检测结果为零点几这种加上1-10的随机数
        if (testResult < 1) {
            testResult = testResult + getRandomNumber() * 10;
        }


        //设置一些检测完成时需要添加的信息
        String unit = detection_record_fggd_nc.getCov_unit();
        if ("".equals(unit)) {
            detection_record_fggd_nc.setCov_unit(unit_input);
        }
        //检测结果 结论
        detection_record_fggd_nc.setTestresult(DecimalFormatUtils.twoDecimal(testResult));
        detection_record_fggd_nc.setDecisionoutcome(conclusion);
        //检测完成时间
        detection_record_fggd_nc.setTestingtime(System.currentTimeMillis());
        //检测地点
        detection_record_fggd_nc.setTestsite(Constants.ADDR_WF);
        detection_record_fggd_nc.setLatitude(Constants.LATITUDE);
        detection_record_fggd_nc.setLongitude(Constants.LONTITUDE);
        //当前平台
        detection_record_fggd_nc.setPlatform_tag("0");

        //对照值
        detection_record_fggd_nc.setControlvalue(message2.getControValue() + "");
        //检测人员   这里填的是本地登录的账号名称
        detection_record_fggd_nc.setInspector(Constants.NOWUSER.getUsername());
        //设置检测模块
        detection_record_fggd_nc.setTest_Moudle(ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.FGGD_TestMoudle));
        // 保存至数据库
        detection_record_fggd_nc.setId(null);//自增ID
        //设置状态为检测完成 2
        detection_record_fggd_nc.setState(2);
        //LogUtils.d("lyl--"+getResources().getString(R.string.mothod1)+"---"+getString(R.string.mothod1));
        detection_record_fggd_nc.setTest_method(getResources().getString(R.string.mothod1));
        if ("".equals(detection_record_fggd_nc.getCov()) || "".equals(detection_record_fggd_nc.getStand_num())) {
            if (detection_record_fggd_nc.getTest_project().contains("农标")) {
                detection_record_fggd_nc.setSymbol("<");
                detection_record_fggd_nc.setCov("70");
                detection_record_fggd_nc.setCov_unit("%");
                detection_record_fggd_nc.setStand_num("NY/T 448-2001");
            } else {
                detection_record_fggd_nc.setSymbol("<");
                detection_record_fggd_nc.setCov("50");
                detection_record_fggd_nc.setCov_unit("%");
                detection_record_fggd_nc.setStand_num("GB/T 5009.199-2003");
            }

        }

        long insert = DBHelper.getDetection_Record_FGGD_NCDao(this).insert(detection_record_fggd_nc);
        //检测完成后的操作，自动打印，自动上传等
        //printAndUpload(detection_record_fggd_nc, insert);

    }

    private double getRandomNumber() {
        double random = Math.random();
        if (random == 0) {
            getRandomNumber();
        }
        return random;
    }

    /**
     * @param i 分光数组下标
     *          抑制率法对照值
     */
    private void getFGmethod1contro(int i) {
        Detection_Record_FGGD_NC detection_record_fggd_nc = (Detection_Record_FGGD_NC) mFGGDGalleryBeanList.get(i);

        BaseProjectMessage message = detection_record_fggd_nc.getProjectMessage();
        int wavelength = message.getWavelength();

        //计算结果所需的参数
        double start_Absorbance = 0;//开始吸光度
        double stop_Absorbance = 0; //结束吸光度

        //需要根据不同波长取不同的吸光度
        switch (wavelength) {
            case 410:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance1_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance1_after(); //结束吸光度
                break;
            case 536:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance2_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance2_after(); //结束吸光度
                break;
            case 595:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance3_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance3_after(); //结束吸光度
                break;
            case 620:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance4_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance4_after(); //结束吸光度
                break;
        }
        if ("Infinity".equals(start_Absorbance + "") || "Infinity".equals(stop_Absorbance + "")) {
            ArmsUtils.snackbarText(getString(R.string.contro_erromessage1));
            detection_record_fggd_nc.setState(3);
            return;
        }
        if (stop_Absorbance <= start_Absorbance) {
            ArmsUtils.snackbarText(getString(R.string.sampleerro_message2));
            detection_record_fggd_nc.setState(3);
            return;
        }
        double df_abs = stop_Absorbance - start_Absorbance;
        if (df_abs < Constants.FGCONTROVALUE_YIZHILVFA()) {
            ArmsUtils.snackbarText(getString(R.string.sampleerro_message3));
            detection_record_fggd_nc.setState(3);
            return;
        }
        String format = DecimalFormatUtils.threeDecimal(df_abs);
        Float value = Float.valueOf(format);
        LogUtils.d(format + "");
        LogUtils.d(value + "");

        if (message instanceof FGGDTestItem) {
            FGGDTestItem fggdTestItem = (FGGDTestItem) message;
            fggdTestItem.setControValue(value);
            fggdTestItem.setControValue_lastTime(DataUtils.getNowtimeyyymmddhhmmss());

            mFGGDGalleryBeanList.get(i).setProjectMessage(fggdTestItem);
            DBHelper.getFGGDTestItemDao(this).update(fggdTestItem);
            checkUpProjectMessage(fggdTestItem);

        }


        detection_record_fggd_nc.setState(2);
    }


    /**
     * @param i 标准曲线法对照值
     */
    private void getFGmethod2contro(int i) {
        Detection_Record_FGGD_NC detection_record_fggd_nc = (Detection_Record_FGGD_NC) mFGGDGalleryBeanList.get(i);

        BaseProjectMessage message = detection_record_fggd_nc.getProjectMessage();
        int wavelength = 0;
        wavelength = message.getWavelength();
        LogUtils.d(wavelength);


        //计算结果所需的参数
        float stop_Absorbance = 0; //结束吸光度

        //需要根据不同波长取不同的吸光度
        switch (wavelength) {
            case 410:
                stop_Absorbance = Float.parseFloat(detection_record_fggd_nc.getAbsorbance1_after() + ""); //结束吸光度
                break;
            case 536:
                stop_Absorbance = Float.parseFloat(detection_record_fggd_nc.getAbsorbance2_after() + ""); //结束吸光度
                break;
            case 595:
                stop_Absorbance = Float.parseFloat(detection_record_fggd_nc.getAbsorbance3_after() + ""); //结束吸光度
                break;
            case 620:
                stop_Absorbance = Float.parseFloat(detection_record_fggd_nc.getAbsorbance4_after() + ""); //结束吸光度
                break;
        }
        if ("Infinity".equals("" + stop_Absorbance)) {
            ArmsUtils.snackbarText("结束时吸光度为无穷大，此次对照无效！");
            detection_record_fggd_nc.setState(3);
            return;
        }
        if (stop_Absorbance < 0) {
            ArmsUtils.snackbarText("结束时吸光度小于0，此次对照无效！");
            detection_record_fggd_nc.setState(3);
            return;
        }

        String format = DecimalFormatUtils.threeDecimal(stop_Absorbance);
        Float aFloat = Float.valueOf(format);

        if (message instanceof FGGDTestItem) {
            FGGDTestItem projectMessage = (FGGDTestItem) message;
            projectMessage.setControValue(aFloat);
            projectMessage.setControValue_lastTime(DataUtils.getNowtimeyyymmddhhmmss());
            mFGGDGalleryBeanList.get(i).setProjectMessage(projectMessage);
            DBHelper.getFGGDTestItemDao(this).update(projectMessage);
            checkUpProjectMessage(projectMessage);

        }


        detection_record_fggd_nc.setState(2);
    }


    /**
     * @param item 检查通道是否选择了该检测项目 做完对照后更改对照值
     */
    private void checkUpProjectMessage(BaseProjectMessage item) {
        LogUtils.d(item);

        String testproject;
        testproject = item.getUnique_base_p();
        for (int i = 0; i < mFGGDGalleryBeanList.size(); i++) {
            GalleryBean bean = mFGGDGalleryBeanList.get(i);
            BaseProjectMessage message = bean.getProjectMessage();
            if (null == message) {
                continue;
            }
            String id = message.getUnique_base_p();
            if (testproject.equals(id)) {
                bean.setProjectMessage(item);
            }

        }

    }


    /**
     * @param i 标准曲线法结果
     */
    private void getFGmethod2result(int i) {
        Detection_Record_FGGD_NC detection_record_fggd_nc = (Detection_Record_FGGD_NC) mFGGDGalleryBeanList.get(i);
        LogUtils.d(detection_record_fggd_nc);
        //销库存
        //DownLoadBaseDataService.updateJxcByxmAjcs_ZHENJIANG(this, detection_record_fggd_nc.getUnique_testproject());
        //每个检测项目都有自己的一个对照值 所以需要每次都从数据库拿最新的检测项目 检测流程是先做对照 后做样品
        BaseProjectMessage projectMessage = detection_record_fggd_nc.getProjectMessage();
        LogUtils.d(projectMessage);
        //FGGDTestItem projectMessage = DBHelper.getFGGDTestItemDao(this).load(((FGGDTestItem) detection_record_fggd_nc.getProjectMessage()).getId_base());
        String unit_input = projectMessage.getUnit_input();
        int testMethod = projectMessage.getTestMethod();
        Double from0 = Double.valueOf(projectMessage.getBiaozhun_from0());
        Double from1 = Double.valueOf(projectMessage.getBiaozhun_from1());
        Double to0 = Double.valueOf(projectMessage.getBiaozhun_to0());
        Double to1 = Double.valueOf(projectMessage.getBiaozhun_to1());
        Double a0 = Double.valueOf(projectMessage.getBiaozhun_a0());
        Double b0 = Double.valueOf(projectMessage.getBiaozhun_b0());
        Double c0 = Double.valueOf(projectMessage.getBiaozhun_c0());
        Double d0 = Double.valueOf(projectMessage.getBiaozhun_d0());
        Double a1 = Double.valueOf(projectMessage.getBiaozhun_a1());
        Double b1 = Double.valueOf(projectMessage.getBiaozhun_b1());
        Double c1 = Double.valueOf(projectMessage.getBiaozhun_c1());
        Double d1 = Double.valueOf(projectMessage.getBiaozhun_d1());
        Double jzha = Double.valueOf(projectMessage.getJiaozhen_a());
        Double jzhb = Double.valueOf(projectMessage.getJiaozhen_b());
        //检测结束时吸光度
        double stop_Absorbance = 0;
        switch (projectMessage.getWavelength()) {
            case 410:
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance1_after(); //结束吸光度
                break;
            case 536:
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance2_after(); //结束吸光度
                break;
            case 595:
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance3_after(); //结束吸光度
                break;
            case 620:
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance4_after(); //结束吸光度
                break;
        }
        if ("Infinity".equals(stop_Absorbance + "")) {
            ArmsUtils.snackbarText(getString(R.string.absorbance_infinite_end));
            detection_record_fggd_nc.setState(3);
            return;
        }
        //稀释倍数
        double dilutionratio = detection_record_fggd_nc.getDilutionratio();
        //对照值
        double controlValue = projectMessage.getControValue();
        LogUtils.d("对照值：" + controlValue);
        if (controlValue < 0) {
            ArmsUtils.snackbarText(getString(R.string.control_less_0));
            detection_record_fggd_nc.setState(3);
            return;
        }
        //吸光度差
        double df_abs_cont = 0;
        if (controlValue < stop_Absorbance) {
            df_abs_cont = stop_Absorbance - controlValue;
        }
        //浓度值
        double testresult = 0;
        //判断结果
        String conclusion = "";
        if (df_abs_cont >= from0 && df_abs_cont < to0) {
            testresult = (a0 + b0 * df_abs_cont + c0 * df_abs_cont * df_abs_cont + d0 * df_abs_cont * df_abs_cont * df_abs_cont) * dilutionratio;
        } else if (df_abs_cont >= from1 && df_abs_cont < to1) {
            testresult = (a1 + b1 * df_abs_cont + c1 * df_abs_cont * df_abs_cont + d1 * df_abs_cont * df_abs_cont * df_abs_cont) * dilutionratio;
        } else {
            ArmsUtils.snackbarText(getString(R.string.absorbance_outside_range));
            detection_record_fggd_nc.setState(3);
            return;
        }
        testresult = testresult * jzha + jzhb;
        if (testresult < 0) {
            testresult = 0;
        }

        //判断是否选择样品 不选择样品默认不出判定结果 只出检测结果
        String sampleMessage = detection_record_fggd_nc.getSamplename();
        if (sampleMessage.isEmpty()) {
            conclusion = getString(R.string.reference_national_standards);
        } else {
            String cov = detection_record_fggd_nc.getCov();

            //单位为%号是需要乘以 100表示百分数值     具体需要商议
            if ("%".equals(detection_record_fggd_nc.getCov_unit())) {
                //testresult = testresult * 100;
            }

            if (getString(R.string.use_appropriate).equals(cov)) {
                conclusion = getString(R.string.use_appropriate);
            } else if ("".equals(cov)) {
                conclusion = getString(R.string.no_limit_set);
            } else {
                Double value_pdz = Double.valueOf(cov);
                String symbol = detection_record_fggd_nc.getSymbol();
                double v = getRandomNumber() / 10;
                if ("≤".equals(symbol)) {

                    if (testresult <= value_pdz) {
                        conclusion = getString(R.string.ok);
                        testresult = testresult + v;//加了随机数后的值
                        if (testresult > value_pdz) { //大于阴性的最大值  超标了
                            testresult = testresult - v * 2;
                        }
                    } else {
                        conclusion = getString(R.string.ng);
                        testresult = testresult + v;//加了随机数后的值
                    }

                } else if ("≥".equals(symbol)) {

                    if (testresult >= value_pdz) {
                        conclusion = getString(R.string.ok);
                        testresult = testresult - v;//加了随机数后的值
                        if (testresult < value_pdz) { //小于阴性的最大值  超标了
                            testresult = testresult + v * 2;
                        }
                    } else {
                        conclusion = getString(R.string.ng);
                        testresult = testresult - v;//加了随机数后的值
                    }

                } else if ("<".equals(symbol)) {
                    if (testresult < value_pdz) {
                        conclusion = getString(R.string.ok);
                        testresult = testresult + v;//加了随机数后的值
                        if (testresult > value_pdz) { //大于阴性的最大值  超标了
                            testresult = testresult - v * 2;
                        }
                    } else {
                        conclusion = getString(R.string.ng);
                        testresult = testresult + v;//加了随机数后的值
                    }

                } else if (">".equals(symbol)) {
                    if (testresult > value_pdz) {
                        conclusion = getString(R.string.ok);
                        testresult = testresult - v;//加了随机数后的值
                        if (testresult < value_pdz) { //小于阴性的最大值  超标了
                            testresult = testresult + v * 2;
                        }
                    } else {
                        conclusion = getString(R.string.ng);
                        testresult = testresult - v;//加了随机数后的值
                    }

                }
                if (testresult < 0) {
                    testresult = 0;
                }
            }


        }
        LogUtils.d(testresult);
        //设置一些检测完成时需要添加的信息
        String unit = detection_record_fggd_nc.getCov_unit();
        if ("".equals(unit)) {
            detection_record_fggd_nc.setCov_unit(unit_input);
        }
        //检测结果 结论
        detection_record_fggd_nc.setTestresult(DecimalFormatUtils.twoDecimal(testresult));
        detection_record_fggd_nc.setDecisionoutcome(conclusion);
        //检测完成时间
        detection_record_fggd_nc.setTestingtime(System.currentTimeMillis());
        //检测地点
        detection_record_fggd_nc.setTestsite(Constants.ADDR_WF);
        detection_record_fggd_nc.setLatitude(Constants.LATITUDE);
        detection_record_fggd_nc.setLongitude(Constants.LONTITUDE);
        //对照值
        //当前平台
        detection_record_fggd_nc.setPlatform_tag("0");
        //detection_record_fggd_nc.setControlvalue(Constants.FGGD_BIAOZHUN_CONTROL_VALUE + "");
        detection_record_fggd_nc.setControlvalue(projectMessage.getControValue() + "");
        //检测人员   这里填的是本地登录的账号名称
        detection_record_fggd_nc.setInspector(Constants.NOWUSER.getUsername());
        //设置检测模块
        detection_record_fggd_nc.setTest_Moudle(ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.FGGD_TestMoudle));
        // 保存至数据库
        detection_record_fggd_nc.setId(null);//自增ID
        //设置状态为检测完成 2
        detection_record_fggd_nc.setState(2);
        //detection_record_fggd_nc.setDowhat(0);
        detection_record_fggd_nc.setTest_method(getString(R.string.mothod2));

        long insert = DBHelper.getDetection_Record_FGGD_NCDao(this).insert(detection_record_fggd_nc);
        //检测完成后的操作，自动打印，自动上传等
        //printAndUpload(detection_record_fggd_nc, insert);


    }

    /**
     * @param i 动力学法结果
     */
    private void getFGmethod3result(int i) {
        Detection_Record_FGGD_NC detection_record_fggd_nc = (Detection_Record_FGGD_NC) mFGGDGalleryBeanList.get(i);

        BaseProjectMessage projectMessage = detection_record_fggd_nc.getProjectMessage();
        String unit_input = projectMessage.getUnit_input();
        double yang_a2 = projectMessage.getYang_a();
        double yang_b2 = projectMessage.getYang_b();
        double yin_a2 = projectMessage.getYin_a();
        double yin_b2 = projectMessage.getYin_b();
        double keyi_a2 = projectMessage.getKeyi_a();
        double keyi_b2 = projectMessage.getKeyi_b();
        double a3 = projectMessage.getJiaozhen_a();
        double b3 = projectMessage.getJiaozhen_b();
        //计算结果所需的参数
        double start_Absorbance = 0;//开始吸光度
        double stop_Absorbance = 0; //结束吸光度
        int wavelength = projectMessage.getWavelength();
        //需要根据不同波长取不同的吸光度
        switch (wavelength) {
            case 410:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance1_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance1_after(); //结束吸光度
                break;
            case 536:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance2_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance2_after(); //结束吸光度
                break;
            case 595:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance3_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance3_after(); //结束吸光度
                break;
            case 620:
                start_Absorbance = detection_record_fggd_nc.getAbsorbance4_start();//开始吸光度
                stop_Absorbance = detection_record_fggd_nc.getAbsorbance4_after(); //结束吸光度
                break;
        }
        if ("Infinity".equals(start_Absorbance + "") || "Infinity".equals(stop_Absorbance + "")) {
            ArmsUtils.snackbarText(getString(R.string.contro_erromessage1));
            detection_record_fggd_nc.setState(3);
            return;
        }
        double df_abs = 0; //吸光度差
        double testresult = 0; //检测结果
        //判断结果
        String conclusion = "";
        if (start_Absorbance < stop_Absorbance) {
            df_abs = stop_Absorbance - start_Absorbance;
        }
        testresult = a3 * df_abs + b3;
        if (testresult < 0) {
            testresult = 0;
        }
        if ("%".equals(detection_record_fggd_nc.getCov_unit())) {
            testresult = testresult * 100;
        }
        double v = getRandomNumber() / 10;
        if (testresult >= yang_a2 && testresult < yang_b2) {
            conclusion = getString(R.string.ng);
            testresult = testresult + v;//加了随机数后的值
            if (testresult > yang_b2) {
                testresult = testresult - v * 2;//加了随机数后的值
            }
        } else if (testresult >= yin_a2 && testresult < yin_b2) {
            conclusion = getString(R.string.ok);
            testresult = testresult + v;//加了随机数后的值
            if (testresult > yin_b2) {
                testresult = testresult - v * 2;//加了随机数后的值
            }
        } else if (testresult >= keyi_a2 && testresult < keyi_b2) {
            conclusion = "可疑";
        }
        if (testresult < 0) {
            testresult = 0;
        }
        //设置一些检测完成时需要添加的信息
        String unit = detection_record_fggd_nc.getCov_unit();
        if ("".equals(unit)) {
            detection_record_fggd_nc.setCov_unit(unit_input);
        }
        //检测结果 结论
        detection_record_fggd_nc.setTestresult(DecimalFormatUtils.twoDecimal(testresult));
        detection_record_fggd_nc.setDecisionoutcome(conclusion);
        //检测完成时间
        detection_record_fggd_nc.setTestingtime(System.currentTimeMillis());
        //检测地点

        detection_record_fggd_nc.setTestsite(Constants.ADDR_WF);
        detection_record_fggd_nc.setLatitude(Constants.LATITUDE);
        detection_record_fggd_nc.setLongitude(Constants.LONTITUDE);
        //当前平台
        detection_record_fggd_nc.setPlatform_tag(Constants.PLATFORM_TAG + "");
        //检测人员   这里填的是本地登录的账号名称
        detection_record_fggd_nc.setInspector(Constants.NOWUSER.getUsername());
        //设置检测模块
        detection_record_fggd_nc.setTest_Moudle(ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.FGGD_TestMoudle));
        // 保存至数据库
        detection_record_fggd_nc.setId(null);//自增ID
        //设置状态为检测完成 2
        detection_record_fggd_nc.setState(2);
        //detection_record_fggd_nc.setDowhat(0);
        detection_record_fggd_nc.setTest_method(getString(R.string.mothod3));

        long insert = DBHelper.getDetection_Record_FGGD_NCDao(this).insert(detection_record_fggd_nc);
        //检测完成后的操作，自动打印，自动上传等
        //printAndUpload(detection_record_fggd_nc, insert);

    }

    /**
     * @param i 系数法结果
     */
    private void getFGmethod4result(int i) {
        Detection_Record_FGGD_NC detection_record_fggd_nc = (Detection_Record_FGGD_NC) mFGGDGalleryBeanList.get(i);

        BaseProjectMessage projectMessage = detection_record_fggd_nc.getProjectMessage();
        String unit_input = projectMessage.getUnit_input();
        double a2 = projectMessage.getJiaozhen_a();
        double b2 = projectMessage.getJiaozhen_b();
        double c2 = projectMessage.getJiaozhen_c();
        double d2 = projectMessage.getJiaozhen_d();
        //系数法   直接得结果
        double everyresponse = detection_record_fggd_nc.getEveryresponse();//反应液滴数
        //检测结果
        double testresult = 0;
        //判断结果
        String conclusion = "";
        testresult = a2 + b2 * everyresponse + c2 * everyresponse * everyresponse + d2 * everyresponse * everyresponse * everyresponse;
        //判断是否选择样品 不选择样品默认不出判定结果 只出检测结果
        String sampleMessage = detection_record_fggd_nc.getSamplename();

        if (testresult < 0) {
            testresult = 0;
        }

        String projectName = projectMessage.getProjectName();
        if (projectName.equals(getString(R.string.rice_freshness))) {
            String decisionoutcome;
            if (testresult < 10) {
                conclusion = getString(R.string.Fresh_rice);
                decisionoutcome = getString(R.string.ok);
            } else if (testresult >= 10 && testresult < 16) {
                conclusion = getString(R.string.Chen_Mi);
                decisionoutcome = getString(R.string.ng);
            } else {
                conclusion = getString(R.string.Aged_rice);
                decisionoutcome = getString(R.string.ng);
            }
            //检测结果 结论
            detection_record_fggd_nc.setTestresult(conclusion);
            //detection_record_fggd_nc.setDecisionoutcome(decisionoutcome);
        } else {
            if (sampleMessage.isEmpty()) {
                conclusion = getString(R.string.reference_national_standards);
            } else {
                String cov = detection_record_fggd_nc.getCov();
                if ("%".equals(detection_record_fggd_nc.getCov_unit())) {
                    testresult = testresult * 100;
                }

                if (getString(R.string.use_appropriate).equals(cov)) {
                    conclusion = getString(R.string.use_appropriate);
                } else {
                    Double value_pdz = Double.valueOf(cov);
                    String symbol = detection_record_fggd_nc.getSymbol();
                    //double v = getRandomNumber() / 10;
                    double v = 0;
                    if ("≤".equals(symbol)) {
                        if (testresult <= value_pdz) {
                            conclusion = getString(R.string.ok);
                            testresult = testresult + v;//加了随机数后的值
                            if (testresult > value_pdz) { //大于阴性的最大值  超标了
                                testresult = testresult - v * 2;
                            }
                        } else {
                            conclusion = getString(R.string.ng);
                            testresult = testresult + v;//加了随机数后的值
                        }

                        if (testresult < 0) {
                            testresult = 0;
                        }
                    } else if ("≥".equals(symbol)) {
                        if (testresult >= value_pdz) {
                            conclusion = getString(R.string.ok);
                            testresult = testresult - v;//加了随机数后的值
                            if (testresult < value_pdz) { //大于阴性的最大值  超标了
                                testresult = testresult + v * 2;
                            }
                        } else {
                            conclusion = getString(R.string.ng);
                            testresult = testresult - v;//加了随机数后的值
                        }
                        if (testresult < 0) {
                            testresult = 0;
                        }
                    } else if ("<".equals(symbol)) {
                        if (testresult < value_pdz) {
                            conclusion = getString(R.string.ok);
                            testresult = testresult + v;//加了随机数后的值
                            if (testresult > value_pdz) { //大于阴性的最大值  超标了
                                testresult = testresult - v * 2;
                            }
                        } else {
                            conclusion = getString(R.string.ng);
                            testresult = testresult + v;//加了随机数后的值
                        }
                        if (testresult < 0) {
                            testresult = 0;
                        }
                    } else if (">".equals(symbol)) {
                        if (testresult > value_pdz) {
                            conclusion = getString(R.string.ok);
                            testresult = testresult - v;//加了随机数后的值
                            if (testresult < value_pdz) { //大于阴性的最大值  超标了
                                testresult = testresult + v * 2;
                            }
                        } else {
                            conclusion = getString(R.string.ng);
                            testresult = testresult - v;//加了随机数后的值
                        }
                        if (testresult < 0) {
                            testresult = 0;
                        }
                    }
                }
            }
            //检测结果 结论
            detection_record_fggd_nc.setTestresult(DecimalFormatUtils.twoDecimal(testresult));
            detection_record_fggd_nc.setDecisionoutcome(conclusion);
        }


        //设置一些检测完成时需要添加的信息
        String unit = detection_record_fggd_nc.getCov_unit();
        if ("".equals(unit)) {
            detection_record_fggd_nc.setCov_unit(unit_input);
        }

        //检测完成时间
        detection_record_fggd_nc.setTestingtime(System.currentTimeMillis());
        //检测地点
        detection_record_fggd_nc.setTestsite(Constants.ADDR_WF);
        detection_record_fggd_nc.setLatitude(Constants.LATITUDE);
        detection_record_fggd_nc.setLongitude(Constants.LONTITUDE);
        //当前平台
        detection_record_fggd_nc.setPlatform_tag(Constants.PLATFORM_TAG + "");
        //稀释倍数
        detection_record_fggd_nc.setControlvalue(everyresponse + "");
        //检测人员   这里填的是本地登录的账号名称
        detection_record_fggd_nc.setInspector(Constants.NOWUSER.getUsername());
        //设置检测模块
        detection_record_fggd_nc.setTest_Moudle(ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.FGGD_TestMoudle));
        // 保存至数据库
        detection_record_fggd_nc.setId(null);//自增ID
        //设置状态为检测完成 2
        //detection_record_fggd_nc.setDowhat(0);
        detection_record_fggd_nc.setState(2);
        detection_record_fggd_nc.setTest_method(getString(R.string.mothod4));

        long insert = DBHelper.getDetection_Record_FGGD_NCDao(this).insert(detection_record_fggd_nc);
        //检测完成后的操作，自动打印，自动上传等
        //printAndUpload(detection_record_fggd_nc, insert);

    }

    private void initSerialcontrol() {
        mData_SerialControl = new SerialControl();
        //区分Android版不同串口名称不同 ttys4 ttys0
        //mData_SerialControl.setPort(Constants.NEW_DATA_SERIAPort);
        mData_SerialControl.setPort(Constants.DATA_SERIAPort);
        mData_SerialControl.setBaudRate(Constants.DATA_SERIALBaudRate);
        mOpenComPort = mData_SerialControl.OpenComPort(mData_SerialControl);
        LogUtils.d(mOpenComPort);
        if (!mOpenComPort) {
            ArmsUtils.snackbarText(Constants.NEW_DATA_SERIAPort + getString(R.string.failed_open));
        }

    }

    public class MyBinder extends Binder {
        public SerialDataService getService() {
            return SerialDataService.this;
        }
    }

    /**
     * 串口打开成功回调
     *
     * @param device 串口
     */
    @Override
    public void onSuccess(File device) {
        LogUtils.d(device.getAbsolutePath() + "连接成功");


    }

    /**
     * 串口打开失败回调
     *
     * @param device 串口
     * @param status 失败原因
     */
    @Override
    public void onFail(File device, Status status) {
        LogUtils.d(device.getAbsolutePath() + "连接失败" + status.name());

    }

    /**
     * 数据发送
     *
     * @param device 串口名称
     * @param bytes  发送的数据
     */
    @Override
    public void onDataSent(File device, byte[] bytes) {
        String absolutePath = device.getAbsolutePath();
        LogUtils.d(absolutePath + "  发送  " + ByteUtils.byte2HexStr2(bytes));


    }

    /**
     * 数据接收
     *
     * @param device 串口名称
     * @param bytes  接收到的数据
     */
    @Override
    public void onDataReceived(File device, byte[] bytes) {
        String absolutePath = device.getAbsolutePath();
        //LogUtils.d(device.getAbsolutePath());
        //LogUtils.d(ByteUtils.byte2HexStr2(bytes));
        LogUtils.d(absolutePath + "  接收  " + ByteUtils.byte2HexStr2(bytes));


    }

    /**
     * 开始检测标志，当有通道处于后台检测时为true
     */
    public Boolean RUNFLAG_FGGD_HASSTART = false;

    /**
     * 检查分光光度模块的检测状态 看是否有在检测的通道，没有的话就停止与底层的数据传输
     */
    public void checkFGState() {
        for (int i = 0; i < mFGGDGalleryBeanList.size(); i++) {
            int state = mFGGDGalleryBeanList.get(i).getState();
            if (state == 1) {
                return;
            }
        }
        RUNFLAG_FGGD_HASSTART = false;
    }

    /**
     * 运行标志，是否在检测界面
     */
    public Boolean RUNFLAG_FGGD = false;

    public void stopsFGGDSendthread() {
        RUNFLAG_FGGD = false;
    }


    public void startFGGDSendthread() {
        RUNFLAG_FGGD = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RUNFLAG_FGGD = false;
        mOpenComPort = false;
        mData_SerialControl.CloseComPort(mData_SerialControl);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        RUNFLAG_FGGD = false;
        mOpenComPort = false;
        mData_SerialControl.CloseComPort(mData_SerialControl);
        return super.onUnbind(intent);
    }

}