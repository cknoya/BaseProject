package com.aigame.baselib.utils.crash;

import android.text.TextUtils;

import com.aigame.baselib.log.DebugLog;


/**
 * Created by songguobin on 2017/6/13.
 */

public class ExceptionUtils {

    private static final String TAG = ExceptionUtils.class.getSimpleName();

    public static void printStackTrace(String customTag,Throwable e){

        if (e != null && e.getMessage() != null) {
            if(!TextUtils.isEmpty(customTag)){
                DebugLog.d(customTag, e.getMessage());
            }else{
                DebugLog.d(TAG, e.getMessage());
            }
        }

        if(e != null && DebugLog.isDebug()){
            e.printStackTrace();
        }

    }

    public static void printStackTrace(Throwable e){
        if (e != null && e.getMessage() != null) {
            DebugLog.d(TAG, e.getMessage());
        }
        if(e != null && DebugLog.isDebug()){
            e.printStackTrace();
        }
    }
}
