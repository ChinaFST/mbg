package com.dy.colony.mvp.ui.widget;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import com.apkfuns.logutils.LogUtils;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

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
 * 　  ┃　  ┗━━━┓ + +
 * 　　┃ 　　　　┣┓
 * 　　┃ 　　　 ┏┛
 * 　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　 ┃┫┫ ┃┫┫
 * 　　 ┗┻┛ ┗┻┛+ + + +
 *
 * @author: wangzhenxiong
 * @data: 8/6/24 2:24 PM
 * Description:
 */
public class MyJavaCameraView extends CameraBridgeViewBase implements Camera.PreviewCallback {
    private static final int MAGIC_TEXTURE_ID = 10;
    private static final String TAG = "JavaCameraView";

    private byte mBuffer[];
    private Mat[] mFrameChain;
    private int mChainIdx = 0;
    private Thread mThread;
    private boolean mStopThread;

    protected Camera mCamera;
    protected JavaCameraFrame[] mCameraFrame;
    private SurfaceTexture mSurfaceTexture;
    private int mPreviewFormat = ImageFormat.NV21;

    public Camera getmCamera() {
        return mCamera;
    }


    public static class JavaCameraSizeAccessor implements ListItemAccessor {

        @Override
        public int getWidth(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.width;
        }

        @Override
        public int getHeight(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.height;
        }
    }

    public MyJavaCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public static int pic_with=1440;
    public static int pic_height=1088;
    protected boolean initializeCamera(int width, int height) {
        Log.d(TAG, "Initialize java camera");
        boolean result = true;
        synchronized (this) {
            mCamera = null;

            // 获取你当前设置的索引 (比如 0, 1, 98前置, 99后置, -1任意)
            int localCameraIndex = getCameraIndex();

            // ================= 开始补充的多路查找逻辑 =================
            if (localCameraIndex == CameraBridgeViewBase.CAMERA_ID_ANY) {
                Log.d(TAG, "Trying to open camera with old open()");
                try {
                    mCamera = Camera.open(); // 先试默认的
                } catch (Exception e) {
                    Log.e(TAG, "默认相机不可用: " + e.getLocalizedMessage());
                }

                // 如果默认方法失败，遍历主板上所有节点
                if (mCamera == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    boolean connected = false;
                    for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                        Log.d(TAG, "Trying to open camera with new open(" + camIdx + ")");
                        try {
                            mCamera = Camera.open(camIdx);
                            connected = true;
                        } catch (RuntimeException e) {
                            Log.e(TAG, "相机节点 #" + camIdx + " 打开失败: " + e.getLocalizedMessage());
                        }
                        if (connected) {
                            break;
                        }
                    }
                }
            } else {
                // 如果你指定了具体的 ID (比如 0, 1) 或 前/后置常量 (98, 99)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    int mappedCameraId = localCameraIndex;

                    // 自动将常量映射为主板真实的物理 ID
                    if (localCameraIndex == CameraBridgeViewBase.CAMERA_ID_BACK) {
                        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                            Camera.getCameraInfo(camIdx, cameraInfo);
                            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                                mappedCameraId = camIdx;
                                break;
                            }
                        }
                    } else if (localCameraIndex == CameraBridgeViewBase.CAMERA_ID_FRONT) {
                        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                            Camera.getCameraInfo(camIdx, cameraInfo);
                            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                                mappedCameraId = camIdx;
                                break;
                            }
                        }
                    }

                    if (mappedCameraId == CameraBridgeViewBase.CAMERA_ID_BACK) {
                        Log.e(TAG, "未找到物理后置摄像头!");
                    } else if (mappedCameraId == CameraBridgeViewBase.CAMERA_ID_FRONT) {
                        Log.e(TAG, "未找到物理前置摄像头!");
                    } else {
                        Log.d(TAG, "Trying to open camera with new open(" + mappedCameraId + ")");
                        try {
                            mCamera = Camera.open(mappedCameraId); // 通过真实 ID 打开
                        } catch (RuntimeException e) {
                            Log.e(TAG, "相机节点 #" + mappedCameraId + " 打开失败: " + e.getLocalizedMessage());
                        }
                    }
                }
            }
            // ================= 结束补充的多路查找逻辑 =================

            // 终极拦截：如果主板上真的没有任何可用节点，安全退出
            if (mCamera == null) {
                Log.e(TAG, "所有尝试均失败，无法获取到 mCamera 对象");
                return false;
            }

            /* Now set camera parameters */
            try {
                Camera.Parameters params = mCamera.getParameters();
                Log.d(TAG, "getSupportedPreviewSizes()");
                List<Camera.Size> sizes = params.getSupportedPreviewSizes();
                for (int i = 0; i < sizes.size(); i++) {
                    Camera.Size object = sizes.get(i);
                    LogUtils.d(object.width+"*"+object.height);
                }
                if (sizes != null) {
                    /* Select the size that fits surface considering maximum size allowed */
                    //Size frameSize = calculateCameraFrameSize(sizes, new JavaCameraSizeAccessor(), width, height);
                    //Size frameSize = new Size(320,240);
                    //Size frameSize = new Size(640,480);
                    //Size frameSize = new Size(800,600);
                    //Size frameSize = new Size(1024,768);
                    //Size frameSize = new Size(1280,720);
                    //Size frameSize = new Size(1280,960);
                    //Size frameSize = new Size(1600,1200);
                    //Size frameSize = new Size(1920, 1080);
                    Size frameSize = new Size(pic_with, pic_height);
                    //Size frameSize = new Size(2048,1536);
                    //Size frameSize = new Size(2592,1944);
                    //Size frameSize = new Size(3264,2448);
                    //Size frameSize = new Size(3840,2160);
                    //Size frameSize = new Size(3840,2880);

                    /* Image format NV21 causes issues in the Android emulators */
                    if (Build.FINGERPRINT.startsWith("generic")
                            || Build.FINGERPRINT.startsWith("unknown")
                            || Build.MODEL.contains("google_sdk")
                            || Build.MODEL.contains("Emulator")
                            || Build.MODEL.contains("Android SDK built for x86")
                            || Build.MANUFACTURER.contains("Genymotion")
                            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                            || "google_sdk".equals(Build.PRODUCT)) {
                        params.setPreviewFormat(ImageFormat.YV12);  // "generic" or "android" = android emulator
                    } else {
                        params.setPreviewFormat(ImageFormat.NV21);
                    }

                    mPreviewFormat = params.getPreviewFormat();

                    Log.d(TAG, "Set preview size to " + Integer.valueOf((int) frameSize.width) + "x" + Integer.valueOf((int) frameSize.height));
                    params.setPreviewSize((int) frameSize.width, (int) frameSize.height);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && !Build.MODEL.equals("GT-I9100")) {
                        params.setRecordingHint(true);
                    }

                    List<String> FocusModes = params.getSupportedFocusModes();
                    if (FocusModes != null && FocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                        LogUtils.d("自动对焦？");
                    }
                    //设置放大倍数
                    //params.setZoom(4);
                    mCamera.setParameters(params);
                    params = mCamera.getParameters();

                    Log.d("wang", params.getMaxZoom() + "");
                    Log.d("wang", params.getZoom() + "");
                    mFrameWidth = params.getPreviewSize().width;
                    mFrameHeight = params.getPreviewSize().height;
                    Log.d("wang", mFrameWidth + "," + mFrameHeight);
                    if ((getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT) && (getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT)) {
                        mScale = Math.min(((float) height) / mFrameHeight, ((float) width) / mFrameWidth);
                    } else {
                        mScale = 0;
                    }

                    if (mFpsMeter != null) {
                        mFpsMeter.setResolution(mFrameWidth, mFrameHeight);
                    }

                    int size = mFrameWidth * mFrameHeight;
                    size = size * ImageFormat.getBitsPerPixel(params.getPreviewFormat()) / 8;
                    mBuffer = new byte[size];

                    mCamera.addCallbackBuffer(mBuffer);
                    mCamera.setPreviewCallbackWithBuffer(this);

                    mFrameChain = new Mat[4];
                    mFrameChain[0] = new Mat(mFrameHeight + (mFrameHeight / 2), mFrameWidth, CvType.CV_8UC1);
                    mFrameChain[1] = new Mat(mFrameHeight + (mFrameHeight / 2), mFrameWidth, CvType.CV_8UC1);
                    mFrameChain[2] = new Mat(mFrameHeight + (mFrameHeight / 2), mFrameWidth, CvType.CV_8UC1);
                    mFrameChain[3] = new Mat(mFrameHeight + (mFrameHeight / 2), mFrameWidth, CvType.CV_8UC1);

                    AllocateCache();

                    mCameraFrame = new JavaCameraFrame[4];
                    mCameraFrame[0] = new JavaCameraFrame(mFrameChain[0], mFrameWidth, mFrameHeight);
                    mCameraFrame[1] = new JavaCameraFrame(mFrameChain[1], mFrameWidth, mFrameHeight);
                    mCameraFrame[2] = new JavaCameraFrame(mFrameChain[2], mFrameWidth, mFrameHeight);
                    mCameraFrame[3] = new JavaCameraFrame(mFrameChain[3], mFrameWidth, mFrameHeight);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        mSurfaceTexture = new SurfaceTexture(MAGIC_TEXTURE_ID);
                        mCamera.setPreviewTexture(mSurfaceTexture);
                    } else {
                        mCamera.setPreviewDisplay(null);
                    }
                    mCamera.setDisplayOrientation(90);

                    /* Finally we are ready to start the preview */
                    Log.d(TAG, "startPreview");
                    Log.d(TAG, getCameraIndex()+"  CameraIndex()");
                    mCamera.startPreview();
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void releaseCamera() {
        synchronized (this) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);

                mCamera.release();
            }
            mCamera = null;
            if (mFrameChain != null) {
                mFrameChain[0].release();
                mFrameChain[1].release();
            }
            if (mCameraFrame != null) {
                mCameraFrame[0].release();
                mCameraFrame[1].release();
            }
        }
    }

    private boolean mCameraFrameReady = false;

    @Override
    protected boolean connectCamera(int width, int height) {

        /* 1. We need to instantiate camera
         * 2. We need to start thread which will be getting frames
         */
        /* First step - initialize camera connection */
        Log.d(TAG, "Connecting to camera");
        if (!initializeCamera(width, height)) {
            return false;
        }

        mCameraFrameReady = false;

        /* now we can start update thread */
        Log.d(TAG, "Starting processing thread");
        mStopThread = false;
        mThread = new Thread(new CameraWorker());
        mThread.start();

        return true;
    }

    @Override
    protected void disconnectCamera() {
        /* 1. We need to stop thread which updating the frames
         * 2. Stop camera and release it
         */
        Log.d(TAG, "Disconnecting from camera");
        try {
            mStopThread = true;
            Log.d(TAG, "Notify thread");
            synchronized (this) {
                this.notify();
            }
            Log.d(TAG, "Waiting for thread");
            if (mThread != null) {
                mThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mThread = null;
        }

        /* Now release camera */
        releaseCamera();

        mCameraFrameReady = false;
    }

    @Override
    public void onPreviewFrame(byte[] frame, Camera arg1) {
        /*if (BuildConfig.DEBUG) {
            Log.d(TAG, "Preview Frame received. Frame size: " + frame.length);
        }*/
        synchronized (this) {
            mFrameChain[mChainIdx].put(0, 0, frame);
            mCameraFrameReady = true;
            this.notify();
        }
        if (mCamera != null) {
            mCamera.addCallbackBuffer(mBuffer);
        }
    }

    private class JavaCameraFrame implements CvCameraViewFrame {
        @Override
        public Mat gray() {
            return mYuvFrameData.submat(0, mHeight, 0, mWidth);
        }

        @Override
        public Mat rgba() {
            if (mPreviewFormat == ImageFormat.NV21) {
                Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2RGBA_NV21, 4);
            } else if (mPreviewFormat == ImageFormat.YV12) {
                Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2RGB_I420, 4);  // COLOR_YUV2RGBA_YV12 produces inverted colors
            } else {
                throw new IllegalArgumentException("Preview Format can be NV21 or YV12");
            }
            // 关键修改：使用 Core.rotate() 实现 180° 旋转
            //Core.rotate(mRgba, mRgba, Core.ROTATE_180);


            return mRgba;
        }

        public JavaCameraFrame(Mat Yuv420sp, int width, int height) {
            super();
            mWidth = width;
            mHeight = height;
            mYuvFrameData = Yuv420sp;
            mRgba = new Mat();
        }

        public void release() {
            mRgba.release();
        }

        private Mat mYuvFrameData;
        private Mat mRgba;
        private int mWidth;
        private int mHeight;
    }

    ;


    private class CameraWorker implements Runnable {

        @Override
        public void run() {
            do {
                boolean hasFrame = false;
                synchronized (MyJavaCameraView.this) {
                    try {
                        while (!mCameraFrameReady && !mStopThread) {
                            MyJavaCameraView.this.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mCameraFrameReady) {
                        mChainIdx = 1 - mChainIdx;
                        mCameraFrameReady = false;
                        hasFrame = true;
                    }
                }

                if (!mStopThread && hasFrame) {
                    if (!mFrameChain[1 - mChainIdx].empty()) {
                        deliverAndDrawFrame(mCameraFrame[1 - mChainIdx]);
                    }
                }
            } while (!mStopThread);
            Log.d(TAG, "Finish processing thread");
        }
    }
}
