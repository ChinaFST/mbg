package com.dy.colony.mvp.ui.widget;

import com.dy.colony.app.utils.RegressionLine;
import com.dy.colony.mvp.model.entity.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class DyUtils_JTJ_P_6270 {

    private  List<Float> areas = new ArrayList<>(); //面积集合
    private  double[] result;
    int offset;
    /**
     * 默认CT线是在中线的两边
     * 极端情况 CT线处于中间的一边，前半部分或者后半部分，其中包括C线处于中线位置或者t线处于中线位置
     * 这种情况就不好判断CT线，
     * 解决思路
     * 1不设置中线这一说法，直接在检测曲线中找出两个最大值看做CT线 （存在问题，如果是Ct线有一个是没有的情况下会出现误判，)）
     * 2不设置中线 先找出两个面积最大的值看做CT线，然后再看CT线位置，C线超过中线位置某个值时则把C线看做T线（以线宽20为界限值））
     *
     * @param list
     * @return
     */
    public  double[] dyMath(float[] list) {
        areas.clear();
        result=new double[4];
        offset=list.length/5;
        for (int i = offset; i < list.length - 40; i++) {
            RegressionLine line2 = new RegressionLine();
            DataPoint point1 = new DataPoint(i-10, (float) list[i-10]);
            DataPoint point2 = new DataPoint(i + 10, (float) list[i + 10]);
            //LogUtils.d(point1);
            //LogUtils.d(point2);
            line2.addDataPoint(point1);
            line2.addDataPoint(point2);
            float a1 = line2.getA1(); //斜率
            float a0 = line2.getA0(); // b 值
            //LogUtils.d("\n回归线公式:  y = " + a1 + "x + " + a0);
            float v = 0;
            for (int j = i-10; j < i + 10; j++) {
                float v1 = a1 * j + a0;
                float v2 = v1 - list[j];
                if (v2 >= 0) {
                    v = v + v2;
                }
            }
            areas.add(v);
        }

        double[] ct = findC();
        return ct;

    }


    private  double[] findC() {
        //找到最大值后需要验证下是否有正常的上升沿和下降沿
        //没有正常的上升下降沿需要剔除该数据，用-1代替


        //寻找最大面积
        float max1 = 0;
        int max1_index = 0;
        for (int i = 0; i < areas.size() / 2; i++) {
            Float aFloat = areas.get(i);
            if (aFloat > max1) {
                max1_index = i;
                max1 = aFloat;
            }
        }

        if (max1!=-1){
            if (!check(max1_index)) {
                areas.set(max1_index,-1f);
                findC();
            }
        }



        result[0] = max1_index+offset-20;
        result[1] = max1;

        return  findT(max1_index);
    }

    private  double[] findT(int max1_index) {
        float max2 = 0;
        int max2_index = 0;
        //C线太靠近中线的情况
        //T线太靠近中线甚至是处于C线区域的情况
        //C线太靠近中线甚至是处于T线区域的情况
        if (max1_index + 10 > areas.size() / 2) {
            for (int i = max1_index + 10; i < areas.size(); i++) {
                Float aFloat = areas.get(i);
                if (aFloat > max2) {
                    max2_index = i;
                    max2 = aFloat;
                }
            }
        } else {
            for (int i = areas.size() / 2; i < areas.size(); i++) {
                Float aFloat = areas.get(i);
                if (aFloat > max2) {
                    max2_index = i;
                    max2 = aFloat;
                }
            }
        }

        if (max2==-1){
            if (!check(max2_index)) {
                areas.set(max2_index,-1f);
                findT(max1_index);
            }
        }

        result[2] = max2_index+offset-20;
        result[3] = max2;
        return result;
    }



    private  boolean check(int max1_index) {
        Float aFloat = areas.get(max1_index);
        int left = max1_index;
        int right = max1_index;
        Float left_value = aFloat;
        Float right_value = aFloat;
        //从最大往左是下降,从最大往右是下降
        if (aFloat == -1) {
            return false;
        }

        for (int i = 0; i < 5; i++) {
            left--;
            right++;
            if (left < 0) {
                return false;
            }
            if (right > areas.size()) {
                return false;
            }
            if (areas.get(left) > left_value) {
                return false;
            }
            left_value = areas.get(left);
            if (areas.get(right) > right_value) {
                return false;
            }
            right_value = areas.get(right);
        }

        return true;
    }


}
