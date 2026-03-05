package com.dy.colony.app.mqtt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.Api;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
 * @data: 8/1/23 9:59 PM
 * Description:
 */
public class MqttManager {
    private String serverUri;
    private String userName;
    private String passWord;
    private final Context mContext;
    private volatile MqttAndroidClient mqttAndroidClient;
    private static volatile MqttManager mqttManager = null;
    private MqttCallback callback;
    private String[] mqttTopic;
    private int[] qos;
    private boolean autoReconnect = true;


    /**
     * 初始化
     *
     * @param serverUri mqtt域名
     * @param userName  账号
     * @param passWord  密码
     * @param context
     */
    public static MqttManager init(String serverUri, String userName, String passWord, Context context) {
        return init(serverUri, userName, passWord, context, null, null);
    }

    /**
     * 初始化
     *
     * @param serverUri mqtt域名
     * @param userName  账号
     * @param passWord  密码
     * @param context
     * @param topics    主题
     * @param qos       QOS ＝　0/1/2  　最多一次　　最少一次　多次
     * @return
     */
    public static MqttManager init(String serverUri, String userName, String passWord, Context context, String[] topics,
                                   int[] qos) {
        if (mqttManager == null) {
            synchronized (MqttManager.class) {
                if (mqttManager == null) {
                    mqttManager = new MqttManager(serverUri, userName, passWord, context, topics, qos);
                }
            }
        }
        return mqttManager;
    }


    /**
     * 修改订阅主题
     *
     * @param topics 主题
     * @param qos    QOS ＝　0/1/2  　最多一次　　最少一次　多次
     */
    public void setTopicAndQos(String[] topics, int[] qos) {
        if (mqttAndroidClient != null || !mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.unsubscribe(mqttTopic);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        mqttTopic = topics;
        this.qos = qos;
        subscribeToTopic();
    }

    /**
     * 设置是否自动重连,默认为true
     *
     * @param isAuto
     */
    public void setAutoReconnect(boolean isAuto) {
        autoReconnect = isAuto;
    }


    public MqttManager(String serverUri, String userName, String passWord, Context context, String[] topics, int[] qos) {
        this.serverUri = serverUri;
        this.userName = userName;
        this.passWord = passWord;
        this.mqttTopic = topics;
        this.qos = qos;
        mContext = context;
        initConnect();
    }

    public void setCallback(MqttCallback callback) {
        this.callback = callback;
    }

    private boolean isConnect() {
        if (mqttAndroidClient != null) {
            return mqttAndroidClient.isConnected();
        }
        return false;
    }

    private void initConnect() {

        if (isConnect()) {
            return;
        }
        mqttAndroidClient = new MqttAndroidClient(mContext, serverUri, "12");
        mqttAndroidClient.registerResources(mContext);
        mqttAndroidClient.setCallback(new MyMqttCallbackExtended());

        //mqtt连接参数设置
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        //设置自动重连
        mqttConnectOptions.setAutomaticReconnect(autoReconnect);
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录
        // 这里设置为true表示每次连接到服务器都以新的身份连接
        LogUtils.d(serverUri);
        LogUtils.d(userName);
        LogUtils.d(passWord);
        mqttConnectOptions.setCleanSession(false);
        //设置连接的用户名
        mqttConnectOptions.setUserName(userName);
        //设置连接的密码
        mqttConnectOptions.setPassword(passWord.toCharArray());
        // 设置超时时间 单位为秒
        mqttConnectOptions.setConnectionTimeout(10);
        // 设置会话心跳时间 单位为秒 服务器会每隔20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        mqttConnectOptions.setKeepAliveInterval(20);
        try {
            mqttAndroidClient.connect(mqttConnectOptions);
            LogUtils.d("connect");
        } catch (Exception e) {
            LogUtils.d("connect--onFailure:" + e.toString());
            e.printStackTrace();
            if (callback != null) {
                callback.connectFail(e.toString());
            }
        }
    }

    private class MyMqttCallbackExtended implements MqttCallbackExtended {

        /**
         * 连接完成回调
         *
         * @param reconnect true 断开重连,false 首次连接
         * @param serverURI 服务器URI
         */
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            //LogUtils.d("连接成功connectComplete:是否重连+" + reconnect + "-----serverURI:" + serverURI);
            if (callback != null) {
                callback.connectSuccess(reconnect);
            }
            subscribeToTopic();
        }

        @Override
        public void connectionLost(Throwable cause) {
            //LogUtils.d("connectionLost:断开");
            if (callback != null) {
                callback.connectLost("connectionLost:断开");
            }
            cause.printStackTrace();
        }

        /**
         * 消息接收，如果在订阅的时候没有设置IMqttMessageListener，那么收到消息则会在这里回调。
         * 如果设置了IMqttMessageListener，则消息回调在IMqttMessageListener中
         *
         * @param topic
         * @param message
         */
        @Override
        public void messageArrived(String topic, MqttMessage message) {
            LogUtils.d(message.getId() + "-->receive message: " + message.toString());
            if (callback != null) {
                callback.receiveMessage(topic, message.toString());
            }

        }

        /**
         * 交付完成回调。在publish消息的时候会收到此回调.
         * qos:
         * 0 发送完则回调
         * 1 或 2 会在对方收到时候回调
         *
         * @param token
         */
        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            if (callback != null) {
                try {
                    callback.deliveryComplete(token.getMessage().toString());
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
            
            LogUtils.d(token.toString());
        }
    }

    private void subscribeToTopic() {
        if (mqttAndroidClient == null || !mqttAndroidClient.isConnected() || mqttTopic == null || qos == null) {
            return;
        }
        try {
            mqttAndroidClient.subscribe(mqttTopic, qos, mContext.getApplicationContext(), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    /*for (String s : mqttTopic) {
                        LogUtils.d("订阅成功" + "topic:" + s);
                    }*/
                    if (callback != null) {
                        callback.subscribedSuccess(mqttTopic);
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    LogUtils.d("订阅失败");
                    if (callback != null) {
                        callback.subscribedFail(exception.toString());
                    }
                }

            });
        } catch (MqttException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.subscribedFail(e.toString());
            }
        }
    }

    public static MqttManager getInstance() {
        if (mqttManager == null) {
            throw new NullPointerException("请先调用init方法进行初始化");
        }
        return mqttManager;
    }

    /**
     * 发送
     *
     * @param topic    发送的主题
     * @param msg      发送的消息
     *       QOS ＝　0/1/2  　最多一次　　最少一次　多次  默认为2
     * @param retained 是否保留消息，为true时,后来订阅该主题的仍然收到该消息
     */
    public void publishMessage(String topic, String msg, boolean retained) {
        if (isConnect()) {
            try {
                mqttAndroidClient.publish(topic, msg.getBytes(), 2, retained);
                LogUtils.d(topic+" 发送消息：" + msg);
                if (!mqttAndroidClient.isConnected()) {
                    LogUtils.d(mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
                }
            } catch (MqttException e) {
                LogUtils.d("Error Publishing: " + e.toString());
            }
        }
    }

    public void onDestroy() {
        if (mqttAndroidClient == null) {
            return;
        }
        try {
            mqttAndroidClient.close();
            mqttAndroidClient.disconnect();
            mqttAndroidClient.unregisterResources();
            mqttManager = null;
            mqttAndroidClient = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 判断网络是否连接
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            LogUtils.d("MQTT当前网络名称：" + name);
            return true;
        } else {
            LogUtils.d("MQTT 没有可用网络");
            return false;
        }
    }


}
