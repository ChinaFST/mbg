/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dy.colony.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;
import com.dy.colony.R;
import com.dy.colony.app.utils.DataBaseUtil;
import com.dy.colony.app.utils.SPUtils;
import com.dy.colony.language.LanguageListener;
import com.dy.colony.language.LanguageUtils;
import com.dy.colony.language.MultiLanguage;
import com.jess.arms.base.delegate.AppLifecycles;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * ================================================
 * 展示 {@link AppLifecycles} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {
        MultiDex.install(base);  //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
        LanguageUtils.saveSystemCurrentLanguage(base);
    }

    @Override
    public void onCreate(@NonNull Application application) {

        MultiLanguage.init(new LanguageListener() {
            @Override
            public Locale getSetLanguageLocale(Context context) {
                //返回自己本地保存选择的语言设置
                return LanguageUtils.getSetLanguageLocale(context);
            }
        });
        MultiLanguage.setApplicationLanguage(application);


        if (BuildConfig.LOG_DEBUG) {//Timber初始化
            //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
//            Timber.plant(new Timber.DebugTree());
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
//                    Logger.addLogAdapter(new AndroidLogAdapter());
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            Logger.log(priority, tag, message, t);
//                        }
//                    });
            LogUtils.getLogConfig()
                    .configAllowLog(true)
                    .configTagPrefix("colony_log")
                    .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                    .configShowBorders(true)
//                .configMethodOffset(1)
//                .addParserClass(OkHttpResponseParse.class)
                    .configLevel(LogLevel.TYPE_VERBOSE);
            ButterKnife.setDebug(true);


        } else {
            LogUtils.getLogConfig()
                    .configAllowLog(false)
                    .configTagPrefix("colony")
                    .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                    .configShowBorders(false)
                    .configLevel(LogLevel.TYPE_VERBOSE);
            ButterKnife.setDebug(false);
        }

        Constants.init(application);
        copyDatabase(application);

        //opencv库初始化
        BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(application) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {

                    }
                    break;
                    default: {
                        super.onManagerConnected(status);
                    }
                    break;
                }
            }
        };
        if (!OpenCVLoader.initDebug()) {
            LogUtils.d("Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, application, mLoaderCallback);
        } else {
            LogUtils.d("OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }


    }

    private void copyDatabase(Application application) {
        boolean database = (boolean) SPUtils.get(application, "database", false);
        if (!database) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //用户列表
                        DataBaseUtil.copyDataBase(application, R.raw.user_db, "USER-db");
                        DataBaseUtil.copyDataBase(application, R.raw.user_db_journal, "USER-db-journal");
                        //检测项目和标准关联信息
                        DataBaseUtil.copyDataBase(application, R.raw.food_item_and_standard_db, "FOOD_ITEM_AND_STANDARD-db");
                        DataBaseUtil.copyDataBase(application, R.raw.food_item_and_standard_db_journal, "FOOD_ITEM_AND_STANDARD-db-journal");
                        //食品33大类
                        DataBaseUtil.copyDataBase(application, R.raw.simple33_db, "SIMPLE33-db");
                        DataBaseUtil.copyDataBase(application, R.raw.simple33_db_journal, "SIMPLE33-db-journal");
                        //胶体金检测项目
                        DataBaseUtil.copyDataBase(application, R.raw.jtjtest_item_db, "JTJTEST_ITEM-db");
                        DataBaseUtil.copyDataBase(application, R.raw.jtjtest_item_db_journal, "JTJTEST_ITEM-db-journal");
                        //分光光度检测项目
                        DataBaseUtil.copyDataBase(application, R.raw.fggdtest_item_db, "FGGDTEST_ITEM-db");
                        DataBaseUtil.copyDataBase(application, R.raw.fggdtest_item_db_journal, "FGGDTEST_ITEM-db-journal");

                        SPUtils.put(application, "database", true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        }

    }


    /**
     * @param application 只会在虚拟环境下才会调用该方法，也就是说真机永远不会调用，所以这个方法可以说是无用
     */
    @Override
    public void onTerminate(@NonNull Application application) {
        // application.unbindService(mConnection);
    }
}
