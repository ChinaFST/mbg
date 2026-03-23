package com.dy.colony.app.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.BuildConfig;
import com.dy.colony.Constants;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Created by wangzhenxiong on 2019-11-08.
 */
public class PictureToolUtils {
    /**
     * opencv对比两张图片的相似度
     *
     * @param img_1 图片1
     * @param img_2 图片2
     * @return 相似度
     */
    public static double compare_image(Bitmap img_1, Bitmap img_2) {

        Mat mat_1 = new Mat();
        Utils.bitmapToMat(img_1, mat_1);
        Mat mat_2 = new Mat();
        Utils.bitmapToMat(img_2, mat_2);
        Mat mat_3 = new Mat();
        Imgproc.cvtColor(mat_1, mat_3, Imgproc.COLOR_RGB2GRAY);
        Mat mat_4 = new Mat();
        Imgproc.cvtColor(mat_2, mat_4, Imgproc.COLOR_RGB2GRAY);
        Mat hist_1 = new Mat();
        Mat hist_2 = new Mat();

        //颜色范围
        MatOfFloat ranges = new MatOfFloat(0f, 255f);
        //直方图大小， 越大匹配越精确 (越慢)
        MatOfInt histSize = new MatOfInt(2000);

        Mat mask = new Mat();
        Imgproc.calcHist(Arrays.asList(mat_3), new MatOfInt(0), mask, hist_1, histSize, ranges);
        Mat mask1 = new Mat();
        Imgproc.calcHist(Arrays.asList(mat_4), new MatOfInt(0), mask1, hist_2, histSize, ranges);

        // CORREL 相关系数
        double res = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);
        mat_1.release();
        mat_2.release();
        mat_3.release();
        mat_4.release();
        hist_1.release();
        hist_2.release();
        ranges.release();
        histSize.release();
        mask.release();
        mask1.release();
        return res;
    }



    /**
     * opencv对比两张图片的相似度
     *
     * @param img_1 图片1
     * @param img_2 图片2
     * @return 相似度
     */
    public static double compare_imagefilepath(String img_1, Bitmap img_2) {
        Mat mat_1 = Imgcodecs.imread(img_1);

        Mat mat_2 = new Mat();
        Utils.bitmapToMat(img_2, mat_2);
        Mat mat_3 = new Mat();
        Imgproc.cvtColor(mat_1, mat_3, Imgproc.COLOR_RGB2GRAY);
        Mat mat_4 = new Mat();
        Imgproc.cvtColor(mat_2, mat_4, Imgproc.COLOR_RGB2GRAY);
        Mat hist_1 = new Mat();
        Mat hist_2 = new Mat();

        //颜色范围
        MatOfFloat ranges = new MatOfFloat(0f, 255f);
        //直方图大小， 越大匹配越精确 (越慢)
        MatOfInt histSize = new MatOfInt(2000);

        Mat mask = new Mat();
        Imgproc.calcHist(Arrays.asList(mat_3), new MatOfInt(0), mask, hist_1, histSize, ranges);
        Mat mask1 = new Mat();
        Imgproc.calcHist(Arrays.asList(mat_4), new MatOfInt(0), mask1, hist_2, histSize, ranges);

        // CORREL 相关系数
        double res = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);
        mat_1.release();
        mat_2.release();
        mat_3.release();
        mat_4.release();
        hist_1.release();
        hist_2.release();
        ranges.release();
        histSize.release();
        mask.release();
        mask1.release();
        return res;
    }

    /**
     * @param arr
     * @param width
     * @param height
     * @return 返回原始数据对应的绿色色值数组
     */
    public static int[] getGreenFromByte(byte[] arr, int width, int height) {
        int[] date = new int[height];
        for (int i = 0; i < height; i++) {
            int temp = 0;
            for (int j = width / 2 - 4; j < width / 2 + 4; j++) {
                int temp16 = (i * width + j) * 2;

                int rgb = (arr[temp16] & 0x0FF) + (arr[temp16 + 1] & 0x0FF) * 256;
                temp = temp + byteToInt((byte) (((rgb >> 5) & 0x3F) << 2));
            }
            date[height - i - 1] = temp * 10;

        }

        return date;
    }


    /**
     * @param arr 原始数据
     * @return 返回点和面积的二位数组
     */
    public static double[][] getLineArea(double[] arr) {

        double[][] result = new double[2][2];
        ArrayList<Double> list = new ArrayList<>();
        int point = 15;
        for (int i = 0; i < arr.length - point; i++) {
            double area = 0;
            double avg = (arr[i + point - 1] - arr[i]) / (point - 2);
            double tem = 0;
            for (int j = 0; j <= point - 2; j++) {
                tem = arr[i] + avg * j;
                area = area + tem - arr[i + j];
            }
            list.add(area);
        }

        for (int i = 0; i < list.size() / 2; i++) {
            if (list.get(i) > result[0][1]) {
                result[0][1] = list.get(i);
                result[0][0] = i;
            }
            if (list.get(list.size() / 2 + i) > result[1][1]) {
                result[1][1] = list.get(list.size() / 2 + i);
                result[1][0] = list.size() / 2 + i;
            }

        }


        return result;
    }


    public static boolean isHaveCard(Bitmap bmp, int gallery) {
        Bitmap bitmap = FileUtils.getControlBitmap(gallery);
        if (null == bitmap) {
            return true;
        }
        double v = compare_image(bitmap, bmp);
        LogUtils.d(v);
        bitmap.recycle();
        if (v > 0.72) {
            return false;
        }
        return true;
    }





    public static List<Bitmap> interceptBitmaps(Bitmap bmp) {
        int width = bmp.getWidth(); //180
        int height = bmp.getHeight(); //180
        int drowrectheight = 20;
        int drowrectwidth = 20;
        List<Bitmap> bmplist = new ArrayList<>();
        //左上角
        bmplist.add(cropBitmap(bmp, drowrectwidth, drowrectheight, drowrectwidth, drowrectheight));
        //左下角
        bmplist.add(cropBitmap(bmp, drowrectwidth, height - drowrectheight - drowrectheight, drowrectwidth, drowrectheight));
        //右上角
        bmplist.add(cropBitmap(bmp, width - drowrectwidth - drowrectwidth, drowrectheight, drowrectwidth, drowrectheight));
        //右下角
        bmplist.add(cropBitmap(bmp, width - drowrectwidth - drowrectwidth, height - drowrectheight - drowrectheight, drowrectwidth, drowrectheight));
        return bmplist;
    }

    /**
     * 画出四个角的框
     *
     * @param bmp 原始图片
     * @return
     */
    public static Bitmap drawRectFourcorners(Bitmap bmp) {
        Bitmap bitmap = Bitmap.createBitmap(bmp);
        Canvas cvs = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);//不填充

        int width = bitmap.getWidth(); //180
        int height = bitmap.getHeight(); //180
        int drowrectheight = 20;
        int drowrectwidth = 20;
        //左上角
        cvs.drawRect(drowrectwidth, drowrectheight, drowrectwidth + drowrectwidth, drowrectheight + drowrectheight, paint);
        //左下角
        cvs.drawRect(drowrectwidth, height - drowrectheight - drowrectheight, drowrectwidth + drowrectwidth, height - drowrectheight, paint);
        //右上角
        cvs.drawRect(width - drowrectwidth - drowrectwidth, drowrectheight, width - drowrectwidth, drowrectheight + drowrectheight, paint);
        //右下角
        cvs.drawRect(width - drowrectwidth - drowrectwidth, height - drowrectheight - drowrectheight, width - drowrectwidth, height - drowrectheight, paint);

        return bitmap;

    }

    /**
     * @param arr     原始数据
     * @param width_  图片宽度
     * @param height_ 图片高度
     * @return 生成的图片
     * @Describe 传入摄像头生成的原始数据, 返回对应的Bitmap 外接三联卡，廋肉精三项
     */
    public static List<Bitmap> byteToBitMapExternal(byte[] arr, int width_, int height_) {
        byte[] third = new byte[width_ * height_ * 3];
        for (int i = 0; i < height_; i++) {
            for (int j = 0; j < width_; j++) {
                int temp16 = (i * width_ + j) * 2;
                int tem24 = (i * width_ + j) * 3;
                int rgb = (arr[temp16] & 0xFF) + (arr[temp16 + 1] & 0xFF) * 256;

                int red = ((rgb >> 11) & 0x1F) << 3;
                int green = ((rgb >> 5) & 0x3F) << 2;
                int blue = (rgb & 0x1F) << 3;

                third[tem24] = (byte) red;
                third[tem24 + 1] = (byte) green;
                third[tem24 + 2] = (byte) blue;

            }

        }

        //  Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        List<Bitmap> bmplist = new ArrayList<>();
        Bitmap bmp = Bitmap.createBitmap(width_, height_, Bitmap.Config.RGB_565);
        bmp.setPixels(byteToColor(third), 0, width_, 0, 0, width_, height_);
        bmp = rotateBitmap(bmp, 90); //旋转90度
        Bitmap bitmap = Bitmap.createBitmap(bmp);


        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);
        Mat dst = new Mat();
        //高斯滤波 ,降噪
        //Imgproc.GaussianBlur(src,dst,new Size(3,3),3,3);

        //Mat kernel = new Mat();

        //Imgproc.filter2D(src,dst,-1, kernel);
        //转 灰度图
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_RGB2GRAY);

        Mat keenel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.dilate(dst, dst, keenel); // 膨胀
        Imgproc.erode(dst, dst, keenel);  // 腐蚀

        //边缘检测
        //Imgproc.Sobel(dst,dst,2,1,1);
        //二值化二值化必须是用灰度图，不能用彩色图片
        //Imgproc.threshold(dst, dst,160,255,Imgproc.THRESH_OTSU);
        //自适应二值化
        Imgproc.adaptiveThreshold(dst, dst, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 3, 0);
        //二值化二值化必须是用灰度图，不能用彩色图片
        //Imgproc.threshold(dst, dst,160,255,Imgproc.THRESH_OTSU);

        //Mat keenel=Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(11,11));
        //Imgproc.morphologyEx(dst,dst,Imgproc.MORPH_CLOSE,keenel);
        //Imgproc.morphologyEx(dst,dst,Imgproc.MORPH_OPEN,keenel);
        //颜色转化 先要把rgb转hsv
        //Imgproc.cvtColor(src,src,Imgproc.COLOR_RGB2HSV);

        //边缘检测
        //Imgproc.Canny(dst,dst,200,255);

        //轮廓寻找
        ArrayList<MatOfPoint> es = new ArrayList<>();
        ArrayList<MatOfPoint> userfules = new ArrayList<>();
        Mat hierarchy = new Mat();
        //寻找轮廓必须用二值化图片
        Imgproc.findContours(dst, es, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        LogUtils.d(es.size());

        for (int i = 0; i < es.size(); i++) {
            MatOfPoint point = es.get(i);
            if (point.total() > 200) {
                userfules.add(point);


            }
        }


        //绘制轮廓
        Imgproc.drawContours(src, userfules, -1, new Scalar(0, 0, 255), 4);
       /* Mat dst1 = new Mat();
        Imgproc.bilateralFilter(dst,dst1,2,100,10);*/


        Utils.matToBitmap(dst, bitmap);
        bmplist.add(bitmap);

        Canvas cvs = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);//不填充

        int width = bitmap.getWidth(); //180
        int height = bitmap.getHeight(); //180


        int drowrectheight = Constants.drowrectheight;
        int drowrectwidth = Constants.drowrectwidth;
        int cardSpacing = Constants.cardSpacing;

        int lift = width / 2 - drowrectwidth / 2;
        int right = width / 2 + drowrectwidth / 2;
        int lift_ = (width) / 2;


        int top1 = height / 2 - drowrectheight / 2 - cardSpacing - drowrectheight;
        int bottom1 = height / 2 - drowrectheight / 2 - cardSpacing;
        Rect rect1 = new Rect(lift, top1, right, bottom1);
        //cvs.drawRect(rect1, paint);
        Rect rect1_ = new Rect(lift_, top1, lift_ + 1, bottom1);
        //cvs.drawRect(rect1_, paint);

        int top2 = height / 2 - drowrectheight / 2;
        int bottom2 = height / 2 + drowrectheight / 2;
        Rect rect2 = new Rect(lift, top2, right, bottom2);
        //cvs.drawRect(rect2, paint);
        Rect rect2_ = new Rect(lift_, top2, lift_ + 1, bottom2);
        //cvs.drawRect(rect2_, paint);

        int top3 = height / 2 + drowrectheight / 2 + cardSpacing;
        int bottom3 = height / 2 + drowrectheight / 2 + cardSpacing + drowrectheight;
        Rect rect3 = new Rect(lift, top3, right, bottom3);
        //cvs.drawRect(rect3, paint);
        Rect rect3_ = new Rect(lift_, top3, lift_ + 1, bottom3);
        //cvs.drawRect(rect3_, paint);


        bmplist.add(cropBitmap(bmp, lift, top1 + 1, right - lift, drowrectheight - 2));
        bmplist.add(cropBitmap(bmp, lift, top2 + 1, right - lift, drowrectheight - 2));
        bmplist.add(cropBitmap(bmp, lift, top3 + 1, right - lift, drowrectheight - 2));
        //bmp = scaleBitmap(bmp, 460, 254); //比例缩放
        //bmplist.add(cropBitmap(bmp, 20, top2 + 1, width1 - 40 - 2, 20 - 2));
        //bmplist.add(cropBitmap(bmp, 20, top3 + 1, width1 - 40 - 2, 20 - 2));
        //bmp = handleImageEffect(bmp, 5.3f, 5.5f, 1.1f); //亮度，饱和度调节
        return bmplist;
    }


    /**
     * @param arr     原始数据
     * @param width_  图片宽度
     * @param height_ 图片高度
     * @return 生成的图片
     * @Describe 传入摄像头生成的原始数据, 返回对应的Bitmap 外接三联卡，廋肉精三项
     */
   /* public static List<Bitmap> byteToBitMapExternal(byte[] arr, int width_, int height_) {
        byte[] third = new byte[width_ * height_ * 3];
        for (int i = 0; i < height_; i++) {
            for (int j = 0; j < width_; j++) {
                int temp16 = (i * width_ + j) * 2;
                int tem24 = (i * width_ + j) * 3;
                int rgb = (arr[temp16] & 0xFF) + (arr[temp16 + 1] & 0xFF) * 256;

                int red = ((rgb >> 11) & 0x1F) << 3;
                int green = ((rgb >> 5) & 0x3F) << 2;
                int blue = (rgb & 0x1F) << 3;

                third[tem24] = (byte) red;
                third[tem24 + 1] = (byte) green;
                third[tem24 + 2] = (byte) blue;

            }

        }

        //  Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        List<Bitmap> bmplist = new ArrayList<>();
        Bitmap bmp = Bitmap.createBitmap(width_, height_, Bitmap.Config.RGB_565);
        bmp.setPixels(byteToColor(third), 0, width_, 0, 0, width_, height_);
        bmp = rotateBitmap(bmp, 90); //旋转90度


        Bitmap bitmap = Bitmap.createBitmap(bmp);
        Canvas cvs = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);//不填充

        int width = bitmap.getWidth(); //180
        int height = bitmap.getHeight(); //180


        int drowrectheight = Constants.drowrectheight;
        int drowrectwidth = Constants.drowrectwidth;
        int cardSpacing = Constants.cardSpacing;

        int lift = width / 2 - drowrectwidth / 2;
        int right = width / 2 + drowrectwidth / 2;
        int lift_ = (width) / 2;


        int top1 = height / 2 - drowrectheight / 2 - cardSpacing - drowrectheight;
        int bottom1 = height / 2 - drowrectheight / 2 - cardSpacing;
        Rect rect1 = new Rect(lift, top1, right, bottom1);
        cvs.drawRect(rect1, paint);
        Rect rect1_ = new Rect(lift_, top1, lift_ + 1, bottom1);
        cvs.drawRect(rect1_, paint);

        int top2 = height / 2 - drowrectheight / 2;
        int bottom2 = height / 2 + drowrectheight / 2;
        Rect rect2 = new Rect(lift, top2, right, bottom2);
        cvs.drawRect(rect2, paint);
        Rect rect2_ = new Rect(lift_, top2, lift_ + 1, bottom2);
        cvs.drawRect(rect2_, paint);

        int top3 = height / 2 + drowrectheight / 2 + cardSpacing;
        int bottom3 = height / 2 + drowrectheight / 2 + cardSpacing + drowrectheight;
        Rect rect3 = new Rect(lift, top3, right, bottom3);
        cvs.drawRect(rect3, paint);
        Rect rect3_ = new Rect(lift_, top3, lift_ + 1, bottom3);
        cvs.drawRect(rect3_, paint);


        bmplist.add(bitmap);
        bmplist.add(cropBitmap(bmp, lift, top1 + 1, right - lift, drowrectheight - 2));
        bmplist.add(cropBitmap(bmp, lift, top2 + 1, right - lift, drowrectheight - 2));
        bmplist.add(cropBitmap(bmp, lift, top3 + 1, right - lift, drowrectheight - 2));
        //bmp = scaleBitmap(bmp, 460, 254); //比例缩放
        //bmplist.add(cropBitmap(bmp, 20, top2 + 1, width1 - 40 - 2, 20 - 2));
        //bmplist.add(cropBitmap(bmp, 20, top3 + 1, width1 - 40 - 2, 20 - 2));
        //bmp = handleImageEffect(bmp, 5.3f, 5.5f, 1.1f); //亮度，饱和度调节
        return bmplist;
    }*/

    /**
     * @param arr    原始数据
     * @param width  图片款低
     * @param height 图片高度
     * @return 生成的图片
     * @Describe 传入摄像头生成的原始数据, 返回对应的Bitmap
     */
    public static List<Bitmap> byteToBitMap(byte[] arr, int width, int height) {
        byte[] third = new byte[width * height * 3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int temp16 = (i * width + j) * 2;
                int tem24 = (i * width + j) * 3;
                int rgb = (arr[temp16] & 0xFF) + (arr[temp16 + 1] & 0xFF) * 256;

                int red = ((rgb >> 11) & 0x1F) << 3;
                int green = ((rgb >> 5) & 0x3F) << 2;
                int blue = (rgb & 0x1F) << 3;

                third[tem24] = (byte) red;
                third[tem24 + 1] = (byte) green;
                third[tem24 + 2] = (byte) blue;

            }

        }

        //  Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        List<Bitmap> bmplist = new ArrayList<>();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Bitmap bitmap1 = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        bmp.setPixels(byteToColor(third), 0, width, 0, 0, width, height);
        bitmap1.setPixels(byteToColor(third), 0, width, 0, 0, width, height);
        bmp = rotateBitmap(bmp, 90); //旋转90度
        bitmap1 = rotateBitmap(bitmap1, 90); //旋转90度
        Bitmap bitmap = Bitmap.createBitmap(bmp);
        Canvas cvs = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);//不填充
        int width1 = bitmap.getWidth();
        int height1 = bitmap.getHeight();
        int top = height1 / 2 - 10;
        int right = width1 - 20;
        cvs.drawRect(20, top, right, top + 20, paint);
        cvs.drawRect((height) / 2, top, (height) / 2 + 1, top + 20, paint);
        bmplist.add(bitmap);
        bmp = cropBitmap(bmp, 20, top + 1, width1 - 40 - 2, 20 - 2); //裁剪  是容器的宽高
        //bmp = scaleBitmap(bmp, 460, 254); //比例缩放
        bmplist.add(bmp);
        //bmp = handleImageEffect(bmp, 5.3f, 5.5f, 1.1f); //亮度，饱和度调节
        bmplist.add(bitmap1);
        return bmplist;
    }

    public static boolean checkCard(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            LogUtils.d("can't use bitmap");
            return false;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bitmap1 = cropBitmap(bitmap, 0, 20, width, 20);//裁剪  是容器的宽高
        Bitmap bitmap2 = cropBitmap(bitmap, 0, height - 40, width, 20);//裁剪  是容器的宽高
        //FileUtils.saveBitmaplevel1(bitmap1,"shangmian");
        //FileUtils.saveBitmaplevel1(bitmap2,"xiamian");
        ArrayList<Bitmap> objects = new ArrayList<>();
        objects.add(bitmap1);
        objects.add(bitmap2);
        Bitmap bitmap3 = BitmapUtils.mergeBitmap(objects);
        List<List<Float>> list = bitmap2RGB_list(bitmap3);
        if (BuildConfig.DEBUG) {
            FileUtils.saveBitmaplevel1(bitmap3, "bebing");
        }
        LogUtils.d(list);
        List<Float> floats = list.get(3);
        float a = 0;
        for (int i1 = 0; i1 < floats.size(); i1++) {
            a = a + floats.get(i1);
        }
        a = a / floats.size();
        LogUtils.d(a);
        if (a > 100) {
            return true;
        }
        return false;
    }

    /**
     * @param bm         图像 （不可修改）
     * @param hue        色相
     * @param saturation 饱和度
     * @param lum        亮度
     * @return
     */
    public static Bitmap handleImageEffect(Bitmap bm, float hue, float saturation, float lum) {

        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmp);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, 0.1f); // R
        hueMatrix.setRotate(1, hue); // G
        hueMatrix.setRotate(2, 0.1f); // B

        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);

        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum, lum, lum, 1);

        //融合
        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);

        return bmp;
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @param x
     * @param y
     * @param ww
     * @param hh
     */
    public static Bitmap cropBitmap(Bitmap bitmap, int x, int y, int ww, int hh) {

        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, x, y, ww, hh, null, false);
        return bitmap1;
    }

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ww
     * @param hh
     * @param origin 比例  @return 新的bitmap
     */
    private static Bitmap scaleBitmap(Bitmap origin, int ww, int hh) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ww / width, hh / height);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * @param data 原始byte类型的图片数据
     * @return 可以被Bitmap构造的 int数组
     */
    @NotNull
    private static int[] byteToColor(byte[] data) {
        int size = data.length;
        if (size == 0) {
            return null;
        }

        int arg = 0;
        if (size % 3 != 0) {
            arg = 1;
        }

        int[] color = new int[size / 3 + arg];
        int red, green, blue;

        if (arg == 0) {
            for (int i = 0; i < color.length; ++i) {
                red = byteToInt(data[i * 3]);
                green = byteToInt(data[i * 3 + 1]);
                blue = byteToInt(data[i * 3 + 2]);

                color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
            }
        } else {
            for (int i = 0; i < color.length - 1; ++i) {
                red = byteToInt(data[i * 3]);
                green = byteToInt(data[i * 3 + 1]);
                blue = byteToInt(data[i * 3 + 2]);
                color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
            }

            color[color.length - 1] = 0xFF000000;
        }

        return color;
    }

    private static int byteToInt(byte data) {

        int heightBit = (int) ((data >> 4) & 0x0F);
        int lowBit = (int) (0x0F & data);
        return heightBit * 16 + lowBit;
    }

    public static String getRGBvalue(Bitmap bitmap) {
        int width = bitmap.getWidth(); //300
        int height = bitmap.getHeight(); //80
        float[] list_r = new float[width * height];
        float[] list_g = new float[width * height];
        float[] list_b = new float[width * height];
        int m = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = bitmap.getPixel(i, j);
                list_r[m] = Color.red(pixel);
                list_g[m] = Color.green(pixel);
                list_b[m] = Color.blue(pixel);
                m++;
            }
        }
        float average = getAverage(list_r);
        float average1 = getAverage(list_g);
        float average2 = getAverage(list_b);
        return "R: " + NumberUtils.three(average) + "\r\n" + "G: " + NumberUtils.three(average1) + "\r\n" + "B: " + NumberUtils.three(average2) + "\r\n" + "Gray: " + NumberUtils.three(average * 0.3 + average1 * 0.59 + average2 * 0.11) + "";
    }


    public interface DateCallBack {
        void CallBack(int channel, byte[] arr, int width, int height, int count, int errCount);

        void ErrCallBack(int channel, String errInfo);
    }

    /**
     * @方法描述 Bitmap转RGB
     */
    public static List<float[]> bitmap2RGB_arry(Bitmap bitmap) {
        int width = bitmap.getWidth(); //300
        int height = bitmap.getHeight(); //80

        List<float[]> list = new ArrayList<>();
        float[] list_r = new float[width - 10];
        float[] list_g = new float[width - 10];
        float[] list_b = new float[width - 10];
        //bitmap.getPixels();                  // j=80 i=300
        for (int i = 0; i < width - 10; i++) { // x                       //258个面 的G值 的平均值
            float[] r = new float[10 * height];
            float[] g = new float[10 * height];
            float[] b = new float[10 * height];
            int m = 0;
            for (int j = 0; j < height; j++) {  // y  //循环20次 竖线
                for (int k = i; k < i + 10; k++) {   //循环20次 横线
                    int pixel = bitmap.getPixel(k, j); //xy
                    r[m] = Color.red(pixel);
                    g[m] = Color.green(pixel);
                    b[m] = Color.blue(pixel);
                    m++;
                    // LogUtils.d(m);
                }
            }

            list_r[i] = (getAverage(r));
            list_g[i] = (getAverage(g));
            list_b[i] = (getAverage(b));
        }
        list.add(list_r);
        list.add(list_g);
        list.add(list_b);
        return list;
    }

    /**
     * @param bitmap 检测框区域图片
     * @return
     */
    public static List<List<Float>> bitmap2RGB_list(Bitmap bitmap) {
        int width = bitmap.getWidth(); //300
        int height = bitmap.getHeight(); //80
        LogUtils.d("width:" + width + "  height:" + height);
        List<List<Float>> list = new ArrayList<>();
        List<Float> list_r = new ArrayList<>();
        List<Float> list_g = new ArrayList<>();
        List<Float> list_b = new ArrayList<>();
        List<Float> list_gay = new ArrayList<>();
        //bitmap.getPixels();                  // j=80 i=300
        for (int i = 0; i < width - 10; i++) { // x                       //258个面 的G值 的平均值

            float[] r = new float[10 * height];
            float[] g = new float[10 * height];
            float[] b = new float[10 * height];
            float[] gay = new float[10 * height];
            int m = 0;
            for (int j = 0; j < height; j++) {  // y  //循环20次 竖线
                for (int k = i; k < i + 10; k++) {   //循环20次 横线
                    int pixel = bitmap.getPixel(k, j); //xy
                    int red = Color.red(pixel);
                    r[m] = red;
                    int green = Color.green(pixel);
                    g[m] = green;
                    int blue = Color.blue(pixel);
                    b[m] = blue;
                    gay[m] = (float) (red * 0.299 + green * 0.587 + blue * 0.114);
                    m++;
                    // LogUtils.d(m);
                }
            }
            //平均值
            list_r.add(getAverage(r));
            list_g.add(getAverage(g));
            list_b.add(getAverage(b));
            list_gay.add(getAverage(gay));
        }
        list.add(list_r);
        list.add(list_g);
        list.add(list_b);
        list.add(list_gay);
        //bitmap.recycle();
        return list;
    }

    public static List<List<Float>> bitmap2RGB_list_6270(Bitmap bitmap) {
        int width = bitmap.getWidth(); //300
        int height = bitmap.getHeight();
        LogUtils.d("width:" + width + "  height:" + height);
        int i1 = height / 3;//80
        LogUtils.d(i1 + "  --  " + (height - i1));
        List<List<Float>> list = new ArrayList<>();
        List<Float> list_r = new ArrayList<>();
        List<Float> list_g = new ArrayList<>();
        List<Float> list_b = new ArrayList<>();
        List<Float> list_gay = new ArrayList<>();
        //bitmap.getPixels();                  // j=80 i=300
        for (int i = 0; i < width - 10; i++) { // x                       //258个面 的G值 的平均值

            float[] r = new float[10 * height];
            float[] g = new float[10 * height];
            float[] b = new float[10 * height];
            float[] gay = new float[10 * height];
            int m = 0;
            for (int j = i1; j < height - i1; j++) {  // y  //循环20次 竖线
                for (int k = i; k < i + 10; k++) {   //循环20次 横线
                    int pixel = bitmap.getPixel(k, j); //xy
                    int red = Color.red(pixel);
                    r[m] = red;
                    int green = Color.green(pixel);
                    g[m] = green;
                    int blue = Color.blue(pixel);
                    b[m] = blue;
                    gay[m] = (float) (red * 0.299 + green * 0.587 + blue * 0.114);
                    m++;
                    // LogUtils.d(m);
                }
            }

            list_r.add(getAverage(r));
            list_g.add(getAverage(g));
            list_b.add(getAverage(b));
            list_gay.add(getAverage(gay));
        }
        /*list.add(list_r);
        list.add(list_g);
        list.add(list_b);*/
        list.add(shiftAverage(10, list_r));
        list.add(shiftAverage(10, list_g));
        list.add(shiftAverage(10, list_b));
        list.add(shiftAverage(10, list_gay));
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        return list;
    }

    private static List<Float> shiftAverage(int number, List<Float> r) {
        List<Float> list = new ArrayList<>();
        for (int i = 0; i < r.size() - number; i++) {
            List<Float> l = new ArrayList<>();
            for (int j = 0; j < number; j++) {
                Float aFloat = r.get(i + j);
                l.add(aFloat);
            }
            float average = average(l);
            list.add(average);
        }

        return list;
    }

    /**
     * @方法描述 Bitmap转RGB
     */
    public static List<Float> bitmap2RGBAverage_list(Bitmap bitmap) {
        int width = bitmap.getWidth(); //300
        int height = bitmap.getHeight(); //80

        List<Float> list = new ArrayList<>();
        List<Float> list_r = new ArrayList<>();
        List<Float> list_g = new ArrayList<>();
        List<Float> list_b = new ArrayList<>();
        //bitmap.getPixels();                  // j=80 i=300
        for (int i = 0; i < width - 10; i++) { // x                       //258个面 的G值 的平均值

            float[] r = new float[10 * height];
            float[] g = new float[10 * height];
            float[] b = new float[10 * height];
            int m = 0;
            for (int j = 0; j < height; j++) {  // y  //循环20次 竖线
                for (int k = i; k < i + 10; k++) {   //循环20次 横线
                    int pixel = bitmap.getPixel(k, j); //xy
                    r[m] = Color.red(pixel);
                    g[m] = Color.green(pixel);
                    b[m] = Color.blue(pixel);
                    m++;
                    // LogUtils.d(m);
                }
            }

            list_r.add(getAverage(r));
            list_g.add(getAverage(g));
            list_b.add(getAverage(b));
        }
        list.add(getAverage(list_r));
        list.add(getAverage(list_g));
        list.add(getAverage(list_b));

        return list;
    }

    public static List<List<Float>> bitmap2RGB_list_External(Bitmap bitmap) {
        int width = bitmap.getWidth(); //300
        int height = bitmap.getHeight(); //80
        LogUtils.d(width + "--" + height);
        List<List<Float>> list = new ArrayList<>();
        List<Float> list_r = new ArrayList<>();
        List<Float> list_g = new ArrayList<>();
        List<Float> list_b = new ArrayList<>();
        //bitmap.getPixels();                  // j=80 i=300
        for (int i = 0; i < width - 5; i++) { // x                       //258个面 的G值 的平均值

            float[] r = new float[5 * height / 2];
            float[] g = new float[5 * height / 2];
            float[] b = new float[5 * height / 2];
            int m = 0;
            for (int j = height / 2 / 2; j < height / 2; j++) {  // y  //循环20次 竖线
                for (int k = i; k < i + 5; k++) {   //循环20次 横线
                    int pixel = bitmap.getPixel(k, j); //xy
                    r[m] = Color.red(pixel);
                    g[m] = Color.green(pixel);
                    b[m] = Color.blue(pixel);
                    m++;
                    // LogUtils.d(m);
                }
            }

            list_r.add(getAverage(r));
            list_g.add(getAverage(g));
            list_b.add(getAverage(b));
        }
        list.add(list_r);
        list.add(list_g);
        list.add(list_b);
        return list;
    }


    public static float getAverage(float[] array) {
        float sum = 0;
        float num = 0;
        float biggest = 0;
        float smallest = 100000;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > biggest) {
                biggest = array[i];
            }
            if (array[i] < smallest) {
                smallest = array[i];
            }
        }
        for (int j = 0; j < array.length; j++) {

            sum += (double) array[j];
            num++;

        }
        // LogUtils.d(sum+"<-->"+num);
        //return (sum-biggest-smallest)/(num-2);
        return (sum - biggest - smallest) / (num - 2);
        //return (sum-biggest-smallest);
    }

    public static float getAverage(List<Float> array) {
        float sum = 0;
        float num = 0;
        float biggest = 0;
        float smallest = 100000;
        for (int i = 1; i < array.size(); i++) {
            Float smallest1 = array.get(i);
            if (smallest1 > biggest) {
                biggest = smallest1;
            }
            if (smallest1 < smallest) {
                smallest = smallest1;
            }
        }
        for (int j = 0; j < array.size(); j++) {

            sum += (double) array.get(j);
            num++;

        }
        // LogUtils.d(sum+"<-->"+num);
        //return (sum-biggest-smallest)/(num-2);
        return (sum - biggest - smallest) / (num - 2);
        //return (sum-biggest-smallest);
    }


    public static float average(List<Float> array) {
        float sum = 0;
        float num = 0;

        for (int j = 0; j < array.size(); j++) {

            sum += (double) array.get(j);
            num++;

        }
        // LogUtils.d(sum+"<-->"+num);
        //return (sum-biggest-smallest)/(num-2);
        return (sum) / (num);
        //return (sum-biggest-smallest);
    }

    public static void saveBitmap(Bitmap bitmap, String fileName) {
        FileOutputStream fos = null;
        try {
            // 创建文件
            File file = new File("/mnt/sdcard/dayuan", fileName);
            fos = new FileOutputStream(file);

            // 将 Bitmap 写入文件
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // 使用 PNG 格式，质量为 100
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
