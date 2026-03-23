package com.dy.colony.android_serialport_api;


import com.apkfuns.logutils.LogUtils;

import java.io.IOException;
import java.security.InvalidParameterException;


/**
 * Created by @WangZhenXiong on 2016/11/25.
 */

public class SerialControl extends SerialHelper {
    public byte[] mRec;
    public String mPort;
    public String mTime;

    public SerialControl() {

    }

    //打开串口
    public boolean OpenComPort(SerialHelper ComPort) {
        boolean flag=false;
        try {
            ComPort.open();
             flag=true;
            LogUtils.d("串口打开成功");
        } catch (SecurityException e) {
            LogUtils.d("打开串口失败:没有串口读/写权限!");
        } catch (IOException e) {
            LogUtils.d("打开串口失败:未知错误!");
        } catch (InvalidParameterException e) {
            LogUtils.d("打开串口失败:参数错误!");
        }
        return flag;
    }
    //显示消息
    public void ShowMessage(String sMsg) {
        //Toast.makeText(MyAppLocation.getApplication(), sMsg, Toast.LENGTH_SHORT).show();
    }
    //关闭串口
    public void CloseComPort(SerialHelper ComPort) {
        if (ComPort != null) {
            ComPort.stopSend();
            ComPort.close();
        }
    }
    //串口发送
    public void sendPortData(SerialHelper ComPort, String sOut) {
        LogUtils.d(sOut);
        if (ComPort != null && ComPort.isOpen()) {
            ComPort.sendTxt(sOut);
        }
    }
}
