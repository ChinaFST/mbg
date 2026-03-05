package com.dy.colony.app.service;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.view.Display;
import android.view.WindowManager;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.presentation.SecondScreenPresentation;
import com.jess.arms.base.BaseService;

/**
 *  Description :离屏渲染服务
 */
public class MultiScreenService extends BaseService {
    /**  屏幕管理器 **/
    private DisplayManager mDisplayManager;
    /**  屏幕数组 **/
    private Display[] displays;
    /**  第二块屏 **/
    private SecondScreenPresentation presentation;


    @Override
    public IBinder onBind(Intent intent) {
        return new MultiScreenBinder();
    }



    @Override
    public void init() {
        initPresentation();
    }

    /** 初始化第二块屏幕 **/
    public void initPresentation(){
        if(null==presentation){
            mDisplayManager = (DisplayManager) this.getSystemService(Context.DISPLAY_SERVICE);
            displays = mDisplayManager.getDisplays();
            LogUtils.d(displays.length);
            if(displays.length > 1){
                // displays[1]是副屏
                presentation = new SecondScreenPresentation(this, displays[1]);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    presentation.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                } else {
                    presentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }
            }
        }
    }

    /** 显示第二块屏 **/
    public void showSearchPresentation(){
        if (presentation==null){
            return;
        }
        if (!presentation.isShowing()){
            presentation.show();
        }
    }


    public class MultiScreenBinder extends Binder {
        public MultiScreenService getService(){
            return MultiScreenService.this;
        }
    }


}