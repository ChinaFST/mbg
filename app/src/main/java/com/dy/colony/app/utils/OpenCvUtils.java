package com.dy.colony.app.utils;

import android.graphics.Bitmap;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;
import com.dy.colony.mvp.model.opencvDetector.CardDetector;
import com.dy.colony.mvp.model.opencvDetector.CardType;
import com.dy.colony.mvp.model.opencvDetector.DataSource;
import com.jess.arms.utils.ArmsUtils;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.opencv.imgproc.Imgproc.MORPH_ELLIPSE;

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


    /**
     * 寻找三个卡条
     *
     * @param bitmap
     * @return
     */
    public static List<Bitmap> getCardBitmap_External(Bitmap bitmap, CardType cardType) {
        List<Bitmap> list = new ArrayList<>();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, dst);
        if (BuildConfig.DEBUG) {

        }

        //动态处理图片
        //List<DataSource> list1 = dynamicProcessing(rgb2gray, Constants.ALPHAXVALUE, Constants.ALPHAYVALUE, Constants.THRESHOLDVALUE, Constants.DEKEENELVALUE);
        CardDetector detector = new CardDetector(dst, cardType);
        List<DataSource> list1 = detector.startSeachCards_External();
        LogUtils.d(list1);
        for (int i = 0; i < list1.size(); i++) {
            DataSource source = list1.get(i);
            ArrayList<RotatedRect> rectArrayList = source.getUsefulRotatedRect();
            for (int i1 = 0; i1 < rectArrayList.size(); i1++) {
                RotatedRect rect = rectArrayList.get(i1);
                Mat mat = guiyihuaMatByRoi_(dst, rect);
                if (null == mat) {
                    continue;
                }
                Bitmap bmp = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(mat, bmp);
                list.add(bmp);
                mat.release();
            }
        }
        dst.release();
        return list;
    }

    public static DataSource processingKeyRect_new(ArrayList<MatOfPoint> src) {

        ArrayList<MatOfPoint> usefulpoint = new ArrayList<>();
        for (int i = 0; i < src.size(); i++) {
            MatOfPoint point = src.get(i);
            if (point.toArray().length > 40) {
                usefulpoint.add(point);
            }

        }
        LogUtils.d(usefulpoint.size());
        ArrayList<RotatedRect> usefulRotatedRect = new ArrayList<>();
        ArrayList<MatOfPoint> usefules = new ArrayList<>();
        List<MatOfPoint> hullList = new ArrayList<MatOfPoint>();
        for (int i = 0; i < usefulpoint.size(); i++) {
            MatOfInt hull = new MatOfInt();
            Imgproc.convexHull(usefulpoint.get(i), hull);
            MatOfPoint point = matOfIntToPoints(usefulpoint.get(i), hull);
            hullList.add(point);

            //MatOfPoint point = usefulpoint.get(i);
            List<Point> points1 = new ArrayList<>(point.toList());
            Point[] a = new Point[points1.size()];
            points1.toArray(a);
            MatOfPoint2f points = new MatOfPoint2f(a);

            RotatedRect rect = Imgproc.minAreaRect(points);

            double width = rect.size.width;
            double height = rect.size.height;
            //宽度限制
            if (width < Constants.MINWIDTH) {
                //LogUtils.d("宽度限制 "+width);
                continue;
            }
            //高度限制
            if (height < Constants.MINHEIGHT) {
                //LogUtils.d("高度限制 "+height);
                continue;
            }
            //宽高比例限制
            double v = width / height;
            if (height > width) {
                v = height / width;
            }
            if (v < 3 || v > 6) {
                continue;
            }
            //角度限制
            if (Constants.LIMITANGLEA < rect.angle && rect.angle < Constants.LIMITANGLEB) {
                continue;
            }


            usefulRotatedRect.add(rect);
            usefules.add(point);

        }

        DataSource source = new DataSource();
        source.setUsefules(usefules);
        Map<Double, RotatedRect> map = new TreeMap<>(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o1.compareTo(o2);
            }
        });
        for (int i = 0; i < usefulRotatedRect.size(); i++) {
            RotatedRect rect = usefulRotatedRect.get(i);
            double x = rect.center.x;
            map.put(x, rect);
        }
        usefulRotatedRect.clear();
        for (Double aDouble : map.keySet()) {
            usefulRotatedRect.add(map.get(aDouble));
        }
        source.setUsefulRotatedRect(usefulRotatedRect);
        return source;
    }

    /**
     * 需要找出三个通道
     *
     * @param src
     * @return
     */
    public static DataSource processingKeyRect(ArrayList<MatOfPoint> src, CardType cardType, int matwidth, int matheight) {
        int limitwidth = 0;
        int limitheight = 0;
        int limitProportion = 0;
        int limitangle = 0;
        int limitLocation = 0;

        ArrayList<MatOfPoint> usefulpoint = new ArrayList<>();
        for (int i = 0; i < src.size(); i++) {
            MatOfPoint point = src.get(i);
            if (point.toArray().length > 40) {
                usefulpoint.add(point);
            }

        }
        LogUtils.d(src.size() + "--->" + usefulpoint.size());
        ArrayList<RotatedRect> rotatedRect = new ArrayList<>();
        ArrayList<RotatedRect> usefulRotatedRect = new ArrayList<>();
        ArrayList<MatOfPoint> usefules = new ArrayList<>();
        for (int i = 0; i < usefulpoint.size(); i++) {
            MatOfPoint point = usefulpoint.get(i);
            List<Point> points1 = new ArrayList<>(point.toList());
            Point[] a = new Point[points1.size()];
            points1.toArray(a);
            MatOfPoint2f points = new MatOfPoint2f(a);

            RotatedRect rect = Imgproc.minAreaRect(points);
            rotatedRect.add(rect);
            double width = rect.size.height;
            double height = rect.size.width;
            if (rect.size.width > rect.size.height) {
                width = rect.size.width;
                height = rect.size.height;
            }

            rect.size.height = height;
            rect.size.width = width;

            //宽度限制
            if (width < Constants.MINWIDTH) {
                limitwidth++;
                //LogUtils.d("宽度限制 "+width);
                continue;
            }
            //高度限制
            if (height < Constants.MINHEIGHT) {
                limitheight++;
                //LogUtils.d("高度限制 "+height);
                continue;
            }

            //宽高比例限制
            double v = width / height;
            if (v < 3 || v > 6) {
                limitProportion++;
                //LogUtils.d("宽高比例限制 "+v);
                continue;
            }

            //角度限制
            if (Constants.LIMITANGLEA < rect.angle && rect.angle < Constants.LIMITANGLEB) {
                limitangle++;
                //LogUtils.d("角度限制 "+rect.angle);
                continue;
            }
            //单卡的卡条必须是居中，宽分成三份，必须处于中间一份上
            if (cardType == CardType.ONE) {
                if (matwidth / 3 > rect.center.x || rect.center.x > matwidth / 3 * 2) {
                    limitLocation++;
                    LogUtils.d("单卡位置不对 " + rect.center + " " + matwidth / 3 + " " + matwidth / 3 * 2);
                    continue;
                }
            }
            //LogUtils.d(rect);
            usefulRotatedRect.add(rect);
            usefules.add(point);

            if (cardType == CardType.ONE) {
                LogUtils.d(usefulRotatedRect);
                if (usefulRotatedRect.size() == 1) {
                    break;
                }
            }

        }
        LogUtils.d(rotatedRect);
        LogUtils.d(
                "宽度限制:" + limitwidth + "\r\n" +
                        "高度限制:" + limitheight + "\r\n" +
                        "宽高比例限制:" + limitProportion + "\r\n" +
                        "角度限制:" + limitangle + "\r\n" +
                        "单卡位置不对:" + limitLocation + "\r\n"
        );

        if (cardType == CardType.ONE) {
            if (usefulRotatedRect.size() != 1) {
                LogUtils.d("返回位置 识别数量不对");
                return null;
            }
        } else if (cardType == CardType.THREE) {
            if (usefulRotatedRect.size() != 3) {
                LogUtils.d("返回位置 识别数量不对");
                return null;
            }
        } else if (cardType == CardType.FOUR) {
            if (usefulRotatedRect.size() != 4) {
                LogUtils.d("返回位置 识别数量不对");
                return null;
            }
        }


        DataSource source = new DataSource();
        source.setUsefules(usefules);
        Map<Double, RotatedRect> map = new TreeMap<>(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o1.compareTo(o2);
            }
        });
        source.setUsefulRotatedRect(usefulRotatedRect);
        if (cardType == CardType.THREE) {
            //排序
            for (int i = 0; i < usefulRotatedRect.size(); i++) {
                RotatedRect rect = usefulRotatedRect.get(i);
                double x = rect.center.x;
                map.put(x, rect);
            }
            usefulRotatedRect.clear();
            for (Double aDouble : map.keySet()) {
                usefulRotatedRect.add(map.get(aDouble));
            }
            source.setUsefulRotatedRect(usefulRotatedRect);
            LogUtils.d(usefulRotatedRect);
            RotatedRect rect1 = usefulRotatedRect.get(0);
            RotatedRect rect2 = usefulRotatedRect.get(1);
            RotatedRect rect3 = usefulRotatedRect.get(2);
            double abs = Math.abs(Math.abs(rect2.center.x - rect1.center.x) - Math.abs(rect3.center.x - rect2.center.x));

            if (abs > Constants.LIMITCHANNL) {
                LogUtils.d("返回位置");
                return null;
            }
            double width1 = rect1.size.width;
            double width2 = rect2.size.width;
            double width3 = rect3.size.width;

            double height1 = rect1.size.height;
            double height2 = rect2.size.height;
            double height3 = rect3.size.height;

            double angle1 = rect1.angle;
            double angle2 = rect1.angle;
            double angle3 = rect1.angle;

            double minw;
            double averageValuew;
            double abs1 = Math.abs(width1 - width2);
            minw = abs1;
            averageValuew = (width1 + width2) / 2;
            double abs2 = Math.abs(width2 - width3);
            if (abs2 < minw) {
                minw = abs2;
                averageValuew = (width2 + width3) / 2;
            }
            double abs3 = Math.abs(width3 - width1);
            if (abs3 < minw) {
                minw = abs3;
                averageValuew = (width3 + width1) / 2;
            }
            double minh;
            double averageValueh;
            double abs4 = Math.abs(height1 - height2);
            minh = abs4;
            averageValueh = (height1 + height2) / 2;
            double abs5 = Math.abs(height2 - height3);
            if (abs5 < minh) {
                minh = abs3;
                averageValueh = (height2 + height3) / 2;
            }
            double abs6 = Math.abs(height3 - height1);
            if (abs6 < minh) {
                minh = abs6;
                averageValueh = (height3 + height1) / 2;
            }

            double mina;
            double averageValuea;
            double abs7 = Math.abs(angle1 - angle2);
            mina = abs7;
            averageValuea = (angle1 + angle2) / 2;

            double abs8 = Math.abs(angle2 - angle3);
            if (abs8 < mina) {
                mina = abs8;
                averageValuea = (angle2 + angle3) / 2;
            }
            double abs9 = Math.abs(angle3 - angle1);
            if (abs9 < mina) {
                mina = abs9;
                averageValuea = (angle3 + angle1) / 2;
            }


            if (minw > Constants.MINWDIFFERENCEVALUE) {
                LogUtils.d("返回位置 " + minw);
                return null;
            }
            if (minh > Constants.MINHDIFFERENCEVALUE) {
                LogUtils.d("返回位置 " + minh);
                return null;
            }

            rect1.size.width = averageValuew;
            rect2.size.width = averageValuew;
            rect3.size.width = averageValuew;

            rect1.size.height = averageValueh;
            rect2.size.height = averageValueh;
            rect3.size.height = averageValueh;

            rect1.angle = averageValuea;
            rect2.angle = averageValuea;
            rect3.angle = averageValuea;

            usefulRotatedRect.clear();
            usefulRotatedRect.add(rect1);
            usefulRotatedRect.add(rect2);
            usefulRotatedRect.add(rect3);

        } else if (cardType == CardType.FOUR) {
            //排序
            for (int i = 0; i < usefulRotatedRect.size(); i++) {
                RotatedRect rect = usefulRotatedRect.get(i);
                double x = rect.center.x;
                map.put(x, rect);
            }
            usefulRotatedRect.clear();
            for (Double aDouble : map.keySet()) {
                usefulRotatedRect.add(map.get(aDouble));
            }
            source.setUsefulRotatedRect(usefulRotatedRect);
            LogUtils.d(usefulRotatedRect);
            RotatedRect rect1 = usefulRotatedRect.get(0);
            RotatedRect rect2 = usefulRotatedRect.get(1);
            RotatedRect rect3 = usefulRotatedRect.get(2);
            RotatedRect rect4 = usefulRotatedRect.get(3);
            double abs = Math.abs(
                    Math.abs(
                            rect2.center.x - rect1.center.x
                    )
                            -
                            Math.abs(
                                    rect3.center.x - rect2.center.x
                            )
            );

            if (abs > Constants.LIMITCHANNL) {
                LogUtils.d("返回位置");
                return null;
            }
            double width1 = rect1.size.width;
            double width2 = rect2.size.width;
            double width3 = rect3.size.width;
            double width4 = rect4.size.width;

            double height1 = rect1.size.height;
            double height2 = rect2.size.height;
            double height3 = rect3.size.height;
            double height4 = rect4.size.height;

            double angle1 = rect1.angle;
            double angle2 = rect2.angle;
            double angle3 = rect3.angle;
            double angle4 = rect4.angle;

            double minw;
            double averageValuew;
            double abs1 = Math.abs(width1 - width2);
            minw = abs1;
            averageValuew = (width1 + width2) / 2;
            double abs2 = Math.abs(width2 - width3);
            if (abs2 < minw) {
                minw = abs2;
                averageValuew = (width2 + width3) / 2;
            }
            double abs3 = Math.abs(width3 - width1);
            if (abs3 < minw) {
                minw = abs3;
                averageValuew = (width3 + width1) / 2;
            }
            double minh;
            double averageValueh;
            double abs4 = Math.abs(height1 - height2);
            minh = abs4;
            averageValueh = (height1 + height2) / 2;
            double abs5 = Math.abs(height2 - height3);
            if (abs5 < minh) {
                minh = abs3;
                averageValueh = (height2 + height3) / 2;
            }
            double abs6 = Math.abs(height3 - height1);
            if (abs6 < minh) {
                minh = abs6;
                averageValueh = (height3 + height1) / 2;
            }

            double mina;
            double averageValuea;
            double abs7 = Math.abs(angle1 - angle2);
            mina = abs7;
            averageValuea = (angle1 + angle2) / 2;

            double abs8 = Math.abs(angle2 - angle3);
            if (abs8 < mina) {
                mina = abs8;
                averageValuea = (angle2 + angle3) / 2;
            }
            double abs9 = Math.abs(angle3 - angle1);
            if (abs9 < mina) {
                mina = abs9;
                averageValuea = (angle3 + angle1) / 2;
            }


            if (minw > Constants.MINWDIFFERENCEVALUE) {
                LogUtils.d("返回位置 " + minw);
                return null;
            }
            if (minh > Constants.MINHDIFFERENCEVALUE) {
                LogUtils.d("返回位置 " + minh);
                return null;
            }

            rect1.size.width = averageValuew;
            rect2.size.width = averageValuew;
            rect3.size.width = averageValuew;
            rect4.size.width = averageValuew;

            rect1.size.height = averageValueh;
            rect2.size.height = averageValueh;
            rect3.size.height = averageValueh;
            rect4.size.height = averageValueh;

            rect1.angle = averageValuea;
            rect2.angle = averageValuea;
            rect3.angle = averageValuea;
            rect4.angle = averageValuea;

            usefulRotatedRect.clear();
            usefulRotatedRect.add(rect1);
            usefulRotatedRect.add(rect2);
            usefulRotatedRect.add(rect3);
            usefulRotatedRect.add(rect4);

        }


        return source;
    }

    public static Mat psMap(Mat dst, int ksize) {
        try{
            Mat gaussi1 = OpenCvUtils.gaussi(dst, ksize==-1?3:7);

            Mat rgb2gray = OpenCvUtils.rgb2gray(gaussi1);

            // Mat medianBlur1 = new Mat();
            //Imgproc.medianBlur(rgb2gray, medianBlur1, 5);

            Mat adaptiveThresh = new Mat();
            Imgproc.adaptiveThreshold(rgb2gray, adaptiveThresh, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 9, 2);


            Mat medianBlur = new Mat();
            Imgproc.medianBlur(adaptiveThresh, medianBlur, ksize==-1?3:ksize);


            Mat laplacian = new Mat();
            Imgproc.Laplacian(medianBlur, laplacian, -1, ksize==-1?3:ksize);


            Mat structuringElement = Imgproc.getStructuringElement(MORPH_ELLIPSE, new org.opencv.core.Size(ksize==-1?13:11, ksize==-1?13:11));
            Mat dilate = new Mat();
            Imgproc.dilate(laplacian, dilate, structuringElement);
            if (ksize==-1){
                return dilate;
            }

            Mat keenel9 = Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new org.opencv.core.Size(7, 7));
            Mat erode = new Mat();
            Imgproc.erode(dilate, erode, keenel9);  // 腐蚀 黑色扩张

            return erode;
        }catch (Exception e){
            ArmsUtils.snackbarText(e.toString());
        }
        return dst;
    }

}
