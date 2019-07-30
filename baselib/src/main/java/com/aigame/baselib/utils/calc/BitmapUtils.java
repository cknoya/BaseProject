package com.aigame.baselib.utils.calc;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.provider.MediaStore;

import com.aigame.baselib.io.file.CloseUtils;
import com.aigame.baselib.utils.crash.ExceptionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片压缩工具类
 */
public class BitmapUtils {

    /**
     * 压缩图片
     *
     * @param image 待压缩图片对象
     * @return 压缩后图片对象
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        try {
            while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;//每次都减少10
            }

            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
            return bitmap;
        } catch (Exception e) {
            image.recycle();
            try {
                baos.close();
            } catch (IOException e1) {
                ExceptionUtils.printStackTrace(e1);
            }
            return null;
        }

    }

    /**
     * 裁剪图片
     *
     * @param bitmap 待裁剪图片对象
     * @param point  裁剪点
     * @return 裁剪后图片对象
     */
    public static Bitmap centerCrop(Bitmap bitmap, Point point) {

        final int tw = bitmap.getWidth();
        final int th = bitmap.getHeight();
        if (tw != point.x || th != point.y) {
            Bitmap bm = Bitmap.createBitmap(point.x, point.y, bitmap.getConfig());

            // Use ScaleType.CENTER_CROP, except we leave the top edge at the top.
            float scale;
            float dx = 0, dy = 0;
            if (tw * point.x > point.y * th) {
                scale = (float) point.x / (float) th;
                dx = (point.y - tw * scale) * 0.5f;
            } else {
                scale = (float) point.y / (float) tw;
                dy = (point.x - th * scale) * 0.5f;
            }
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate((int) (dx + 0.5f), 0);

            Canvas canvas = new Canvas(bm);
            canvas.drawBitmap(bitmap, matrix, null);
            canvas.setBitmap(null);
            bitmap = bm;
        }
        return bitmap;
    }

    /**
     * @param src
     * @param ratio w/h 目标宽高比
     * @return
     */
    public static Bitmap centerCrop(Bitmap src, int ratio) {
        if (src == null || ratio == 0) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        /**
         * 如果目标宽高比 > 原图，则原图宽度不变，高度为(h1 = w / ratio)
         * 画布宽高(w, h1), 原图切割点(0, (h - h1) / 2)
         *
         * 如果目标宽高比 < 原图，则原图高度不变，宽度为(w1 = h * ratio),
         * 画布宽高(w1, h), 原图切割点((w - w1) / 2, 0)
         */
        if (ratio > w / h) {
            int h1 = w / ratio;
            Bitmap bitmap = Bitmap.createBitmap(src, 0, (h - h1) / 2, w, h1);
            src.recycle();
            return bitmap;

        } else {
            int w1 = h * ratio;
            Bitmap bitmap = Bitmap.createBitmap(src, (w - w1) / 2, 0, w1, h);
            src.recycle();
            return bitmap;
        }
    }

    /**
     * 将bitmap写入文件
     *
     * @param path   保存路径
     * @param bitmap 图片对象
     */
    public static void saveBitmap(String path, Bitmap bitmap) {
        FileOutputStream ops = null;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ops = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            ops.write(baos.toByteArray());
            ops.flush();
        } catch (IOException e) {
            ExceptionUtils.printStackTrace(e);
        } finally {
            if (ops != null) {
                CloseUtils.closeIOQuietly(ops);
            }
        }
    }

    /**
     * 将图片进行高斯模糊
     *
     * @param bitmap 图片对象
     * @param radius 半径
     * @return
     */
    public static Bitmap createBlurBitmap(Bitmap bitmap, int radius) {
        if (bitmap == null || radius < 1) {
            return (null);
        }
        Bitmap tempBitmap = null;
        if (bitmap.getConfig() != null) {
            tempBitmap = bitmap.copy(bitmap.getConfig(), true);
            try {
                int w = tempBitmap.getWidth();
                int h = tempBitmap.getHeight();
                int[] pix = new int[w * h];
                tempBitmap.getPixels(pix, 0, w, 0, 0, w, h);
                int wm = w - 1;
                int hm = h - 1;
                int wh = w * h;
                int div = radius + radius + 1;
                int r[] = new int[wh];
                int g[] = new int[wh];
                int b[] = new int[wh];
                int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
                int vmin[] = new int[Math.max(w, h)];
                int divsum = (div + 1) >> 1;
                divsum *= divsum;
                int dv[] = new int[256 * divsum];
                for (i = 0; i < 256 * divsum; i++) {
                    dv[i] = (i / divsum);
                }
                yw = yi = 0;
                int[][] stack = new int[div][3];
                int stackpointer;
                int stackstart;
                int[] sir;
                int rbs;
                int r1 = radius + 1;
                int routsum, goutsum, boutsum;
                int rinsum, ginsum, binsum;
                for (y = 0; y < h; y++) {
                    rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                    for (i = -radius; i <= radius; i++) {
                        p = pix[yi + Math.min(wm, Math.max(i, 0))];
                        sir = stack[i + radius];
                        sir[0] = (p & 0xff0000) >> 16;
                        sir[1] = (p & 0x00ff00) >> 8;
                        sir[2] = (p & 0x0000ff);
                        rbs = r1 - Math.abs(i);
                        rsum += sir[0] * rbs;
                        gsum += sir[1] * rbs;
                        bsum += sir[2] * rbs;
                        if (i > 0) {
                            rinsum += sir[0];
                            ginsum += sir[1];
                            binsum += sir[2];
                        } else {
                            routsum += sir[0];
                            goutsum += sir[1];
                            boutsum += sir[2];
                        }
                    }
                    stackpointer = radius;
                    for (x = 0; x < w; x++) {
                        r[yi] = dv[rsum];
                        g[yi] = dv[gsum];
                        b[yi] = dv[bsum];
                        rsum -= routsum;
                        gsum -= goutsum;
                        bsum -= boutsum;
                        stackstart = stackpointer - radius + div;
                        sir = stack[stackstart % div];
                        routsum -= sir[0];
                        goutsum -= sir[1];
                        boutsum -= sir[2];
                        if (y == 0) {
                            vmin[x] = Math.min(x + radius + 1, wm);
                        }
                        p = pix[yw + vmin[x]];
                        sir[0] = (p & 0xff0000) >> 16;
                        sir[1] = (p & 0x00ff00) >> 8;
                        sir[2] = (p & 0x0000ff);
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                        rsum += rinsum;
                        gsum += ginsum;
                        bsum += binsum;
                        stackpointer = (stackpointer + 1) % div;
                        sir = stack[(stackpointer) % div];
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                        rinsum -= sir[0];
                        ginsum -= sir[1];
                        binsum -= sir[2];
                        yi++;
                    }
                    yw += w;
                }
                for (x = 0; x < w; x++) {
                    rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                    yp = -radius * w;
                    for (i = -radius; i <= radius; i++) {
                        yi = Math.max(0, yp) + x;
                        sir = stack[i + radius];
                        sir[0] = r[yi];
                        sir[1] = g[yi];
                        sir[2] = b[yi];
                        rbs = r1 - Math.abs(i);
                        rsum += r[yi] * rbs;
                        gsum += g[yi] * rbs;
                        bsum += b[yi] * rbs;
                        if (i > 0) {
                            rinsum += sir[0];
                            ginsum += sir[1];
                            binsum += sir[2];
                        } else {
                            routsum += sir[0];
                            goutsum += sir[1];
                            boutsum += sir[2];
                        }
                        if (i < hm) {
                            yp += w;
                        }
                    }
                    yi = x;
                    stackpointer = radius;
                    for (y = 0; y < h; y++) {
                        pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
                        rsum -= routsum;
                        gsum -= goutsum;
                        bsum -= boutsum;
                        stackstart = stackpointer - radius + div;
                        sir = stack[stackstart % div];
                        routsum -= sir[0];
                        goutsum -= sir[1];
                        boutsum -= sir[2];
                        if (x == 0) {
                            vmin[y] = Math.min(y + r1, hm) * w;
                        }
                        p = x + vmin[y];
                        sir[0] = r[p];
                        sir[1] = g[p];
                        sir[2] = b[p];
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                        rsum += rinsum;
                        gsum += ginsum;
                        bsum += binsum;
                        stackpointer = (stackpointer + 1) % div;
                        sir = stack[stackpointer];
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                        rinsum -= sir[0];
                        ginsum -= sir[1];
                        binsum -= sir[2];
                        yi += w;
                    }
                }
                tempBitmap.setPixels(pix, 0, w, 0, 0, w, h);
            } catch (Exception e) {
                ExceptionUtils.printStackTrace(e);
            }
        }
        return (tempBitmap);
    }

    /**
     * 添加遮罩
     *
     * @param bitmap 原图
     * @param color  遮罩颜色
     */
    public static void addMask(Bitmap bitmap, int color) {
        if (bitmap == null) {
            return;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(bitmap);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawColor(color);
    }

    /**
     * 图片等比压缩
     *
     * @param bmp     图片对象
     * @param file    保存文件
     * @param minEdge 最小边
     * @param degree  角度
     * @param quality 质量
     */
    public static void compressBitmapToFile(Bitmap bmp, File file, int minEdge, int degree, int quality) {
        int curWidth = bmp.getWidth();
        int curHeight = bmp.getHeight();
        float scale;
        if (curWidth < curHeight) {
            scale = 1.0f * minEdge / curWidth;
        } else {
            scale = 1.0f * minEdge / curHeight;
        }
        // 压缩Bitmap到对应尺寸并且调整图片方向
        Matrix matrix = new Matrix();
        if (scale < 1.0f) {
            matrix.setScale(scale, scale);
        }
        matrix.postRotate(degree);
        Bitmap result = Bitmap.createBitmap(bmp, 0, 0, curWidth, curHeight, matrix, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();

        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    ExceptionUtils.printStackTrace(e);
                }
            }
        }
    }

    /**
     * 获取最近照片
     * 这个方法最好是异步去调用
     *
     * @param context
     * @return
     */
    public static String getRecentlyPhotoPath(Context context) {
        Cursor myCursor = null;
        String pathLast = "";
        // Create a Cursor to obtain the file Path for the large image
        String[] largeFileProjection = {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.ORIENTATION,
                MediaStore.Images.ImageColumns.DATE_TAKEN};
        String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
        myCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, largeFileProjection,
                null, null, largeFileSort);

        if (myCursor == null) {
            return pathLast;
        }
        if (myCursor.getCount() < 1) {
            myCursor.close();
            return pathLast;
        }

        while (myCursor.moveToNext()) {
            String data = myCursor.getString(myCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            File f = new File(data);
            if (f.exists()) {//第一个图片文件，就是最近一次拍照的文件；
                pathLast = f.getPath();
                System.out.println("f.getPath() = " + pathLast);
                myCursor.close();
                return pathLast;
            }
        }
        myCursor.close();
        return pathLast;
    }
}

