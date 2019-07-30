package com.aigame.basecore.job.helper;

import android.util.Log;

import com.aigame.basecore.job.config.Configuration;

/**
 * by default, logs to nowhere
 */
public class JobLog {

    private static final String TAG = "JOBS";

    private static boolean DBG = Configuration.SHOWLOG;

    public static boolean isDebug() {
        return DBG;
    }

    public static void d(String text, Object... args) {
        if (isDebug()) {
            Log.d(TAG, String.format(text, args));
        }
    }

    public static void e(Throwable t, String text, Object... args) {
        if (isDebug()) {
            Log.d(TAG, String.format(text, args), t);
        }
    }

    public static void e(String text, Object... args) {
        if (isDebug()) {
            Log.d(TAG, String.format(text, args));
        }
    }
}
