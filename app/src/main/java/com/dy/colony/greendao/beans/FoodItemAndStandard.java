package com.dy.colony.greendao.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringRes;


import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.model.entity.base.BaseSampleMessage;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/12
 */
@Entity
public class FoodItemAndStandard extends BaseSampleMessage implements Parcelable {
    /**
     * checkId : 000cdc68d2644a7ebbd61f15f308d2c0
     * checkSign : ≤
     * checkValueUnit : mg/kg
     * foodPCode : 00001001100010001
     * itemName : 苋菜红
     * sampleName : 流质糖果
     * sampleNum : 000010011000100010011
     * standardName : GB 2760-2014
     * standardValue : 50.0
     * uDate : 2017-09-30 15:16:33.0
     */
    public String toMyString() {
        return getMyString(R.string.sample_details) + "\r\n" +
                getMyString(R.string.unique_id) + checkId + "\r\n" +
                getMyString(R.string.sample_name_colon) + sampleName + "\r\n" +
                getMyString(R.string.test_project_colon) + itemName + "\r\n" +
                getMyString(R.string.sample_number_colon) + sampleNum + "\r\n" +
                getMyString(R.string.parent_class_number_colon) + foodPCode + "\r\n" +
                getMyString(R.string.testing_standards_colon) + standardName + "\r\n" +
                getMyString(R.string.limit_value_colon) + checkSign + standardValue + checkValueUnit + "\r\n" +
                getMyString(R.string.update_date_colon) + uDate + "\r\n" +
                getMyString(R.string.manually_add_colon) + (flag == 2 ? getMyString(R.string.txt_yes) : getMyString(R.string.txt_no));
    }

    @Id(autoincrement = true)
    private Long id;
    private String checkId;
    private String checkSign;
    private String checkValueUnit;
    private String foodPCode;
    private String itemName;
    private String sampleName;
    private String sampleNum;
    private String standardName;
    private String standardValue;
    private String uDate;
    private int flag;

    private Long priority;//优先级

    private String getMyString(@StringRes int resId) {
        return MyAppLocation.myAppLocation.getString(resId);
    }

   /* public OutMoudle<String> toJxlTitle() {
        return new OutMoudle<String>(MyAppLocation.myAppLocation.getString(R.string.sample_jxl_title_1) +
                MyAppLocation.myAppLocation.getString(R.string.sample_jxl_title_2));
    }*/

    public OutMoudle<String> toJxlString() {
        return new OutMoudle<String>(id + "," + checkId + "," + checkSign + "," + checkValueUnit + "," + foodPCode + ","
                + itemName.replaceAll(",", "#") + "," + sampleName + "," + sampleNum + "," + standardName + "," + standardValue + "," + uDate + "," + flag);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCheckId() {
        return checkId == null ? "" : checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId == null ? "" : checkId;
    }

    public String getCheckSign() {
        return checkSign == null ? "" : checkSign;
    }

    public void setCheckSign(String checkSign) {
        this.checkSign = checkSign == null ? "" : checkSign;
    }

    public String getCheckValueUnit() {
        return checkValueUnit == null ? "" : checkValueUnit;
    }

    public void setCheckValueUnit(String checkValueUnit) {
        this.checkValueUnit = checkValueUnit == null ? "" : checkValueUnit;
    }

    public String getFoodPCode() {
        return foodPCode == null ? "" : foodPCode;
    }

    public void setFoodPCode(String foodPCode) {
        this.foodPCode = foodPCode == null ? "" : foodPCode;
    }

    public String getItemName() {
        return itemName == null ? "" : itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? "" : itemName;
    }

    @Override
    public String getSampleName() {
        return sampleName == null ? "" : sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName == null ? "" : sampleName;
    }

    public String getSampleNum() {
        return sampleNum == null ? "" : sampleNum;
    }

    public void setSampleNum(String sampleNum) {
        this.sampleNum = sampleNum == null ? "" : sampleNum;
    }

    public String getStandardName() {
        return standardName == null ? "" : standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName == null ? "" : standardName;
    }

    public String getStandardValue() {
        return standardValue == null ? "" : standardValue;
    }

    public void setStandardValue(String standardValue) {
        this.standardValue = standardValue == null ? "" : standardValue;
    }

    public String getuDate() {
        return uDate == null ? "" : uDate;
    }

    public void setuDate(String uDate) {
        this.uDate = uDate == null ? "" : uDate;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUDate() {
        return this.uDate;
    }

    public void setUDate(String uDate) {
        this.uDate = uDate;
    }

    public Long getPriority() {
        return this.priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public FoodItemAndStandard() {
    }

     


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.checkId);
        dest.writeString(this.checkSign);
        dest.writeString(this.checkValueUnit);
        dest.writeString(this.foodPCode);
        dest.writeString(this.itemName);
        dest.writeString(this.sampleName);
        dest.writeString(this.sampleNum);
        dest.writeString(this.standardName);
        dest.writeString(this.standardValue);
        dest.writeString(this.uDate);
        dest.writeInt(this.flag);
        dest.writeValue(this.priority);
    }

    protected FoodItemAndStandard(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.checkId = in.readString();
        this.checkSign = in.readString();
        this.checkValueUnit = in.readString();
        this.foodPCode = in.readString();
        this.itemName = in.readString();
        this.sampleName = in.readString();
        this.sampleNum = in.readString();
        this.standardName = in.readString();
        this.standardValue = in.readString();
        this.uDate = in.readString();
        this.flag = in.readInt();
        this.priority = (Long) in.readValue(Long.class.getClassLoader());
    }

    @Generated(hash = 250557484)
    public FoodItemAndStandard(Long id, String checkId, String checkSign, String checkValueUnit, String foodPCode, String itemName, String sampleName,
            String sampleNum, String standardName, String standardValue, String uDate, int flag, Long priority) {
        this.id = id;
        this.checkId = checkId;
        this.checkSign = checkSign;
        this.checkValueUnit = checkValueUnit;
        this.foodPCode = foodPCode;
        this.itemName = itemName;
        this.sampleName = sampleName;
        this.sampleNum = sampleNum;
        this.standardName = standardName;
        this.standardValue = standardValue;
        this.uDate = uDate;
        this.flag = flag;
        this.priority = priority;
    }

    public static final Creator<FoodItemAndStandard> CREATOR = new Creator<FoodItemAndStandard>() {
        @Override
        public FoodItemAndStandard createFromParcel(Parcel source) {
            return new FoodItemAndStandard(source);
        }

        @Override
        public FoodItemAndStandard[] newArray(int size) {
            return new FoodItemAndStandard[size];
        }
    };
}
