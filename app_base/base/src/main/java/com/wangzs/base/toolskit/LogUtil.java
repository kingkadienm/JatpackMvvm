package com.wangzs.base.toolskit;

import android.util.Log;

import java.util.Locale;

/**
 * 日志工具类
 * <p>
 */
public final class LogUtil {

    private static final String TAG = "Wangzs";
    private static final String TAG_LOG = " wangzs |||(~_~).zZ --------->>> ";

    private static final int VERBOSE = 5;
    private static final int DEBUG = 4;
    private static final int INFO = 3;
    private static final int WARN = 2;
    private static final int ERROR = 1;

    // 输出的log级别
    private static final int LEVEL = VERBOSE;

    // create trace 开关 true 为输出日志和异常信息
    private static boolean sIsLogEnabled = true;

    private static boolean sIsMockData = false;

    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void setLogEnabled(boolean enabled) {
        sIsLogEnabled = enabled;
    }

    public static boolean isLogEnabled() {
        return sIsLogEnabled;
    }

    public static boolean isMockData() {
        return isLogEnabled() && sIsMockData;
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void v(String tag, String msg) {
        if (sIsLogEnabled && LEVEL >= VERBOSE) {
            Log.v(tag, getCurrentStackTraceElement() + TAG_LOG + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sIsLogEnabled && LEVEL >= DEBUG) {
            Log.d(tag, getCurrentStackTraceElement() + TAG_LOG + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sIsLogEnabled && LEVEL >= INFO) {
            Log.i(tag, getCurrentStackTraceElement() + TAG_LOG + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sIsLogEnabled && LEVEL >= WARN) {
            Log.w(tag, getCurrentStackTraceElement() + TAG_LOG + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sIsLogEnabled && LEVEL >= ERROR) {
            Log.e(tag, getCurrentStackTraceElement() + TAG_LOG + msg);
        }
    }

    /**
     * 获取方法的调用栈信息，并格式化
     *
     * @return
     */
    private static String getCurrentStackTraceElement() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String className = stackTraceElement.getClassName();
        int classNameStartIndex = className.lastIndexOf(".") + 1;
        className = className.substring(classNameStartIndex);
        String methodName = stackTraceElement.getMethodName();
        int methodLine = stackTraceElement.getLineNumber();
        String format = "%s-%s(Line:%d)";
        return String.format(Locale.CHINESE, format, className, methodName, methodLine);
    }

}