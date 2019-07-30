package com.yuxi.jxs.util;

import android.util.Log;

import com.yuxi.jxs.BuildConfig;

/**
 * author 陈开.
 * Date: 2019/7/10
 * Time: 16:27
 */
public class LogUtils {
    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message);
        }
    }

}
