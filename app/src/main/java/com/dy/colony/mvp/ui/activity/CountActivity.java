package com.dy.colony.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.app.utils.OpenCvUtils;
import com.dy.colony.di.component.DaggerCountComponent;
import com.dy.colony.mvp.contract.CountContract;
import com.dy.colony.mvp.presenter.CountPresenter;
import com.dy.colony.usbcamera.CameraCallBack;
import com.dy.colony.usbcamera.Status;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.appbar.AppBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.SimpleBlobDetector;
import org.opencv.features2d.SimpleBlobDetector_Params;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static org.opencv.core.CvType.CV_8UC1;

public class CountActivity extends BaseActivity<CountPresenter> implements CountContract.View {

    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarly)
    AppBarLayout mToolbarly;
    @BindView(R.id.btnstart)
    Button mBtnstart;
    @BindView(R.id.surfaceview01)
    SurfaceView mSurfaceview01;
    @BindView(R.id.surfaceview02)
    SurfaceView mSurfaceview02;
    @BindView(R.id.surfaceview03)
    SurfaceView mSurfaceview03;
    @BindView(R.id.surfaceview04)
    SurfaceView mSurfaceview04;
    @BindView(R.id.surfaceview05)
    SurfaceView mSurfaceview05;
    @BindView(R.id.surfaceview06)
    SurfaceView mSurfaceview06;
    @BindView(R.id.surfaceview07)
    SurfaceView mSurfaceview07;
    @BindView(R.id.surfaceview08)
    SurfaceView mSurfaceview08;
    @BindView(R.id.surfaceview09)
    SurfaceView mSurfaceview09;
    @BindView(R.id.surfaceview10)
    SurfaceView mSurfaceview10;
    @BindView(R.id.surfaceview11)
    SurfaceView mSurfaceview11;
    @BindView(R.id.surfaceview12)
    SurfaceView mSurfaceview12;
    @BindView(R.id.surfaceview13)
    SurfaceView mSurfaceview13;
    @BindView(R.id.surfaceview14)
    SurfaceView mSurfaceview14;
    @BindView(R.id.surfaceview15)
    SurfaceView mSurfaceview15;
    @BindView(R.id.surfaceview16)
    SurfaceView mSurfaceview16;
    @BindView(R.id.photoview1)
    PhotoView mPhotoview1;
    @BindView(R.id.photoview2)
    PhotoView mPhotoview2;
    @BindView(R.id.btn_color)
    Button mBtnColor;
    private Bitmap photo;
    Mat mat = new Mat();

    private SurfaceHolder mSurfaceHolder01, mSurfaceHolder02, mSurfaceHolder03, mSurfaceHolder04, mSurfaceHolder05, mSurfaceHolder06, mSurfaceHolder07, mSurfaceHolder08, mSurfaceHolder09, mSurfaceHolder10, mSurfaceHolder11, mSurfaceHolder12, mSurfaceHolder13, mSurfaceHolder14, mSurfaceHolder15, mSurfaceHolder16;
    private int mSurfaceviewState01, mSurfaceviewState02, mSurfaceviewState03, mSurfaceviewState04, mSurfaceviewState05, mSurfaceviewState06, mSurfaceviewState07, mSurfaceviewState08, mSurfaceviewState09, mSurfaceviewState10, mSurfaceviewState11, mSurfaceviewState12, mSurfaceviewState13, mSurfaceviewState14, mSurfaceviewState15, mSurfaceviewState16;
    private CameraCallBack status;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCountComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_count; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0

    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(Status status) {

        //LogUtils.d(status);
        //System.out.println("ExternalJTJActivity: 接收图片 ");
        //photo = status.getBitmap();
        photo = MyAppLocation.myAppLocation.uvcCameraService.bmp;
        Utils.bitmapToMat(photo, mat);
        //setPicture(photo, 1);
    }

    @Override
    protected void onResume() {
        status = new CameraCallBack() {
            @Override
            public void onCallBack(Status status) {
                //LogUtils.d(status);
                photo = MyAppLocation.myAppLocation.uvcCameraService.bmp;
                /*Utils.bitmapToMat(photo, mat);
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV_FULL);
                for (int i = 0; i < mat.rows(); i++) {
                    for (int j = 0; j < mat.cols(); j++) {
                        double[] doubles = mat.get(i, j);
                        mat.put(i,j,new double[]{0,doubles[1],0});
                    }
                }
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_HSV2BGR);*/
                setPicture(mat, 1, "原图");
            }
        };

        MyAppLocation.myAppLocation.uvcCameraService.addStatusListener(status);

        super.onResume();
    }

    @Override
    protected void onPause() {
        MyAppLocation.myAppLocation.uvcCameraService.removeStatusListener(status);
        status = null;

        super.onPause();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (null == MyAppLocation.myAppLocation.uvcCameraService) {
            ArmsUtils.snackbarText("线程初始化失败");
            finish();
        }
        initSurfaceView();
        File[] files = this.getFilesDir().listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.getName().equals("pic")) {
                File[] files1 = file.listFiles();
                for (int i1 = 0; i1 < files1.length; i1++) {
                    String absolutePath = files1[i1].getAbsolutePath();
                    paths.add(absolutePath);
                }
            }

        }
    }

    private void initSurfaceView() {
        mSurfaceHolder01 = mSurfaceview01.getHolder();
        mSurfaceHolder01.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState01 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState01 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState01 = 3;
            }
        });
        mSurfaceview01.setZOrderOnTop(true);

        mSurfaceHolder02 = mSurfaceview02.getHolder();
        mSurfaceHolder02.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState02 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState02 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState02 = 3;
            }
        });
        mSurfaceview02.setZOrderOnTop(true);

        mSurfaceHolder03 = mSurfaceview03.getHolder();
        mSurfaceHolder03.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState03 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState03 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState03 = 3;
            }
        });
        mSurfaceview03.setZOrderOnTop(true);

        mSurfaceHolder04 = mSurfaceview04.getHolder();
        mSurfaceHolder04.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState04 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState04 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState04 = 3;
            }
        });
        mSurfaceview04.setZOrderOnTop(true);

        mSurfaceHolder05 = mSurfaceview05.getHolder();
        mSurfaceHolder05.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState05 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState05 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState05 = 3;
            }
        });
        mSurfaceview05.setZOrderOnTop(true);

        mSurfaceHolder06 = mSurfaceview06.getHolder();
        mSurfaceHolder06.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState06 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState06 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState06 = 3;
            }
        });
        mSurfaceview06.setZOrderOnTop(true);

        mSurfaceHolder07 = mSurfaceview07.getHolder();
        mSurfaceHolder07.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState07 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState07 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState07 = 3;
            }
        });
        mSurfaceview07.setZOrderOnTop(true);

        mSurfaceHolder08 = mSurfaceview08.getHolder();
        mSurfaceHolder08.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState08 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState08 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState08 = 3;
            }
        });
        mSurfaceview08.setZOrderOnTop(true);

        mSurfaceHolder09 = mSurfaceview09.getHolder();
        mSurfaceHolder09.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState09 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState09 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState09 = 3;
            }
        });
        mSurfaceview09.setZOrderOnTop(true);

        mSurfaceHolder10 = mSurfaceview10.getHolder();
        mSurfaceHolder10.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState10 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState10 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState10 = 3;
            }
        });
        mSurfaceview10.setZOrderOnTop(true);

        mSurfaceHolder11 = mSurfaceview11.getHolder();
        mSurfaceHolder11.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState11 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState11 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState11 = 3;
            }
        });
        mSurfaceview11.setZOrderOnTop(true);

        mSurfaceHolder12 = mSurfaceview12.getHolder();
        mSurfaceHolder12.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState12 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState12 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState12 = 3;
            }
        });
        mSurfaceview12.setZOrderOnTop(true);

        mSurfaceHolder13 = mSurfaceview13.getHolder();
        mSurfaceHolder13.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState13 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState13 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState13 = 3;
            }
        });
        mSurfaceview13.setZOrderOnTop(true);

        mSurfaceHolder14 = mSurfaceview14.getHolder();
        mSurfaceHolder14.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState14 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState14 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState14 = 3;
            }
        });
        mSurfaceview14.setZOrderOnTop(true);

        mSurfaceHolder15 = mSurfaceview15.getHolder();
        mSurfaceHolder15.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState15 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState15 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState15 = 3;
            }
        });
        mSurfaceview15.setZOrderOnTop(true);

        mSurfaceHolder16 = mSurfaceview16.getHolder();
        mSurfaceHolder16.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceviewState16 = 1;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceviewState16 = 2;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceviewState16 = 3;
            }
        });
        mSurfaceview16.setZOrderOnTop(true);


    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    List<String> paths = new ArrayList<>();

    @OnClick({R.id.btnstart, R.id.btn_color})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnstart:
                //colonyCountDetail(mat);
                //裁剪下
                int width = mat.width();
                int height = mat.height();
                LogUtils.d(width + " " + height);
                org.opencv.core.Rect rect = new org.opencv.core.Rect(width / 2 / 2, 100, 560, 460);

                colonyCountDetail(mat.submat(rect));

                break;
            case R.id.btn_color:


                break;
        }
    }

    private void colonyCountDetail2(Mat mat) {
        Mat src_src = mat.clone();
        Mat src_src12 = mat.clone();
        Mat src_src1 = mat.clone();
        Mat src_src1_hsv = mat.clone();
        Mat src = mat.clone();
        Mat src1 = mat.clone();
        Mat src2 = mat.clone();
        Mat clone_h = src_src1_hsv.clone();
        Imgproc.cvtColor(clone_h, clone_h, Imgproc.COLOR_BGR2HSV_FULL);
        for (int i = 0; i < clone_h.rows(); i++) {
            for (int j = 0; j < clone_h.cols(); j++) {
                double[] doubles = clone_h.get(i, j);
                clone_h.put(i, j, new double[]{doubles[0], 0, 0});
            }
        }
        setPicture(clone_h, 2, "erode");
        Mat clone_s = src_src1_hsv.clone();
        Imgproc.cvtColor(clone_s, clone_s, Imgproc.COLOR_BGR2HSV_FULL);
        for (int i = 0; i < clone_s.rows(); i++) {
            for (int j = 0; j < clone_s.cols(); j++) {
                double[] doubles = clone_s.get(i, j);
                clone_s.put(i, j, new double[]{0, doubles[1], 0});
            }
        }
        setPicture(clone_s, 3, "erode");
        Mat clone_v = src_src1_hsv.clone();
        Imgproc.cvtColor(clone_v, clone_v, Imgproc.COLOR_BGR2HSV_FULL);
        for (int i = 0; i < clone_v.rows(); i++) {
            for (int j = 0; j < clone_v.cols(); j++) {
                double[] doubles = clone_v.get(i, j);
                clone_v.put(i, j, new double[]{0, 0, doubles[2]});
            }
        }
        setPicture(clone_v, 5, "erode");

        Imgproc.cvtColor(src_src1_hsv, src_src1_hsv, Imgproc.COLOR_BGR2HSV_FULL);

        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
        //Mat erodeAnddilate = erodeAnddilate(gray, 5);

        Mat keenel = Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(5, 5));
        Imgproc.erode(src, src, keenel);  // 腐蚀
        //setPicture(src, 2, "erode");
        Imgproc.dilate(src, src, keenel); // 膨胀
        // setPicture(src, 3, "dilate");

        //Mat th2 = new Mat();
        Mat th4 = new Mat();
        //Imgproc.adaptiveThreshold(src, th2, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 9);
        Imgproc.adaptiveThreshold(src, th4, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 3);

        //setPicture(th2, 4, "adaptiveThreshold_9");
        //setPicture(th4, 5, "adaptiveThreshold_3");
        // Imgproc.rectangle(th2, new Point(0, 0), new Point(src.width(), src.height()), new Scalar(0, 0, 0), 3);
        //setPicture(th2,1);
        // Imgproc.rectangle(th2, new Point(0, 0), new Point(30, src.height()), new Scalar(0, 0, 0), 30);
        //setPicture(th2,2);
        // Imgproc.rectangle(th2, new Point(src.width() - 30, 0), new Point(src.width(), src.height()), new Scalar(0, 0, 0), 30);
        // setPicture(th2, 6, "rectangle");
        // Mat th3 = th2.clone();
        Mat gaussianBlur = new Mat();
        Mat gaussianBlur1 = new Mat();
        // Imgproc.GaussianBlur(th2, gaussianBlur, new Size(5, 5), 0);
        Imgproc.GaussianBlur(th4, gaussianBlur1, new Size(5, 5), 0);
        //setPicture(gaussianBlur, 7, "th2GaussianBlur");
        //simpleBlobDetector(gaussianBlur,src_src,src_src1,src_src12);
        //setPicture(gaussianBlur1, 8, "th4GaussianBlur");
        Mat canny = new Mat();
        Imgproc.Canny(gaussianBlur1, canny, 100, 200);
        setPicture(canny, 9, "canny");
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(canny, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        Imgproc.drawContours(src2, contours, -1, new Scalar(0, 255, 0));
        setPicture(src2, 10, "findContours");

        List<MatOfPoint> userful = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint matOfPoint = contours.get(i);
            List<Point> points1 = new ArrayList<>(matOfPoint.toList());
            Point[] a = new Point[points1.size()];
            points1.toArray(a);
            MatOfPoint2f points = new MatOfPoint2f(a);
            //if (Imgproc.arcLength(points, true) >= 20) {
            MatOfInt hull = new MatOfInt();
            Imgproc.convexHull(matOfPoint, hull);
            userful.add(matOfPoint);
            //}
        }

        Mat th2_1 = Mat.zeros(new Size(src.width(), src.height()), CV_8UC1);
        Imgproc.drawContours(th2_1, userful, -1, new Scalar(255, 255, 255), 5);
        setPicture(th2_1, 11, "drawContours");

        Imgproc.erode(th2_1, th2_1, Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(3, 3)));  // 膨胀
        setPicture(th2_1, 12, "erode");
        //Core.add(th2_1, th3, th2_1);
        setPicture(th2_1, 13, "add");
        Imgproc.dilate(th2_1, th2_1, Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(1, 1)));  // 膨胀
        Imgproc.erode(th2_1, th2_1, Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(1, 1)));  // 膨胀
        Imgproc.GaussianBlur(th2_1, th2_1, new Size(5, 5), 0);
        setPicture(th2_1, 14, "GaussianBlur");

        simpleBlobDetector(th2_1, src_src, src_src1, src_src12);
    }

    private void colonyCountDetail1(Mat mat) {
        Mat src_src = mat.clone();
        Mat src_src12 = mat.clone();
        Mat src_src1 = mat.clone();
        Mat src = mat.clone();
        Mat src1 = mat.clone();
        Mat src2 = mat.clone();


        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
        Mat keenel = Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(3, 3));
        Imgproc.erode(src, src, keenel);  // 腐蚀
        setPicture(src, 2, "erode");
        Imgproc.dilate(src, src, keenel); // 膨胀
        setPicture(src, 3, "dilate");

        Mat th4 = new Mat();
        Imgproc.adaptiveThreshold(src, th4, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 0);
        //Imgproc.threshold(src, th4, 30,255,Imgproc.THRESH_BINARY);

        setPicture(th4, 5, "adaptiveThreshold_3");

        Mat gaussianBlur1 = new Mat();
        Imgproc.GaussianBlur(th4, gaussianBlur1, new Size(3, 3), 0);

        Mat canny = new Mat();
        Imgproc.Canny(gaussianBlur1, canny, 100, 200);
        setPicture(canny, 9, "canny");
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(canny, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        Imgproc.drawContours(src2, contours, -1, new Scalar(0, 255, 0));
        setPicture(src2, 10, "findContours");

        List<MatOfPoint> userful = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint matOfPoint = contours.get(i);
            List<Point> points1 = new ArrayList<>(matOfPoint.toList());
            Point[] a = new Point[points1.size()];
            points1.toArray(a);
            MatOfPoint2f points = new MatOfPoint2f(a);
            //if (Imgproc.arcLength(points, true) >= 20) {
            MatOfInt hull = new MatOfInt();
            Imgproc.convexHull(matOfPoint, hull);
            userful.add(matOfPoint);
            //}
        }

        Mat th2_1 = Mat.zeros(new Size(src.width(), src.height()), CV_8UC1);
        Imgproc.drawContours(th2_1, userful, -1, new Scalar(255, 255, 255), 5);
        setPicture(th2_1, 11, "drawContours");

        Imgproc.erode(th2_1, th2_1, Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(3, 3)));  // 膨胀
        setPicture(th2_1, 12, "erode");
        //Core.add(th2_1, th3, th2_1);
        setPicture(th2_1, 13, "add");
        Imgproc.dilate(th2_1, th2_1, Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(1, 1)));  // 膨胀
        Imgproc.erode(th2_1, th2_1, Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(1, 1)));  // 膨胀
        Imgproc.GaussianBlur(th2_1, th2_1, new Size(5, 5), 0);
        setPicture(th2_1, 14, "GaussianBlur");

        simpleBlobDetector(th2_1, src_src, src_src1, src_src12);
    }


    public void colonyCountDetail(Mat mat) {
        Mat src_src = mat.clone();
        Mat src_src12 = mat.clone();
        Mat src_src1 = mat.clone();
        Mat src_src1_hsv = mat.clone();
        Mat src = mat.clone();
        Mat src1 = mat.clone();
        Mat src2 = mat.clone();

        Imgproc.cvtColor(src_src1_hsv, src_src1_hsv, Imgproc.COLOR_BGR2HSV_FULL);
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);


        Mat sobel = sobel(src, 2, 2);
        setPicture(sobel, 9, "sobel");
        Mat keenel = Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(3, 3));
        Imgproc.erode(sobel, sobel, keenel);  // 腐蚀
        setPicture(src, 2, "erode");
        Imgproc.dilate(src, src, keenel); // 膨胀
        setPicture(src, 3, "dilate");

        Mat th2 = new Mat();
        Mat th4 = new Mat();
        //int blockSize = 15;
        int blockSize = 15;
        Imgproc.adaptiveThreshold(src, th2, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, blockSize, 9);
        Imgproc.adaptiveThreshold(src, th4, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, blockSize, 3);

        setPicture(th2, 4, "adaptiveThreshold_9");
        setPicture(th4, 5, "adaptiveThreshold_3");
        //Imgproc.rectangle(th2, new Point(0, 0), new Point(src.width(), src.height()), new Scalar(0, 0, 0), 3);
        //setPicture(th2,1);
        //Imgproc.rectangle(th2, new Point(0, 0), new Point(30, src.height()), new Scalar(0, 0, 0), 30);
        //setPicture(th2,2);
        //Imgproc.rectangle(th2, new Point(src.width() - 30, 0), new Point(src.width(), src.height()), new Scalar(0, 0, 0), 30);
        setPicture(th2, 6, "rectangle");
        Mat th3 = th2.clone();
        Mat gaussianBlur = new Mat();
        Mat gaussianBlur1 = new Mat();
        Imgproc.GaussianBlur(th2, gaussianBlur, new Size(5, 5), 0);
        Imgproc.GaussianBlur(th4, gaussianBlur1, new Size(5, 5), 0);
        setPicture(gaussianBlur, 7, "th2GaussianBlur");
        //simpleBlobDetector(gaussianBlur,src_src,src_src1,src_src12);
        setPicture(gaussianBlur1, 8, "th4GaussianBlur");
        //Mat canny = new Mat();
        //Imgproc.Canny(gaussianBlur1, canny, 100, 200);
        //setPicture(canny, 9, "canny");


        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(gaussianBlur, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        Imgproc.drawContours(src2, contours, -1, new Scalar(0, 255, 0));
        setPicture(src2, 10, "findContours");

        List<MatOfPoint> userful = new ArrayList<>();
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint matOfPoint = contours.get(i);
            List<Point> points1 = new ArrayList<>(matOfPoint.toList());
            Point[] a = new Point[points1.size()];
            points1.toArray(a);
            MatOfPoint2f points = new MatOfPoint2f(a);
            //if (Imgproc.arcLength(points, true) >= 20) {
            MatOfInt hull = new MatOfInt();
            Imgproc.convexHull(matOfPoint, hull);
            userful.add(matOfPoint);
            //}
        }

        Mat th2_1 = Mat.zeros(new Size(src.width(), src.height()), CV_8UC1);
        Imgproc.drawContours(th2_1, userful, -1, new Scalar(255, 255, 255), 5);
        setPicture(th2_1, 11, "drawContours");

        Imgproc.erode(th2_1, th2_1, Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(3, 3)));  // 膨胀
        setPicture(th2_1, 12, "erode");
        Core.add(th2_1, th3, th2_1);
        setPicture(th2_1, 13, "add");
        Imgproc.dilate(th2_1, th2_1, Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(1, 1)));  // 膨胀
        Imgproc.erode(th2_1, th2_1, Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(1, 1)));  // 膨胀
        Imgproc.GaussianBlur(th2_1, th2_1, new Size(5, 5), 0);
        setPicture(th2_1, 14, "GaussianBlur");

        simpleBlobDetector(th2_1, src_src, src_src1, src_src12);


        //Imgproc.rectangle(mSource, points.get(0), points.get(points.size()-1), new Scalar(0.0, 0.0, 255.0), 6);

    }


    private void simpleBlobDetector(Mat th2_1, Mat src_src12, Mat src_src, Mat src_src1) {

        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        SimpleBlobDetector_Params simpleBlobDetector_params = new SimpleBlobDetector_Params();
        //改变阈值:
        //simpleBlobDetector_params.set_minThreshold(230);
        //simpleBlobDetector_params.set_maxThreshold(250);
        //根据面积过滤:
        simpleBlobDetector_params.set_filterByArea(true);
        simpleBlobDetector_params.set_minArea(10);
        //根据圆度过滤(圆的圆度为1,正方形的圆度为0.785,不熟悉圆度的以此为参考):
        simpleBlobDetector_params.set_filterByCircularity(true);
        simpleBlobDetector_params.set_minCircularity((float) 0.3);
        //根据凹凸性过滤(凹凸性=Blob面积/凸包面积):
        simpleBlobDetector_params.set_filterByConvexity(false);
        simpleBlobDetector_params.set_minConvexity((float) 0.8);
        //根据惯性比过滤(0<=惯性比<=1):
        simpleBlobDetector_params.set_filterByInertia(true);
        simpleBlobDetector_params.set_minInertiaRatio((float) 0.1);
        //根据颜色过滤:
        simpleBlobDetector_params.set_filterByColor(false);

        SimpleBlobDetector simpleBlobDetector = SimpleBlobDetector.create(simpleBlobDetector_params);
        //SimpleBlobDetector simpleBlobDetector = SimpleBlobDetector.create();


        simpleBlobDetector.detect(th2_1, keypoints);

        List<Point> points = new ArrayList<>();

        List<KeyPoint> keyPoints = keypoints.toList();

        List<RotatedRect> rotatedRectList = new ArrayList<>();
        List<Mat> matArrayList = new ArrayList<>();
        if (keyPoints.size() > 0) {
            for (int i = 0; i < keyPoints.size(); i++) {
                KeyPoint keyPoint = keyPoints.get(i);
                Point pt = keyPoint.pt;
                Point center = new Point(pt.x, pt.y);
                points.add(center);

                //在中心点绘制一个圆圈
                //Imgproc.circle(src_src, center, 5, new Scalar(0, 255, 0), 4);
                RotatedRect rotatedRect = new RotatedRect(pt, new Size(keyPoint.size, keyPoint.size), keyPoint.angle);
                rotatedRectList.add(rotatedRect);
                Mat mat1 = OpenCvUtils.guiyihuaMatByRoi_(src_src12, rotatedRect);
                matArrayList.add(mat1);

                Imgproc.erode(mat1, mat1, Imgproc.getStructuringElement(Imgproc.RETR_CCOMP, new Size(mat1.width() / 2, mat1.height() / 2)));  // 膨胀
                Imgproc.cvtColor(mat1, mat1, Imgproc.COLOR_BGR2HSV);
                double sum_h = 0;
                double sum_s = 0;
                double sum_v = 0;
                int times = 0;
                for (int i1 = 0; i1 < mat1.width(); i1++) {
                    for (int i2 = 0; i2 < mat1.height(); i2++) {
                        double[] doubles = mat1.get(i1, i2);
                        if (doubles != null) {
                            times++;
                            sum_h = sum_h + doubles[0];
                            sum_s = sum_s + doubles[1];
                            sum_v = sum_v + doubles[2];
                        }

                    }
                }
                LogUtils.d("H:" + sum_h / times + "-S:" + sum_s / times + "-V:" + sum_v / times);


                Scalar color = new Scalar(255, 255, 0);
                Imgproc.ellipse(src_src, rotatedRect, color);
                //String s = ((int) (sum_h / times)) > 125 ? "R" : "B";
                //String text = "H:" + ((int) (sum_h / times)) + s;
                //Imgproc.putText(src_src, text, pt, 1, 0.8, color, 1);
            }
        }


        LogUtils.d(rotatedRectList);
        LogUtils.d(matArrayList);

        LogUtils.d(points);
        setPicture(src_src, 15, "circle   " + keyPoints.size());


        setPhotoview(src_src, src_src1);
    }

    private void setPhotoview(Mat dst, Mat dst1) {
        Bitmap bmp = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.RGB_565);
        Bitmap bitmap = Bitmap.createBitmap(bmp);
        Utils.matToBitmap(dst, bitmap);
        mPhotoview1.setImageBitmap(bitmap);

        Bitmap bmp1 = Bitmap.createBitmap(dst1.width(), dst1.height(), Bitmap.Config.RGB_565);
        Bitmap bitmap1 = Bitmap.createBitmap(bmp1);
        Utils.matToBitmap(dst1, bitmap1);
        mPhotoview2.setImageBitmap(bitmap1);
    }

    private Rect rect;
    private int dw, dh;

    public void setPicture(Mat src, int number, String des) {
        Mat dst = src.clone();
        SurfaceHolder holder = initSetPicture(number);


        Bitmap bmp = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.RGB_565);
        Bitmap bitmap = Bitmap.createBitmap(bmp);
        Utils.matToBitmap(dst, bitmap);
        Canvas canvas = holder.lockCanvas();
        if (null != canvas) {
            canvas.drawColor(Color.WHITE);
            int height1 = canvas.getHeight();
            int width1 = canvas.getWidth();
            int height2 = bitmap.getHeight();
            int width2 = bitmap.getWidth();
            int left = (width1 - width2) / 2;
            int top = (height1 - height2) / 2;

            canvas.drawBitmap(bitmap, null, rect, null);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawText(number + "--" + des, 0, width1 / 2, paint);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setPicture(Bitmap bitmap, int number) {
        SurfaceHolder holder = initSetPicture(number);
        Canvas canvas = holder.lockCanvas();
        if (null != canvas) {
            canvas.drawColor(Color.WHITE);
            int height1 = canvas.getHeight();
            int width1 = canvas.getWidth();
            int width2 = bitmap.getWidth();
            int height2 = bitmap.getHeight();
            int left = (width1 - width2) / 2;
            int top = (height1 - height2) / 2;
            //Rect src = new Rect(0,0,width2,height2);

            canvas.drawBitmap(bitmap, null, new Rect(left, top, left + width2, top + height2), null);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private SurfaceHolder initSetPicture(int number) {
        int state = 0;
        SurfaceView surfaceView = null;
        SurfaceHolder holder = null;
        switch (number) {
            case 1:
                surfaceView = mSurfaceview01;
                state = mSurfaceviewState01;
                holder = mSurfaceHolder01;
                break;
            case 2:
                surfaceView = mSurfaceview02;
                state = mSurfaceviewState02;
                holder = mSurfaceHolder02;
                break;
            case 3:
                surfaceView = mSurfaceview03;
                state = mSurfaceviewState03;
                holder = mSurfaceHolder03;
                break;
            case 4:
                surfaceView = mSurfaceview04;
                state = mSurfaceviewState04;
                holder = mSurfaceHolder04;
                break;
            case 5:
                surfaceView = mSurfaceview05;
                state = mSurfaceviewState05;
                holder = mSurfaceHolder05;
                break;
            case 6:
                surfaceView = mSurfaceview06;
                state = mSurfaceviewState06;
                holder = mSurfaceHolder06;
                break;
            case 7:
                surfaceView = mSurfaceview07;
                state = mSurfaceviewState07;
                holder = mSurfaceHolder07;
                break;
            case 8:
                surfaceView = mSurfaceview08;
                state = mSurfaceviewState08;
                holder = mSurfaceHolder08;
                break;

            case 9:
                surfaceView = mSurfaceview09;
                state = mSurfaceviewState09;
                holder = mSurfaceHolder09;
                break;
            case 10:
                surfaceView = mSurfaceview10;
                state = mSurfaceviewState10;
                holder = mSurfaceHolder10;
                break;
            case 11:
                surfaceView = mSurfaceview11;
                state = mSurfaceviewState11;
                holder = mSurfaceHolder11;
                break;
            case 12:
                surfaceView = mSurfaceview12;
                state = mSurfaceviewState12;
                holder = mSurfaceHolder12;
                break;
            case 13:
                surfaceView = mSurfaceview13;
                state = mSurfaceviewState13;
                holder = mSurfaceHolder13;
                break;
            case 14:
                surfaceView = mSurfaceview14;
                state = mSurfaceviewState14;
                holder = mSurfaceHolder14;
                break;
            case 15:
                surfaceView = mSurfaceview15;
                state = mSurfaceviewState15;
                holder = mSurfaceHolder15;
                break;
            case 16:
                surfaceView = mSurfaceview16;
                state = mSurfaceviewState16;
                holder = mSurfaceHolder16;
                break;

        }
        if (state == 3) {
            return holder;
        }
        if (null == holder) {
            return holder;
        }

        int winWidth = surfaceView.getWidth();
        int winHeight = surfaceView.getHeight();

        if (winWidth * 3 / 4 <= winHeight) {
            dw = 0;
            dh = (winHeight - winWidth * 3 / 4) / 2;
            //rate = ((float) winWidth) / IMG_WIDTH;
            rect = new Rect(dw, dh, dw + winWidth - 1, dh + winWidth * 3 / 4 - 1);
        } else {
            dw = (winWidth - winHeight * 4 / 3) / 2;
            dh = 0;
            //rate = ((float) winHeight) / IMG_HEIGHT;
            rect = new Rect(dw, dh, dw + winHeight * 4 / 3 - 1, dh + winHeight - 1);
        }
        return holder;
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
}