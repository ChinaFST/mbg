package com.dy.colony;

import android.app.Application;

import com.dy.colony.app.utils.SPUtils;
import com.dy.colony.greendao.beans.User;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/5
 */
public class Constants {
    /**
     * {@link #ISREMBERUSERNAME}是否记住用户密码（登录页面）<p>
     * {@link #KEY_REMBERUSERNAME}<p>
     */
    public static boolean ISREMBERUSERNAME;
    public static String KEY_REMBERUSERNAME = "KEY_REMBERUSERNAME";

    /**
     * 保存当前用户的key
     */
    public static final String KEY_USERINFOR_JSON = "KEY_USERINFOR_JSON";

    /**
     * 当前登录用户 仪器本身
     */
    public static User NOWUSER;

    public static String KEY_AUTO_UPLOAD_ENABLED = "auto_upload_enabled";


    /**
     * 初始化软件启动需要的全局变量
     *
     * @param application
     */
    public static void init(Application application) {
        //是否记住用户（登录界面）
        Constants.ISREMBERUSERNAME = (boolean) SPUtils.get(application, Constants.KEY_REMBERUSERNAME, false);
    }
} 
