package com.dy.colony.app.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * Created by wangzx on 2018/3/22.
 */

public class BeanUtils {
    /**
     * 把modelA对象的属性值赋值给bClass对象的属性。
     *
     * @param <T>
     * @param modelA
     * @param bClass
     * @return
     */
    public static <A, T> T modelAconvertoB_Gson(A modelA, Class<T> bClass) {
        try {
            Gson gson = new Gson();
            String gsonA = gson.toJson(modelA);
            T instanceB = gson.fromJson(gsonA, bClass);
            return instanceB;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 把modelA对象的属性值赋值给bClass对象的属性。
     *
     * @param <T>
     * @param modelA
     * @param bClass
     * @return
     */
    public static <A, T> T modelAconvertoB_Json(A modelA, Class<T> bClass) {
        try {
            //Gson gson = new Gson();
            String gsonA = JSON.toJSONString(modelA);
            T instanceB = JSON.parseObject(gsonA, bClass);
            return instanceB;
        } catch (Exception e) {
            return null;
        }
    }
}
