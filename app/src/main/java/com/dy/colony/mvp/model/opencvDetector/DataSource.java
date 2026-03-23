package com.dy.colony.mvp.model.opencvDetector;

import org.opencv.core.MatOfPoint;
import org.opencv.core.RotatedRect;

import java.util.ArrayList;

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
 * @data: 10/29/21 3:30 PM
 * Description:
 */
public class DataSource {
    public ArrayList<RotatedRect> usefulRotatedRect = new ArrayList<>();
    public ArrayList<MatOfPoint> usefules = new ArrayList<>();

    public ArrayList<RotatedRect> getUsefulRotatedRect() {
        return usefulRotatedRect;
    }

    public void setUsefulRotatedRect(ArrayList<RotatedRect> usefulRotatedRect) {
        this.usefulRotatedRect = usefulRotatedRect;
    }

    public ArrayList<MatOfPoint> getUsefules() {
        return usefules;
    }

    public void setUsefules(ArrayList<MatOfPoint> usefules) {
        this.usefules = usefules;
    }
}
