package com.yuxi.jxs.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

    public static boolean saveBitmap(Bitmap bitmap, String filePath) {
        File file=new File(filePath);
        try {
            BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap( drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

//    public static Bitmap drawable2Bitmap(Drawable drawable) {
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        } else if (drawable instanceof NinePatchDrawable) {
//            Bitmap bitmap = Bitmap
//                    .createBitmap(
//                            drawable.getIntrinsicWidth(),
//                            drawable.getIntrinsicHeight(),
//                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                                    : Bitmap.Config.RGB_565);
//            Canvas canvas = new Canvas(bitmap);
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
//                    drawable.getIntrinsicHeight());
//            drawable.draw(canvas);
//            return bitmap;
//        } else {
//            return null;
//        }
//    }

}
