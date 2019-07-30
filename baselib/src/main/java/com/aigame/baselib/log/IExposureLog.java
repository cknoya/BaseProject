package com.aigame.baselib.log;

/**
 * @author songguobin
 */
public interface IExposureLog {

    void setDebug(boolean isDebug);

    boolean isDebug();

    void log(String tag, Object... msg);

    void i(String tag, Object... msg);

    void d(String tag, Object... msg);

    void v(String tag, Object... msg);

    void w(String tag, Object... msg);

    void e(String tag, Object... msg);

}
