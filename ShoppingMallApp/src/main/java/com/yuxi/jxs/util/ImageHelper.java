package com.yuxi.jxs.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageHelper {

    public static final String TAG = "ImageHelper";

    /**
     * 解码图片，使用采样率
     *
     * @param context  上下文
     * @param filepath 图片路径
     * @return 采样后的bitmap
     */
    public static Bitmap decodeBitmap(Activity context, String filepath) {
        Display display = context.getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
        int ratioWidth = (int) Math.ceil(options.outWidth / screenWidth);
        int ratioHeight = (int) Math.ceil(options.outHeight / screenHeight);
        int sampleSize = 1;
        if (ratioWidth > 1 && ratioHeight > 1) {
            if (ratioWidth > ratioHeight) {
                sampleSize = ratioWidth;
            } else {
                sampleSize = ratioHeight;
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        bitmap = BitmapFactory.decodeFile(filepath, options);
        return bitmap;
    }

    public static Bitmap getimage(String srcPath) {
        Options newOpts = new Options();
        newOpts.inJustDecodeBounds = true;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800.0F;
        float ww = 480.0F;
        int be = 1;
        if (w > h && (float) w > ww) {
            be = (int) ((float) newOpts.outWidth / ww);
        } else if (w < h && (float) h > hh) {
            be = (int) ((float) newOpts.outHeight / hh);
        }

        if (be <= 0) {
            boolean var8 = true;
        }

        newOpts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream isBm = null;
        image.compress(CompressFormat.JPEG, 80, baos);
        int options = 80;

        try {
            while (baos.toByteArray().length / 1024 > 10) {
                LogUtils.i("ImageHelper", "maxMemory = " + Runtime.getRuntime().maxMemory());
                LogUtils.i("ImageHelper", "totalMemory = " + Runtime.getRuntime().totalMemory());
                LogUtils.i("ImageHelper", "freeMemory = " + Runtime.getRuntime().freeMemory());
                LogUtils.i("ImageHelper", "baos.toByteArray().length = " + baos.toByteArray().length);
                baos.reset();
                image.compress(CompressFormat.JPEG, options, baos);
                options -= 30;
            }

            isBm = new ByteArrayInputStream(baos.toByteArray());
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, (Rect) null, (Options) null);
            LogUtils.i("ImageHelper", "maxMemory = " + Runtime.getRuntime().maxMemory());
            LogUtils.i("ImageHelper", "totalMemory = " + Runtime.getRuntime().totalMemory());
            LogUtils.i("ImageHelper", "freeMemory = " + Runtime.getRuntime().freeMemory());
            LogUtils.i("ImageHelper", "baos.toByteArray().length = " + baos.toByteArray().length);
            LogUtils.i("ImageHelper", "length = " + getBitmapsize(bitmap));
            image.recycle();
            return bitmap;
        } catch (Exception var7) {
            image.recycle();

            try {
                isBm.close();
                baos.close();
                baos = null;
                isBm = null;
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            return null;
        }
    }

    public static long getBitmapsize(Bitmap bitmap) {
        return VERSION.SDK_INT >= 12 ? (long) bitmap.getByteCount() : (long) (bitmap.getRowBytes() * bitmap.getHeight());
    }

    public static void saveBitmap(String path, Bitmap img) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            FileOutputStream ops = new FileOutputStream(path);
            img.compress(CompressFormat.JPEG, 100, baos);
            ops.write(baos.toByteArray());
            ops.flush();
            ops.close();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    public static void saveBitmap(String path, Bitmap img, final int compress) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            FileOutputStream ops = new FileOutputStream(path);
            img.compress(CompressFormat.JPEG, compress, baos);
            ops.write(baos.toByteArray());
            ops.flush();
            ops.close();
            img.recycle();
            img = null;
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    /**
     * 保存方法
     */
    public static void saveBitmapInPictureDir(Context context, String filename, Bitmap bitmap) {
        String filepath = SystemUtil.getPicturePath(context, filename);
        try {
            FileOutputStream out = new FileOutputStream(new File(filepath));
            bitmap.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateToDegrees(Bitmap tempBitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees);
        return Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight(), matrix,
                true);
    }

    /**
     * 旋转，翻转
     *
     * @param tempBitmap 原始图像
     * @param degree     旋转角度
     * @param xFlip      沿X轴翻转
     * @param yFlip      沿Y轴翻转
     * @return 预处理后图像
     */
    public static Bitmap rotateAndFlip(Bitmap tempBitmap, float degree, boolean xFlip, boolean yFlip) {
        Matrix matrix = new Matrix();
        matrix.reset();
        if (yFlip) {
            matrix.postScale(1, -1);
        }
        if (xFlip) {
            matrix.postScale(-1, 1);
        }
        matrix.postRotate(degree);

        return Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight(), matrix,
                true);
    }

    public static Bitmap scaleImage(Bitmap tmpBitmap, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setScale(scaleX, scaleY);
        return Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight(), matrix,
                true);
    }

    public static Bitmap getBitmapFromView(View curView) {
        View view = curView;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        int width = view.getWidth();
        int height = view.getHeight();
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        view.destroyDrawingCache();
        bitmap = null;
        return bitmap1;
    }

    /**
     * 对bitmap进行旋转
     *
     * @param bitmap
     * @param i
     * @return
     */
    public Bitmap matrixBitmap(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        Bitmap finalBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return finalBitmap;
    }

    public static Bitmap decodeWithCompressBitmap(Activity context, String filepath, int compress) {
        if (context == null) {
            return null;
        }
        float density = context.getResources().getDisplayMetrics().density;
        Display display = context.getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
        int ratioWidth = (int) Math.ceil(options.outWidth / (screenWidth / 3));
        int ratioHeight = (int) Math.ceil(options.outHeight / (screenHeight / 3));
        int sampleSize = 1;
        if (ratioWidth > 1 && ratioHeight > 1) {
            if (ratioWidth > ratioHeight) {
                sampleSize = ratioWidth;
            } else {
                sampleSize = ratioHeight;
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        bitmap = BitmapFactory.decodeFile(filepath, options);
        int wid = bitmap.getWidth();
        return bitmap;
    }
    public static Bitmap snapShotWithStatusBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        bmp = Bitmap.createBitmap(bmp, 0, 25, width, height-25);
        view.destroyDrawingCache();
        return bmp;

    }
    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


}

