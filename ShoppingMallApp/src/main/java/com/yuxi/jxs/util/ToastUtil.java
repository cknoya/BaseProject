package com.yuxi.jxs.util;

import android.content.Context;
import android.widget.Toast;


public class ToastUtil {
    public static void shorts(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void longs(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 之前显示的内容
     */

    private static String oldMsg;

    /**
     * Toast对象
     */

    private static Toast toast = null;

    /**
     * 第一次时间
     */

    private static long oneTime = 0;

    /**
     * 第二次时间
     */

    private static long twoTime = 0;


    /**
     * 显示Toast
     *
     * @param context
     * @param message
     */

    public static void showToast(Context context, String message) {
        if (context == null) {
            return;
        }
        if (toast == null) {

            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);

            toast.show();

            oneTime = System.currentTimeMillis();

        } else {

            twoTime = System.currentTimeMillis();

            if (message.equals(oldMsg)) {

                if (twoTime - oneTime > Toast.LENGTH_SHORT) {

                    toast.show();

                }

            } else {

                oldMsg = message;

                toast.setText(message);

                toast.show();

            }

        }

        oneTime = twoTime;

    }


}



