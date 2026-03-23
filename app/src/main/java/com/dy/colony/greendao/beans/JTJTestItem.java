package com.dy.colony.greendao.beans;

import android.os.Parcel;
import android.os.Parcelable;


import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.model.entity.base.BaseProjectMessage;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by 王振雄 on 2017/2/7.
 */
@Entity
public class JTJTestItem extends BaseProjectMessage implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String number;
    private String projectName;
    private int testMethod;
    private double c1;
    // private double t1;
    private double t1A;
    private double t1B;
    private double c1_t1A;
    private double c1_t1B;

    private double c2; ///0.05
    // private double t2;
    private double t2A;
    private double t2B;
    private double c2_t2A;
    private double c2_t2B;
    private int testTime;
    private String password;
    private int serialNumber;
    private String unique_testproject;
    private String item_type;//1 扫描模块 2 摄像头模块 3 V9000
    private String version;

    private Long priority;//优先级

    @Override
    public String toMyStringProject() {
        return projectName + "\r\n" +
                "检测项目唯一ID：" + unique_testproject + "\r\n" +
                "检测方法：" + (testMethod == 1 ? "定性消线法" : "定性比色法") + "\r\n";
    }

    @Override
    public String toMyStringMethod() {
        String s = null;
        switch (testMethod) {
            case 1:
                s = "定性消线法" + "\r\n" +
                        "C线出线值：" + c1 + "\r\n" +
                        "T线出线值：" + t1A + "--" + t1B + "\r\n";
                break;
            case 2:
                s = "定性比色法" + "\r\n" +
                        "C线出线值：" + c1 + "\r\n" +
                        "T线出线值：" + t1A + "--" + t1B + "\r\n" +
                        "C/T出线值：" + c1_t1A + "--" + c1_t1B;
                break;
        }
        return s;
    }

    public OutMoudle<String> toJxlTitle() {
        return new OutMoudle<String>("编号," + "项目名称," + "方法（1消线;2比色）," + "C1值," + "T1A值," + "T1B值," + "T1/C1A值," + "T1/C1B值," + "C2值,"
                + "T2A值," + "T2B值," + "T2/C2A值," + "T2/C2B值," + "流水号," + "检测时间," + "密码," + "唯一编号," + "检测项目类型,"+"最后更新时间");
    }

    public OutMoudle<String> toJxlString() {
        return new OutMoudle<String>(number + "," + projectName.replaceAll(",","#") + "," + testMethod + "," + c1 + "," + t1A + "," + t1B + "," + c1_t1A + "," +
                c1_t1B + "," + c2 + "," + t2A + "," + t2B + "," + c2_t2A + "," +
                c2_t2B + "," + serialNumber + "," + testTime + "," + password + "," + unique_testproject +","+ item_type+","+version);
    }


    public JTJTestItem() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number == null ? "" : number;
    }

    public void setNumber(String number) {
        this.number = number == null ? "" : number;
    }

    @Override
    public String getProjectName() {
        return projectName == null ? "" : projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? "" : projectName;
    }

    @Override
    public int getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(int testMethod) {
        this.testMethod = testMethod;
    }

    @Override
    public double getC1() {
        return c1;
    }

    public void setC1(double c1) {
        this.c1 = c1;
    }

    @Override
    public double getT1A() {
        return t1A;
    }

    public void setT1A(double t1A) {
        this.t1A = t1A;
    }

    @Override
    public double getT1B() {
        return t1B;
    }

    public void setT1B(double t1B) {
        this.t1B = t1B;
    }

    @Override
    public double getC1_t1A() {
        return c1_t1A;
    }

    public void setC1_t1A(double c1_t1A) {
        this.c1_t1A = c1_t1A;
    }

    @Override
    public double getC1_t1B() {
        return c1_t1B;
    }

    public void setC1_t1B(double c1_t1B) {
        this.c1_t1B = c1_t1B;
    }

    @Override
    public double getC2() {
        return c2;
    }

    public void setC2(double c2) {
        this.c2 = c2;
    }

    @Override
    public double getT2A() {
        return t2A;
    }

    public void setT2A(double t2A) {
        this.t2A = t2A;
    }

    @Override
    public double getT2B() {
        return t2B;
    }

    public void setT2B(double t2B) {
        this.t2B = t2B;
    }

    @Override
    public double getC2_t2A() {
        return c2_t2A;
    }

    public void setC2_t2A(double c2_t2A) {
        this.c2_t2A = c2_t2A;
    }

    @Override
    public double getC2_t2B() {
        return c2_t2B;
    }

    public void setC2_t2B(double c2_t2B) {
        this.c2_t2B = c2_t2B;
    }

    @Override
    public int getTestTime() {
        return testTime;
    }

    public void setTestTime(int testTime) {
        this.testTime = testTime;
    }

    @Override
    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password == null ? "" : password;
    }

    @Override
    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUnique_testproject() {
        return unique_testproject == null ? "" : unique_testproject;
    }

    public void setUnique_testproject(String unique_testproject) {
        this.unique_testproject = unique_testproject == null ? "" : unique_testproject;
    }

    @Override
    public String getItem_type() {
        return item_type == null ? "" : item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type == null ? "" : item_type;
    }

    @Override
    public String getVersion() {
        return version == null ? "" : version;
    }

    public void setVersion(String version) {
        this.version = version == null ? "" : version;
    }

    public static Creator<JTJTestItem> getCREATOR() {
        return CREATOR;
    }

    public Long getPriority() {
        return this.priority;
    }

    public void setPriority(Long priority) {
        this.priority=priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.number);
        dest.writeString(this.projectName);
        dest.writeInt(this.testMethod);
        dest.writeDouble(this.c1);
        dest.writeDouble(this.t1A);
        dest.writeDouble(this.t1B);
        dest.writeDouble(this.c1_t1A);
        dest.writeDouble(this.c1_t1B);
        dest.writeDouble(this.c2);
        dest.writeDouble(this.t2A);
        dest.writeDouble(this.t2B);
        dest.writeDouble(this.c2_t2A);
        dest.writeDouble(this.c2_t2B);
        dest.writeInt(this.testTime);
        dest.writeString(this.password);
        dest.writeInt(this.serialNumber);
        dest.writeString(this.unique_testproject);
        dest.writeString(this.item_type);
        dest.writeString(this.version);
        dest.writeValue(this.priority);
    }

    protected JTJTestItem(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.number = in.readString();
        this.projectName = in.readString();
        this.testMethod = in.readInt();
        this.c1 = in.readDouble();
        this.t1A = in.readDouble();
        this.t1B = in.readDouble();
        this.c1_t1A = in.readDouble();
        this.c1_t1B = in.readDouble();
        this.c2 = in.readDouble();
        this.t2A = in.readDouble();
        this.t2B = in.readDouble();
        this.c2_t2A = in.readDouble();
        this.c2_t2B = in.readDouble();
        this.testTime = in.readInt();
        this.password = in.readString();
        this.serialNumber = in.readInt();
        this.unique_testproject = in.readString();
        this.item_type = in.readString();
        this.version = in.readString();
        this.priority = (Long) in.readValue(Long.class.getClassLoader());
    }

    @Generated(hash = 1699490018)
    public JTJTestItem(Long id, String number, String projectName, int testMethod, double c1, double t1A, double t1B, double c1_t1A, double c1_t1B, double c2,
            double t2A, double t2B, double c2_t2A, double c2_t2B, int testTime, String password, int serialNumber, String unique_testproject, String item_type,
            String version, Long priority) {
        this.id = id;
        this.number = number;
        this.projectName = projectName;
        this.testMethod = testMethod;
        this.c1 = c1;
        this.t1A = t1A;
        this.t1B = t1B;
        this.c1_t1A = c1_t1A;
        this.c1_t1B = c1_t1B;
        this.c2 = c2;
        this.t2A = t2A;
        this.t2B = t2B;
        this.c2_t2A = c2_t2A;
        this.c2_t2B = c2_t2B;
        this.testTime = testTime;
        this.password = password;
        this.serialNumber = serialNumber;
        this.unique_testproject = unique_testproject;
        this.item_type = item_type;
        this.version = version;
        this.priority = priority;
    }

    

    public static final Creator<JTJTestItem> CREATOR = new Creator<JTJTestItem>() {
        @Override
        public JTJTestItem createFromParcel(Parcel source) {
            return new JTJTestItem(source);
        }

        @Override
        public JTJTestItem[] newArray(int size) {
            return new JTJTestItem[size];
        }
    };
}