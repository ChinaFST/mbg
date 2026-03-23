package com.dy.colony.mvp.model.entity;


import com.dy.colony.app.utils.DyUtils_JTJ_P;

import java.util.List;

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
 * Created by wangzhenxiong on 2019-10-17.
 * 负责usb端的收发数据 计算结果等
 */
public class JTJDataModel_P {
    private List<Float> mBytes;
    public JTJDataModel_P(List<Float> bytes) {
        this.mBytes=bytes;
    }

    public double[] getData_S(){
        float[] f=new float[mBytes.size()];
        for (int i = 0; i < mBytes.size(); i++) {
            f[i]=mBytes.get(i);
        }
        return DyUtils_JTJ_P.dyMath(f);
    }

}
