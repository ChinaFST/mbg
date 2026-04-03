package com.dy.colony.greendao.beans;

import android.os.Parcel;
import android.os.Parcelable;


import androidx.annotation.IdRes;

import com.blankj.utilcode.util.StringUtils;
import com.dy.colony.R;
import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/12
 */
@Entity
public class FGGDTestItem extends BaseProjectMessage implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String project_name;//项目名称/
    private String password;//密码/
    private String method;//四种方法对应名称
    private int wavelength;//波长段/
    private int yuretime;//预热时间/
    private int jiancetime;//检测时间/
    private String unit_input;//检测值单位/
    private int serialNumber;//流水号
    private float controValue;//每个检测项目都有一个对照值
    private String controValue_lastTime;//产生对照值的时间

    private String biaozhun_a0;
    private String biaozhun_b0;
    private String biaozhun_c0;
    private String biaozhun_d0;
    private String biaozhun_from0;
    private String biaozhun_to0;

    private String biaozhun_a1;
    private String biaozhun_b1;
    private String biaozhun_c1;
    private String biaozhun_d1;
    private String biaozhun_from1;
    private String biaozhun_to1;

    private double jiaozhen_a;
    private double jiaozhen_b;
    private double jiaozhen_c;
    private double jiaozhen_d;

    private double yin_a;
    private double yin_b;
    private double yang_a;
    private double yang_b;
    private double keyi_a;
    private double keyi_b;
    private boolean usetuise;
    private String ceshi;
    private String unique_testproject;
    private String version;

    private Long priority;//优先级

    @Override
    public String toMyStringProject() {
        return project_name + "\r\n" +
                StringUtils.getString(R.string.project_unique_id) + unique_testproject + "\r\n" +
                StringUtils.getString(R.string.ed_waves) + wavelength + "\r\n" +
                StringUtils.getString(R.string.test_method_colon) + method;
    }

    @Override
    public String toMyStringMethod() {
        String s = null;
        switch (method) {
            case "0":
                s = StringUtils.getString(R.string.mothod1) + "\r\n" +
                        getAppendStr(R.string.str_yure_time)   + yuretime + "\r\n" +
                        getAppendStr(R.string.testtime) + jiancetime + "\r\n" +
                        getAppendStr(R.string.unit_input) + unit_input + "\r\n" +
                    StringUtils.getString(R.string.negative_range_colon) + yin_a + "--" + yin_b + "\r\n" +
                    StringUtils.getString(R.string.positive_range_colon) + yang_a + "--" + yang_b + "\r\n" +
                    StringUtils.getString(R.string.keyi_range_colon) + keyi_a + "--" + keyi_b;
                break;
            case "1":
                s = StringUtils.getString(R.string.mothod2) + "\r\n" +
                        getAppendStr(R.string.str_yure_time) + yuretime + "\r\n" +
                        getAppendStr(R.string.testtime) + jiancetime + "\r\n" +
                        getAppendStr(R.string.unit_input) + unit_input + "\r\n" +
                        StringUtils.getString(R.string.standard_curve_a_param) + "  X0：" + biaozhun_a0 + "  X1：" + biaozhun_b0 + "  X2：" + biaozhun_c0 + "  X3：" + biaozhun_d0 + "  FROM0：" + biaozhun_from0 + "  TO0：" + biaozhun_to0 + "\r\n" +
                        StringUtils.getString(R.string.standard_curve_b_param) + "  X0：" + biaozhun_a1 + "  X1：" + biaozhun_b1 + "  X2：" + biaozhun_c1 + "  X3：" + biaozhun_d1 + "  FROM1：" + biaozhun_from1 + "  TO1：" + biaozhun_to1 + "\r\n" +
                        StringUtils.getString(R.string.correction_curve_param) + "  A：" + jiaozhen_a + "  B：" + jiaozhen_b +
                        StringUtils.getString(R.string.use_tuise)  + (usetuise ? StringUtils.getString(R.string.txt_yes)  : StringUtils.getString(R.string.txt_no));
                break;
            case "2":
                s = StringUtils.getString(R.string.mothod3) + "\r\n" +
                        getAppendStr(R.string.str_yure_time) + yuretime + "\r\n" +
                        getAppendStr(R.string.testtime) + jiancetime + "\r\n" +
                        getAppendStr(R.string.unit_input) + unit_input + "\r\n" +
                        StringUtils.getString(R.string.standard_curve_colon) + "  A：" + jiaozhen_a + "  B：" + jiaozhen_b +
                        StringUtils.getString(R.string.negative_range_colon) + yin_a + "--" + yin_b + "\r\n" +
                        StringUtils.getString(R.string.positive_range_colon)+ yang_a + "--" + yang_b + "\r\n" +
                        StringUtils.getString(R.string.keyi_range_colon)+ keyi_a + "--" + keyi_b +
                        StringUtils.getString(R.string.use_tuise) + (usetuise ? StringUtils.getString(R.string.txt_yes)  : StringUtils.getString(R.string.txt_no));
                break;
            case "3":
                s = StringUtils.getString(R.string.mothod4) + "\r\n" +

                        getAppendStr(R.string.unit_input) + unit_input + "\r\n" +
                        StringUtils.getString(R.string.correction_curve_param)+ "  A：" + jiaozhen_a + "  B：" + jiaozhen_b + "  C：" + jiaozhen_c + "  D：" + jiaozhen_d;
                break;
        }
        return s;
    }

    private String getAppendStr(@IdRes int id) {
        return StringUtils.getString(id) + ": ";
    }

    public String getUseMethod() {
        String s = null;
        switch (method) {
            case "0":
                s = StringUtils.getString(R.string.mothod1);
                break;
            case "1":
                s = StringUtils.getString(R.string.mothod2);
                break;
            case "2":
                s = StringUtils.getString(R.string.mothod3);
                break;
            case "3":
                s = StringUtils.getString(R.string.mothod4);
                break;
        }
        return s;
    }

    public OutMoudle<String> toJxlTitle() {
        return new OutMoudle<String>("编号,项目名称,方法名称,波长段,预热时间,检测时间,密码," +
                "标准曲线a0,标准曲线b0,标准曲线c0,标准曲线d0,标准曲线from0,标准曲线to0," +
                "标准曲线a1,标准曲线b1,标准曲线c1,标准曲线d1,标准曲线from1,标准曲线to1," +
                "校正曲线a,校正曲线b,校正曲线c,校正曲线d," +
                "阴性范围a,阴性范围b,阳性范围a,阳性范围b,可疑范围a,可疑范围b," +
                "是否使用褪色法,测试,简要提示,检测值单位,流水号,唯一编号,最后更新时间");
    }

    public OutMoudle<String> toJxlString() {
        return new OutMoudle<String>(id + "," + project_name + "," + method + "," + wavelength + "," + yuretime + "," + jiancetime + "," + password + "," +
                biaozhun_a0 + "," + biaozhun_b0 + "," + biaozhun_c0 + "," + biaozhun_d0 + "," + biaozhun_from0 + "," + biaozhun_to0 + "," +
                biaozhun_a1 + "," + biaozhun_b1 + "," + biaozhun_c1 + "," + biaozhun_d1 + "," + biaozhun_from1 + "," + biaozhun_to1 + "," +
                jiaozhen_a + "," + jiaozhen_b + "," + jiaozhen_c + "," + jiaozhen_d + "," +
                yin_a + "," + yin_b + "," + yang_a + "," + yang_b + "," + keyi_a + "," + keyi_b + "," +
                usetuise + "," + StringUtils.getString(R.string.str_test) + "," + StringUtils.getString(R.string.str_brief_tips) + "," + unit_input + "," + serialNumber + "," + unique_testproject + "," + version);
    }

    public FGGDTestItem() {
    }

    @Override
    public String getVersion() {
        return version == null ? "" : version;
    }

    public void setVersion(String version) {
        this.version = version == null ? "" : version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setProject_name(String project_name) {
        this.project_name = project_name == null ? "" : project_name;
    }

    public String getProject_name() {
        return project_name;
    }

    @Override
    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password == null ? "" : password;
    }

    @Override
    public String getMethod() {
        return method == null ? "" : method;
    }

    public void setMethod(String method) {
        this.method = method == null ? "" : method;
    }

    @Override
    public int getWavelength() {
        return wavelength;
    }

    public void setWavelength(int wavelength) {
        this.wavelength = wavelength;
    }

    @Override
    public int getYuretime() {
        return yuretime;
    }

    public void setYuretime(int yuretime) {
        this.yuretime = yuretime;
    }

    @Override
    public int getJiancetime() {
        return jiancetime;
    }

    public void setJiancetime(int jiancetime) {
        this.jiancetime = jiancetime;
    }

    @Override
    public String getUnit_input() {
        return unit_input == null ? "" : unit_input;
    }

    public void setUnit_input(String unit_input) {
        this.unit_input = unit_input == null ? "" : unit_input;
    }

    @Override
    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String getBiaozhun_a0() {
        return biaozhun_a0 == null ? "" : biaozhun_a0;
    }

    public void setBiaozhun_a0(String biaozhun_a0) {
        this.biaozhun_a0 = biaozhun_a0 == null ? "" : biaozhun_a0;
    }

    @Override
    public String getBiaozhun_b0() {
        return biaozhun_b0 == null ? "" : biaozhun_b0;
    }

    public void setBiaozhun_b0(String biaozhun_b0) {
        this.biaozhun_b0 = biaozhun_b0 == null ? "" : biaozhun_b0;
    }

    @Override
    public String getBiaozhun_c0() {
        return biaozhun_c0 == null ? "" : biaozhun_c0;
    }

    public void setBiaozhun_c0(String biaozhun_c0) {
        this.biaozhun_c0 = biaozhun_c0 == null ? "" : biaozhun_c0;
    }

    @Override
    public String getBiaozhun_d0() {
        return biaozhun_d0 == null ? "" : biaozhun_d0;
    }

    public void setBiaozhun_d0(String biaozhun_d0) {
        this.biaozhun_d0 = biaozhun_d0 == null ? "" : biaozhun_d0;
    }

    @Override
    public String getBiaozhun_from0() {
        return biaozhun_from0 == null ? "" : biaozhun_from0;
    }

    public void setBiaozhun_from0(String biaozhun_from0) {
        this.biaozhun_from0 = biaozhun_from0 == null ? "" : biaozhun_from0;
    }

    @Override
    public String getBiaozhun_to0() {
        return biaozhun_to0 == null ? "" : biaozhun_to0;
    }

    public void setBiaozhun_to0(String biaozhun_to0) {
        this.biaozhun_to0 = biaozhun_to0 == null ? "" : biaozhun_to0;
    }

    @Override
    public String getBiaozhun_a1() {
        return biaozhun_a1 == null ? "" : biaozhun_a1;
    }

    public void setBiaozhun_a1(String biaozhun_a1) {
        this.biaozhun_a1 = biaozhun_a1 == null ? "" : biaozhun_a1;
    }

    @Override
    public String getBiaozhun_b1() {
        return biaozhun_b1 == null ? "" : biaozhun_b1;
    }

    public void setBiaozhun_b1(String biaozhun_b1) {
        this.biaozhun_b1 = biaozhun_b1 == null ? "" : biaozhun_b1;
    }

    @Override
    public String getBiaozhun_c1() {
        return biaozhun_c1 == null ? "" : biaozhun_c1;
    }

    public void setBiaozhun_c1(String biaozhun_c1) {
        this.biaozhun_c1 = biaozhun_c1 == null ? "" : biaozhun_c1;
    }

    @Override
    public String getBiaozhun_d1() {
        return biaozhun_d1 == null ? "" : biaozhun_d1;
    }

    public void setBiaozhun_d1(String biaozhun_d1) {
        this.biaozhun_d1 = biaozhun_d1 == null ? "" : biaozhun_d1;
    }

    @Override
    public String getBiaozhun_from1() {
        return biaozhun_from1 == null ? "" : biaozhun_from1;
    }

    public void setBiaozhun_from1(String biaozhun_from1) {
        this.biaozhun_from1 = biaozhun_from1 == null ? "" : biaozhun_from1;
    }

    @Override
    public String getBiaozhun_to1() {
        return biaozhun_to1 == null ? "" : biaozhun_to1;
    }

    public void setBiaozhun_to1(String biaozhun_to1) {
        this.biaozhun_to1 = biaozhun_to1 == null ? "" : biaozhun_to1;
    }

    @Override
    public double getJiaozhen_a() {
        return jiaozhen_a;
    }

    public void setJiaozhen_a(double jiaozhen_a) {
        this.jiaozhen_a = jiaozhen_a;
    }

    @Override
    public double getJiaozhen_b() {
        return jiaozhen_b;
    }

    public void setJiaozhen_b(double jiaozhen_b) {
        this.jiaozhen_b = jiaozhen_b;
    }

    @Override
    public double getJiaozhen_c() {
        return jiaozhen_c;
    }

    public void setJiaozhen_c(double jiaozhen_c) {
        this.jiaozhen_c = jiaozhen_c;
    }

    @Override
    public double getJiaozhen_d() {
        return jiaozhen_d;
    }

    public void setJiaozhen_d(double jiaozhen_d) {
        this.jiaozhen_d = jiaozhen_d;
    }

    @Override
    public double getYin_a() {
        return yin_a;
    }

    public void setYin_a(double yin_a) {
        this.yin_a = yin_a;
    }

    @Override
    public double getYin_b() {
        return yin_b;
    }

    public void setYin_b(double yin_b) {
        this.yin_b = yin_b;
    }

    @Override
    public double getYang_a() {
        return yang_a;
    }

    public void setYang_a(double yang_a) {
        this.yang_a = yang_a;
    }

    @Override
    public double getYang_b() {
        return yang_b;
    }

    public void setYang_b(double yang_b) {
        this.yang_b = yang_b;
    }

    @Override
    public double getKeyi_a() {
        return keyi_a;
    }

    public void setKeyi_a(double keyi_a) {
        this.keyi_a = keyi_a;
    }

    @Override
    public double getKeyi_b() {
        return keyi_b;
    }

    public void setKeyi_b(double keyi_b) {
        this.keyi_b = keyi_b;
    }

    @Override
    public boolean isUsetuise() {
        return usetuise;
    }

    public void setUsetuise(boolean usetuise) {
        this.usetuise = usetuise;
    }

    public String getCeshi() {
        return ceshi == null ? "" : ceshi;
    }

    public void setCeshi(String ceshi) {
        this.ceshi = ceshi == null ? "" : ceshi;
    }

    public static Creator<FGGDTestItem> getCREATOR() {
        return CREATOR;
    }

    public boolean getUsetuise() {
        return this.usetuise;
    }


    @Override
    public float getControValue() {
        return controValue;
    }

    public void setControValue(float controValue) {
        this.controValue = controValue;
    }

    public String getControValue_lastTime() {
        return controValue_lastTime == null ? "" : controValue_lastTime;
    }

    public void setControValue_lastTime(String controValue_lastTime) {
        this.controValue_lastTime = controValue_lastTime == null ? "" : controValue_lastTime;
    }

    public String getUnique_testproject() {
        return unique_testproject == null ? "" : unique_testproject;
    }

    public void setUnique_testproject(String unique_testproject) {
        this.unique_testproject = unique_testproject == null ? "" : unique_testproject;
    }

    public Long getPriority() {
        return this.priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.project_name);
        dest.writeString(this.password);
        dest.writeString(this.method);
        dest.writeInt(this.wavelength);
        dest.writeInt(this.yuretime);
        dest.writeInt(this.jiancetime);
        dest.writeString(this.unit_input);
        dest.writeInt(this.serialNumber);
        dest.writeFloat(this.controValue);
        dest.writeString(this.controValue_lastTime);
        dest.writeString(this.biaozhun_a0);
        dest.writeString(this.biaozhun_b0);
        dest.writeString(this.biaozhun_c0);
        dest.writeString(this.biaozhun_d0);
        dest.writeString(this.biaozhun_from0);
        dest.writeString(this.biaozhun_to0);
        dest.writeString(this.biaozhun_a1);
        dest.writeString(this.biaozhun_b1);
        dest.writeString(this.biaozhun_c1);
        dest.writeString(this.biaozhun_d1);
        dest.writeString(this.biaozhun_from1);
        dest.writeString(this.biaozhun_to1);
        dest.writeDouble(this.jiaozhen_a);
        dest.writeDouble(this.jiaozhen_b);
        dest.writeDouble(this.jiaozhen_c);
        dest.writeDouble(this.jiaozhen_d);
        dest.writeDouble(this.yin_a);
        dest.writeDouble(this.yin_b);
        dest.writeDouble(this.yang_a);
        dest.writeDouble(this.yang_b);
        dest.writeDouble(this.keyi_a);
        dest.writeDouble(this.keyi_b);
        dest.writeByte(this.usetuise ? (byte) 1 : (byte) 0);
        dest.writeString(this.ceshi);
        dest.writeString(this.unique_testproject);
        dest.writeString(this.version);
        dest.writeValue(this.priority);
    }

    protected FGGDTestItem(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.project_name = in.readString();
        this.password = in.readString();
        this.method = in.readString();
        this.wavelength = in.readInt();
        this.yuretime = in.readInt();
        this.jiancetime = in.readInt();
        this.unit_input = in.readString();
        this.serialNumber = in.readInt();
        this.controValue = in.readFloat();
        this.controValue_lastTime = in.readString();
        this.biaozhun_a0 = in.readString();
        this.biaozhun_b0 = in.readString();
        this.biaozhun_c0 = in.readString();
        this.biaozhun_d0 = in.readString();
        this.biaozhun_from0 = in.readString();
        this.biaozhun_to0 = in.readString();
        this.biaozhun_a1 = in.readString();
        this.biaozhun_b1 = in.readString();
        this.biaozhun_c1 = in.readString();
        this.biaozhun_d1 = in.readString();
        this.biaozhun_from1 = in.readString();
        this.biaozhun_to1 = in.readString();
        this.jiaozhen_a = in.readDouble();
        this.jiaozhen_b = in.readDouble();
        this.jiaozhen_c = in.readDouble();
        this.jiaozhen_d = in.readDouble();
        this.yin_a = in.readDouble();
        this.yin_b = in.readDouble();
        this.yang_a = in.readDouble();
        this.yang_b = in.readDouble();
        this.keyi_a = in.readDouble();
        this.keyi_b = in.readDouble();
        this.usetuise = in.readByte() != 0;
        this.ceshi = in.readString();
        this.unique_testproject = in.readString();
        this.version = in.readString();
        this.priority = (Long) in.readValue(Long.class.getClassLoader());
    }

    @Generated(hash = 1395506498)
    public FGGDTestItem(Long id, String project_name, String password, String method, int wavelength, int yuretime, int jiancetime, String unit_input, int serialNumber, float controValue,
                        String controValue_lastTime, String biaozhun_a0, String biaozhun_b0, String biaozhun_c0, String biaozhun_d0, String biaozhun_from0, String biaozhun_to0, String biaozhun_a1, String biaozhun_b1,
                        String biaozhun_c1, String biaozhun_d1, String biaozhun_from1, String biaozhun_to1, double jiaozhen_a, double jiaozhen_b, double jiaozhen_c, double jiaozhen_d, double yin_a, double yin_b,
                        double yang_a, double yang_b, double keyi_a, double keyi_b, boolean usetuise, String ceshi, String unique_testproject, String version, Long priority) {
        this.id = id;
        this.project_name = project_name;
        this.password = password;
        this.method = method;
        this.wavelength = wavelength;
        this.yuretime = yuretime;
        this.jiancetime = jiancetime;
        this.unit_input = unit_input;
        this.serialNumber = serialNumber;
        this.controValue = controValue;
        this.controValue_lastTime = controValue_lastTime;
        this.biaozhun_a0 = biaozhun_a0;
        this.biaozhun_b0 = biaozhun_b0;
        this.biaozhun_c0 = biaozhun_c0;
        this.biaozhun_d0 = biaozhun_d0;
        this.biaozhun_from0 = biaozhun_from0;
        this.biaozhun_to0 = biaozhun_to0;
        this.biaozhun_a1 = biaozhun_a1;
        this.biaozhun_b1 = biaozhun_b1;
        this.biaozhun_c1 = biaozhun_c1;
        this.biaozhun_d1 = biaozhun_d1;
        this.biaozhun_from1 = biaozhun_from1;
        this.biaozhun_to1 = biaozhun_to1;
        this.jiaozhen_a = jiaozhen_a;
        this.jiaozhen_b = jiaozhen_b;
        this.jiaozhen_c = jiaozhen_c;
        this.jiaozhen_d = jiaozhen_d;
        this.yin_a = yin_a;
        this.yin_b = yin_b;
        this.yang_a = yang_a;
        this.yang_b = yang_b;
        this.keyi_a = keyi_a;
        this.keyi_b = keyi_b;
        this.usetuise = usetuise;
        this.ceshi = ceshi;
        this.unique_testproject = unique_testproject;
        this.version = version;
        this.priority = priority;
    }


    public static final Creator<FGGDTestItem> CREATOR = new Creator<FGGDTestItem>() {
        @Override
        public FGGDTestItem createFromParcel(Parcel source) {
            return new FGGDTestItem(source);
        }

        @Override
        public FGGDTestItem[] newArray(int size) {
            return new FGGDTestItem[size];
        }
    };
}
