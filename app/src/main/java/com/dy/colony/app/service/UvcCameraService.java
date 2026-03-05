package com.dy.colony.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constans;
import com.dy.colony.usbcamera.CameraCallBack;
import com.dy.colony.usbcamera.Status;
import com.dy.colony.usbcamera.UvcCameraPreview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
 * @data: 3/29/23 1:59 PM
 * Description:
 */
public class UvcCameraService extends Service {
    private IBinder binder = new MyBinder();


    private String mUsbPermission = "com.android.example.USB_PERMISSION";
    CameraCallBack cameraCallBack;
    /**
     * 线程池
     */
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
    private UsbManager mUsbManager;
    private String TAG = "UvcCameraService";

    @Override
    public void onCreate() {
        super.onCreate();
        mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        mUsbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d("onBind");
        registerReceivers();
        initUvcCamera();
        return binder;
    }
   public Bitmap bmp;
    public class MyBinder extends Binder {
        public UvcCameraService getService() {
            return UvcCameraService.this;
        }
    }

    private void initUvcCamera() {
        //查找挂载的camera数量，USB连接的，所以查看usb数量就可以
        List<UsbDevice> usbDevice = findUsbDevice(Constans.MYPID_MULTICARD_CAM, Constans.MYVID_MULTICARD_CAM);
        LogUtils.d(usbDevice);
        if (usbDevice.size() > 0) {
            //camera的挂在节点名字都是依次递增的，所以并不知道哪个USB对应哪个camera
            openCamera();
        }
    }


    HashMap<Integer, UvcCameraPreview> openedUvcCameraList = new HashMap<>();
    Status status = new Status(Status.MyEvent.BITMAPRECEVER);
    private void openCamera() {

        LogUtils.d("openCamera");
        for (int i = 0; i < 10; i++) {
            UvcCameraPreview cameraPreview = new UvcCameraPreview(i, 1280, 720);
            LogUtils.d(cameraPreview.isCameraExists());
            if (cameraPreview.isCameraExists()) {
                openedUvcCameraList.put(i, cameraPreview);
                cameraPreview.setPrepareGetPhotoListener(new UvcCameraPreview.MyListener() {
                    @Override
                    public void reciver(int cameraId, Bitmap bitmap) {
                        bmp = cameraPreview.getBitmap();
                        if (null != cameraCallBack) {

                            status.setCameraId(cameraId);
                            setCallBack(status);
                        }


                    }
                });
                mScheduledThreadPoolExecutor.execute(cameraPreview);
                return;
            }else {
                cameraPreview.recycleBmp();
            }
        }
        LogUtils.d(openedUvcCameraList);
    }

    private void closeCamera() {
        LogUtils.d(openedUvcCameraList.keySet());
        Set<Integer> integers = openedUvcCameraList.keySet();
        for (Integer integer : integers) {
            UvcCameraPreview cameraPreview = openedUvcCameraList.get(integer);
            cameraPreview.STARTREADFLAG = false;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cameraPreview.stopCamera();
            mScheduledThreadPoolExecutor.remove(cameraPreview);
            cameraPreview = null;
        }
        openedUvcCameraList.clear();

    }


    @Override
    public boolean onUnbind(Intent intent) {
        unregisterReceiver(reciverUSBStatus);
        cameraCallBack = null;
        return super.onUnbind(intent);
    }

    private void registerReceivers() {
        LogUtils.d("registerReceivers");
        //监听USB拔插
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        intentFilter.addAction(mUsbPermission);
        registerReceiver(reciverUSBStatus, intentFilter);
    }

    public void addStatusListener(CameraCallBack status)  {
        if (null == status) {
            return;
        }
        System.out.println("添加监听");
        cameraCallBack = status;
        LogUtils.d("addStatusListener");
    }

    public void removeStatusListener(CameraCallBack status) {
        if (null == status) {
            return;
        }
        System.out.println("移除监听");
        cameraCallBack = null;
    }


    private BroadcastReceiver reciverUSBStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //usb连接
            //usb断开连接
            LogUtils.d(action);
            if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                Bundle extras = intent.getExtras();
                if (null != extras) {
                    UsbDevice device = (UsbDevice) extras.get("device");
                    LogUtils.d(device);
                    if (device.getProductId() == Constans.MYPID_MULTICARD_CAM && device.getVendorId() == Constans.MYVID_MULTICARD_CAM) {
                        LogUtils.d("true");
                        openCamera();
                    }
                }


            }

            if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                Bundle extras = intent.getExtras();
                if (null != extras) {
                    UsbDevice device = (UsbDevice) extras.get("device");
                    LogUtils.d(device);
                    if (device.getProductId() == Constans.MYPID_MULTICARD_CAM && device.getVendorId() == Constans.MYVID_MULTICARD_CAM) {
                        LogUtils.d("true");
                        closeCamera();
                    }
                }
            }


        }
    };


    private List<UsbDevice> findUsbDevice(int mypidMulticardCam, int myvidMulticardCam) {
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        List<UsbDevice> usbDeviceList = new ArrayList<>();
        for (String key : deviceList.keySet()) {
            UsbDevice device = deviceList.get(key);
            int productId = device.getProductId();
            int vendorId = device.getVendorId();
            if ((vendorId == myvidMulticardCam && productId == mypidMulticardCam)) {
                usbDeviceList.add(device);
            }
        }
        return usbDeviceList;
    }


    /**
     * 设置回调
     *
     * @param status
     */
    private void setCallBack(Status status) {
        if (cameraCallBack == null) {
            return;
        }
        cameraCallBack.onCallBack(status);



    }


}
