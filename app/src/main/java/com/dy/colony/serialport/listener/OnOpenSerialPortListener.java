package com.dy.colony.serialport.listener;

import java.io.File;

/**
 * 打开串口监听
 */
public interface OnOpenSerialPortListener {

    /**
     * 打开成功
     * @param device
     */
    void onSuccess(File device);

    /**
     * 打开失败
     * @param device
     * @param status
     */
    void onFail(File device, Status status);

    enum Status {
        NO_READ_WRITE_PERMISSION,
        OPEN_FAIL
    }
}
