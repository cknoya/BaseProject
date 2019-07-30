package com.yuxi.jxs.sp;

import android.text.TextUtils;
import android.util.Log;



/**
 * Created by songguobin on 2017/6/13.
 */

public class ExceptionUtils {

    private static final String TAG = ExceptionUtils.class.getSimpleName();

    public static void printStackTrace(String customTag,Throwable e){

        if (e != null && e.getMessage() != null) {
            if(!TextUtils.isEmpty(customTag)){
                Log.d(customTag, e.getMessage());
            }else{
                Log.d(TAG, e.getMessage());
            }
        }

        if(e != null){
            e.printStackTrace();
        }

    }

    public static void printStackTrace(Throwable e){
        if (e != null && e.getMessage() != null) {
            Log.d(TAG, e.getMessage());
        }
        if(e != null){
            e.printStackTrace();
        }
    }
}
