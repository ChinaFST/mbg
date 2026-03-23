package com.dy.colony.android_serialport_api;


import com.aill.androidserialport.SerialPort;
import com.dy.colony.MyAppLocation;
import com.dy.colony.app.service.SerialDataService;
import com.dy.colony.app.utils.CRC8Util;
import com.jess.arms.utils.ArmsUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;


public abstract class SerialHelper {
    private OnFGGDDataReceiveListener onFGGDDataReceiveListener = null;
    private OnGSNCDataReceiveListener onGSNCDataReceiveListener = null;
    private OnGSNCTEMPDataReceiveListener onGSNCTEMPDataReceiveListener = null;

    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private SendThread mSendThread;
    private String sPort;
    private int iBaudRate;
    private boolean _isOpen = false;
    private byte[] _bLoopData = new byte[]{0x30};
    private int iDelay = 500;

    //----------------------------------------------------
    public SerialHelper(String sPort, int iBaudRate) {
        this.sPort = sPort;
        this.iBaudRate = iBaudRate;
    }

    public SerialHelper() {
        this("/dev/ttyS4", 9600);
    }

    public SerialHelper(String sPort) {
        this(sPort, 115200);
    }

    public SerialHelper(String sPort, String sBaudRate) {
        this(sPort, Integer.parseInt(sBaudRate));
    }

    //----------------------------------------------------
    public void open() throws SecurityException, IOException, InvalidParameterException {
        mSerialPort = new SerialPort(new File(sPort), iBaudRate, 0);
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
        mReadThread = new ReadThread();
        mReadThread.start();
        //initTimer();
        mSendThread = new SendThread();
        mSendThread.setSuspendFlag();
        mSendThread.start();
        _isOpen = true;
    }

    //----------------------------------------------------
    public void close() {
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        _isOpen = false;
    }

    //----------------------------------------------------
    public void send(byte[] bOutArray) {
       /* for (int i = 0; i < bOutArray.length; i++) {
            byte b = bOutArray[i];
            String s = ByteUtils.byteToHex(b);
            System.out.print(s+" ");
        }
        System.out.println(" ");*/
        try {
            mOutputStream.write(bOutArray);
            // System.out.println(ByteUtils.byte2HexStr(bOutArray));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------
    public void sendHex(String sHex) {
        byte[] bOutArray = MyFunc.HexToByteArr(sHex);
        send(bOutArray);
    }

    //----------------------------------------------------
    public void sendTxt(String sTxt) {
        byte[] bOutArray = new byte[0];
        try {
            bOutArray = sTxt.getBytes("GB18030");
            send(bOutArray);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    //----------------------------------------------------
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();

            while (!isInterrupted()) {

                //现成遇到while（true）一定要给点时间 睡下 不然会造成cpu占用率过高
                //没加之前cpu占用率 50%多 加了以后 5%左右
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                SerialDataService service = MyAppLocation.myAppLocation.mSerialDataService;
                if (null == service) {
                    continue;
                }

                if (service.RUNFLAG_FGGD == false && service.RUNFLAG_FGGD_HASSTART == false) {
                    continue;
                }

                try {
                    if (mInputStream == null) {
                        return;
                    }

                    if (mInputStream.available() > 0 == false) {
                        continue;
                    } else {
                        sleep(50);
                    }

                    int size = 0;
                    byte[] buffer = new byte[198];
                    size = mInputStream.read(buffer);

                    if (size > 0) {
                        //	LogUtils.d(buffer[1]);
                        switch (buffer[1]) {
                            case 22:
                                if (null != onFGGDDataReceiveListener) {
                                    //LogUtils.d(buffer);
                                    //LogUtils.d(size);
                                    if (buffer[2] == 0 && buffer[3] == 0) {
                                        break;
                                    }
                                    //头部校验
                                    int hear = buffer[0] & 0xff;
                                    //LogUtils.d(hear);
                                    if (hear != 126) {
                                        break;
                                    }
                                    //尾部校验
                                    int end = buffer[size - 1] & 0xff;
                                    //LogUtils.d(end);
                                    //DY1000的数据返回是0x7e结尾 DY3500P的是0xaa结尾，目前就这两种，这里没具体去区分仪器型号
                                    if ((end != 126) && (end != 170)) {
                                        break;
                                    }
                                    //和校验
                                    int crc_sun = CRC8Util.byteCheckSum(buffer, size, 1, 1);
                                    //LogUtils.d(crc_sun);
                                    int sun = buffer[size - 2] & 0xff;
                                    //LogUtils.d(sun);
                                    if (crc_sun != sun) {
                                        break;
                                    }
                                    onFGGDDataReceiveListener.onDataReceive(buffer, size);
                                }
                                break;
                            case 33:
                                if (null != onGSNCDataReceiveListener) {
                                    onGSNCDataReceiveListener.onDataReceive(buffer, size);
                                }
                                break;
                            case 39:
                                if (null != onGSNCTEMPDataReceiveListener) {
                                    onGSNCTEMPDataReceiveListener.onDataReceive(buffer, size);
                                }
                                break;
                            case 41:
                                if (buffer[4] == 1) {
                                    ArmsUtils.snackbarText("温度设置成功！");
                                } else {
                                    ArmsUtils.snackbarText("温度设置失败！");
                                }
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ArmsUtils.snackbarText(e.getMessage());
                    continue;
                }

            }
        }
    }

    //----------------------------------------------------
    private class SendThread extends Thread {
        public boolean suspendFlag = true;// 控制线程的执行

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                synchronized (this) {
                    while (suspendFlag) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                send(getbLoopData());
                try {
                    Thread.sleep(iDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //线程暂停
        public void setSuspendFlag() {
            this.suspendFlag = true;
        }

        //唤醒线程
        public synchronized void setResume() {
            this.suspendFlag = false;
            notify();
        }
    }

    //----------------------------------------------------
    public int getBaudRate() {
        return iBaudRate;
    }

    public boolean setBaudRate(int iBaud) {
        if (_isOpen) {
            return false;
        } else {
            iBaudRate = iBaud;
            return true;
        }
    }

    public boolean setBaudRate(String sBaud) {
        int iBaud = Integer.parseInt(sBaud);
        return setBaudRate(iBaud);
    }

    //----------------------------------------------------
    public String getPort() {
        return sPort;
    }

    public boolean setPort(String sPort) {
        if (_isOpen) {
            return false;
        } else {
            this.sPort = sPort;
            return true;
        }
    }

    //----------------------------------------------------
    public boolean isOpen() {
        return _isOpen;
    }

    //----------------------------------------------------
    public byte[] getbLoopData() {
        return _bLoopData;
    }

    //----------------------------------------------------
    public void setbLoopData(byte[] bLoopData) {
        this._bLoopData = bLoopData;
    }

    //----------------------------------------------------
    public void setTxtLoopData(String sTxt) {
        this._bLoopData = sTxt.getBytes();
    }

    //----------------------------------------------------
    public void setHexLoopData(String sHex) {
        this._bLoopData = MyFunc.HexToByteArr(sHex);
    }

    //----------------------------------------------------
    public int getiDelay() {
        return iDelay;
    }

    //----------------------------------------------------
    public void setiDelay(int iDelay) {
        this.iDelay = iDelay;
    }

    //----------------------------------------------------
    public void startSend() {
        if (mSendThread != null) {
            mSendThread.setResume();
        }
    }

    //----------------------------------------------------
    public void stopSend() {
        if (mSendThread != null) {
            mSendThread.setSuspendFlag();
        }
    }

    //----------------------------------------------------
    public interface OnFGGDDataReceiveListener {
        void onDataReceive(byte[] buffer, int size);
    }

    public void setOnFGGDDataReceiveListener(OnFGGDDataReceiveListener dataReceiveListener) {
        onFGGDDataReceiveListener = dataReceiveListener;
    }

    public interface OnGSNCDataReceiveListener {
        void onDataReceive(byte[] buffer, int size);
    }

    public void setOnGSNCDataReceiveListener(OnGSNCDataReceiveListener dataReceiveListener) {
        onGSNCDataReceiveListener = dataReceiveListener;
    }

    public interface OnGSNCTEMPDataReceiveListener {
        void onDataReceive(byte[] buffer, int size);
    }

    public void setOnGSNCTEMPDataReceiveListener(OnGSNCTEMPDataReceiveListener dataReceiveListener) {
        onGSNCTEMPDataReceiveListener = dataReceiveListener;
    }
}