package com.dy.colony;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;

import com.apkfuns.logutils.LogUtils;
import com.blankj.utilcode.util.LanguageUtils;
import com.dy.colony.app.service.MyMqttService;
import com.dy.colony.app.service.SerialDataService;
import com.dy.colony.app.service.UvcCameraService;
import com.dy.colony.app.service.VoicePlayService;
import com.dy.colony.app.utils.DataBaseUtil;
import com.dy.colony.app.utils.SPUtils;
import com.dy.colony.crash.CaocConfig;
import com.dy.colony.crash.CustomActivityOnCrash;
import com.dy.colony.mvp.ui.activity.HomeActivity;
import com.jess.arms.base.BaseApplication;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

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
 * @data: 7/25/23 11:09 PM
 * Description:
 */
public class MyAppLocation extends BaseApplication implements TextToSpeech.OnInitListener {
    private TextToSpeech mTextToSpeech;
    public static MyAppLocation myAppLocation = null;
    private Intent mIntent;
    private Intent mIntent1;
    private Intent mIntent2;
    private Intent mIntent3;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.d("SerialDataService启动成功");
            mSerialDataService = ((SerialDataService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d("SerialDataService启动失败");
        }
    };
    private ServiceConnection mConnection1 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.d("MyMqttService启动成功");
            myMqttService = ((MyMqttService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d("MyMqttService启动失败");
        }
    };
    private ServiceConnection mConnection2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.d("VoicePlayService启动成功");
            voicePlayService = ((VoicePlayService.VoicePlayBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d("MyMqttService启动失败");
        }
    };
    private ServiceConnection mConnection3 = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定成功
            uvcCameraService = ((UvcCameraService.MyBinder) service).getService();
            LogUtils.d("CameraAidlInterface启动成功");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d("CameraAidlInterface启动失败");
        }
    };
    public SerialDataService mSerialDataService;
    public MyMqttService myMqttService;
    public VoicePlayService voicePlayService;
    public UvcCameraService uvcCameraService;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.INITCRASH) {
            initCrash();
        }
        myAppLocation = this;
        mIntent = new Intent(this, SerialDataService.class);
        mIntent1 = new Intent(this, MyMqttService.class);
        mIntent2 = new Intent(this, VoicePlayService.class);
        mIntent3 = new Intent(this, UvcCameraService.class);
        boolean process = isAppProcess();
        LogUtils.d(process);
        if (process) {
            LogUtils.d("start  SerialDataService");
            bindService(mIntent, mConnection, BIND_AUTO_CREATE);
            bindService(mIntent1, mConnection1, BIND_AUTO_CREATE);
            bindService(mIntent2, mConnection2, BIND_AUTO_CREATE);
            bindService(mIntent3, mConnection3, BIND_AUTO_CREATE);
            initTextToSpeech();
        }
        LanguageUtils.applySystemLanguage();
    }

    private void initCrash() {
        System.out.println("initCrash");
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.drawable.error_icn) //错误图标
                .restartActivity(HomeActivity.class) //重新启动后的activity
                //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
                //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
        CustomActivityOnCrash.install(this);
    }
    /**
     * 判断该进程是否是app进程
     *
     * @return
     */
    public boolean isAppProcess() {
        String processName = getThisProcessName();
        if (processName == null || !processName.equalsIgnoreCase(this.getPackageName())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取运行该方法的进程的进程名
     *
     * @return 进程名称
     */
    public String getThisProcessName() {
        int processId = android.os.Process.myPid();
        String processName = null;
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        Iterator iterator = manager.getRunningAppProcesses().iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo processInfo = (ActivityManager.RunningAppProcessInfo) (iterator.next());
            try {
                if (processInfo.pid == processId) {
                    processName = processInfo.processName;
                    return processName;
                }
            } catch (Exception e) {
//                LogD(e.getMessage())
            }
        }
        return processName;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //用户在系统设置页面切换语言时保存系统选择语言(为了选择随系统语言时使用，如果不保存，切换语言后就拿不到了）
    }
    /**
     * 1普通话 2粤语
     */
    public int languagetype = 1;
    /**
     * 文字转语音
     */
    public void initTextToSpeech() {
        if (null != mTextToSpeech) {
            mTextToSpeech.stop();
            mTextToSpeech = null;
        }
        // 参数Context,TextToSpeech.OnInitListener
        mTextToSpeech = new TextToSpeech(this, this, languagetype == 1 ? "com.google.android.tts" : "com.iflytek.speechcloud");
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        mTextToSpeech.setPitch(1.0f);
        // 设置语速
        mTextToSpeech.setSpeechRate(1.0f);

    }
    public void speak(String paramString) {

        LogUtils.d("speak");
        if (null == mTextToSpeech) {
            return;
        }
        if (mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
        }
        if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
            /*
                TextToSpeech的speak方法有两个重载。
                // 执行朗读的方法
                speak(CharSequence text,int queueMode,Bundle params,String utteranceId);
                // 将朗读的的声音记录成音频文件
                synthesizeToFile(CharSequence text,Bundle params,File file,String utteranceId);
                第二个参数queueMode用于指定发音队列模式，两种模式选择
                （1）TextToSpeech.QUEUE_FLUSH：该模式下在有新任务时候会清除当前语音任务，执行新的语音任务
                （2）TextToSpeech.QUEUE_ADD：该模式下会把新的语音任务放到语音任务之后，
                等前面的语音任务执行完了才会执行新的语音任务
             */
            mTextToSpeech.speak(paramString, TextToSpeech.QUEUE_FLUSH, null);
            LogUtils.d("speak");
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            /*
                使用的是小米手机进行测试，打开设置，在系统和设备列表项中找到更多设置，
            点击进入更多设置，在点击进入语言和输入法，见语言项列表，点击文字转语音（TTS）输出，
            首选引擎项有三项为Pico TTs，科大讯飞语音引擎3.0，度秘语音引擎3.0。其中Pico TTS不支持
            中文语言状态。其他两项支持中文。选择科大讯飞语音引擎3.0。进行测试。

                如果自己的测试机里面没有可以读取中文的引擎，
            那么不要紧，我在该Module包中放了一个科大讯飞语音引擎3.0.apk，将该引擎进行安装后，进入到
            系统设置中，找到文字转语音（TTS）输出，将引擎修改为科大讯飞语音引擎3.0即可。重新启动测试
            Demo即可体验到文字转中文语言。
             */
            // setLanguage设置语言
            int result;
            result = mTextToSpeech.setLanguage(Locale.CHINA);
            /*if (languagetype == 1) {
            } else {
                Locale locale = new Locale("yue", "HKG");
                result = mTextToSpeech.setLanguage(locale);
            }*/


            // TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失 -1
            // TextToSpeech.LANG_NOT_SUPPORTED：不支持
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                LogUtils.d("表示语言的数据丢失或者不支持" + result);
            }
        }
    }


}
