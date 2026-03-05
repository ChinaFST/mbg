package com.dy.colony.app.utils;

import android.graphics.Bitmap;

import com.apkfuns.logutils.LogUtils;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

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
 * @data: 9/24/21 10:10 AM
 * Description:
 */
public class OpenCvUtils {





    private static Mat cropPicture(Mat cpsrcMat, RotatedRect rotatedRect, double maxrectbian, double minrectbian) {
        // String name = UUID.randomUUID().toString();
        // LogUtils.d(name);
        //LogUtils.d(rotated);
        //LogUtils.d(rotatedRect);

        //裁剪
        int pading = 0;
        //新坐标系下rect中心坐标
        double x = rotatedRect.center.x;
        double y = rotatedRect.center.y;


        int startrow = (int) (y - minrectbian / 2) - pading;
        if (startrow < 0) {
            startrow = 0;
        }
        int endrow = (int) (y + minrectbian / 2) + pading;
        if (endrow >= cpsrcMat.height()) {
            endrow = cpsrcMat.height();
        }
        int startcls = (int) (x - maxrectbian / 2) - pading;
        if (startcls < 0) {
            startcls = 0;
        }
        int endcls = (int) (x + maxrectbian / 2) + pading;
        if (endcls >= cpsrcMat.width()) {
            endcls = cpsrcMat.width();
        }
        //LogUtils.d("startrow:" + startrow + " endrow:" + endrow + " startcls:" + startcls + " endcls:" + endcls);
        if (startrow > endrow || startcls > endcls) {
            LogUtils.d("返回位置");
            return null;
        }
        cpsrcMat = cpsrcMat.submat(startrow, endrow, startcls, endcls);

        Mat mat = rotateImage(cpsrcMat, 90);
        return mat;
    }


    //旋转图像内容不变，尺寸相应变大
    public static Mat rotateImage(Mat img, double degree) {
        double angleHUD = degree * Math.PI / 180; // 弧度
        double sin = Math.abs(Math.sin(angleHUD)),
                cos = Math.abs(Math.cos(angleHUD)),
                tan = Math.abs(Math.tan(angleHUD));
        int width = img.width();
        int height = img.height();
        int width_rotate = (int) (height * sin + width * cos);
        int height_rotate = (int) (width * sin + height * cos);
        //旋转数组map
        // [ m0  m1  m2 ] ===>  [ A11  A12   b1 ]
        // [ m3  m4  m5 ] ===>  [ A21  A22   b2 ]
        Mat map_matrix = new Mat(2, 3, CvType.CV_32F);
        // 旋转中心
        Point center = new Point(width / 2, height / 2);
        map_matrix = Imgproc.getRotationMatrix2D(center, degree, 1.0);
        map_matrix.put(0, 2, map_matrix.get(0, 2)[0] + (width_rotate - width) / 2);
        map_matrix.put(1, 2, map_matrix.get(1, 2)[0] + (height_rotate - height) / 2);
        Mat rotated = new Mat();
        Imgproc.warpAffine(img, rotated, map_matrix, new Size(width_rotate, height_rotate), Imgproc.INTER_LINEAR | Imgproc.WARP_FILL_OUTLIERS, 0, new Scalar(255, 255, 255));

        return rotated;
    }

    public static Mat rotateImage1_(Mat img, double degree, Point center) {

        int width = img.width();
        int height = img.height();
        Mat map_matrix = new Mat(2, 3, CvType.CV_32F);
        map_matrix = Imgproc.getRotationMatrix2D(center, degree, 1.0);

        Mat rotated = new Mat();
        Imgproc.warpAffine(img, rotated, map_matrix, new Size(width, height), Imgproc.INTER_LINEAR | Imgproc.WARP_FILL_OUTLIERS, 0, new Scalar(255, 255, 255));

        return rotated;
    }


    public static Mat gaussi(Mat src) {
        Mat gaussi = new Mat();
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2RGB);
        Imgproc.bilateralFilter(src, gaussi, 9, 10, 10);
        return gaussi;
    }


    public static Mat sobel(Mat src, int alphaxvalue, int alphayvalue) {
        // 计算水平方向梯度
        Mat grad_y = new Mat();
        Mat grad_y_abs = new Mat();
        Imgproc.Sobel(src, grad_y, -1, 0, 1, 3, 1, 0);
        // 计算y方向上的梯度的绝对值
        Core.convertScaleAbs(grad_y, grad_y_abs);
        //setPicture(grad_y_abs, 3);
        // 计算垂直方向梯度
        Mat grad_x = new Mat();
        Mat grad_x_abs = new Mat();
        Imgproc.Sobel(src, grad_x, -1, 1, 0, 3, 1, 0);
        // 计算x方向上的梯度的绝对值
        Core.convertScaleAbs(grad_x, grad_x_abs);
        //setPicture(grad_x_abs, 4);
        Mat sobel = new Mat();

        // 计算结果梯度
        Core.addWeighted(grad_x_abs, alphaxvalue, grad_y_abs, alphayvalue, 1, sobel);
        return sobel;
    }

    public static Mat threshold(Mat src, int thresholdvalue) {
        Mat threshold = new Mat();
        Imgproc.threshold(src, threshold, thresholdvalue, 255, Imgproc.THRESH_BINARY);
        return threshold;
    }

    public static Mat dilateAndErode(Mat src, int dekeenelvalue) {
        Mat keenel = Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(dekeenelvalue, dekeenelvalue));
        Mat dilate = new Mat();
        Imgproc.dilate(src, dilate, keenel); // 膨胀
        Mat erode = new Mat();
        Imgproc.erode(dilate, erode, keenel);  // 腐蚀
        return erode;
    }

    public static Mat erodeAnddilate(Mat src, int dekeenelvalue) {
        Mat keenel = Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(dekeenelvalue, dekeenelvalue));
        Mat erode = new Mat();
        Imgproc.erode(src, erode, keenel);  // 腐蚀
        Mat dilate = new Mat();
        Imgproc.dilate(erode, dilate, keenel); // 膨胀

        return dilate;
    }

    public static ArrayList<MatOfPoint> findContours(Mat src) {
        ArrayList<MatOfPoint> es = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(src, es, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        return es;
    }



    public static MatOfPoint matOfIntToPoints(MatOfPoint contour, MatOfInt indexes) {
        int[] arrIndex = indexes.toArray();
        Point[] arrContour = contour.toArray();
        Point[] arrPoints = new Point[arrIndex.length];

        for (int i = 0; i < arrIndex.length; i++) {
            arrPoints[i] = arrContour[arrIndex[i]];
        }

        MatOfPoint hull = new MatOfPoint();
        hull.fromArray(arrPoints);
        return hull;
    }



    public static Mat mergeBitmaptoMat(List<Bitmap> list) {
        Bitmap bitmap1 = list.get(0);
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap1, dst);

        for (int i = 1; i < list.size(); i++) {
            Bitmap bitmap = list.get(i);
            Mat mat = new Mat();
            Utils.bitmapToMat(bitmap, mat);
            //第一个参数：src1，表示进行加权操作的第一个图像对象，即输入图片1；
            //第二个参数：double 型的alpha，表示第一个图像的加权系数，即图片1的融合比例；
            //第三个参数：src2，表示进行加权操作的第二个图像对象，即输入图片2；
            //第四个参数：double型的beta，表示第二个图像的加权系数，即图片2的融合比例。很多情况下，有关系 alpha + beta = 1.0；
            //第五个参数：double型的gamma，表示一个作用到加权和后的图像上的标量，可以理解为加权和后的图像的偏移量；
            //第六个参数：dst，表示两个图像加权和后的图像，尺寸和图像类型与src1和src2相同，即输出图像；
            //第七个参数：输出阵列的可选深度，有默认值 - 1。当两个输入数组具有相同的深度时，这个参数设置为 - 1（默认值），即等同于src1.depth（）
            Core.addWeighted(dst, 0.5, mat, 0.5, 0, dst);
        }

        return dst;
    }

    public static Bitmap mergeBitmaptobitmap(List<Bitmap> list) {
        Bitmap bitmap1 = list.get(0);
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap1, dst);

        for (int i = 1; i < list.size(); i++) {
            Bitmap bitmap = list.get(i);
            Mat mat = new Mat();
            Utils.bitmapToMat(bitmap, mat);
            //第一个参数：src1，表示进行加权操作的第一个图像对象，即输入图片1；
            //第二个参数：double 型的alpha，表示第一个图像的加权系数，即图片1的融合比例；
            //第三个参数：src2，表示进行加权操作的第二个图像对象，即输入图片2；
            //第四个参数：double型的beta，表示第二个图像的加权系数，即图片2的融合比例。很多情况下，有关系 alpha + beta = 1.0；
            //第五个参数：double型的gamma，表示一个作用到加权和后的图像上的标量，可以理解为加权和后的图像的偏移量；
            //第六个参数：dst，表示两个图像加权和后的图像，尺寸和图像类型与src1和src2相同，即输出图像；
            //第七个参数：输出阵列的可选深度，有默认值 - 1。当两个输入数组具有相同的深度时，这个参数设置为 - 1（默认值），即等同于src1.depth（）
            Core.addWeighted(dst, 0.5, mat, 0.5, 0, dst);
        }
        Utils.matToBitmap(dst, bitmap1);
        return bitmap1;
    }




    public static Mat gaussi(Mat src, int kernel) {
        Mat gaussi = new Mat();
        //Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2RGB);
        Imgproc.GaussianBlur(src, gaussi, new Size(kernel, kernel), 0);
        return gaussi;
    }

    public static Mat adaptiveThreshold(Mat src) {
        Mat threshold = new Mat();
        Imgproc.adaptiveThreshold(src, threshold, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 2);
        //Imgproc.threshold(src, threshold, seekBarValue1, 255, Imgproc.THRESH_BINARY);
        return threshold;
    }

    public static Mat rgb2gray(Mat src) {
        Mat rgb2gray = new Mat();
        Imgproc.cvtColor(src, rgb2gray, Imgproc.COLOR_BGR2GRAY);
        return rgb2gray;
    }

    public static Mat guiyihuaMatByRoi_(Mat cpsrcMat, RotatedRect rotatedRect) {
        //角度
        double angle = rotatedRect.angle;
        if (angle > 45) {
            angle = angle - 90;
        }
        double width = 0;
        double height = 0;
        if (rotatedRect.size.height > rotatedRect.size.width) {
            width = rotatedRect.size.width;
            height = rotatedRect.size.height;
        } else {
            width = rotatedRect.size.height;
            height = rotatedRect.size.width;
        }

        //90度时 是60*310
        if (angle == -90 || angle == 0) {
            //直接裁剪
            return cropPicture(cpsrcMat, rotatedRect, width, height);
        }

        Mat rotated = rotateImage1_(cpsrcMat, angle, rotatedRect.center);

        return cropPicture(rotated, rotatedRect, width, height);
        //执行旋转

    }


    //菌落计数处理


}
