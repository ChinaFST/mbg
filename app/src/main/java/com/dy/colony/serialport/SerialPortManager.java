package com.dy.colony.serialport;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.serialport.listener.OnOpenSerialPortListener;
import com.dy.colony.serialport.listener.OnSerialPortDataListener;
import com.dy.colony.serialport.thread.SerialPortReadThread;
import com.jess.arms.utils.ArmsUtils;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class SerialPortManager extends SerialPort {

    private static final String TAG = SerialPortManager.class.getSimpleName();
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private FileDescriptor mFd;
    private OnOpenSerialPortListener mOnOpenSerialPortListener;
    private OnSerialPortDataListener mOnSerialPortDataListener;

    private HandlerThread mSendingHandlerThread;
    private Handler mSendingHandler;
    private SerialPortReadThread mSerialPortReadThread;
    private ConfigurationSdk mSdk;
    //标记是否初始化
    // private boolean isInit = false;
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
    private boolean isOpen = false;

    public SerialPortManager() {
    }


    private List<byte[]> sendList = new ArrayList<>();

    public void init(ConfigurationSdk sdk) {
        /*if (isInit) {
            return;
        }*/
        this.mSdk = sdk;
        openSerialPort();
        // isInit = true;
        if (mScheduledThreadPoolExecutor != null) {
            return;
        }
        mScheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) ArmsUtils.obtainAppComponentFromContext(MyAppLocation.myAppLocation).executorService();

        mScheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                if (listEmpty) {
                    if (sendList.size() != 0) {
                        sendList.clear();
                    }
                   listEmpty=false;
                } else {
                    if (sendList.size() != 0) {
                        byte[] bytes = sendList.get(0);
                        if (!listEmpty){
                            sendBytes(bytes);
                        }
                        sendList.remove(0);
                    }
                }

            }
        }, 0, 1000, TimeUnit.MILLISECONDS);

    }

    boolean  listEmpty = false;

    public void cleanSendByteToList() {
        LogUtils.d("清除list");
        listEmpty = true;
    }


    /**
     * 打开串口
     *
     * @return 打开是否成功
     */
    public boolean openSerialPort() {
        LogUtils.d("openSerialPort: " + String.format("打开串口 %s  波特率 %s", mSdk.getDevice().getPath(), mSdk.getBaudRate()));
        // 校验串口权限
        if (!mSdk.getDevice().canRead() || !mSdk.getDevice().canWrite()) {
            boolean chmod777 = chmod777(mSdk.getDevice());
            if (!chmod777) {
                LogUtils.d("openSerialPort: 没有读写权限");
                if (null != mOnOpenSerialPortListener) {
                    mOnOpenSerialPortListener.onFail(mSdk.getDevice(), OnOpenSerialPortListener.Status.NO_READ_WRITE_PERMISSION);
                }
                return false;
            }
        }

        if (!mSdk.getDevice().canRead() || !mSdk.getDevice().canWrite()) {
            try {
                List<String> commnandList1 = new ArrayList<>();
                commnandList1.add("chmod 777 " + mSdk.getDevice().getAbsolutePath());
                ShellUtils.execCommand(commnandList1, true);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

        try {
            mFd = open(mSdk.getDevice().getAbsolutePath(), mSdk.getBaudRate(), 0);
            mFileInputStream = new FileInputStream(mFd);
            mFileOutputStream = new FileOutputStream(mFd);
            LogUtils.d("openSerialPort: 串口已经打开 " + mFd);
            if (null != mOnOpenSerialPortListener) {
                mOnOpenSerialPortListener.onSuccess(mSdk.getDevice());
            }
            isOpen = true;
            // 开启发送消息的线程
            startSendThread();
            // 开启接收消息的线程
            startReadThread();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (null != mOnOpenSerialPortListener) {
                mOnOpenSerialPortListener.onFail(mSdk.getDevice(), OnOpenSerialPortListener.Status.OPEN_FAIL);
            }
            isOpen = false;
        }
        return false;
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        //isInit = false;
        if (null != mFd) {
            try {
                close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mFd = null;
        }
        // 停止发送消息的线程
        stopSendThread();
        // 停止接收消息的线程
        stopReadThread();

        if (null != mFileInputStream) {
            try {
                mFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mFileInputStream = null;
        }

        if (null != mFileOutputStream) {
            try {
                mFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mFileOutputStream = null;
        }
    }

    /**
     * 添加打开串口监听
     *
     * @param listener listener
     * @return SerialPortManager
     */
    public SerialPortManager setOnOpenSerialPortListener(OnOpenSerialPortListener listener) {
        mOnOpenSerialPortListener = listener;
        return this;
    }

    /**
     * 添加数据通信监听
     *
     * @param listener listener
     * @return SerialPortManager
     */
    public SerialPortManager setOnSerialPortDataListener(OnSerialPortDataListener listener) {
        mOnSerialPortDataListener = listener;
        return this;
    }

    /**
     * 开启发送消息的线程
     */
    private void startSendThread() {
        // 开启发送消息的线程
        mSendingHandlerThread = new HandlerThread("mSendingHandlerThread");
        mSendingHandlerThread.start();
        // Handler
        mSendingHandler = new Handler(mSendingHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                byte[] sendBytes = (byte[]) msg.obj;
                if (null != mFileOutputStream && null != sendBytes && 0 < sendBytes.length) {
                    try {
                        mFileOutputStream.write(sendBytes);
                        if (null != mOnSerialPortDataListener) {
                            mOnSerialPortDataListener.onDataSent(mSdk.getDevice(), sendBytes);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    /**
     * 停止发送消息线程
     */
    private void stopSendThread() {
        mSendingHandler = null;
        if (null != mSendingHandlerThread) {
            mSendingHandlerThread.interrupt();
            mSendingHandlerThread.quit();
            mSendingHandlerThread = null;
        }
    }

    /**
     * 现磨串口当前的数据
     */
    boolean isSend = false;

    /**
     * 开启接收消息的线程
     */
    private void startReadThread() {
        mSerialPortReadThread = new SerialPortReadThread(mFileInputStream, mSdk) {
            @Override
            public void onDataReceived(byte[] bytes) {
                //第一时间接收到的数据
                if (null != mOnSerialPortDataListener) {

                        //其他数据
                        mOnSerialPortDataListener.onDataReceived(mSdk.getDevice(), bytes);

                }
            }
        };
        mSerialPortReadThread.start();
    }

    /**
     * 停止接收消息的线程
     */
    private void stopReadThread() {
        if (null != mSerialPortReadThread) {
            mSerialPortReadThread.release();
        }
    }

    boolean prossing = false;
    long prossing_time = System.currentTimeMillis();

    /**
     * 直接发送数据
     *
     * @param sendBytes 发送数据
     * @return 发送是否成功
     */
    public boolean sendBytes(byte[] sendBytes) {
        // LogUtils.d(ByteUtils.byte2HexStr(sendBytes));
        if (null != mFd && null != mFileInputStream && null != mFileOutputStream) {
            if (null != mSendingHandler) {
                Message message = Message.obtain();
                message.obj = sendBytes;

                return mSendingHandler.sendMessage(message);
            }
        }
        return false;
    }

    /**
     * 加入列表等待发送
     *
     * @param bytes
     */
    public void addSendByteToList(byte[] bytes) {
        if (isOpen) {
            if (null != bytes) {
                sendList.add(bytes);
            }
        } else {
            LogUtils.d("串口未打开");
            ArmsUtils.snackbarText(MyAppLocation.myAppLocation.getString(R.string.openserialfirst));
        }


    }
}
