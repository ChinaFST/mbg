package com.dy.colony.greendao.beans;

import android.os.Parcel;
import android.os.Parcelable;


import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.model.entity.base.BaseSimple33Message;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * Created by wangzhenxiong on 2019-08-16.
 */
@Entity
public class Simple33 extends BaseSimple33Message implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String foodCode;
    private String foodId;
    private String foodName;
    private String foodPCode;
    private int isParent;
    private String udate;
    private int flag;

    public OutMoudle<String> toJxlTitle(){
        return  new OutMoudle<String>("ID,样品编号,样品ID,样品名称,样品父类编号,是否是父类,更新时间,flag");
    }
    public OutMoudle<String>  toJxlString(){
        return  new OutMoudle<String>(id+","+foodCode+","+foodId+","+foodName+","+foodPCode+","+isParent+","+udate+","+flag);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodCode() {
        return foodCode == null ? "" : foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode == null ? "" : foodCode;
    }

    public String getFoodId() {
        return foodId == null ? "" : foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId == null ? "" : foodId;
    }

    public String getFoodName() {
        return foodName == null ? "" : foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName == null ? "" : foodName;
    }

    public String getFoodPCode() {
        return foodPCode == null ? "" : foodPCode;
    }

    public void setFoodPCode(String foodPCode) {
        this.foodPCode = foodPCode == null ? "" : foodPCode;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public String getUdate() {
        return udate == null ? "" : udate;
    }

    public void setUdate(String udate) {
        this.udate = udate == null ? "" : udate;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.foodCode);
        dest.writeString(this.foodId);
        dest.writeString(this.foodName);
        dest.writeString(this.foodPCode);
        dest.writeInt(this.isParent);
        dest.writeString(this.udate);
        dest.writeInt(this.flag);
    }

    public Simple33() {
    }

    protected Simple33(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.foodCode = in.readString();
        this.foodId = in.readString();
        this.foodName = in.readString();
        this.foodPCode = in.readString();
        this.isParent = in.readInt();
        this.udate = in.readString();
        this.flag = in.readInt();
    }
    @Generated(hash = 1368309139)
    public Simple33(Long id, String foodCode, String foodId, String foodName, String foodPCode, int isParent, String udate,
            int flag) {
        this.id = id;
        this.foodCode = foodCode;
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPCode = foodPCode;
        this.isParent = isParent;
        this.udate = udate;
        this.flag = flag;
    }

    



    public static final Creator<Simple33> CREATOR = new Creator<Simple33>() {
        @Override
        public Simple33 createFromParcel(Parcel source) {
            return new Simple33(source);
        }

        @Override
        public Simple33[] newArray(int size) {
            return new Simple33[size];
        }
    };
}
