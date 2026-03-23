package com.dy.colony.mvp.model.opencvDetector;

import com.apkfuns.logutils.LogUtils;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
 * @data: 10/29/21 3:07 PM
 * Description:
 */
public class CardDetector {

    private static ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
    private Mat mMat;
    private CardType mCardType;
    private int mCardNumber;

    public CardDetector(Mat mat, CardType cardType) {
        mMat = mat.clone();
        mCardType = cardType;
        if (null == mScheduledThreadPoolExecutor) {
            mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(3);
        }
        //startSeachCards();
    }


    public List<DataSource> startSeachCards() {
        List<DataSource> list = new ArrayList<>();
        Future<List<DataSource>> submit1 = mScheduledThreadPoolExecutor.submit(new GetCardTask(mMat, mCardType));

        //mScheduledThreadPoolExecutor.shutdown();
        try {
            List<DataSource> list1 = submit1.get();
            if (list1 != null) {
                list.addAll(list1);
                LogUtils.d(list1);
                return list;
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<DataSource> startSeachCards_External() {
        List<DataSource> list = new ArrayList<>();
        Future<List<DataSource>> submit1 = mScheduledThreadPoolExecutor.submit(new GetCardTask_External(mMat, mCardType));

        //mScheduledThreadPoolExecutor.shutdown();
        try {
            List<DataSource> list1 = submit1.get();
            LogUtils.d(list1);
            if (list1 != null) {
                list.addAll(list1);
                LogUtils.d(list1);
                return list;
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LogUtils.d("释放资源Mat");
            mMat.release();
        }
        return list;
    }

    /*public List<DataSource> startSeachCards_ALLTYPE() {
        List<DataSource> list = new ArrayList<>();
        Future<List<DataSource>> submit1 = mScheduledThreadPoolExecutor.submit(new GetCardTask_ALLTYPE(mMat, mCardType));

        //mScheduledThreadPoolExecutor.shutdown();
        try {
            List<DataSource> list1 = submit1.get();
            LogUtils.d(list1);
            if (list1 != null) {
                list.addAll(list1);
                LogUtils.d(list1);
                return list;
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }*/
}
