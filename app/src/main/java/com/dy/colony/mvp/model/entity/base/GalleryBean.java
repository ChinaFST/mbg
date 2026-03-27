package com.dy.colony.mvp.model.entity.base;

import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.app.WorkerThreadFactory;
import com.dy.colony.app.service.UpLoadIntentService;
import com.dy.colony.app.utils.BitmapUtils;
import com.dy.colony.app.utils.DataUtils;
import com.dy.colony.app.utils.FileUtils;
import com.dy.colony.app.utils.NumberUtils;
import com.dy.colony.app.utils.OpenCvUtils;
import com.dy.colony.app.utils.PictureToolUtils;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.Company_Point;
import com.dy.colony.greendao.beans.Company_Point_Unit;
import com.dy.colony.greendao.beans.Detection_Record_FGGD_NC;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.greendao.beans.JTJPoint;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.greendao.beans.Simple33;
import com.dy.colony.greendao.daos.Detection_Record_FGGD_NCDao;
import com.dy.colony.greendao.daos.FGGDTestItemDao;
import com.dy.colony.greendao.daos.JTJTestItemDao;
import com.dy.colony.greendao.daos.Simple33Dao;
import com.dy.colony.mvp.model.entity.JTJDataModel_P;
import com.dy.colony.mvp.model.entity.ObjUserData;
import com.dy.colony.mvp.model.opencvDetector.CardType;
import com.dy.colony.mvp.ui.widget.JTJDataModel_P_6270;
import com.dy.colony.usbhelp.UsbReadWriteHelper;
import com.google.gson.Gson;
import com.jess.arms.BuildConfig;
import com.jess.arms.utils.ArmsUtils;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/12
 */
public abstract class GalleryBean implements UsbReadWriteHelper.onUsbReciver {

    private boolean isCheck;
    private int galleryNum;
    private int JTJ_MAC = -2;//胶体金模块编号
    private int state;//通道状态  0等待测试 1正在测试 2测试结束 3测试失败 4等待测试
    private int dowhat; //样品1  对照2
    private boolean clearn = true;//清零标志位 需要清零设为true，在解析数据时会重新设置作为对照的ad值 ，默认为true，会设置一次
    private boolean projectChange = false;//清零标志位 需要清零设为true，在解析数据时会重新设置作为对照的ad值 ，默认为true，会设置一次

    private int howmanysecond;//检测时长
    private int remainingtime;//剩余时间
    private String testmoudle;//检测模块 //1分光光度，2胶体金

    //分光光度属性
    private int wave1; //实时的波长
    private int wave2;
    private int wave3;
    private int wave4;

    private int dzh_wave1_start;//对照ad值，用于检测是否放入比色皿
    private int dzh_wave2_start;
    private int dzh_wave3_start;
    private int dzh_wave4_start;

    private int wave1_start;//开始检测时的波长
    private int wave2_start;
    private int wave3_start;
    private int wave4_start;

    private float luminousness1;//透光率1
    private float luminousness2;//透光率2
    private float luminousness3;//透光率3
    private float luminousness4;//透光率4


    private double absorbance1; //实时吸光度
    private double absorbance2;
    private double absorbance3;
    private double absorbance4;

    private double absorbance1_start;  //开始反应时吸光度 ,在点击开始检测时将当前吸光度设置给它即可
    private double absorbance2_start;
    private double absorbance3_start;
    private double absorbance4_start;

    private double absorbance1_after;  //反应后吸光度 ，计算结果时取当前的吸光度即可
    private double absorbance2_after;
    private double absorbance3_after;
    private double absorbance4_after;

    private BaseProjectMessage mProjectMessage;
    private BaseSampleMessage mSampleMessage;
    private BaseUntilMessage mUntilMessage;
    private boolean isCountedDown;


    private UsbDevice mUsbDevice;
    /**
     * 1 摄像头
     */
    private int mJTJModel;//
    private int mJTJCardModel = 0;// 默认为单卡
    private double[] JTJResultData;
    private List<double[]> JTJResultDatas;
    private int backgroundResous;
    private List<Float> mUserfullData;
    private List<List<Float>> mUserfullDatas;
    public RunnableScheduledFuture<?> mRunnableScheduledFuture;
    private UsbReadWriteHelper mJTJRWHelper;


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    /**
     * 胶体金卡样式  0为单卡
     *
     * @return
     */
    public int getJTJCardModel() {
        return mJTJCardModel;
    }

    /**
     * 胶体金卡样式  0为单卡
     *
     * @param JTJCardModel
     */
    public void setJTJCardModel(int JTJCardModel) {
        mJTJCardModel = JTJCardModel;
    }

    public RunnableScheduledFuture<?> getRunnableScheduledFuture() {
        return mRunnableScheduledFuture;
    }

    public void setRunnableScheduledFuture(RunnableScheduledFuture<?> runnableScheduledFuture) {
        mRunnableScheduledFuture = runnableScheduledFuture;
    }


    public double[] getJTJResultData() {
        return JTJResultData;
    }

    public void setJTJResultData(double[] JTJResultData) {
        this.JTJResultData = JTJResultData;
    }


    public void setJTJResultDatas(List<double[]> JTJResultData) {
        this.JTJResultDatas = JTJResultData;
    }

    public List<double[]> getJTJResultDatas() {
        return JTJResultDatas;
    }

    public int getBackgroundResous() {
        return backgroundResous;
    }

    public void setBackgroundResous(int backgroundResous) {
        this.backgroundResous = backgroundResous;
    }

    public List<Float> getUserfullData() {
        return mUserfullData;
    }

    public void setUserfullData(List<Float> userfullData) {
        mUserfullData = userfullData;
    }

    public void setUserfullDatas(List<Float> userfullData) {
        if (null == mUserfullDatas) {
            mUserfullDatas = new ArrayList<>();
        }
        mUserfullDatas.add(userfullData);
    }


    public int getJTJ_MAC() {
        return JTJ_MAC;
    }

    public void setJTJ_MAC(int JTJ_MAC) {
        this.JTJ_MAC = JTJ_MAC;
        setGalleryNum(JTJ_MAC);
    }


    /**
     * 1=摄像头
     *
     * @return
     */
    public int getJTJModel() {
        return mJTJModel;
    }

    /**
     * 1=摄像头
     *
     * @param JTJModel
     */
    public void setJTJModel(int JTJModel) {
        mJTJModel = JTJModel;
    }

    public UsbDevice getUsbDevice() {
        return mUsbDevice;
    }

    public void setUsbDevice(UsbDevice usbDevice) {
        mUsbDevice = usbDevice;
        mJTJRWHelper = new UsbReadWriteHelper(mUsbDevice);
        mJTJRWHelper.setReciverListener(this);
    }

    public <T> void setStandard(T t1) {
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;
        if (t1 instanceof FoodItemAndStandard) {
            FoodItemAndStandard t11 = (FoodItemAndStandard) t1;
            nc.setStand_num(t11.getStandardName());
            nc.setSymbol(t11.getCheckSign());
            nc.setCov(t11.getStandardValue());
            nc.setCov_unit(t11.getCheckValueUnit());
        }
    }


    public interface onJTJResultRecive {
        /**
         * @param userfuldata 接收成功 针对扫描模块
         * @param data
         */
        void onReciverSuccess(List<Float> userfuldata, double[] data);


        void onReciverSuccess(List<List<Float>> userfuldata, List<double[]> data);

        /**
         * @param bitmap 接收成功，针对摄像头模块
         */
        void onReciverSuccess(Bitmap bitmap);

        default void onReciverSuccess(Bitmap bitmap, boolean runFlag) {

            onReciverSuccess(bitmap);
        }

        /**
         * 接收失败
         */
        void onReciverfail();

        /**
         * @param timer 倒计时
         */
        void onTimer(int timer);

        /**
         * 通知刷新UI
         */
        void onRefrish();


    }


    protected WeakReference<onJTJResultRecive> mReciveWeakReference;

    public WeakReference<onJTJResultRecive> getmReciveWeakReference() {
        return mReciveWeakReference;
    }

    public void setJTJResultReciverListener(onJTJResultRecive reciverListener) {

        mReciveWeakReference = new WeakReference<>(reciverListener);
    }

    public void removeJTJResultReciverListener() {
        if (mReciveWeakReference != null) {
            mReciveWeakReference.clear();
            mReciveWeakReference = null;
        }
    }


    public boolean checkState;

    public void cleanValue() {
        setState(0);
        ((Detection_Record_FGGD_NC) this).setDecisionoutcome(null);
        ((Detection_Record_FGGD_NC) this).setTestresult(null);
        ((Detection_Record_FGGD_NC) this).setTestingtime(null);
        ((Detection_Record_FGGD_NC) this).setInspector(null);
        ((Detection_Record_FGGD_NC) this).setTestsite(null);
        ((Detection_Record_FGGD_NC) this).setQrcode(null);

        setClearn(true);
        setDowhat(0);
    }


    public void cleanValueMore() { //深度清零，把检测项目，样品等都清了
        cleanValue();
        removeAll();
        if (mReciveWeakReference != null) {

            onJTJResultRecive recive = mReciveWeakReference.get();
            if (recive != null) {
                recive.onRefrish();
            }

        }
    }


    public void cleanJTJ() {
        if (state == 1) {
            ArmsUtils.snackbarText("正在检测中，请稍后");
            return;
        }
        state = 0;
        JTJResultData = null;
        backgroundResous = 0;
        mUserfullData = null;
        removeAll();

        if (mReciveWeakReference != null) {
            onJTJResultRecive recive = mReciveWeakReference.get();
            if (recive != null) {
                recive.onRefrish();
            }

        }
    }

    public void cleanJTJNotJudgeState() {

        //removeAll();
        state = 0;
        JTJResultData = null;
        backgroundResous = 0;
        mUserfullData = null;

        resetState();

        if (mReciveWeakReference != null) {
            onJTJResultRecive recive = mReciveWeakReference.get();
            if (recive != null) {
                recive.onRefrish();
            }

        }
    }

    public void resetState() {
        LogUtils.d("resetState");
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;
        nc.setTestresult(null);
        nc.setTestingtime(null);
        nc.setTestsite(null);
        nc.setInspector(null);
        nc.setRetest(0);
        nc.setParentSysCode(null);
        nc.setQrcode(null);
        nc.setTest_method(null);


        nc.setReservedfield1(null);
        nc.setReservedfield2(null);
        nc.setReservedfield3(null);
        nc.setReservedfield4(null);
        nc.setReservedfield5(null);
        nc.setReservedfield6(null);
        nc.setReservedfield7(null);
        nc.setReservedfield8(null);
        nc.setReservedfield9(null);
        nc.setReservedfield10(null);
        nc.setReservedfield10(null);
    }


    public void startFGGDTest(int dowhat) {
        LogUtils.d("分光光度开始" + (dowhat == 1 ? "样品" : "对照") + "试验");
        LogUtils.d(this);
        setState(1);
        setDowhat(dowhat);
        if (null == mProjectMessage) {
            return;
        }
        if (dowhat == 1) {
            ((Detection_Record_FGGD_NC) this).setTestresult(null);
            ((Detection_Record_FGGD_NC) this).setDecisionoutcome(null);
            ((Detection_Record_FGGD_NC) this).setSerialNumber(Constants.getFGGDSearinum());
            ((Detection_Record_FGGD_NC) this).setSysCode(UUID.randomUUID().toString());
        }

        setRemainingtime(mProjectMessage.getYuretime() + mProjectMessage.getJiancetime());
    }

    public void stopFGGDTest() {
        LogUtils.d("分光光度停止" + (dowhat == 1 ? "样品" : "对照") + "试验");
        setState(0);
        setDowhat(0);
        ((Detection_Record_FGGD_NC) this).setSerialNumber(null);
        ((Detection_Record_FGGD_NC) this).setSysCode(null);
        setRemainingtime(0);
    }


    public void startJTJTest(int i) {
        setState(1);
        setRemainingtime(i);
        setUserfullData(null);

        ((Detection_Record_FGGD_NC) this).setTestresult(null);
        ((Detection_Record_FGGD_NC) this).setDecisionoutcome(null);
        ((Detection_Record_FGGD_NC) this).setSerialNumber(Constants.getJtjSearinum());
        ((Detection_Record_FGGD_NC) this).setSysCode(UUID.randomUUID().toString());
        if (mReciveWeakReference != null) {
            onJTJResultRecive recive = mReciveWeakReference.get();
            if (recive != null) {
                recive.onRefrish();
            }


        }
    }

    public void startJTJTest() {
        setState(1);
        setUserfullData(null);
        ((Detection_Record_FGGD_NC) this).setTestresult(null);
        ((Detection_Record_FGGD_NC) this).setDecisionoutcome(null);
        String jtjSearinum = Constants.getJtjSearinum();
        //System.out.println("流水号===" + jtjSearinum + "---" + getGalleryNum());
        ((Detection_Record_FGGD_NC) this).setSerialNumber(jtjSearinum);
        ((Detection_Record_FGGD_NC) this).setSysCode(UUID.randomUUID().toString());
        if (mReciveWeakReference != null) {
            onJTJResultRecive recive = mReciveWeakReference.get();
            if (recive != null) {
                recive.onRefrish();
            }

        }
    }


    public void stopJTJTest() {
        setState(0);
        ((Detection_Record_FGGD_NC) this).setSerialNumber(null);
        ((Detection_Record_FGGD_NC) this).setSysCode(null);
    }


    public void startTest() {
        int wavelength = mProjectMessage.getWavelength();
        switch (wavelength) {
            case 410:
                absorbance1_start = absorbance1;
                break;
            case 536:
                absorbance2_start = absorbance2;
                break;
            case 595:
                absorbance3_start = absorbance3;
                break;
            case 620:
                absorbance4_start = absorbance4;
                break;
        }
    }

    public void testFinished() {
        int wavelength = mProjectMessage.getWavelength();
        switch (wavelength) {
            case 410:
                absorbance1_after = absorbance1;
                break;
            case 536:
                absorbance2_after = absorbance2;
                break;
            case 595:
                absorbance3_after = absorbance3;
                break;
            case 620:
                absorbance4_after = absorbance4;
                break;
        }

    }


    public BaseProjectMessage getProjectMessage() {

        return mProjectMessage;
    }

    public void justSetProjectMessage(BaseProjectMessage baseProjectMessage) {
        LogUtils.d(baseProjectMessage);
        mProjectMessage = baseProjectMessage;
    }

    public void setProjectMessage(BaseProjectMessage baseProjectMessage) {
        LogUtils.d(baseProjectMessage);
        String mMethod = "";
        if (!(this instanceof Detection_Record_FGGD_NC)) {
            return;
        }

        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;
        if (nc.getRetest() == 1) {
            nc.setRetest(0);
            nc.setParentSysCode(null);
        }


        if (null != mProjectMessage) {
            if (!mProjectMessage.getUnique_base_p().equals(baseProjectMessage.getUnique_base_p())) {
                removeSampleMessage();
            }
        } else {
            removeSampleMessage();
        }
        nc.setTest_project(baseProjectMessage.getProjectName());
        nc.setUnique_testproject(baseProjectMessage.getUnique_base_p());
        nc.setTest_method(baseProjectMessage.getMethod());

        mProjectMessage = baseProjectMessage;
    }


    public void setProjectMessage(BaseProjectMessage baseProjectMessage, boolean clearn) {
        LogUtils.d(baseProjectMessage);
        String mMethod = "";
        if (!(this instanceof Detection_Record_FGGD_NC)) {
            return;
        }
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;

        if (null != mProjectMessage) {
            if (!mProjectMessage.getUnique_base_p().equals(baseProjectMessage.getUnique_base_p())) {
                removeSampleMessage();
            }
        }
        nc.setTest_project(baseProjectMessage.getProjectName());
        nc.setUnique_testproject(baseProjectMessage.getUnique_base_p());
        nc.setTest_method(baseProjectMessage.getMethod());


        mProjectMessage = baseProjectMessage;
    }

    public void removeProjectMessage() {
        LogUtils.d("removeProjectMessage");
        mProjectMessage = null;

        if (!(this instanceof Detection_Record_FGGD_NC)) {
            return;
        }
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;
        nc.setTest_project(null);
        nc.setTest_method(null);
        nc.setSerialNumber(null);
        nc.setUnique_testproject(null);
        removeSampleMessage();
    }


    public void removeAll() {
        LogUtils.d("removeAll");
        removeSampleMessage();
        removeProjectMessage();
        removeUntilMessage();
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;
        nc.setTestresult(null);
        nc.setTestingtime(null);
        nc.setTestsite(null);
        nc.setInspector(null);
        nc.setRetest(0);
        nc.setParentSysCode(null);
        nc.setQrcode(null);
        nc.setTest_method(null);


        nc.setReservedfield1(null);
        nc.setReservedfield2(null);
        nc.setReservedfield3(null);
        nc.setReservedfield4(null);
        nc.setReservedfield5(null);
        nc.setReservedfield6(null);
        nc.setReservedfield7(null);
        nc.setReservedfield8(null);
        nc.setReservedfield9(null);
        nc.setReservedfield10(null);
        nc.setReservedfield10(null);
    }


    public BaseSampleMessage getSampleMessage() {
        return mSampleMessage;
    }


    public void setSampleMessage(BaseSampleMessage sampleMessage) {

        LogUtils.d(sampleMessage);
        if (!(this instanceof Detection_Record_FGGD_NC)) {
            LogUtils.d("RETURN");
            return;
        }
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;
        if (nc.getRetest() == 1) {
            nc.setRetest(0);
            nc.setParentSysCode(null);
        }
        if (sampleMessage instanceof FoodItemAndStandard) {
            FoodItemAndStandard message = (FoodItemAndStandard) sampleMessage;
            String code = message.getFoodPCode();
            // LogUtils.d(message);
            Simple33Dao dao = DBHelper.getSimple33Dao(MyAppLocation.myAppLocation);
            List<Simple33> list = dao.queryBuilder().where(Simple33Dao.Properties.FoodCode.like(code)).list();

            if (list.size() != 0) {
                nc.setSampletype(list.get(0).getFoodName());
            } else {
                nc.setSampletype("");
            }
            nc.setFoodCode(code);
            nc.setSamplename(message.getSampleName());
            nc.setSamplenum(message.getSampleNum());
            nc.setSymbol(message.getCheckSign());
            nc.setCov(message.getStandardValue());
            nc.setCov_unit(message.getCheckValueUnit());
            nc.setStand_num(message.getStandardName());
            nc.setUnique_sample(message.getCheckId());
            mSampleMessage = sampleMessage;
        }

    }

    public void setSampleType(BaseSimple33Message sample) {
        if (!(this instanceof Detection_Record_FGGD_NC)) {
            LogUtils.d("RETURN");
            return;
        }
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;
        if (nc.getRetest() == 1) {
            nc.setRetest(0);
            nc.setParentSysCode(null);
        }

    }

    public void removeSampleMessage() {
        LogUtils.d("removeSampleMessage");
        mSampleMessage = null;
        if (!(this instanceof Detection_Record_FGGD_NC)) {
            LogUtils.d("RETURN");
            return;
        }
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;
        nc.setTestresult(null);
        nc.setDecisionoutcome(null);
        nc.setFoodCode(null);
        nc.setSamplename(null);
        nc.setSampletype(null);
        nc.setSamplenum(null);
        nc.setSymbol(null);
        nc.setCov(null);
        nc.setCov_unit(null);
        nc.setStand_num(null);
        nc.setUnique_sample(null);
    }


    public BaseUntilMessage getUntilMessage() {
        return mUntilMessage;
    }

    public void setUntilMessage(BaseUntilMessage untilMessage) {
        if (!(this instanceof Detection_Record_FGGD_NC)) {
            return;
        }
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;
        if (nc.getRetest() == 1) {
            nc.setRetest(0);
            nc.setParentSysCode(null);
        }
        if (untilMessage instanceof Company_Point) {
            Company_Point message = (Company_Point) untilMessage;
            String name = message.getRegName();
            nc.setProsecutedunits(name);
            String address = message.getRegAddress();
            nc.setProsecutedunits_adress(address);
            nc.setUnique_beunit(message.getRegId());
            nc.setSampleplace(message.getRegAddress());
            // LogUtils.d(address);
        } else if (untilMessage instanceof Company_Point_Unit) {
            Company_Point_Unit message = (Company_Point_Unit) untilMessage;
            String name = message.getCiName() + " " + (message.getCdIdNum().equals("") ? message.getCdName() : message.getCdIdNum());
            nc.setProsecutedunits(name);
            String address = message.getRegAddress() + message.getCiAddr();
            nc.setProsecutedunits_adress(address);
            nc.setUnique_beunit(message.getRegId());
            nc.setSampleplace(message.getCiAddr());
        }
        //LogUtils.d(nc);
        mUntilMessage = untilMessage;
    }

    public void removeUntilMessage() {
        mUntilMessage = null;
        if (!(this instanceof Detection_Record_FGGD_NC)) {
            return;
        }
        Detection_Record_FGGD_NC nc = (Detection_Record_FGGD_NC) this;
        nc.setProsecutedunits(null);
        nc.setProsecutedunits_adress(null);
        nc.setUnique_beunit(null);
        nc.setSampleplace(null);
        nc.setReservedfield9(null);

    }

    public double getAbsorbance1_start() {
        return absorbance1_start;
    }

    public void setAbsorbance1_start(float absorbance1_start) {
        this.absorbance1_start = absorbance1_start;
    }

    public double getAbsorbance2_start() {
        return absorbance2_start;
    }

    public void setAbsorbance2_start(double absorbance2_start) {
        this.absorbance2_start = absorbance2_start;
    }

    public double getAbsorbance3_start() {
        return absorbance3_start;
    }

    public void setAbsorbance3_start(double absorbance3_start) {
        this.absorbance3_start = absorbance3_start;
    }

    public double getAbsorbance4_start() {
        return absorbance4_start;
    }

    public void setAbsorbance4_start(double absorbance4_start) {
        this.absorbance4_start = absorbance4_start;
    }

    public boolean isCheckState() {
        return checkState;
    }

    public void setCheckState(boolean checkState) {
        this.checkState = checkState;
    }

    /**
     * 读取模块编号
     */
    public void getCardNum() {

        mJTJRWHelper.sendMessage(Constants.COLLAURUM_NUMBER_ASK_P, true);


    }

    /**
     * 模块编号
     *
     * @return
     */
    public int getGalleryNum() {
        return galleryNum;
    }

    /**
     * 模块编号
     *
     * @param galleryNum
     */
    public void setGalleryNum(int galleryNum) {
        this.galleryNum = galleryNum;
        ((Detection_Record_FGGD_NC) this).setGallery(galleryNum);
    }

    /**
     * 通道状态  0初始化 1正在测试 2测试结束 3测试失败 4等待测试
     *
     * @return
     */
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;

    }

    public int getDowhat() {
        return dowhat;
    }

    public void setDowhat(int dowhat) {
        this.dowhat = dowhat;
    }

    public boolean isClearn() {
        return clearn;
    }

    public void setClearn(boolean clearn) {
        this.clearn = clearn;
    }

    public int getHowmanysecond() {
        return howmanysecond;
    }

    public void setHowmanysecond(int howmanysecond) {
        this.howmanysecond = howmanysecond;
    }

    public int getRemainingtime() {
        //LogUtils.d(galleryNum+"---"+remainingtime);
        return remainingtime;
    }

    public void setRemainingtime(int remainingtime) {
        this.remainingtime = remainingtime;
    }


    /**
     * 检测模块
     *
     * @param test_moudle
     */
    public void setTestMoudle(String test_moudle) {
        this.testmoudle = test_moudle;
        ((Detection_Record_FGGD_NC) this).setTest_Moudle(test_moudle);
    }

    public String getTestmoudle() {
        return testmoudle;
    }

    public int getWave1() {
        return wave1;
    }

    public void setWave1(int wave1) {
        this.wave1 = wave1;
    }

    public int getWave2() {
        return wave2;
    }

    public void setWave2(int wave2) {
        this.wave2 = wave2;
    }

    public int getWave3() {
        return wave3;
    }

    public void setWave3(int wave3) {
        this.wave3 = wave3;
    }

    public int getWave4() {
        return wave4;
    }

    public void setWave4(int wave4) {
        this.wave4 = wave4;
    }

    public int getDzh_wave1_start() {
        return dzh_wave1_start;
    }

    public void setDzh_wave1_start(int dzh_wave1_start) {
        this.dzh_wave1_start = dzh_wave1_start;
    }

    public int getDzh_wave2_start() {
        return dzh_wave2_start;
    }

    public void setDzh_wave2_start(int dzh_wave2_start) {
        this.dzh_wave2_start = dzh_wave2_start;
    }

    public int getDzh_wave3_start() {
        return dzh_wave3_start;
    }

    public void setDzh_wave3_start(int dzh_wave3_start) {
        this.dzh_wave3_start = dzh_wave3_start;
    }

    public int getDzh_wave4_start() {
        return dzh_wave4_start;
    }

    public void setDzh_wave4_start(int dzh_wave4_start) {
        this.dzh_wave4_start = dzh_wave4_start;
    }

    public int getWave1_start() {
        return wave1_start;
    }

    public void setWave1_start(int wave1_start) {
        this.wave1_start = wave1_start;
    }

    public int getWave2_start() {
        return wave2_start;
    }

    public void setWave2_start(int wave2_start) {
        this.wave2_start = wave2_start;
    }

    public int getWave3_start() {
        return wave3_start;
    }

    public void setWave3_start(int wave3_start) {
        this.wave3_start = wave3_start;
    }

    public int getWave4_start() {
        return wave4_start;
    }

    public void setWave4_start(int wave4_start) {
        this.wave4_start = wave4_start;
    }

    public float getLuminousness1() {
        return luminousness1;
    }

    public void setLuminousness1(float luminousness1) {
        this.luminousness1 = luminousness1;
    }

    public float getLuminousness2() {
        return luminousness2;
    }

    public void setLuminousness2(float luminousness2) {
        this.luminousness2 = luminousness2;
    }

    public float getLuminousness3() {
        return luminousness3;
    }

    public void setLuminousness3(float luminousness3) {
        this.luminousness3 = luminousness3;
    }

    public float getLuminousness4() {
        return luminousness4;
    }

    public void setLuminousness4(float luminousness4) {
        this.luminousness4 = luminousness4;
    }

    public double getAbsorbance1() {
        return absorbance1;
    }

    public void setAbsorbance1(double absorbance1) {
        this.absorbance1 = absorbance1;
    }

    public double getAbsorbance2() {
        return absorbance2;
    }

    public void setAbsorbance2(double absorbance2) {
        this.absorbance2 = absorbance2;
    }

    public double getAbsorbance3() {
        return absorbance3;
    }

    public void setAbsorbance3(double absorbance3) {
        this.absorbance3 = absorbance3;
    }

    public double getAbsorbance4() {
        return absorbance4;
    }

    public void setAbsorbance4(double absorbance4) {
        this.absorbance4 = absorbance4;
    }

    public double getAbsorbance1_after() {
        return absorbance1_after;
    }

    public void setAbsorbance1_after(double absorbance1_after) {
        this.absorbance1_after = absorbance1_after;
    }

    public double getAbsorbance2_after() {
        return absorbance2_after;
    }

    public void setAbsorbance2_after(double absorbance2_after) {
        this.absorbance2_after = absorbance2_after;
    }

    public double getAbsorbance3_after() {
        return absorbance3_after;
    }

    public void setAbsorbance3_after(double absorbance3_after) {
        this.absorbance3_after = absorbance3_after;
    }

    public double getAbsorbance4_after() {
        return absorbance4_after;
    }

    public void setAbsorbance4_after(double absorbance4_after) {
        this.absorbance4_after = absorbance4_after;
    }


    public boolean isProjectChange() {
        return projectChange;
    }

    public void setProjectChange(boolean projectChange) {
        this.projectChange = projectChange;
    }

    public void setAbsorbance1_start(double absorbance1_start) {
        this.absorbance1_start = absorbance1_start;
    }


    public boolean isSeachqrcode_flag() {
        return seachqrcode_flag;
    }

    public void setSeachqrcode_flag(boolean isok) {
        this.seachqrcode_flag = isok;
    }

    private boolean seachqrcode_flag = false;


    private List<Bitmap> mBitmapList;


    private boolean checkCard(Bitmap bitmap) {
        return PictureToolUtils.checkCard(bitmap);
    }

    private void saveErrorCard(Bitmap bitmap, int cardType, int findcount) {
        String path = "/mnt/sdcard/dayuan/failure/" + DataUtils.getFileNameNowtimeYYMMDD() + "/" + System.currentTimeMillis() + "type_" + cardType + "_find_" + findcount + ".jpg";
        FileUtils.createOrExistsFile(path);
        FileUtils.saveErrorJTJCardBitmap(bitmap, path);
    }


    Map<Integer, List<Float>> hashMap_userfuldata = new HashMap<>();

    public Map<Integer, List<Float>> getHashMap_userfuldata() {
        return hashMap_userfuldata;
    }


    protected void judgeAndSaveJTJData_P(double[] data) {
        LogUtils.d(data);
        Detection_Record_FGGD_NC detection_record_fggd_nc = (Detection_Record_FGGD_NC) this;

        //根据胶体金检测项目的相关参数 判定是否合格
        int method = mProjectMessage.getTestMethod();
        LogUtils.d(mProjectMessage);
        //根据通道号来取不同的参数（其实两个参数都是一样的，防止通道偏差造成结果不准）
        int i = JTJ_MAC % 2;
        double mCValue;
        double mTValueA;
        double mTValueB;
        double mCTValueA;
        double mCTValueB;
        mCValue = mProjectMessage.getC2();
        mTValueA = mProjectMessage.getT2A();
        mTValueB = mProjectMessage.getT2B();
        mCTValueA = mProjectMessage.getC2_t2A();
        mCTValueB = mProjectMessage.getC2_t2B();
        if (i == 1) { //取通道一的参数
            mCValue = mProjectMessage.getC1();
            mTValueA = mProjectMessage.getT1A();
            mTValueB = mProjectMessage.getT1B();
            mCTValueA = mProjectMessage.getC1_t1A();
            mCTValueB = mProjectMessage.getC1_t1B();
        }


        if (method == 1) {//消线法

            if (data[1] <= mCValue) { //无c线
                //无效卡
                if (data[3] <= mTValueA) {  //无c无t线
                    detection_record_fggd_nc.setTestresult(MyAppLocation.myAppLocation.getString(R.string.txt_invalid));
                    detection_record_fggd_nc.setDecisionoutcome(MyAppLocation.myAppLocation.getString(R.string.txt_invalid));
                    backgroundResous = 1;
                } else { //无c 有t线
                    detection_record_fggd_nc.setTestresult(MyAppLocation.myAppLocation.getString(R.string.txt_invalid));
                    detection_record_fggd_nc.setDecisionoutcome(MyAppLocation.myAppLocation.getString(R.string.txt_invalid));
                    backgroundResous = 2;
                }
            } else {//有c线/  判断t线
                if (data[3] <= mTValueA) {//有c无t线 /阳性
                    detection_record_fggd_nc.setTestresult(MyAppLocation.myAppLocation.getString(R.string.yang));
                    detection_record_fggd_nc.setDecisionoutcome(MyAppLocation.myAppLocation.getString(R.string.ng));
                    backgroundResous = 3;
                } else if (data[3] >= mTValueB) {//有c 有t线 阴性y
                    detection_record_fggd_nc.setTestresult(MyAppLocation.myAppLocation.getString(R.string.yin));
                    detection_record_fggd_nc.setDecisionoutcome(MyAppLocation.myAppLocation.getString(R.string.ok));
                    backgroundResous = 4;
                } else {
                    detection_record_fggd_nc.setTestresult(MyAppLocation.myAppLocation.getString(R.string.keyi));
                    detection_record_fggd_nc.setDecisionoutcome(MyAppLocation.myAppLocation.getString(R.string.keyi));
                    backgroundResous = 7;
                }
            }

        } else if (method == 2) {//比色法

            if (data[1] <= mCValue) { //无c线
                //无效卡
                if (data[3] <= mTValueA) {  //无c无t线
                    detection_record_fggd_nc.setTestresult(MyAppLocation.myAppLocation.getString(R.string.txt_invalid));
                    detection_record_fggd_nc.setDecisionoutcome(MyAppLocation.myAppLocation.getString(R.string.txt_invalid));
                    backgroundResous = 1;
                } else { //无c 有t线
                    detection_record_fggd_nc.setTestresult(MyAppLocation.myAppLocation.getString(R.string.txt_invalid));
                    detection_record_fggd_nc.setDecisionoutcome(MyAppLocation.myAppLocation.getString(R.string.txt_invalid));
                    backgroundResous = 2;
                }
            } else {
                if (data[3] / data[1] <= mCTValueA) {  //阳性
                    detection_record_fggd_nc.setTestresult(MyAppLocation.myAppLocation.getString(R.string.yang));
                    detection_record_fggd_nc.setDecisionoutcome(MyAppLocation.myAppLocation.getString(R.string.ng));
                    backgroundResous = 5;
                } else if (data[3] / data[1] >= mCTValueB) {  //阴性
                    detection_record_fggd_nc.setTestresult(MyAppLocation.myAppLocation.getString(R.string.yin));
                    detection_record_fggd_nc.setDecisionoutcome(MyAppLocation.myAppLocation.getString(R.string.ok));
                    backgroundResous = 6;
                } else {
                    detection_record_fggd_nc.setTestresult(MyAppLocation.myAppLocation.getString(R.string.keyi));
                    detection_record_fggd_nc.setDecisionoutcome(MyAppLocation.myAppLocation.getString(R.string.keyi));
                    backgroundResous = 8;
                }
            }


        }


        //检测完成时间
        detection_record_fggd_nc.setTestingtime(System.currentTimeMillis());
        //检测地点
        detection_record_fggd_nc.setTestsite(Constants.ADDR_WF);
        detection_record_fggd_nc.setLatitude(Constants.LATITUDE);
        detection_record_fggd_nc.setLongitude(Constants.LONTITUDE);
        //当前平台
        detection_record_fggd_nc.setPlatform_tag("");

        //检测人员   这里填的是本地登录的账号名称
        detection_record_fggd_nc.setInspector(Constants.NOWUSER.getUsername());
        //设置检测模块
        detection_record_fggd_nc.setTest_Moudle(ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.JTJ_TestMoudle_P));
        // 保存至数据库
        detection_record_fggd_nc.setId(null);//自增ID
        if (backgroundResous == 1 || backgroundResous == 2) {
            detection_record_fggd_nc.setState(3);
            LogUtils.d("结果无效");
        } else {
            //设置状态为检测完成 2
            detection_record_fggd_nc.setState(2);
        }


        String s = "C:" + new DecimalFormat("##0.000").format(data[1])
                + "  T:" + new DecimalFormat("##0.000").format(data[3])
                + "  T/C:" + new DecimalFormat("##0.000").format(data[3] / data[1]);

        detection_record_fggd_nc.setControlvalue(s);
        StringBuilder builder = new StringBuilder(); //使用线程安全的StringBuffer
        for (int i1 = 0; i1 < mUserfullData.size(); i1++) {
            builder.append(mUserfullData.get(i1) + ",");
        }
        JTJPoint entity = new JTJPoint();
        entity.setId(null);
        entity.setPointData(builder.toString());
        entity.setUuid(detection_record_fggd_nc.getSysCode());
        DBHelper.getJTJPointDao(MyAppLocation.myAppLocation).insertOrReplace(entity);

        FileUtils.saveBitmaplevel1(mBitmapList.get(0), detection_record_fggd_nc.getSysCode());
        FileUtils.saveBitmaplevel2(mBitmapList.get(1), detection_record_fggd_nc.getSysCode() + 1);


        LogUtils.d(detection_record_fggd_nc);


    }


    private double[] getData_P(List<Float> bytes) {

        return new JTJDataModel_P(bytes).getData_S();

    }


    public boolean isCountedDown() {
        return isCountedDown;
    }

    public void setCountedDown(boolean countedDown) {
        isCountedDown = countedDown;
    }

    /**
     * 卡状态请求
     */
    public void getCardStatus() {
        mJTJRWHelper.sendMessage(Constants.COLLAURUM_STATE_REQUEST_P, true);
    }

    int l;
    private ScheduledThreadPoolExecutor mTimer;

    public void startCountdown_JTJ(int timer) {
        l = timer;
        startJTJTest();
        if (null == mTimer) {
            mTimer = new ScheduledThreadPoolExecutor(1, new WorkerThreadFactory());
        }
        mTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                l--;
                LogUtils.d(l);
                if (l <= 0) {
                    getCardStatus();
                    stopCountdown_JTJ();
                }
                if (mReciveWeakReference != null) {
                    onJTJResultRecive recive = mReciveWeakReference.get();
                    if (recive != null) {
                        recive.onTimer(l);
                    }

                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void stopCountdown_JTJ() {
        if (null != mTimer) {
            mTimer.shutdown();
            mTimer = null;
        }
    }


    @Override
    public void reciver(byte[] bytes, int width, int height) {
        LogUtils.d("收到bitmap数据");
        if (4 == mJTJModel) {
            mBitmapList = PictureToolUtils.byteToBitMapExternal(bytes, width, height);
        } else {
            mBitmapList = PictureToolUtils.byteToBitMap(bytes, width, height);
        }
        LogUtils.d(mBitmapList.size());
        //更新UI
        if (mReciveWeakReference != null) {
            onJTJResultRecive recive = mReciveWeakReference.get();


            if (recive != null) {
                LogUtils.d(seachqrcode_flag);
                if (seachqrcode_flag) {
                    Bitmap bitmap = mBitmapList.get(mBitmapList.size() - 1);
                    recive.onReciverSuccess(bitmap);
                } else {
                    Bitmap bitmap = mBitmapList.get(0);
                    LogUtils.d(bitmap.getWidth() + "," + bitmap.getHeight());
                    recive.onReciverSuccess(bitmap);
                }

            }

        }
    }

    @Override
    public void reciver(List<Byte> bytes) {
        //LogUtils.d(bytes);
        if (bytes.size() < 2) {
            LogUtils.d(bytes);
            return;
        }

    }

    public Map<Integer, double[]> getHashMap_MultiCard() {
        return hashMap_MultiCard;
    }


    Map<Integer, double[]> hashMap_MultiCard = new HashMap<>();

    public void checkData_External(List<Bitmap> bitmapList) {
        setState(1);

        Bitmap bitmap1 = bitmapList.get(0);

        LogUtils.d(bitmap1.getWidth() + "  " + bitmap1.getHeight());

        LogUtils.d("checkData_External");
        LogUtils.d(getJTJCardModel());

        List<Bitmap> bitmaps;


        bitmaps = OpenCvUtils.getCardBitmap_External(bitmap1, CardType.ONE);
        LogUtils.d("CardType.ONE--" + bitmaps.size());
        if (bitmaps.size() != 1) {
            saveErrorCard(bitmap1, 1, bitmaps.size());
            ArmsUtils.snackbarText("卡识别异常");
            for (int i = 0; i < bitmaps.size(); i++) {
                bitmaps.get(i).recycle();
            }
            for (int i = 0; i < bitmapList.size(); i++) {
                bitmapList.get(i).recycle();
            }
            setState(0);
            bitmapList.clear();
            //System.gc();
            return;
        }

        LogUtils.d("lyl--开始检测1");
        startJTJTest();
        String filename = ((Detection_Record_FGGD_NC) this).getSysCode();
        FileUtils.saveBitmaplevel1(bitmap1, filename);
        hashMap_MultiCard.clear();
        hashMap_userfuldata.clear();
        //LogUtils.d("checkData_External--"+bitmaps.size());
        LogUtils.d("lyl--开始检测1--" + bitmaps.size());
        for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);
            int i1 = i + 1;

            Bitmap bitmapuserful = BitmapUtils.resizeBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            List<List<Float>> lists = PictureToolUtils.bitmap2RGB_list_6270(bitmapuserful);
            List<Float> userfuldata = new ArrayList<>();
            userfuldata.addAll(lists.get(3));
            for (int i2 = 0; i2 < lists.size(); i2++) {
                lists.get(i2).clear();
            }
            lists.clear();
            double[] data = getData_P_6270(userfuldata);
            LogUtils.d(i1);
            LogUtils.d(data);
            hashMap_MultiCard.put(i1, data);
            hashMap_userfuldata.put(i1, userfuldata);
            FileUtils.saveBitmaplevel2(bitmap, filename + i1);
        }
        LogUtils.d("lyl--开始检测2");
        judgeAndSaveMultiCard(hashMap_MultiCard, filename, hashMap_userfuldata, 1);
        LogUtils.d("lyl--开始检测3");

        //System.gc();
        if (mReciveWeakReference != null) {
            onJTJResultRecive recive = mReciveWeakReference.get();
            if (recive != null) {
                recive.onReciverSuccess(new ArrayList<List<Float>>(), new ArrayList<double[]>());
            }
        }
    }

    private double[] getData_P_6270(List<Float> bytes) {

        return new JTJDataModel_P_6270(bytes).getData_S();

    }

    private void judgeAndSaveMultiCard(Map<Integer, double[]> hashMap, String filename, Map<Integer, List<Float>> hashMap1, int testtype) {
        LogUtils.d("judgeAndSaveMultiCard");
        LogUtils.d(getJTJCardModel());
        LogUtils.d(hashMap);
        Detection_Record_FGGD_NC detection_record_fggd_nc = (Detection_Record_FGGD_NC) this;
        BaseProjectMessage message = null;
        StringBuffer testresult = new StringBuffer();
        StringBuffer decisionoutcome = new StringBuffer();
        StringBuffer testmethod = new StringBuffer();
        StringBuffer testdata = new StringBuffer();
        Set<Integer> integers = hashMap.keySet();
        TreeSet<Integer> set = new TreeSet<>(((o1, o2) -> o1.compareTo(o2)));
        set.addAll(integers);
        LogUtils.d(set);
        for (Integer integer : set) {
            double[] data = hashMap.get(integer);
            LogUtils.d(integer);
            LogUtils.d(data);

            message = getProjectMessage();


            testdata.append("C:" + NumberUtils.three(data[1]) + "  T:" + NumberUtils.three(data[3]) + "  T/C:" + NumberUtils.three(data[3] / data[1]) + "\r\n");
            LogUtils.d(message);
            testmethod.append(message.getMethod() + " ");

            int method = message.getTestMethod();
            int i = JTJ_MAC % 2;
            double mCValue = message.getC2();
            double mTValueA = message.getT2A();
            double mTValueB = message.getT2B();
            double mCTValueA = message.getC2_t2A();
            double mCTValueB = message.getC2_t2B();
            LogUtils.d(i);
            LogUtils.d(mCValue + "===" + mTValueA + "===" + mTValueB);
            if (i == 1) { //取通道一的参数
                mCValue = message.getC1();
                mTValueA = message.getT1A();
                mTValueB = message.getT1B();
                mCTValueA = message.getC1_t1A();
                mCTValueB = message.getC1_t1B();
            }
            String result = "";
            String decom = "";
            if (method == 1) {//消线法
                if (data[0] == 0) {
                    testresult.append("无效 ");
                    decisionoutcome.append("无效 ");
                    result = "无效";
                    decom = "无效";
                } else {
                    if (data[1] <= mCValue) { //无c线
                        //无效卡
                        if (data[3] <= mTValueA) {  //无c无t线
                            testresult.append("无效 ");
                            decisionoutcome.append("无效 ");
                            result = "无效";
                            decom = "无效";
                            backgroundResous = 1;
                        } else { //无c 有t线
                            testresult.append("无效 ");
                            decisionoutcome.append("无效 ");
                            result = "无效";
                            decom = "无效";
                            backgroundResous = 2;
                        }
                    } else {//有c线/  判断t线
                        if (data[3] <= mTValueA) {//有c无t线 /阳性
                            testresult.append("阳性 ");
                            decisionoutcome.append("不合格 ");
                            result = "阳性";
                            decom = "不合格";
                            backgroundResous = 3;
                        } else if (data[3] >= mTValueB) {//有c 有t线 阴性y
                            testresult.append("阴性 ");
                            decisionoutcome.append("合格 ");
                            result = "阴性";
                            decom = "合格";
                            backgroundResous = 4;
                        } else {
                            testresult.append("可疑 ");
                            decisionoutcome.append("可疑 ");
                            result = "可疑";
                            decom = "可疑";
                            backgroundResous = 7;
                        }
                    }
                }


            } else if (method == 2) {//比色法
                if (data[0] == 0) {
                    testresult.append("无效 ");
                    decisionoutcome.append("无效 ");
                    result = "无效";
                    decom = "无效";
                } else {
                    if (data[1] <= mCValue) { //无c线
                        //无效卡
                        if (data[3] <= mTValueA) {  //无c无t线
                            testresult.append("无效 ");
                            decisionoutcome.append("无效 ");
                            result = "无效";
                            decom = "无效";
                            backgroundResous = 1;
                        } else { //无c 有t线
                            testresult.append("无效 ");
                            decisionoutcome.append("无效 ");
                            result = "无效";
                            decom = "无效";
                            backgroundResous = 2;
                        }
                    } else {
                        //无T线判阳性
                        if (data[3] <= mTValueA) {//有c无t线 /阳性
                            testresult.append("阳性 ");
                            decisionoutcome.append("不合格 ");
                            result = "阳性";
                            decom = "不合格";
                            backgroundResous = 3;
                        } else {
                            if (data[3] / data[1] <= mCTValueA) {  //阳性
                                testresult.append("阳性 ");
                                decisionoutcome.append("不合格 ");
                                result = "阳性";
                                decom = "不合格";
                                backgroundResous = 5;
                            } else if (data[3] / data[1] >= mCTValueB) {  //阴性
                                testresult.append("阴性 ");
                                decisionoutcome.append("合格 ");
                                result = "阴性";
                                decom = "合格";
                                backgroundResous = 6;
                            } else {
                                testresult.append("可疑 ");
                                decisionoutcome.append("可疑 ");
                                result = "可疑";
                                decom = "可疑";
                                backgroundResous = 8;
                            }
                        }

                    }
                }
            }


        }

        LogUtils.d("检测结果：" + testresult.toString());

        detection_record_fggd_nc.setTest_method(testmethod.toString());
        detection_record_fggd_nc.setTestresult(testresult.toString());
        detection_record_fggd_nc.setControlvalue(testdata.toString());

        String decisionoutcome1 = decisionoutcome.toString().trim();
        if (decisionoutcome1.contains("不合格")) {
            detection_record_fggd_nc.setDecisionoutcome("不合格");
        } else if (decisionoutcome1.contains("无效")) {
            detection_record_fggd_nc.setDecisionoutcome("无效");
        } else if (decisionoutcome1.equals("可疑")) {
            detection_record_fggd_nc.setDecisionoutcome("可疑");
        } else {
            detection_record_fggd_nc.setDecisionoutcome("合格");
        }

        //检测完成时间
        long testingtime = System.currentTimeMillis();
        LogUtils.d(testingtime);
        detection_record_fggd_nc.setTestingtime(testingtime);
        //检测地点
        detection_record_fggd_nc.setTestsite(Constants.ADDR_WF);
        detection_record_fggd_nc.setLatitude(Constants.LATITUDE);
        detection_record_fggd_nc.setLongitude(Constants.LONTITUDE);
        //当前平台
        detection_record_fggd_nc.setPlatform_tag(Constants.PLATFORM_TAG + "");
        //对照值
        //detection_record_fggd_nc.setControlvalue(Constants.FGGD_YIZHILV_CONTROL_VALUE + "");
        String userName;
        if (Constants.IS_OFFLINE_MODE) {
            userName = "";
        } else {
            ObjUserData userPlatform = Constants.USER_PLATFORM;
            userName = userPlatform.getUser().getUser_name();
        }
        //检测人员   这里填的是本地登录的账号名称
        detection_record_fggd_nc.setInspector(userName);
        //设置检测模块
        detection_record_fggd_nc.setTest_Moudle(ArmsUtils.getString(MyAppLocation.myAppLocation, R.string.JTJ_TestMoudle_P));
        // 保存至数据库
        detection_record_fggd_nc.setId(null);//自增ID
        //设置状态为检测完成 2
        detection_record_fggd_nc.setState(2);
        //detection_record_fggd_nc.setDowhat(0);
        LogUtils.d(detection_record_fggd_nc);
        for (Integer integer : hashMap1.keySet()) {
            List<Float> floats = hashMap1.get(integer);
            //LogUtils.d(floats);
            //LogUtils.d(filename + integer);


            StringBuilder builder = new StringBuilder(); //使用线程安全的StringBuffer
            for (int i1 = 0; i1 < floats.size(); i1++) {
                builder.append(floats.get(i1) + ",");
            }
            JTJPoint entity = new JTJPoint();
            entity.setId(null);
            entity.setPointData(builder.toString());
            String uuid = filename + integer;
            LogUtils.d(uuid);
            entity.setUuid(uuid);
            DBHelper.getJTJPointDao(MyAppLocation.myAppLocation).insert(entity);

        }


        printAndUpload(detection_record_fggd_nc);
    }

    private void printAndUpload(Detection_Record_FGGD_NC detection_record_fggd_nc) {
        Detection_Record_FGGD_NCDao detection_record_fggd_ncDao = DBHelper.getDetection_Record_FGGD_NCDao(MyAppLocation.myAppLocation);

        //System.out.println("插入数据库流水号：" + detection_record_fggd_nc.getSerialNumber());
        long insert = detection_record_fggd_ncDao.insertOrReplace(detection_record_fggd_nc);

        String task = detection_record_fggd_nc.getUnique_task();
        LogUtils.d(task);

        //重检数据完成后需要跟新被重检的数据的状态
        if (detection_record_fggd_nc.getRetest() == 1) {
            String code = detection_record_fggd_nc.getParentSysCode();
            Detection_Record_FGGD_NCDao dao = DBHelper.getDetection_Record_FGGD_NCDao(MyAppLocation.myAppLocation);
            List<Detection_Record_FGGD_NC> list = dao.queryBuilder().where(Detection_Record_FGGD_NCDao.Properties.SysCode.eq(code)).list();
            if (list.size() > 0) {
                Detection_Record_FGGD_NC nc = list.get(0);
                nc.setRetest(2);
                dao.update(nc);
            }
        }

        if (Constants.AUTO_UPLOAD && !Constants.IS_OFFLINE_MODE) {
            UpLoadIntentService.startUpLoad(MyAppLocation.myAppLocation, insert);
        }

    }
}
