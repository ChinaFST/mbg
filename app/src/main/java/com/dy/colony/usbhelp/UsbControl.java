package com.dy.colony.usbhelp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
 * Created by wangzhenxiong on 2019/3/11.
 * 根据pid，vid获取相应的usb设备，
 * 并且将该usb设备的实例引用保存至constans中，
 */
public class UsbControl {

    public interface onUsbInitFinshed {
        /**
         * @param mDeviceList 发现的usb集合
         */
        void successed(List<UsbDevice> mDeviceList);

        /**
         * @param s 错误描述
         */
        void failure(String s);
    }

    private onUsbInitFinshed mOnUsbInitFinshed;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbManager mUsbManager;

    private HashMap<String, UsbDevice> mDeviceList;
    private List<UsbDevice> mUsbDeviceList = new ArrayList<>();
    private PendingIntent mPendingIntent;


    public void setUsbInitStateListener(onUsbInitFinshed listener) {
        mOnUsbInitFinshed = listener;
    }


    public UsbControl() {

        mUsbManager = (UsbManager) MyAppLocation.myAppLocation.getSystemService(Context.USB_SERVICE);
        mPendingIntent = PendingIntent.getBroadcast(MyAppLocation.myAppLocation, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_UPDATE_CURRENT);

    }


    /**
     * 初始化usb以及请求权限
     */
    public void initUsbNew() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDeviceList = mUsbManager.getDeviceList();
                mUsbDeviceList.clear();
                LogUtils.d(mDeviceList);
                for (String key : mDeviceList.keySet()) {
                    UsbDevice device = mDeviceList.get(key);
                    int productId = device.getProductId();
                    int vendorId = device.getVendorId();

                    if (vendorId == Constants.MYVID_P && productId == Constants.MYPID_P) {
                        mUsbDeviceList.add(device);
                    }
                }

                LogUtils.d(mUsbDeviceList);
                if (mUsbDeviceList.size() != 0) {
                    //无权限，重新请求权限
                    for (int i = 0; i < mUsbDeviceList.size(); i++) {
                        UsbDevice device = mUsbDeviceList.get(i);
                        LogUtils.d("请求权限--" + device);
                        if (null != device) {
                            if (!mUsbManager.hasPermission(device)) {  //有权限

                                boolean b = false;
                                while (!b) {
                                    LogUtils.d("请求权限--" + b + device);
                                    b = mUsbManager.hasPermission(device);
                                    mUsbManager.requestPermission(device, mPendingIntent);

                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();

                                        if (null != mOnUsbInitFinshed) {
                                            mOnUsbInitFinshed.failure("请求权限时发生错误");
                                        } else {
                                            ArmsUtils.snackbarText("请先设置mOnUsbInitFinshed监听");
                                        }
                                    }
                                }

                            }
                        }
                    }
                    if (null != mOnUsbInitFinshed) {
                        LogUtils.d("请求权限--完成");
                        mOnUsbInitFinshed.successed(mUsbDeviceList);
                    } else {
                        ArmsUtils.snackbarText("请先设置mOnUsbInitFinshed监听");
                    }
                } else {
                    if (null != mOnUsbInitFinshed) {
                        mOnUsbInitFinshed.failure("没有发现胶体金模块");
                    } else {
                        ArmsUtils.snackbarText("请先设置mOnUsbInitFinshed监听");
                    }
                }

            }
        }).start();


    }

}
