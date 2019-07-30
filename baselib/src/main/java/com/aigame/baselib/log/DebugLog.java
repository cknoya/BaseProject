package com.aigame.baselib.log;

import com.aigame.baselib.log.ExposureLog;
import com.aigame.baselib.log.IExposureLog;

/**
 * @author songguobin
 *
 * adapter layer for {@link ExposureLog}
 *
 */
public class DebugLog {

    private static final IExposureLog EXPOSURE_LOG = new ExposureLog();

    public static void setDebug(boolean isDebug){
        EXPOSURE_LOG.setDebug(isDebug);
    }

    public static  boolean isDebug(){
        return EXPOSURE_LOG.isDebug();
    }

    public static  void log(String tag, Object... msg) {
        EXPOSURE_LOG.log(tag,msg);
    }

    public static  void i(String tag, Object... msg) {
        EXPOSURE_LOG.i(tag,msg);
    }

    public static   void d(String tag, Object... msg){
        EXPOSURE_LOG.d(tag,msg);
    }

    public static  void v(String tag, Object... msg){
        EXPOSURE_LOG.v(tag,msg);
    }

    public static  void w(String tag, Object... msg){
        EXPOSURE_LOG.w(tag,msg);
    }

    public static  void e(String tag, Object... msg){
        EXPOSURE_LOG.e(tag,msg);
    }


}
