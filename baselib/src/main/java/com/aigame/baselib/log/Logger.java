package com.aigame.baselib.log;


/**
 * 日志工具输出类
 *
 * 为保证日志功能的唯一性，该类只对当前包有权限，
 * 请勿更改该类的权限为public
 */
 public class Logger {
    private static final ILogPrinter NORMAL_LOGGER = new NormalLogger();
    private static final ThreadLocal<ILogPrinter> LOCAL_LOG_PRINTER = new ThreadLocal<ILogPrinter>();
    private static ILogPrinter sPrinter = NORMAL_LOGGER;



    /**
     * 临时设置查看当前Log中得方法调用个数
     *
     * @param methodCount 方法个数
     */
    public static void setTempMethodCount(int methodCount) {
        FormatLogger formatLogger = new FormatLogger();
        LOCAL_LOG_PRINTER.set(formatLogger);
        formatLogger.t(methodCount);
    }

    private static ILogPrinter getLogPrinter() {
        ILogPrinter printer = LOCAL_LOG_PRINTER.get();
        if (printer != null) {
            LOCAL_LOG_PRINTER.remove();
            return printer;
        }
        return sPrinter;
    }

    public static void v(String TAG, String message) {
        ILogPrinter printer = getLogPrinter();
        printer.v(TAG, message);
    }

    public static void i(String TAG, String message) {
        ILogPrinter printer = getLogPrinter();
        printer.i(TAG, message);
    }

    public static void d(String TAG, String message) {
        ILogPrinter printer = getLogPrinter();
        printer.d(TAG, message);
        ExposureLog.logBuffer.log(TAG, "D", message);
    }

    public static void w(String TAG, String message) {
        ILogPrinter printer = getLogPrinter();
        printer.w(TAG, message);
        ExposureLog.logBuffer.log(TAG, "W", message);
    }

    public static void e(String TAG, String message) {
        ILogPrinter printer = getLogPrinter();
        printer.e(TAG, message);
        ExposureLog.logBuffer.log(TAG, "E", message);
    }

    public static void e(String TAG, String message, Throwable e) {
        ILogPrinter printer = getLogPrinter();
        printer.e(e, TAG, message);
        ExposureLog.logBuffer.log(TAG, "E", message);
    }

}
