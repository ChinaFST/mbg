package com.dy.colony.usbcamera;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 　 ┏┓　  ┏┓+ +
 * 　┏┛┻━━ ━┛┻┓ + +
 * 　┃　　　　 ┃
 * 　┃　　　　 ┃  ++ + + +
 * 　┃████━████+
 * 　┃　　　　 ┃ +
 * 　┃　　┻　  ┃
 * 　┃　　　　 ┃ + +
 * 　┗━┓　  ┏━┛
 * 　  ┃　　┃
 * 　  ┃　　┃　　 + + +
 * 　  ┃　　┃
 * 　  ┃　　┃ + 神兽保佑,代码无bug
 * 　  ┃　　┃
 * 　  ┃　　┃　　+
 * 　  ┃　 　┗━━━┓ + +
 * 　　┃ 　　　　 ┣┓
 * 　　┃ 　　　 ┏┛
 * 　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　 ┃┫┫ ┃┫┫
 * 　　 ┗┻┛ ┗┻┛+ + + +
 *
 * @author: wangzhenxiong
 * @data: 6/17/21 9:23 AM
 * Description:
 */
public class Status implements Parcelable {

    private MyEvent evevt;
    private Bitmap bitmap;
    private int cameraId;
    private String msg;

    public Status(MyEvent evevt) {
        this.evevt = evevt;
    }

    protected Status(Parcel in) {
        evevt = in.readParcelable(MyEvent.class.getClassLoader());
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        cameraId = in.readInt();
        msg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(evevt, flags);
        dest.writeParcelable(bitmap, flags);
        dest.writeInt(cameraId);
        dest.writeString(msg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? "" : msg;
    }

    public MyEvent getEvevt() {
        return evevt;
    }

    public void setEvevt(MyEvent evevt) {
        this.evevt = evevt;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }



    /**
     * 回调事件
     */
    public enum MyEvent implements Parcelable {

        BITMAPRECEVER(1);


        MyEvent(int i) {

        }



        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ordinal());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<MyEvent> CREATOR = new Creator<MyEvent>() {
            @Override
            public MyEvent createFromParcel(Parcel in) {
                return MyEvent.values()[in.readInt()];
            }

            @Override
            public MyEvent[] newArray(int size) {
                return new MyEvent[size];
            }
        };
    }



}
