package com.dy.colony.usbhelp;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbManager;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.app.utils.ByteUtils;
import com.dy.colony.app.utils.CRC8Util;
import com.jess.arms.utils.ArmsUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
 * 　　　　┃　　　　　 ┣┓
 * 　　　　┃　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 *
 * @author wangzhenxiong
 * @date 2019-10-17
 */
public class UsbReadWriteHelper {
    private UsbManager mManager;
    private UsbDevice mUsbDevice;
    private ScheduledThreadPoolExecutor mThreadPoolExecutor;
    private boolean READFLAG = false;

    public UsbReadWriteHelper(UsbDevice usbDevice) {
        mUsbDevice = usbDevice;
    }

    /**
     * usb设备数据监听
     */
    public interface onUsbReciver {

        /**
         * 胶体金摄像头 真菌毒素 接收图片
         *
         * @param bytes
         * @param width
         * @param height
         */
        void reciver(byte[] bytes, int width, int height);

        /**
         * 读取响应
         *
         * @param bytes
         */
        void reciver(List<Byte> bytes);
    }

    protected WeakReference<onUsbReciver> mReciver;

    public void setReciverListener(onUsbReciver reciverListener) {
        LogUtils.d("setReciverListener");
        mReciver = new WeakReference<>(reciverListener);
    }

    public void sendMessage(byte[] bytes) {


        if (!checkUsbDeviceIslive()) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LogUtils.d(bytes);
                int count = mUsbDevice.getInterface(1).getEndpointCount();
                UsbEndpoint end_out = null;
                UsbEndpoint end_in = null;
                for (int i = 0; i < count; i++) {
                    UsbEndpoint end = mUsbDevice.getInterface(1).getEndpoint(i);
                    if (end.getDirection() == UsbConstants.USB_DIR_OUT) {
                        end_out = end;
                    } else if (end.getDirection() == UsbConstants.USB_DIR_IN) {
                        end_in = end;
                    }
                }
                if (null == end_out || null == end_in) {
                    ArmsUtils.snackbarText("获取读写端口失败！");
                    LogUtils.d("获取读写端口失败！");
                    return;
                }

                int i1 = connection.bulkTransfer(end_out, bytes, bytes.length, 500);
                LogUtils.d(i1);
                if (i1 != bytes.length) {
                    LogUtils.d("指令发送失败！");
                    ArmsUtils.snackbarText("指令发送失败！");
                    return;
                }
                List<Byte> bytes = new ArrayList<>();
                byte[] mybuffer = new byte[end_in.getMaxPacketSize()];
                int datalength = 0;
                while (true) {
                    datalength = connection.bulkTransfer(end_in, mybuffer, mybuffer.length, 500);
                    LogUtils.d(datalength);
                    for (int j = 0; j < datalength; j++) {
                        bytes.add(mybuffer[j]);
                    }
                    if (bytes.size() >= 7) {
                        break;
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //LogUtils.d("mReciver  "+mReciver.get());
                LogUtils.d(bytes.get(0) + "," + bytes.get(1) + ":  " + bytes.size());

                if (null != mReciver && bytes.size() >= 3) {
                    LogUtils.d("usb reciver byte");
                    onUsbReciver reciver = mReciver.get();
                    if (reciver != null) {
                        reciver.reciver(bytes);
                    }

                }
                //connection.close();

            }
        };


        setWriteExecute(runnable);

    }

    public void sendMessage(byte[] bytes, boolean readResponse) {


        if (!checkUsbDeviceIslive()) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LogUtils.d("发送数据：" + bytes);
                int count = mUsbDevice.getInterface(1).getEndpointCount();
                UsbEndpoint end_out = null;
                UsbEndpoint end_in = null;
                for (int i = 0; i < count; i++) {
                    UsbEndpoint end = mUsbDevice.getInterface(1).getEndpoint(i);
                    if (end.getDirection() == UsbConstants.USB_DIR_OUT) {
                        end_out = end;
                    } else if (end.getDirection() == UsbConstants.USB_DIR_IN) {
                        end_in = end;
                    }
                }
                if (null == end_out || null == end_in) {
                    ArmsUtils.snackbarText("获取读写端口失败！");
                    LogUtils.d("获取读写端口失败！");
                    return;
                }

                int i1 = connection.bulkTransfer(end_out, bytes, bytes.length, 500);
                LogUtils.d(bytes);
                LogUtils.d(i1);
                if (i1 != bytes.length) {
                    LogUtils.d("指令发送失败！");
                    ArmsUtils.snackbarText("指令发送失败！");
                    return;
                }


                List<Byte> bytes = new ArrayList<>();
                byte[] mybuffer = new byte[end_in.getMaxPacketSize()];
                int datalength = 0;
                while (datalength != -1) {
                    datalength = connection.bulkTransfer(end_in, mybuffer, mybuffer.length, 500);
                    LogUtils.d(datalength);
                    for (int j = 0; j < datalength; j++) {
                        bytes.add(mybuffer[j]);
                    }
                    if (datalength < mybuffer.length) {
                        break;
                    }
                }
                LogUtils.d(bytes.get(0) + "," + bytes.get(1) + ":  " + bytes.size());
                if (null != mReciver && bytes.size() >= 3) {
                    LogUtils.d("usb reciver byte");
                    onUsbReciver reciver = mReciver.get();
                    if (reciver != null) {
                        reciver.reciver(bytes);
                    }

                }
                //connection.close();

            }
        };


        setWriteExecute(runnable);

    }

    public void sendMessage(byte[] bytes, boolean readResponse, long delay) {
        if (!checkUsbDeviceIslive()) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //LogUtils.d("发送数据：" + bytes);
                int count = mUsbDevice.getInterface(1).getEndpointCount();
                UsbEndpoint end_out = null;
                UsbEndpoint end_in = null;
                for (int i = 0; i < count; i++) {
                    UsbEndpoint end = mUsbDevice.getInterface(1).getEndpoint(i);
                    if (end.getDirection() == UsbConstants.USB_DIR_OUT) {
                        end_out = end;
                    } else if (end.getDirection() == UsbConstants.USB_DIR_IN) {
                        end_in = end;
                    }
                }
                if (null == end_out || null == end_in) {
                    ArmsUtils.snackbarText("获取读写端口失败！");
                    LogUtils.d("获取读写端口失败！");
                    return;
                }

                int i1 = connection.bulkTransfer(end_out, bytes, bytes.length, 500);

                LogUtils.d(bytes);
                LogUtils.d(i1);
                if (i1 != bytes.length) {
                    LogUtils.d("指令发送失败！");
                    ArmsUtils.snackbarText("指令发送失败！");
                    return;
                }
                List<Byte> bytes = new ArrayList<>();
                byte[] mybuffer = new byte[end_in.getMaxPacketSize()];
                int datalength = 0;
                while (datalength != -1) {
                    datalength = connection.bulkTransfer(end_in, mybuffer, mybuffer.length, 500);
                    //LogUtils.d(datalength);
                    for (int j = 0; j < datalength; j++) {
                        bytes.add(mybuffer[j]);
                    }
                }
                LogUtils.d(bytes.get(0) + "," + bytes.get(1) + ":  " + bytes.size());
                if (null != mReciver && bytes.size() >= 3) {
                    LogUtils.d("usb reciver byte");
                    onUsbReciver reciver = mReciver.get();
                    if (reciver != null) {
                        reciver.reciver(bytes);
                    }

                }
                //connection.close();
            }
        };

        setWriteSchedule(runnable, delay, TimeUnit.MILLISECONDS);
    }


    public void stopReadData_P() {
        READFLAG = false;
    }


    public static int vertical;
    public static int horizontal;

    /**
     * @param delay   多少秒后开始读取数据
     * @param setting 是否需要设置摄像头参数 如果需要设置参数，那就通过 @vertical和@horizontal 来触发设置参数 1或者-1
     */
    public void stratReadData_P(long delay, boolean setting) {
        READFLAG = true;

        if (!checkUsbDeviceIslive()) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LogUtils.d("stratReadData_P");
                int count = mUsbDevice.getInterface(1).getEndpointCount();
                UsbEndpoint endpoint_in = null;
                UsbEndpoint endpoint_out = null;
                for (int i = 0; i < count; i++) {
                    UsbEndpoint end = mUsbDevice.getInterface(1).getEndpoint(i);
                    if (end.getDirection() == UsbConstants.USB_DIR_IN) {
                        endpoint_in = end;
                    }
                    if (end.getDirection() == UsbConstants.USB_DIR_OUT) {
                        endpoint_out = end;
                    }
                }
                while (READFLAG) {
                    LogUtils.d("read");
                    //先读取下无用数据，防止造成数据混乱
                    int lengthunuser = 0;
                    byte[] bufferun = new byte[endpoint_in.getMaxPacketSize()];
                    while (lengthunuser != -1 && READFLAG) {
                        lengthunuser = connection.bulkTransfer(endpoint_in, bufferun, bufferun.length, 300);
                    }
                    LogUtils.d("无用数据");
                    LogUtils.d(bufferun);

                    //ArrayList<Byte> bytes = new ArrayList<>();
                    // TODO: 4/18/23 优化掉list，byte转list太费时

                    //需要停止了就不要发了，发了读不完造成一些不必要的麻烦
                    if (READFLAG) {
                        int i = connection.bulkTransfer(endpoint_out, Constants.COLLAURUM_DATA_REQUEST_P, Constants.COLLAURUM_DATA_REQUEST_P.length, 100);
                        if (i != Constants.COLLAURUM_DATA_REQUEST_P.length) {
                            continue;
                        }
                    }
                    byte[] mybuffer = new byte[endpoint_in.getMaxPacketSize()];
                    int datalength = 0;
                    byte[] bytes_sun = new byte[0];
                    while (datalength != -1 && READFLAG) {
                        datalength = connection.bulkTransfer(endpoint_in, mybuffer, mybuffer.length, 300);
                        LogUtils.d(datalength);
                        LogUtils.d(ByteUtils.byte2HexStr2(mybuffer));
                        if (datalength != -1) {
                            byte[] bytes1 = Arrays.copyOfRange(mybuffer, 0, datalength);
                            if (0 == bytes_sun.length) {
                                bytes_sun = bytes1;
                                continue;
                            }
                            bytes_sun = ByteUtils.byteMerger(bytes_sun, bytes1);
                        }


                    }


                    if (bytes_sun.length < 10) {
                        LogUtils.d(bytes_sun);
                        continue;
                    }
                    //图像的宽度
                    int WIDTH = (bytes_sun[5] & 0x0FF) * 256 + (bytes_sun[4] & 0x0FF);
                    //图像的长度
                    int HEIGHT = (bytes_sun[7] & 0x0FF) * 256 + (bytes_sun[6] & 0x0FF);
                    //图像所占用的字节
                    byte[] temp = new byte[WIDTH * HEIGHT * 2];
                    LogUtils.d(bytes_sun.length);
                    LogUtils.d(ByteUtils.byte2HexStr2(bytes_sun));
                    LogUtils.d(WIDTH + "*" + HEIGHT);
                    //验证数据是否完整
                    if (temp.length == (bytes_sun.length - 10)) {
                        for (int i = 0; i < temp.length; i++) {
                            temp[i] = bytes_sun[i + 8];
                        }
                        if (null != mReciver && READFLAG) {
                            LogUtils.d("usb reciver 图像");
                            onUsbReciver reciver = mReciver.get();
                            if (reciver != null) {
                                reciver.reciver(temp, WIDTH, HEIGHT);
                            }
                        }
                    } else {
                        // LogUtils.d(temp);
                        LogUtils.d("数据错误");
                        continue;
                    }
                    if (setting) {
                        if (vertical == 0 && horizontal == 0) {
                            continue;
                        }
                        //在获取摄像头参数时候失败了
                        if (!getdefaultvalue(connection, endpoint_in, endpoint_out)) {
                            continue;
                        }
                        switch (vertical) {
                            case 1:
                                vertical_d = vertical_d + 1;
                                vertical = 0;
                                break;
                            case -1:
                                vertical_d = vertical_d - 1;
                                vertical = 0;
                                break;
                            default:
                        }
                        switch (horizontal) {
                            case 1:
                                horizontal_d = horizontal_d + 1;
                                horizontal = 0;
                                break;
                            case -1:
                                horizontal_d = horizontal_d - 1;
                                horizontal = 0;
                                break;
                            default:
                        }
                        setdefaultvalue(connection, endpoint_in, endpoint_out);

                    }


                }
                //connection.close();

            }
        };


        setReadSchedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public int horizontal_d = 0;
    public int vertical_d = 0;

    private void setdefaultvalue(UsbDeviceConnection connection, UsbEndpoint usbEpIn, UsbEndpoint usbEpOut) {
        byte[] buffer = {0x7E, 0x1d, 0x02, 0x00, (byte) horizontal_d, (byte) vertical_d, (byte) CRC8Util.FindCRC(new byte[]{0x1d, 0x02, 0x00, (byte) horizontal_d, (byte) vertical_d}), 0x7E};
        LogUtils.d("设置摄像头参数： " + buffer);
        int i = connection.bulkTransfer(usbEpOut, buffer, buffer.length, 100);
        if (i != buffer.length) {
            LogUtils.d("设置摄像头参数失败： " + i);
            return;
        }
        int datalength = 0;
        while (datalength != -1 && READFLAG) {
            byte[] mybuffer = new byte[30];
            datalength = connection.bulkTransfer(usbEpIn, mybuffer, mybuffer.length, 300);
            LogUtils.d("设置摄像头参数响应： " + mybuffer);
            if (datalength < mybuffer.length) {
                break;
            }
        }
    }

    /**
     * 获取摄像头偏移位置
     *
     * @param connection
     * @param usbEpIn
     * @param usbEpOut
     * @return
     */
    private boolean getdefaultvalue(UsbDeviceConnection connection, UsbEndpoint usbEpIn, UsbEndpoint usbEpOut) {
        ArrayList<Byte> bytes = new ArrayList<>();
        int length = Constants.COLLAURUM_GET_ARGMENT.length;
        int i1 = connection.bulkTransfer(usbEpOut, Constants.COLLAURUM_GET_ARGMENT, length, 100);
        LogUtils.d("读取摄像头参数： " + Constants.COLLAURUM_GET_ARGMENT);
        if (i1 != length) {
            LogUtils.d("读取摄像头参数失败： " + i1);
            return false;
        }

        int datalength = 0;
        while (datalength != -1 && READFLAG) {
            byte[] mybuffer = new byte[30];
            datalength = connection.bulkTransfer(usbEpIn, mybuffer, mybuffer.length, 300);
            for (int i = 0; i < datalength; i++) {
                bytes.add(mybuffer[i]);
            }
            if (datalength < mybuffer.length) {
                break;
            }
        }
        //没读够，丢包
        if (bytes.size() < 7) {
            return false;
        }
        LogUtils.d("读取摄像头参数响应： " + bytes);
        horizontal_d = bytes.get(4);
        vertical_d = bytes.get(5);

        return true;
    }


    private UsbDeviceConnection connection;

    /**
     * 检查USB设备是否还在
     *
     * @return
     */
    private boolean checkUsbDeviceIslive() {
        if (null == mManager) {
            mManager = (UsbManager) MyAppLocation.myAppLocation.getSystemService(Context.USB_SERVICE);
        }
        if (null == connection) {
            connection = mManager.openDevice(mUsbDevice);
        }

        if (null == connection) {
            ArmsUtils.snackbarText("USB设备打开失败，连接途中可能出现了模块掉电的情况，请重启软件！！");
            return false;
        }
        if (null == mUsbDevice.getInterface(1)) {
            ArmsUtils.snackbarText("USB端口不存在！");
            return false;
        }

        boolean b = connection.claimInterface(mUsbDevice.getInterface(1), true);
        if (!b) {
            ArmsUtils.snackbarText("USB设备连接失败！");
            LogUtils.d("USB设备连接失败!");
            return false;
        }
        return true;
    }

    private void setWriteSchedule(Runnable runnable, long delay, TimeUnit milliseconds) {
        LogUtils.d("setWriteSchedule");
        if (null == mThreadPoolExecutor) {
            mThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        }
        mThreadPoolExecutor.schedule(runnable, delay, milliseconds);
    }

    private void setReadSchedule(Runnable runnable, long delay, TimeUnit milliseconds) {
        LogUtils.d("setReadSchedule");
        if (null == mThreadPoolExecutor) {
            mThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        }
        mThreadPoolExecutor.schedule(runnable, delay, milliseconds);
    }

    private void setWriteExecute(Runnable runnable) {
        LogUtils.d("setWriteExecute");
        if (null == mThreadPoolExecutor) {
            mThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        }
        mThreadPoolExecutor.execute(runnable);
    }

    private void setReadExecute(Runnable runnable) {
        LogUtils.d("setReadExecute");
        if (null == mThreadPoolExecutor) {
            mThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        }
        mThreadPoolExecutor.execute(runnable);
    }
}
