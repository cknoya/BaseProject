package com.yuxi.jxs.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

/**
 * author 陈开.
 * Date: 2019/7/22
 * Time: 11:34
 */
public class CallHelper {
    static public void call(Context context, String telephone)  {
        try {
            // 首先拿到TelephonyManager
            TelephonyManager telMag = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Class<TelephonyManager> c = TelephonyManager.class;

            // 再去反射TelephonyManager里面的私有方法 getITelephony 得到 ITelephony对象
            Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
            //允许访问私有方法
            mthEndCall.setAccessible(true);
            final Object obj = mthEndCall.invoke(telMag, (Object[]) null);
            Method mt2 = obj.getClass().getMethod("endCall");
            //允许访问私有方法
            mt2.setAccessible(true);
            mt2.invoke(obj);
            // 再通过ITelephony对象去反射里面的call方法，并传入包名和需要拨打的电话号码
            Method mt = obj.getClass().getMethod("call", new Class[] { String.class, String.class });
            //允许访问私有方法
            mt.setAccessible(true);
            mt.invoke(obj, new Object[] { context.getPackageName() + "", telephone});

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}