package com.dy.colony.serialport.thread;


import com.apkfuns.logutils.LogUtils;
import com.dy.colony.serialport.ConfigurationSdk;
import com.dy.colony.app.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 串口消息读取线程
 */
public abstract class SerialPortReadThread extends Thread {

    public abstract void onDataReceived(byte[] bytes);

    private static final String TAG = SerialPortReadThread.class.getSimpleName();
    private InputStream mInputStream;
    ByteArrayOutputStream byteArrayOutputStream;
    private ConfigurationSdk sdk;

    public SerialPortReadThread(InputStream inputStream, ConfigurationSdk mSdk) {
        mInputStream = inputStream;
        sdk = mSdk;
        byteArrayOutputStream = new ByteArrayOutputStream();
    }
    int times=0;
    @Override
    public void run() {
        super.run();

        while (!isInterrupted()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int value;
            try {
                byte[] res = new byte[0];
                /*if (sdk.getDevice().getAbsolutePath().equals(anObject)){
                    if (BuildConfig.DEBUG){
                        if (times==600){
                            times=0;
                            res=ByteUtils.hexTobytes("55 AA 81 18 40 00 7F 65 A0 5E 41 9A 00 80 16 89 0B FD 0F BB 44".replaceAll(" ",""));
                        }else {
                            times++;
                            res=ByteUtils.hexTobytes("55 AA 81 18 40 00 0B 2D A0 5E 13 90 00 F5 16 89 0B EC 0F BB 44".replaceAll(" ",""));
                        }
                        checkRules(res);
//                onDataReceived(res);
                        byteArrayOutputStream.reset();
                    }


                }else {

                }*/
                if (mInputStream == null) {
                    return;
                }
                //inputStream.available()方法返回实际可读字节数，也就是总大小，同时时inputStream.read()方法不阻塞
                if (mInputStream.available() == 0) {
                    continue;
                }
                byte[] buffers = new byte[mInputStream.available()];
                while (mInputStream.available() > 0 && (value = mInputStream.read(buffers)) != 0x0d) {
                    byteArrayOutputStream.write(buffers, 0, value);
                }
                res = byteArrayOutputStream.toByteArray();
                checkRules(res);
//                onDataReceived(res);
                byteArrayOutputStream.reset();

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }


    /**
     * 检查接收数据是否合法，否则丢弃
     *
     * @param readBytes
     */
    private void checkRules(byte[] readBytes) {
        //ArmsUtils.snackbarText(ByteUtils.byte2HexStr2(readBytes));
        byte[] msgHead = sdk.getMsgHead();
        if (msgHead != null) {
            //解析帧头
            String s_head = ByteUtils.byte2HexStr2(msgHead);
            byte[] bytes = ByteUtils.subBytes(readBytes, 0, msgHead.length);
            String s_head_read = ByteUtils.byte2HexStr2(bytes);
            if (s_head.equals(s_head_read)) {
                //LogUtils.d("成立");
                onDataReceived(readBytes);
            } else {
                LogUtils.d("数据不合法丢弃  "+ByteUtils.byte2HexStr2(readBytes));
            }
        } else {
            onDataReceived(readBytes);
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * 关闭线程 释放资源
     */
    public void release() {
        interrupt();

        if (null != mInputStream) {
            try {
                mInputStream.close();
                mInputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public byte[] subByte(byte[] b, int off, int length) {

        byte[] b1 = new byte[length];

        System.arraycopy(b, off, b1, 0, length);

        return b1;

    }


    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}


