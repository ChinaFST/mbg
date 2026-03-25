package com.dy.colony.greendao.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.mvp.model.entity.base.GalleryBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.text.SimpleDateFormat;

/**
 * Created by 王振雄 on 2017/5/25.
 */
@Entity
public class Detection_Record_FGGD_NC extends GalleryBean implements Parcelable {
    @Id(autoincrement = true)
    private Long id; //数据库id
    private String sysCode;//检测唯一编号
    private int gallery;//通道id
    private String samplenum;//样品编号
    private String samplename;//样品名称
    private String sampletype;//样品种类
    private String foodCode;//样品种类别编号
    private String symbol;//判定符号
    private String cov;//判定值sy
    private String cov_unit;//判定单位
    private String stand_num;//判定标准号

    private String prosecutedunits;//被检单位
    private String prosecutedunits_adress;//被检单位地址
    private double dilutionratio = 1; //稀释倍数
    private double everyresponse = 1;//反应液滴数
    private String controlvalue;//对照值
    private String serialNumber;//流水号

    private String testresult;//检测结果值
    private String decisionoutcome;//判定结果
    private String inspector; //检测人员
    private Long testingtime;//检测时间
    private double longitude;//经度
    private double latitude;//纬度
    private String testsite;//检测地点

    private String test_method;//检测方法
    private String test_project;//检测项目
    private String test_moudle;//检测模块

    private String planName;//任务名称

    private int isupload = 2;//是否上传

    private String unique_sample;//样品名称唯一编号
    private String unique_method;//检测方法唯一编号
    private String unique_testproject;//检测项目唯一编号
    private String unique_beunit;//被检单位唯一编号
    private String unique_task;//检测任务唯一编号
    private String platform_tag;//产生记录时使用的平台

    //使用平台时可能会有检测单位
    private String test_unit_id;//检测单位唯一id
    private String test_unit_name;//检测单位名称
    private String test_unit_reserved;//检测单位预留字段


    //增加数据库字段
    private String sampleplace;//样品产地
    private String qrcode;//胶体金卡二维码数据

    /**
     * 0 正常数据
     * 1 为重检数据
     * 2 已经重检
     */
    private int retest = 0;//重检
    private String parentSysCode;//重检的syscode

    private String reservedfield1;//预留字段1
    private String reservedfield2;//预留字段2
    private String reservedfield3;//预留字段3
    private String reservedfield4;//预留字段4
    private String reservedfield5;//预留字段5
    private String reservedfield6;//预留字段6
    private String reservedfield7;//预留字段7
    private String reservedfield8;//预留字段8
    private String reservedfield9;//预留字段9
    private String reservedfield10;//预留字段10


    protected Detection_Record_FGGD_NC(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        sysCode = in.readString();
        gallery = in.readInt();
        samplenum = in.readString();
        samplename = in.readString();
        sampletype = in.readString();
        foodCode = in.readString();
        symbol = in.readString();
        cov = in.readString();
        cov_unit = in.readString();
        stand_num = in.readString();
        prosecutedunits = in.readString();
        prosecutedunits_adress = in.readString();
        dilutionratio = in.readDouble();
        everyresponse = in.readDouble();
        controlvalue = in.readString();
        serialNumber = in.readString();
        testresult = in.readString();
        decisionoutcome = in.readString();
        inspector = in.readString();
        if (in.readByte() == 0) {
            testingtime = null;
        } else {
            testingtime = in.readLong();
        }
        longitude = in.readDouble();
        latitude = in.readDouble();
        testsite = in.readString();
        test_method = in.readString();
        test_project = in.readString();
        test_moudle = in.readString();
        planName = in.readString();
        isupload = in.readInt();
        unique_sample = in.readString();
        unique_method = in.readString();
        unique_testproject = in.readString();
        unique_beunit = in.readString();
        unique_task = in.readString();
        platform_tag = in.readString();
        test_unit_id = in.readString();
        test_unit_name = in.readString();
        test_unit_reserved = in.readString();
        sampleplace = in.readString();
        qrcode = in.readString();
        retest = in.readInt();
        parentSysCode = in.readString();
        reservedfield1 = in.readString();
        reservedfield2 = in.readString();
        reservedfield3 = in.readString();
        reservedfield4 = in.readString();
        reservedfield5 = in.readString();
        reservedfield6 = in.readString();
        reservedfield7 = in.readString();
        reservedfield8 = in.readString();
        reservedfield9 = in.readString();
        reservedfield10 = in.readString();
        sn = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(sysCode);
        dest.writeInt(gallery);
        dest.writeString(samplenum);
        dest.writeString(samplename);
        dest.writeString(sampletype);
        dest.writeString(foodCode);
        dest.writeString(symbol);
        dest.writeString(cov);
        dest.writeString(cov_unit);
        dest.writeString(stand_num);
        dest.writeString(prosecutedunits);
        dest.writeString(prosecutedunits_adress);
        dest.writeDouble(dilutionratio);
        dest.writeDouble(everyresponse);
        dest.writeString(controlvalue);
        dest.writeString(serialNumber);
        dest.writeString(testresult);
        dest.writeString(decisionoutcome);
        dest.writeString(inspector);
        if (testingtime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(testingtime);
        }
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(testsite);
        dest.writeString(test_method);
        dest.writeString(test_project);
        dest.writeString(test_moudle);
        dest.writeString(planName);
        dest.writeInt(isupload);
        dest.writeString(unique_sample);
        dest.writeString(unique_method);
        dest.writeString(unique_testproject);
        dest.writeString(unique_beunit);
        dest.writeString(unique_task);
        dest.writeString(platform_tag);
        dest.writeString(test_unit_id);
        dest.writeString(test_unit_name);
        dest.writeString(test_unit_reserved);
        dest.writeString(sampleplace);
        dest.writeString(qrcode);
        dest.writeInt(retest);
        dest.writeString(parentSysCode);
        dest.writeString(reservedfield1);
        dest.writeString(reservedfield2);
        dest.writeString(reservedfield3);
        dest.writeString(reservedfield4);
        dest.writeString(reservedfield5);
        dest.writeString(reservedfield6);
        dest.writeString(reservedfield7);
        dest.writeString(reservedfield8);
        dest.writeString(reservedfield9);
        dest.writeString(reservedfield10);
        dest.writeString(sn);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Detection_Record_FGGD_NC> CREATOR = new Creator<Detection_Record_FGGD_NC>() {
        @Override
        public Detection_Record_FGGD_NC createFromParcel(Parcel in) {
            return new Detection_Record_FGGD_NC(in);
        }

        @Override
        public Detection_Record_FGGD_NC[] newArray(int size) {
            return new Detection_Record_FGGD_NC[size];
        }
    };

    public String getQrcode() {
        return qrcode == null ? "" : qrcode;
    }

    public void setQrcode(String qrcode) {
        LogUtils.d("设置 qrcode：" + qrcode);
        this.qrcode = qrcode == null ? "" : qrcode;
    }


    public String getSn() {
        return sn == null ? "" : sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? "" : sn;
    }

    @Transient
    private String sn;


    @Override
    public String toString() {
        return "Detection_Record_FGGD_NC{" +
                "id=" + id +
                ", sysCode='" + sysCode + '\'' +
                ", gallery=" + gallery +
                ", samplenum='" + samplenum + '\'' +
                ", samplename='" + samplename + '\'' +
                ", sampletype='" + sampletype + '\'' +
                ", foodCode='" + foodCode + '\'' +
                ", symbol='" + symbol + '\'' +
                ", cov='" + cov + '\'' +
                ", cov_unit='" + cov_unit + '\'' +
                ", stand_num='" + stand_num + '\'' +
                ", prosecutedunits='" + prosecutedunits + '\'' +
                ", prosecutedunits_adress='" + prosecutedunits_adress + '\'' +
                ", dilutionratio=" + dilutionratio +
                ", everyresponse=" + everyresponse +
                ", controlvalue='" + controlvalue + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", testresult='" + testresult + '\'' +
                ", decisionoutcome='" + decisionoutcome + '\'' +
                ", inspector='" + inspector + '\'' +
                ", testingtime=" + testingtime +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", testsite='" + testsite + '\'' +
                ", test_method='" + test_method + '\'' +
                ", test_project='" + test_project + '\'' +
                ", test_moudle='" + test_moudle + '\'' +
                ", planName='" + planName + '\'' +
                ", isupload=" + isupload +
                ", unique_sample='" + unique_sample + '\'' +
                ", unique_method='" + unique_method + '\'' +
                ", unique_testproject='" + unique_testproject + '\'' +
                ", unique_beunit='" + unique_beunit + '\'' +
                ", unique_task='" + unique_task + '\'' +
                ", platform_tag='" + platform_tag + '\'' +
                ", test_unit_id='" + test_unit_id + '\'' +
                ", test_unit_name='" + test_unit_name + '\'' +
                ", test_unit_reserved='" + test_unit_reserved + '\'' +
                ", sampleplace='" + sampleplace + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", retest=" + retest +
                ", parentSysCode='" + parentSysCode + '\'' +
                ", reservedfield1='" + reservedfield1 + '\'' +
                ", reservedfield2='" + reservedfield2 + '\'' +
                ", reservedfield3='" + reservedfield3 + '\'' +
                ", reservedfield4='" + reservedfield4 + '\'' +
                ", reservedfield5='" + reservedfield5 + '\'' +
                ", reservedfield6='" + reservedfield6 + '\'' +
                ", reservedfield7='" + reservedfield7 + '\'' +
                ", reservedfield8='" + reservedfield8 + '\'' +
                ", reservedfield9='" + reservedfield9 + '\'' +
                ", reservedfield10='" + reservedfield10 + '\'' +
                ", sn='" + sn + '\'' +
                '}';
    }


    private String getTxt(int resId) {
        return MyAppLocation.myAppLocation.getString(resId);
    }

    /**
     * 0 正常数据
     * 1 为重检数据
     * 2 已经重检
     */
    public int getRetest() {
        return retest;
    }

    public void setRetest(int retest) {
        this.retest = retest;
    }

    public String getParentSysCode() {
        return parentSysCode == null ? "" : parentSysCode;
    }

    public void setParentSysCode(String parentSysCode) {
        this.parentSysCode = parentSysCode == null ? "" : parentSysCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGallery() {
        return gallery;
    }

    public void setGallery(int gallery) {
        this.gallery = gallery;
    }

    public String getSamplenum() {
        return samplenum == null ? "" : samplenum;
    }

    public void setSamplenum(String samplenum) {
        this.samplenum = samplenum == null ? "" : samplenum;
    }

    public String getSamplename() {
        return samplename == null ? "" : samplename;
    }


    public void setSamplename(String samplename) {
        this.samplename = samplename == null ? "" : samplename;
    }

    public void setSamplenameByself(String samplename) {

        this.samplename = samplename == null ? "" : samplename;
    }

    public String getCov() {

        return cov == null ? "" : cov;
    }

    public void setCov(String cov) {
        LogUtils.d(cov);
        this.cov = cov == null ? "" : cov;
    }

    public String getSymbol() {

        return symbol == null ? "" : symbol;
    }

    public String getSymbolText() {

        String s = symbol == null ? "" : symbol;
        if (s.equals("<")) {
            return "小于";
        } else if (s.equals(">")) {
            return "大于";
        }
        return s;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol == null ? "" : symbol;
    }

    public String getCov_unit() {

        return cov_unit == null ? "" : cov_unit;
    }

    public void setCov_unit(String cov_unit) {
        this.cov_unit = cov_unit == null ? "" : cov_unit;
    }

    public String getStand_num() {
        return stand_num == null ? "" : stand_num;
    }

    public void setStand_num(String stand_num) {
        this.stand_num = stand_num == null ? "" : stand_num;
    }

    public String getProsecutedunits() {
        return prosecutedunits == null ? "" : prosecutedunits;
    }

    public void setProsecutedunits(String prosecutedunits) {
        this.prosecutedunits = prosecutedunits == null ? "" : prosecutedunits;
    }

    public String getProsecutedunits_adress() {
        return prosecutedunits_adress == null ? "" : prosecutedunits_adress;
    }

    public void setProsecutedunits_adress(String prosecutedunits_adress) {
        this.prosecutedunits_adress = prosecutedunits_adress == null ? "" : prosecutedunits_adress;
    }

    public double getDilutionratio() {
        return dilutionratio;
    }

    public void setDilutionratio(double dilutionratio) {
        this.dilutionratio = dilutionratio;
    }

    public double getEveryresponse() {
        return everyresponse;
    }

    public void setEveryresponse(double everyresponse) {
        this.everyresponse = everyresponse;
    }

    public String getControlvalue() {
        return controlvalue == null ? "" : controlvalue;
    }

    public void setControlvalue(String controlvalue) {
        this.controlvalue = controlvalue == null ? "" : controlvalue;
    }

    public String getSerialNumber() {
        return serialNumber == null ? "" : serialNumber;
    }

    public int getIntSerialNumber() {
        try {

            return serialNumber == null ? 0 : Integer.valueOf(serialNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? "" : serialNumber;
    }

    public String getTestresult() {

        return testresult == null ? "" : testresult;
    }


    public void setTestresult(String testresult) {
        this.testresult = testresult == null ? "" : testresult;
    }

    public String getDecisionoutcome() {

        return decisionoutcome == null ? "" : decisionoutcome;
    }


    public void setDecisionoutcome(String decisionoutcome) {
        this.decisionoutcome = decisionoutcome == null ? "" : decisionoutcome;
    }

    public String getInspector() {
        return inspector == null ? "" : inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector == null ? "" : inspector;
    }

    public Long getTestingtime() {
        return testingtime;
    }

    public String getdfTestingtimeYY_MM_DD() {
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd").format(testingtime);
        }
        return format;
    }

    public String getdfTestingtimeYYMMDD() {
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyyMMdd").format(testingtime);
        }
        return format;
    }

    /**
     * @param s 需要转换的样式 yymmdd ，yyyy-MM-dd
     * @return
     */
    public String getdfTestingtime(String s) {
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat(s).format(testingtime);
        }
        return format;
    }


    public String getdfTestingtimeyy_mm_dd_hh_mm_ss() {
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testingtime);
        }
        return format;
    }


    public void setTestingtime(Long testingtime) {

        this.testingtime = testingtime;

    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTestsite() {
        return testsite == null ? "" : testsite;
    }

    public void setTestsite(String testsite) {
        this.testsite = testsite == null ? "" : testsite;
    }


    public String getTest_method() {
        if (test_method == null) {
            return "";
        } else {
            if ("0".equals(test_method)) {
                return MyAppLocation.myAppLocation.getString(R.string.mothod1);
            } else if ("1".equals(test_method)) {
                return MyAppLocation.myAppLocation.getString(R.string.mothod2);
            } else if ("2".equals(test_method)) {
                return MyAppLocation.myAppLocation.getString(R.string.mothod3);
            } else if ("3".equals(test_method)) {
                return MyAppLocation.myAppLocation.getString(R.string.mothod4);
            } else {
                return test_method;
            }
        }
    }

    public void setTest_method(String test_method) {
        this.test_method = test_method == null ? "" : test_method;
    }

    public String getTest_project() {

        return test_project == null ? "" : test_project;
    }

    public void setTest_project(String test_project) {

        this.test_project = test_project == null ? "" : test_project;
    }

    public String getTest_Moudle() {
        return test_moudle == null ? "" : test_moudle;
    }

    public void setTest_Moudle(String test_moudle) {
        this.test_moudle = test_moudle == null ? "" : test_moudle;
    }

    public String getSysCode() {
        return sysCode == null ? "" : sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode == null ? "" : sysCode.replace("-", "");
    }

    public String getSampletype() {
        return sampletype == null ? "" : sampletype;
    }

    public void setSampletype(String sampletype) {
        this.sampletype = sampletype == null ? "" : sampletype;
    }


    public String getFoodCode() {
        return foodCode == null ? "" : foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode == null ? "" : foodCode;
    }


    public int getIsupload() {
        return isupload;
    }

    public void setIsupload(int isupload) {
        this.isupload = isupload;
    }


    public Detection_Record_FGGD_NC() {
    }

    @Generated(hash = 1869665736)
    public Detection_Record_FGGD_NC(Long id, String sysCode, int gallery, String samplenum, String samplename,
                                    String sampletype, String foodCode, String symbol, String cov, String cov_unit, String stand_num,
                                    String prosecutedunits, String prosecutedunits_adress, double dilutionratio, double everyresponse,
                                    String controlvalue, String serialNumber, String testresult, String decisionoutcome, String inspector,
                                    Long testingtime, double longitude, double latitude, String testsite, String test_method,
                                    String test_project, String test_moudle, String planName, int isupload, String unique_sample,
                                    String unique_method, String unique_testproject, String unique_beunit, String unique_task,
                                    String platform_tag, String test_unit_id, String test_unit_name, String test_unit_reserved,
                                    String sampleplace, String qrcode, int retest, String parentSysCode, String reservedfield1,
                                    String reservedfield2, String reservedfield3, String reservedfield4, String reservedfield5,
                                    String reservedfield6, String reservedfield7, String reservedfield8, String reservedfield9,
                                    String reservedfield10) {
        this.id = id;
        this.sysCode = sysCode;
        this.gallery = gallery;
        this.samplenum = samplenum;
        this.samplename = samplename;
        this.sampletype = sampletype;
        this.foodCode = foodCode;
        this.symbol = symbol;
        this.cov = cov;
        this.cov_unit = cov_unit;
        this.stand_num = stand_num;
        this.prosecutedunits = prosecutedunits;
        this.prosecutedunits_adress = prosecutedunits_adress;
        this.dilutionratio = dilutionratio;
        this.everyresponse = everyresponse;
        this.controlvalue = controlvalue;
        this.serialNumber = serialNumber;
        this.testresult = testresult;
        this.decisionoutcome = decisionoutcome;
        this.inspector = inspector;
        this.testingtime = testingtime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.testsite = testsite;
        this.test_method = test_method;
        this.test_project = test_project;
        this.test_moudle = test_moudle;
        this.planName = planName;
        this.isupload = isupload;
        this.unique_sample = unique_sample;
        this.unique_method = unique_method;
        this.unique_testproject = unique_testproject;
        this.unique_beunit = unique_beunit;
        this.unique_task = unique_task;
        this.platform_tag = platform_tag;
        this.test_unit_id = test_unit_id;
        this.test_unit_name = test_unit_name;
        this.test_unit_reserved = test_unit_reserved;
        this.sampleplace = sampleplace;
        this.qrcode = qrcode;
        this.retest = retest;
        this.parentSysCode = parentSysCode;
        this.reservedfield1 = reservedfield1;
        this.reservedfield2 = reservedfield2;
        this.reservedfield3 = reservedfield3;
        this.reservedfield4 = reservedfield4;
        this.reservedfield5 = reservedfield5;
        this.reservedfield6 = reservedfield6;
        this.reservedfield7 = reservedfield7;
        this.reservedfield8 = reservedfield8;
        this.reservedfield9 = reservedfield9;
        this.reservedfield10 = reservedfield10;
    }


    public String getUnique_sample() {
        return unique_sample == null ? "" : unique_sample;
    }

    public void setUnique_sample(String unique_sample) {
        this.unique_sample = unique_sample == null ? "" : unique_sample;
    }

    public String getUnique_method() {
        return unique_method == null ? "" : unique_method;
    }

    public void setUnique_method(String unique_method) {
        this.unique_method = unique_method == null ? "" : unique_method;
    }

    public String getUnique_testproject() {
        return unique_testproject == null ? "" : unique_testproject;
    }

    public void setUnique_testproject(String unique_testproject) {
        this.unique_testproject = unique_testproject == null ? "" : unique_testproject;
    }

    public String getUnique_beunit() {
        return unique_beunit == null ? "" : unique_beunit;
    }

    public void setUnique_beunit(String unique_beunit) {
        this.unique_beunit = unique_beunit == null ? "" : unique_beunit;
    }

    public String getUnique_task() {
        return unique_task == null ? "" : unique_task;
    }

    public void setUnique_task(String unique_task) {
        this.unique_task = unique_task == null ? "" : unique_task;
    }


    public String getPlatform_tag() {
        return platform_tag == null ? "" : platform_tag;
    }

    public void setPlatform_tag(String platform_tag) {
        this.platform_tag = platform_tag == null ? "" : platform_tag;
    }


    public String getPlanName() {
        return planName == null ? "" : planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName == null ? "" : planName;
    }

    public String getTest_unit_name() {
        return test_unit_name == null ? "" : test_unit_name;
    }

    public void setTest_unit_name(String test_unit_name) {
        this.test_unit_name = test_unit_name == null ? "" : test_unit_name;
    }

    public String getTest_unit_id() {
        return test_unit_id == null ? "" : test_unit_id;
    }

    public void setTest_unit_id(String test_unit_id) {
        this.test_unit_id = test_unit_id == null ? "" : test_unit_id;
    }

    public String getTest_unit_reserved() {
        return test_unit_reserved == null ? "" : test_unit_reserved;
    }

    public void setTest_unit_reserved(String test_unit_reserved) {
        this.test_unit_reserved = test_unit_reserved == null ? "" : test_unit_reserved;
    }


    public String getSampleplace() {
        return this.sampleplace == null ? "" : sampleplace;
    }

    public void setSampleplace(String sampleplace) {
        if (this.getRetest() == 1) {
            this.setRetest(0);
            this.setParentSysCode(null);
        }
        this.sampleplace = sampleplace;
    }


    public String getReservedfield1() {
        return reservedfield1 == null ? "" : reservedfield1;
    }

    public void setReservedfield1(String reservedfield1) {
        this.reservedfield1 = reservedfield1 == null ? "" : reservedfield1;
    }

    public String getReservedfield2() {
        return reservedfield2 == null ? "" : reservedfield2;
    }

    public void setReservedfield2(String reservedfield2) {
        this.reservedfield2 = reservedfield2 == null ? "" : reservedfield2;
    }

    public String getReservedfield3() {
        return reservedfield3 == null ? "" : reservedfield3;
    }

    public void setReservedfield3(String reservedfield3) {
        this.reservedfield3 = reservedfield3 == null ? "" : reservedfield3;
    }

    public String getReservedfield4() {
        return reservedfield4 == null ? "" : reservedfield4;
    }

    public void setReservedfield4(String reservedfield4) {
        this.reservedfield4 = reservedfield4 == null ? "" : reservedfield4;
    }

    public String getReservedfield5() {
        return reservedfield5 == null ? "" : reservedfield5;
    }

    public void setReservedfield5(String reservedfield5) {
        this.reservedfield5 = reservedfield5 == null ? "" : reservedfield5;
    }


    public String getReservedfield6() {
        return reservedfield6 == null ? "" : reservedfield6;
    }

    public void setReservedfield6(String reservedfield6) {
        this.reservedfield6 = reservedfield6 == null ? "" : reservedfield6;
    }

    public String getReservedfield7() {
        return reservedfield7 == null ? "" : reservedfield7;
    }

    public void setReservedfield7(String reservedfield7) {
        this.reservedfield7 = reservedfield7 == null ? "" : reservedfield7;
    }

    public String getReservedfield8() {
        return reservedfield8 == null ? "" : reservedfield8;
    }

    public void setReservedfield8(String reservedfield8) {
        this.reservedfield8 = reservedfield8 == null ? "" : reservedfield8;
    }

    public String getReservedfield9() {
        return reservedfield9 == null ? "" : reservedfield9;
    }

    public void setReservedfield9(String reservedfield9) {
        this.reservedfield9 = reservedfield9 == null ? "" : reservedfield9;
    }

    public String getReservedfield10() {
        return reservedfield10 == null ? "" : reservedfield10;
    }

    public void setReservedfield10(String reservedfield10) {
        this.reservedfield10 = reservedfield10 == null ? "" : reservedfield10;
    }


    public String getTest_moudle() {
        return this.test_moudle;
    }

    public void setTest_moudle(String test_moudle) {
        this.test_moudle = test_moudle;
    }


    public String toMyString() {
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        if (test_project == null) {
            test_project = "-----";
        }
        if (test_method == null) {
            test_method = "-----";
        }
        if (serialNumber == null) {
            serialNumber = "-----";
        }
        if (samplename == null) {
            samplename = "-----";
        }
        if (gallery > 20 && gallery != 101 && gallery != 102) {
            gallery = gallery - 20;
        } else if (gallery == 101) {
            gallery = 01;
        } else if (gallery == 102) {
            gallery = 02;
        }
        if (stand_num == null) {
            stand_num = "-----";
        }
        if (symbol == null) {
            symbol = "-----";
        }
        if (controlvalue == null) {
            controlvalue = "-----";
        }
        if (testresult == null) {
            testresult = "-----";
        }
        if (decisionoutcome == null) {
            decisionoutcome = "-----";
        }
        if (inspector == null) {
            inspector = "-----";
        }

        if (testsite == null) {
            testsite = "-----";
        }
        if (prosecutedunits == null) {
            prosecutedunits = "-----";
        }
        if (cov == null) {
            cov = "-----";
        }
        if (cov_unit == null) {
            cov_unit = "-----";
        }
        String s = "";
        String s1 = "";
        String s2 = "";

        s = (symbol) + (cov) + (cov_unit);
        s1 = testresult;
        s2 = decisionoutcome;


        String useSampleNum = samplenum;

        String use_test_moudle = test_moudle;
        if (test_moudle.contains(getTxt(R.string.JTJ_TestMoudle_P))) {
            use_test_moudle = getTxt(R.string.JTJ_TestMoudle_P);
        }


        StringBuilder builder = new StringBuilder();
        builder.append(getTxt(R.string.detect_result_split) + "\n");
        builder.append(getTxt(R.string.testmoudle_colon) + use_test_moudle + "\n");
        builder.append(getTxt(R.string.test_project_colon) + test_project + " " + "\n");
        builder.append(getTxt(R.string.test_method_colon) + (test_method) + "\n");
        builder.append(getTxt(R.string.sample_number_colon) + (useSampleNum) + "\n");

        builder.append(getTxt(R.string.serial_number_colon) + (serialNumber) + "\n");
        builder.append(getTxt(R.string.sample_name_colon) + (samplename) + "\n");
        builder.append(getTxt(R.string.channel_number_colon) + (gallery) + "\n");
        builder.append(getTxt(R.string.judgment_basis_colon) + (stand_num) + "\n");
        builder.append(getTxt(R.string.dilution_factor) + (dilutionratio) + "\n");
        builder.append(getTxt(R.string.number_reaction_drops) + (everyresponse) + "\n");
        builder.append(getTxt(R.string.control_value_colon) + (controlvalue) + "\n");
        String str = (test_moudle.contains(getTxt(R.string.JTJ_TestMoudle_P)) ? (getTxt(R.string.code_testing_card) + qrcode + "\n") : "");
        builder.append(str);
        builder.append(getTxt(R.string.test_result_colon) + s1 + "\n");
        builder.append(getTxt(R.string.limit_value_colon) + s + "\n");
        builder.append(getTxt(R.string.judgment_result_colon) + s2 + "\n");
        //String unit = getTxt(R.string.inspected_unit_colon) + (prosecutedunits);
        //builder.append(unit + "\n");
        builder.append(getTxt(R.string.sample_origin_colon) + (sampleplace) + "\n");
        builder.append(getTxt(R.string.inspectors_colon) + inspector + "\n");
        String str1 = (test_moudle.contains(getTxt(R.string.JTJ_TestMoudle_P)) ? getTxt(R.string.reading_time_colon) : getTxt(R.string.detect_time_colon)) + (df1.format(testingtime)) + "\n";
        builder.append(str1);
        builder.append(getTxt(R.string.testing_locations_colon) + (testsite) + "\n");
        builder.append(getTxt(R.string.longitude_colon) + (longitude) + "\n");
        builder.append(getTxt(R.string.latitude_colon) + (latitude) + "\n");
        return builder.toString();
    }


}
