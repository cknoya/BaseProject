package com.aigame.baselib.log;

/**
 * 日志打印输出
 *
 */
public interface ILogPrinter {


    ILogPrinter t(int methodCount);


    void d(String tag, String message, Object... args);

    void e(String tag, String message, Object... args);

    void e(Throwable throwable, String tag, String message, Object... args);

    void w(String tag, String message, Object... args);

    void i(String tag, String message, Object... args);

    void v(String tag, String message, Object... args);


}
