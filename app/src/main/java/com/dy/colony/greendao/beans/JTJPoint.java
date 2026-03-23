package com.dy.colony.greendao.beans;

import android.os.Parcel;
import android.os.Parcelable;


import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.mvp.model.entity.OutMoudle;

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
 * Created by wangzhenxiong on 2019-11-07.
 */
@Entity
public class JTJPoint implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String uuid;
    private String pointData;


   /* public OutMoudle<String> toJxlTitle() {
        return new OutMoudle<String>(MyAppLocation.myAppLocation.getString(R.string.JTJPoint_title));
    }*/

    public OutMoudle<String> toJxlString() {
        return new OutMoudle<String>(id + "," + uuid + "," + pointData );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.uuid);
        dest.writeString(this.pointData);
    }

    public JTJPoint() {
    }

    protected JTJPoint(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.uuid = in.readString();
        this.pointData = in.readString();
    }

    @Generated(hash = 962180474)
    public JTJPoint(Long id, String uuid, String pointData) {
        this.id = id;
        this.uuid = uuid;
        this.pointData = pointData;
    }

    public static final Creator<JTJPoint> CREATOR = new Creator<JTJPoint>() {
        @Override
        public JTJPoint createFromParcel(Parcel source) {
            return new JTJPoint(source);
        }

        @Override
        public JTJPoint[] newArray(int size) {
            return new JTJPoint[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid == null ? "" : uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? "" : uuid;
    }

    public String getPointData() {
        return pointData == null ? "" : pointData;
    }

    public void setPointData(String pointData) {
        this.pointData = pointData == null ? "" : pointData;
    }

    public static Creator<JTJPoint> getCREATOR() {
        return CREATOR;
    }
}
