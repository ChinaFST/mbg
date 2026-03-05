package com.dy.colony;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.app.utils.FileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

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
 * @data: 5/4/23 10:48 AM
 * Description:
 */
public class Constans {
    //public static int MYVID_MULTICARD_CAM = 7119, MYPID_MULTICARD_CAM = 2825;
    public static int MYVID_MULTICARD_CAM = 5546, MYPID_MULTICARD_CAM = 5461;

    public static String PATH_USERBEAN = "/data/data/" + BuildConfig.APPLICATION_ID + "/platfromuser.json";

    public static boolean checkPla(){
        if (BuildConfig.FLAVOR.equals("productFlavor_HAVEjtj_JiangXiNongYeNongCunJu")){
            return true;
        }
        return false;
    }


    public static <T> T CheckUser(Class<T> object) {
        boolean exists = FileUtils.isFileExists(Constans.PATH_USERBEAN);
        if (exists) {
            File user = FileUtils.getFileByPath(Constans.PATH_USERBEAN);
            String filetostring = null;
            try {
                filetostring = FileUtils.filetostring(user);

            } catch (IOException e) {
                e.printStackTrace();
            }
            LogUtils.d(filetostring);
            return new Gson().fromJson(filetostring, object);

        }
        return null;
    }
}
