package com.dy.colony.app.service;

import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.MyAppLocation;
import com.dy.colony.app.utils.ByteUtils;
import com.dy.colony.serialport.ConfigurationSdk;
import com.dy.colony.serialport.SerialPortManager;
import com.dy.colony.serialport.listener.OnOpenSerialPortListener;
import com.dy.colony.serialport.listener.OnSerialPortDataListener;
import com.jess.arms.base.BaseService;
import com.jess.arms.utils.ArmsUtils;

import java.io.File;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class SerialDataService extends BaseService implements OnOpenSerialPortListener, OnSerialPortDataListener {
    private IBinder bind = new MyBinder();
    public SerialPortManager mSerialPortManager_TTYS1;
    /**
     * 是否进行16进制转化
     */
    public boolean mConversionNotice = true;
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
    private int times1;
    private boolean issendCurrentFinish=false;
    private Handler mHandler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }

    @Override
    public void init() {
        mHandler = new Handler(Looper.getMainLooper());
        mScheduledThreadPoolExecutor = ((ScheduledThreadPoolExecutor) ArmsUtils.obtainAppComponentFromContext(MyAppLocation.myAppLocation).executorService());
        initTTYS1();
    }

    private void initTTYS1() {
        if (mSerialPortManager_TTYS1 != null) {
            mSerialPortManager_TTYS1.closeSerialPort();
        }else {
            mSerialPortManager_TTYS1 = new SerialPortManager().setOnOpenSerialPortListener(this);
            mSerialPortManager_TTYS1.setOnSerialPortDataListener(this);
        }

        //构建初始化参数
        ConfigurationSdk sdk = new ConfigurationSdk.ConfigurationBuilder(new File("/dev/ttyS1"), 115200)
                .log("TAG", false, false)
                //打开说明需要效验
                .build();
        mSerialPortManager_TTYS1.init(sdk);
    }


    public class MyBinder extends Binder {
        public SerialDataService getService() {
            return SerialDataService.this;
        }
    }

    /**
     * 串口打开成功回调
     *
     * @param device 串口
     */
    @Override
    public void onSuccess(File device) {
        LogUtils.d(device.getAbsolutePath() + "连接成功");


    }

    /**
     * 串口打开失败回调
     *
     * @param device 串口
     * @param status 失败原因
     */
    @Override
    public void onFail(File device, Status status) {
        LogUtils.d(device.getAbsolutePath() + "连接失败" + status.name());

    }

    /**
     * 数据发送
     *
     * @param device 串口名称
     * @param bytes  发送的数据
     */
    @Override
    public void onDataSent(File device, byte[] bytes) {
        String absolutePath = device.getAbsolutePath();
        LogUtils.d(absolutePath + "  发送  " + ByteUtils.byte2HexStr2(bytes));


    }

    /**
     * 数据接收
     *
     * @param device 串口名称
     * @param bytes  接收到的数据
     */
    @Override
    public void onDataReceived(File device, byte[] bytes) {
        String absolutePath = device.getAbsolutePath();
        //LogUtils.d(device.getAbsolutePath());
        //LogUtils.d(ByteUtils.byte2HexStr2(bytes));
        LogUtils.d(absolutePath + "  接收  " + ByteUtils.byte2HexStr2(bytes));



    }



}