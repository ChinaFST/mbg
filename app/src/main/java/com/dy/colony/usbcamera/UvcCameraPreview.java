package com.dy.colony.usbcamera;

import android.graphics.Bitmap;

import com.apkfuns.logutils.LogUtils;

import org.greenrobot.eventbus.EventBus;


public class UvcCameraPreview implements Runnable {
    public MyListener prepareGetPhotoListener;

    public interface MyListener {
        void reciver(int cameraId, Bitmap bitmap);
    }

    public void setPrepareGetPhotoListener(MyListener reciverListener) {
        prepareGetPhotoListener=reciverListener;
        //prepareGetPhotoListener =new WeakReference<MyListener>(reciverListener) ;
    }

    private Bitmap bmp = null;
    public Bitmap getBitmap(){
        return bmp;
    }
    public boolean isCameraExists() {
        return cameraExists;
    }

    public void recycleBmp(){
        if (bmp!=null){
            bmp.recycle();
            bmp=null;
        }
    }
    private boolean cameraExists = false;

    public boolean isShouldStop() {
        return shouldStop;
    }

    public void setShouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    private boolean shouldStop = false;

    // /dev/videox (x=cameraId+cameraBase) is used.
    // In some omap devices, system uses /dev/video[0-3],
    // so users must use /dev/video[4-].
    // In such a case, try cameraId=0 and cameraBase=4
    private int mCameraId = 0;


    public native int prepareCamera(int videoid, int IMG_WIDTH, int IMG_HEIGHT);

    public native int prepareCameraWithBase(int videoid, int camerabase, int IMG_WIDTH, int IMG_HEIGHT);

    public native void processCamera();

    public native void stopCamera();

    public native void pixeltobmp(Bitmap bitmap);

    static {
        System.loadLibrary("uvcCamera-lib");
    }


    int IMG_WIDTH;
    int IMG_HEIGHT;

    public UvcCameraPreview(int cameraId,int w, int h) {
        mCameraId=cameraId;
        IMG_WIDTH = w;
        IMG_HEIGHT = h;
        prepareTheCamera();

    }


    /**
     * 是否初始化摄像头  摄像头初始化后灯才会亮
     */
    private boolean isPrepareTheCamera = false;

    public boolean isPrepareTheCamera() {
        return isPrepareTheCamera;
    }


    /**
     * @return 初始化摄像头
     */
    public boolean prepareTheCamera() {
        if (isPrepareTheCamera) {
            return true;
        }
        if (bmp == null) {
            bmp = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT, Bitmap.Config.ARGB_8888);
        }

        // /dev/videox (x=cameraId + cameraBase) is used
        //int ret = prepareCameraWithBase(cameraId, cameraBase);
        int ret = prepareCamera(mCameraId, IMG_WIDTH, IMG_HEIGHT);
       // LogUtils.d("/dev/video"+mCameraId);
        //LogUtils.d(ret);
        if (ret != -1) {
            cameraExists = true;
            isPrepareTheCamera = true;
        }

        return true;
    }

    public void stopTheCamera() {
        LogUtils.d("stopTheCamera");
        if (cameraExists) {
            shouldStop = true;
            while (shouldStop) {
                try {
                    Thread.sleep(100); // wait for thread stopping
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        stopCamera();
        isPrepareTheCamera = false;
    }

    public boolean STARTREADFLAG = true;

    @Override
    public void run() {
       
        while (STARTREADFLAG && isCameraExists()) {
            // obtaining a camera image (pixel data are stored in an array in JNI).
            processCamera();
            // camera image to bmp
            pixeltobmp(bmp);
            if (null != prepareGetPhotoListener) {
                if (bmp!=null){
                    prepareGetPhotoListener.reciver(mCameraId,bmp);
                }
            }
            //System.out.println("uvc——readp");
            EventBus.getDefault().post(new Status(Status.MyEvent.BITMAPRECEVER));
            if (isShouldStop()) {
                setShouldStop(false);
                break;
            }
        }
    }


}
