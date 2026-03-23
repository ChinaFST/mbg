package com.dy.colony.mvp.model.opencvDetector;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.app.utils.OpenCvUtils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.Callable;

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
 * @data: 10/29/21 2:47 PM
 * Description:
 */
public class GetCardTask_External implements Callable<List<DataSource>> {

    @Override
    public List<DataSource> call() throws Exception {
        /*if (BuildConfig.APPLICATION_ID.equals("com.wangzx.dy.sample_DY3000_BX1")) {
            return getCardBx1(0);
        }*/
        return getCard(0);
    }

    private List<DataSource> getCard(int count) {
        List<DataSource> list = new ArrayList<>();
        int keysize;
        if (count == 0) {
            keysize = 5;
        } else if (count == 1) {
            keysize = 7;
        } else if (count == 2) {
            keysize = 9;
        } else {
            keysize = 3;
        }
        Mat mat = OpenCvUtils.psMap(mMat, keysize);
        ArrayList<MatOfPoint> es = OpenCvUtils.findContours(mat);
        List<MatOfPoint> hullList = new ArrayList<MatOfPoint>();
        for (int i = 0; i < es.size(); i++) {
            MatOfInt hull = new MatOfInt();
            Imgproc.convexHull(es.get(i), hull);
            hullList.add(OpenCvUtils.matOfIntToPoints(es.get(i), hull));

        }
        List<RotatedRect> rotatedRectList = new ArrayList<>();
        for (int i = 0; i < hullList.size(); i++) {
            MatOfPoint matOfPoint = hullList.get(i);

            List<Point> points1 = new ArrayList<>(matOfPoint.toList());
            Point[] a = new Point[points1.size()];
            points1.toArray(a);
            MatOfPoint2f points = new MatOfPoint2f(a);
            RotatedRect rect = Imgproc.minAreaRect(points);
            /*if (rect.center.x<mMat.width()/3||rect.center.x>mMat.width()/3*2){
               continue;
            }*/
            rotatedRectList.add(rect);
            // LogUtils.d(rect);
            double height = rect.size.height;
            double width = rect.size.width;
            if (width * height < 60 * 350) {
                continue;
            }
            if (width > height) {
                if (width > 600) {
                    continue;
                }
                if (width / height > 7 || width / height < 3) {
                    continue;
                }
            } else {
                if (height > 600) {
                    continue;
                }
                if (height / width > 7 || height / width < 3) {
                    continue;
                }
            }
            LogUtils.d(rect);



           /* if (width > height) {
                rect.angle = 90;
            } else {
                rect.angle = 0;
            }*/
            DataSource e = new DataSource();
            ArrayList<MatOfPoint> usefules = new ArrayList<>();
            usefules.add(matOfPoint);
            e.setUsefules(usefules);
            ArrayList<RotatedRect> usefulRotatedRect = new ArrayList<>();
            usefulRotatedRect.add(rect);
            e.setUsefulRotatedRect(usefulRotatedRect);
            list.add(e);


        }
        LogUtils.d(rotatedRectList);
        LogUtils.d(list);
        if (count == 3) {
            return sortCard(list);
        }
        if (mCardType == CardType.ONE) {
            if (list.size() != 1) {
                return getCard(count + 1);
            }
        } else if (mCardType == CardType.TWO) {
            if (list.size() != 2) {
                return getCard(count + 1);
            }
        } else if (mCardType == CardType.THREE) {
            if (list.size() != 3) {
                return getCard(count + 1);
            }
        } else if (mCardType == CardType.FOUR) {
            if (list.size() != 4) {
                return getCard(count + 1);
            }
        }

        return sortCard(list);
    }

    private List<DataSource> getCardBx1(int count) {
        List<DataSource> list = new ArrayList<>();
        int keysize;
        if (count == 0) {
            keysize = 5;
        } else if (count == 1) {
            keysize = 7;
        } else if (count == 2) {
            keysize = 9;
        } else {
            keysize = 3;
        }
        Mat mat = OpenCvUtils.psMap(mMat, keysize);
        ArrayList<MatOfPoint> es = OpenCvUtils.findContours(mat);
        List<MatOfPoint> hullList = new ArrayList<MatOfPoint>();
        for (int i = 0; i < es.size(); i++) {
            MatOfInt hull = new MatOfInt();
            Imgproc.convexHull(es.get(i), hull);
            hullList.add(OpenCvUtils.matOfIntToPoints(es.get(i), hull));

        }
        List<RotatedRect> rotatedRectList = new ArrayList<>();
        for (int i = 0; i < hullList.size(); i++) {
            MatOfPoint matOfPoint = hullList.get(i);

            List<Point> points1 = new ArrayList<>(matOfPoint.toList());
            Point[] a = new Point[points1.size()];
            points1.toArray(a);
            MatOfPoint2f points = new MatOfPoint2f(a);
            RotatedRect rect = Imgproc.minAreaRect(points);
            int i1 = mMat.width() / 3;
            int i2 = mMat.width() / 3 * 2;
            if (rect.center.x < i1 || rect.center.x > i2) {
                //LogUtils.d("矩形未在图片中心");
                continue;
            }
            rotatedRectList.add(rect);
            // LogUtils.d(rect);
            double height = rect.size.height;
            double width = rect.size.width;
            if (width * height < 60 * 350) {
                continue;
            }
            if (width > height) {
                if (width > 600) {
                    continue;
                }
                if (width / height > 7 || width / height < 3) {
                    continue;
                }
            } else {
                if (height > 600) {
                    continue;
                }
                if (height / width > 7 || height / width < 3) {
                    continue;
                }
            }
            LogUtils.d(rect);



           /* if (width > height) {
                rect.angle = 90;
            } else {
                rect.angle = 0;
            }*/
            DataSource e = new DataSource();
            ArrayList<MatOfPoint> usefules = new ArrayList<>();
            usefules.add(matOfPoint);
            e.setUsefules(usefules);
            ArrayList<RotatedRect> usefulRotatedRect = new ArrayList<>();
            usefulRotatedRect.add(rect);
            e.setUsefulRotatedRect(usefulRotatedRect);
            list.add(e);


        }
        LogUtils.d(rotatedRectList);
        LogUtils.d(list);
        if (count == 3) {
            return sortCard(list);
        }
        if (mCardType == CardType.ONE) {
            if (list.size() != 1) {
                return getCardBx1(count + 1);
            }
        } else if (mCardType == CardType.TWO) {
            if (list.size() != 2) {
                return getCard(count + 1);
            }
        } else if (mCardType == CardType.THREE) {
            if (list.size() != 3) {
                return getCard(count + 1);
            }
        } else if (mCardType == CardType.FOUR) {
            if (list.size() != 4) {
                return getCard(count + 1);
            }
        }

        return sortCard(list);
    }

    private List<DataSource> sortCard(List<DataSource> list) {
        if (mCardType == CardType.ONE) {
            return list;
        }
        TreeMap<Double, DataSource> doubleDataSourceTreeMap = new TreeMap<>();
        List<DataSource> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DataSource dataSource = list.get(i);
            RotatedRect rotatedRect = dataSource.getUsefulRotatedRect().get(0);
            double x = rotatedRect.center.x;
            doubleDataSourceTreeMap.put(x, dataSource);
        }
        for (Double aDouble : doubleDataSourceTreeMap.keySet()) {
            list1.add(doubleDataSourceTreeMap.get(aDouble));
        }
        return list1;
    }


    private Mat mMat;
    private CardType mCardType;

    public GetCardTask_External(Mat mat, CardType cardtype) {
        //FileUtils.saveBitmap1(mat.clone(), Environment.getExternalStorageDirectory().getPath().toString() + "/DCIM/", File.separator + System.currentTimeMillis());
        mMat = mat;
        mCardType = cardtype;
    }


}
