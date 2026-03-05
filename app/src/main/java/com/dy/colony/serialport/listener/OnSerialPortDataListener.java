package com.dy.colony.serialport.listener;

import java.io.File;

/**
 * 串口消息监听
 */
public interface OnSerialPortDataListener {

    /**
     * 数据接收
     *
     * @param device 串口名称
     * @param bytes 接收到的数据
     */
    void onDataReceived(File device,byte[] bytes);

    /**
     * 数据发送
     *
     * @param device 串口名称
     * @param bytes 发送的数据
     */
    void onDataSent(File device,byte[] bytes);
}
