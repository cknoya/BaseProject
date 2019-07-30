package com.aigame.baselib.log;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExposureLog implements IExposureLog {
	/***
	 * Debug开关，由打包工具修改 true 表示开启调试日志,内测或开发版本使用，缺省值 false
	 * 表示关闭调试日志，灰度或正式版本及提供给第三方的包使用
	 */
	public static final boolean DEBUG_LOG_SWITCH = true;

	/**
	 * Debug总开关
	 */
	private static boolean isDebug = DEBUG_LOG_SWITCH;

	private final static boolean sIsShowTrace = true;

	public final static CircularLogBuffer logBuffer = new CircularLogBuffer();

	@Override
	public  void setDebug(boolean b) {
		isDebug = b;
	}

	@Override
	public  boolean isDebug() {
		return isDebug;
	}


	/**
	 * info类型log，打印不确定性个数参数
	 * 
	 * @param LOG_CLASS_NAME
	 * @param msg
	 */
	@Override
	public  void log(String LOG_CLASS_NAME, Object... msg) {
		if (!TextUtils.isEmpty(LOG_CLASS_NAME) && null != msg) {
			if (isDebug()) {
				printLog(Log.INFO, LOG_CLASS_NAME, concateString(msg), null, 0);
			}
		}
	}

	/**
	 * info类型log，打印不确定性个数参数
	 * 
	 * @param LOG_CLASS_NAME
	 * @param msg
	 */
	@Override
	public  void i(String LOG_CLASS_NAME, Object... msg) {
		if (!TextUtils.isEmpty(LOG_CLASS_NAME) && null != msg) {
			if (isDebug()) {
				printLog(Log.INFO, LOG_CLASS_NAME, concateString(msg), null, 0);
			}
		}
	}

	/**
	 * verbose类型log，打印不确定性个数参数
	 * 
	 * @param LOG_CLASS_NAME
	 * @param msg
	 */
	@Override
	public  void v(String LOG_CLASS_NAME, Object... msg) {

		if (!TextUtils.isEmpty(LOG_CLASS_NAME) && null != msg) {
			if (isDebug()) {
				printLog(Log.VERBOSE, LOG_CLASS_NAME, concateString(msg), null,
						0);
			}
		}
	}

	/**
	 * DEBUG类型log，打印不确定性个数参数,加入logBuffer
	 * 
	 * @param LOG_CLASS_NAME
	 * @param msg
	 */
	@Override
	public  void d(String LOG_CLASS_NAME, Object... msg) {

		if (!TextUtils.isEmpty(LOG_CLASS_NAME) && null != msg) {
			String logStr = concateString(msg);
			logBuffer.log(LOG_CLASS_NAME, "D", logStr);
			if (isDebug()) {
				printLog(Log.DEBUG, LOG_CLASS_NAME, logStr, null, 0);
			}
		}
	}

	/**
	 * WARN类型log，打印不确定性个数参数,加入logBuffer
	 * 
	 * @param LOG_CLASS_NAME
	 * @param msg
	 */
	@Override
	public  void w(String LOG_CLASS_NAME, Object... msg) {

		if (!TextUtils.isEmpty(LOG_CLASS_NAME) && null != msg) {
			String logStr = concateString(msg);
			logBuffer.log(LOG_CLASS_NAME, "W", logStr);
			if (isDebug()) {
				printLog(Log.WARN, LOG_CLASS_NAME, logStr, null, 0);
			}
		}
	}

	/**
	 * DEBUG类型log，打印不确定性个数参数,加入logBuffer
	 * 
	 * @param LOG_CLASS_NAME
	 * @param msg
	 */
	@Override
	public  void e(String LOG_CLASS_NAME, Object... msg) {

		if (!TextUtils.isEmpty(LOG_CLASS_NAME) && null != msg) {
			String logStr = concateString(msg);
			logBuffer.log(LOG_CLASS_NAME, "E", logStr);
			if (isDebug()) {
				printLog(Log.ERROR, LOG_CLASS_NAME, logStr, null, 0);
			}
		}
	}



	/**
	 * Handy function to get a loggable stack trace from a Throwable copy from
	 * {@link Log#getStackTraceString(Throwable)} and make some
	 * modify
	 * 
	 * @param tr
	 *            An exception to log
	 */
	private static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			tr.printStackTrace(pw);
			pw.flush();
			return sw.toString();
		} finally {
			pw.close();
		}
	}



	/**
	 * 打印Log的最终入口
	 * 
	 * @param logType
	 * @param tag
	 * @param message
	 * @param tr
	 * @param methodCount
	 */
	private  void printLog(int logType, String tag, String message,
			Throwable tr, int methodCount) {
		if (isDebug() && !TextUtils.isEmpty(tag) && null != message) {
			if (methodCount > 0 && sIsShowTrace) {
				Logger.setTempMethodCount(5);
			}

			if (tr != null) {
				StringBuilder sb = new StringBuilder(message);
				String exMsg = getStackTraceString(tr);
				sb.append('\n').append(exMsg);
				message = sb.toString();
			}

			switch (logType) {
			case Log.ERROR:
				Logger.e(tag, message);
				break;
			case Log.VERBOSE:
				Logger.v(tag, message);
				break;
			case Log.INFO:
				Logger.i(tag, message);
				break;
			case Log.WARN:
				Logger.w(tag, message);
				break;
			// 默认debug
			case Log.DEBUG:
			default:
				Logger.d(tag, message);
				break;
			}
		}
	}


	private static String concateString(Object... msg) {
		StringBuilder sb = new StringBuilder(100);
		for (Object obj : msg) {
			if (obj != null) {
				sb.append(String.valueOf(obj));
			}
		}
		return sb.toString();
	}

}
