package com.dy.colony.app.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Api;
import com.dy.colony.MyAppLocation;
import com.dy.colony.app.mqtt.MqttCallback;
import com.dy.colony.app.mqtt.MqttManager;
import com.jess.arms.base.BaseService;
import com.jess.arms.utils.ArmsUtils;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import it.sauronsoftware.cron4j.Scheduler;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

public class MyMqttService extends BaseService {
    private IBinder bind = new MyBinder();
    private MqttManager mqttManager;
    private Api api;
    private RxErrorHandler mErrorHandler;
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;


    public class MyBinder extends Binder {
        public MyMqttService getService() {
            return MyMqttService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }

    @Override
    public void init() {

        mErrorHandler = ArmsUtils.obtainAppComponentFromContext(this).rxErrorHandler();
        mScheduledThreadPoolExecutor = ((ScheduledThreadPoolExecutor) ArmsUtils.obtainAppComponentFromContext(MyAppLocation.myAppLocation).executorService());



        //分	从 0 到 59
        //时	从 0 到 23
        //天	从 1 到 31，字母 L 可以表示月的最后一天
        //月	从 1 到 12，可以别名：jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov" and "dec"
        //周	从 0 到 6，0 表示周日，6 表示周六，可以使用别名： "sun", "mon", "tue", "wed", "thu", "fri" and "sat"
        //定时任务调度
        Scheduler scheduler_heat = new Scheduler();
        scheduler_heat.schedule("* * * * *", new Runnable() {
            @Override
            public void run() {
                mqttManager.publishMessage("coffee/client/" +  "/heart", System.currentTimeMillis() + "", false);

            }
        });
        scheduler_heat.schedule("/30 * * * *", new Runnable() {
            @Override
            public void run() {

            }
        });
        scheduler_heat.start();

    }


    /**
     *
     */
    private void initMqtt() {
        LogUtils.d("initMqtt");
        //Android 端订阅
        // 订阅的主题 = 设备编号 + 主题
        String s = "Constants.deviceSN()";
        String[] topics = {
                //查询是否制作中  1空闲 2制作中
                s + "devState",
                //清洗速溶，清洗现磨，现磨排空
                s + "claenInstant", s + "cleanGround", s + "emptying",
                //轨道复位(需要轨道位置？)
                s + "resetting",
                //打开/关闭自动门
                s + "openTheDoor", s + "closeTheDoor",
                //打开/关闭电控锁（需要电控锁编号1-255）
                s + "lock", s + "unlock",
                //加水
                s + "addWater",
                //速溶发送指令（例如：AA5503060109）
                s + "orderInstant",
                //查询状态
                s + "state1", s + "state2", s + "state3",
                //结束远程操作，不推送相关串口消息
                s + "finish",
                //1.清洗计划（coffee/server/{设备号}/clean）
                //次数 速溶 现磨
                //1 0 1
                "coffee/server/" + s + "/clean",
                //2.开关机设置变更（coffee/server/{设备号}/power）
                //时间戳
                "coffee/server/" + s + "/power",
                //3.广告新增，修改，删除（coffee/server/{设备号}/programPlan）
                //时间戳
                "coffee/server/" + s + "/programPlan",
                //4.UI资源包下发（coffee/server/{设备号}/issueUi）
                //时间戳
                "coffee/server/" + s + "/issueUi",
                //5.冷热水高低值，服务电话（coffee/server/{设备号}/deviceInfo）
                //时间戳
                "coffee/server/" + s + "/deviceInfo"


        };

        /*web端订阅  订阅的主题 = 设备编号 + 主题
        //查询状态返回 1空闲 2制作中
        Constants.deviceSN() + "devStateBack",
        //速溶串口的数据收发
        Constants.deviceSN() + "serialInstantSend",Constants.deviceSN() + "serialInstantReceive",
        //支付串口的数据收发
        Constants.deviceSN() + "serialPaySend",Constants.deviceSN() + "serialPayReceive",
        //现磨串口的数据收发
        Constants.deviceSN() + "serialCurrentSend",Constants.deviceSN() + "serialCurrentReceive"*/
        // 收发 时间  串口名  指令  （空格隔开）
        // 发送 2023-8-31 22:10:22 速溶串口TTYS4 A55A03210023
        int[] qos = {
                2,
                2, 2, 2,
                2,
                2, 2,
                2, 2,
                2,
                2,
                2, 2, 2,
                2,
                2,
                2,
                2,
                2,
                2
        };
        mqttManager = MqttManager.init(Api.MQTT_URL, "123", Api.MQTT_PASSWORD, this, topics, qos);
        MqttManager.getInstance().setCallback(new MqttCallback() {
            @Override
            public void subscribedSuccess(String[] mqttTopic) {

            }

            @Override
            public void subscribedFail(String message) {
                LogUtils.d("subscribedFail:" + message);
            }

            @Override
            public void receiveMessage(String topic, String message) {
               // LogUtils.d("topic:" + topic + "------receiveMessage:" + message);

            }

            @Override
            public void connectSuccess(boolean reconnect) {
                LogUtils.d("connectSuccess:" + reconnect);
            }

            @Override
            public void connectFail(String message) {
                LogUtils.d("connectFail:" + message);
            }

            @Override
            public void connectLost(String message) {
                LogUtils.d("connectLost:" + message);
            }

        });

    }




  


    @Override
    public boolean onUnbind(Intent intent) {
        MqttManager.getInstance().onDestroy();
        return super.onUnbind(intent);

    }
}