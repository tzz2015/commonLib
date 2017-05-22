package com.zyf.fwms.commonlibrary.utils;

import android.util.Log;

public class LogUtil {

	private final static String TAG = "TCircle";

	//public static int logLevel = Log.ASSERT;

	public static int logLevel = Log.ERROR;
//	 private static boolean logFlag = true;
	private static LogUtil logger = new LogUtil();;

	public static LogUtil getInstance() {
		return logger;
	}

	private LogUtil() {

	}

	private String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			String className = st.getClassName();
			int indexOf = className.lastIndexOf(".");
			return "[类名:" + st.getClassName().substring(indexOf+1,className.length()) + "--方法名:" + st.getMethodName()
					+ "第" + st.getLineNumber() + "行 ]";
		}
		return null;
	}

	public void i(Object str) {
		// if (!AppConf.DEBUG)
		// return;
		if (logLevel <= Log.INFO) {
			String name = getFunctionName();
			if (name != null) {
				Log.i(TAG, name + " - " + str);
			} else {
				Log.i(TAG, str.toString());
			}
		}
	}

	public void v(Object str) {
		// if (!AppConfig.DEBUG)
		// return;
		if (logLevel <= Log.VERBOSE) {
			String name = getFunctionName();
			if (name != null) {
				Log.v(TAG, name + " - " + str);
			} else {
				Log.v(TAG, str.toString());
			}
		}
	}

	public void w(Object str) {
		// if (!AppConfig.DEBUG)
		// return;
		if (logLevel <= Log.WARN) {
			String name = getFunctionName();
			if (name != null) {
				Log.w(TAG, name + " - " + str);
			} else {
				Log.w(TAG, str.toString());
			}
		}
	}

	public void e(Object str) {
		// if (!AppConfig.DEBUG)
		// return;
		if (logLevel <= Log.ERROR) {
			String name = getFunctionName();
			if (name != null) {
				Log.e(TAG, name + " - " + str);
			} else {
				Log.e(TAG, str.toString());
			}
		}
	}

	public void e(Exception ex) {
		// if (!AppConfig.DEBUG)
		// return;
		if (logLevel <= Log.ERROR) {
			Log.e(TAG, "error", ex);
		}
	}

	public void d(Object str) {
		// if (!AppConfig.DEBUG)
		// return;
		if (logLevel <= Log.DEBUG) {
			String name = getFunctionName();
			if (name != null) {
				Log.d(TAG, name + " - " + str);
			} else {
				Log.d(TAG, str.toString());
			}
		}
	}

}
