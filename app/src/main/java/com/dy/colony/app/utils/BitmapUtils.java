package com.dy.colony.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
 * @data: 8/31/21 4:34 PM
 * Description:
 */
public class BitmapUtils {

    public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float scaleWight = ((float) w) / width;
            float scaleHeight = ((float) h) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWight, scaleHeight);
            Bitmap res = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            return res;

        } else {
            return null;
        }
    }
    public static Bitmap mergeBitmap(List<Bitmap> bitmapList) {
        Bitmap bt = null;
        for (int i = 0; i < bitmapList.size(); i++) {
            Bitmap bitmap = bitmapList.get(i);
            if (bitmap == null) {
                continue;
            }
            bt = merge(bt, bitmap);
        }

        return bt;
    }

    private static Bitmap merge(Bitmap bt, Bitmap bitmap) {
        if (bt == null) {
            return bitmap;
        }
        if (bitmap == null) {
            return bt;
        }
        int height = bt.getHeight() + bitmap.getHeight();
        int width = bt.getWidth() > bitmap.getWidth() ? bt.getWidth() : bitmap.getWidth();
        Bitmap bitmap1 = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas=new Canvas(bitmap1);
        canvas.drawBitmap(bt,0,0,new Paint());
        canvas.drawBitmap(bitmap,0,bt.getHeight(),new Paint());
        return bitmap1;
    }


    public static String bitmapToBase64JPG(Bitmap bitmap) {

        return "data:image/jpg;base64," + bitmapToBase64(Bitmap.CompressFormat.JPEG, bitmap);

    }

    public static String bitmapToBase64PNG(Bitmap bitmap) {
        return "data:image/png;base64," + bitmapToBase64(Bitmap.CompressFormat.PNG, bitmap);

    }

    /**
     * bitmap转base64
     *
     * @param bitmap
     * @return
     */
    private static String bitmapToBase64(Bitmap.CompressFormat value, Bitmap bitmap) {

        String result = "";
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();

                bitmap.compress(value, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.replace("+", "%2B");

    }


    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /** 水平s上下翻转
     * @param originalBitmap
     * @return
     */
    public static Bitmap rolloverX_Y(Bitmap originalBitmap) {
        Bitmap mirrorBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mirrorBitmap);
        Matrix matrix = new Matrix();
        matrix.setScale(-1, -1); // 水平翻转
        matrix.postTranslate(originalBitmap.getWidth(), originalBitmap.getHeight()); // 平移
        canvas.drawBitmap(originalBitmap, matrix, null);
        return mirrorBitmap;
    }
    /** 水平翻转
     * @param originalBitmap
     * @return
     */
    public static Bitmap rolloverX(Bitmap originalBitmap) {
        Bitmap mirrorBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mirrorBitmap);
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1); // 水平翻转
        matrix.postTranslate(originalBitmap.getWidth(), 0); // 平移
        canvas.drawBitmap(originalBitmap, matrix, null);
        return mirrorBitmap;
    }
    /** 上下翻转
     * @param originalBitmap
     * @return
     */
    public static Bitmap rolloverY(Bitmap originalBitmap) {
        Bitmap mirrorBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mirrorBitmap);
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1); // 水平翻转
        matrix.postTranslate(0, originalBitmap.getHeight()); // 平移
        canvas.drawBitmap(originalBitmap, matrix, null);
        return mirrorBitmap;
    }



    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        Matrix matrix=new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
