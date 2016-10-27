package com.kk.future.safehelper.utils;

import android.util.Log;


/**
 * Author: Future <br>
 * QQ: <br>
 * Description:封装一些Log的常用方法,单例模式<br>
 * date: 2016/10/27  15:40.
 */

public final class LogCatUtil {
    private static boolean log_open = false;

    private LogCatUtil() {
    }

    /**
     * @return 返回LogCatUtil的实例
     */
    public static final LogCatUtil getInstance() {
        return Hodler.instance;
    }

    /**
     * @return 日志是否打开
     */
    public boolean isLogOpen() {
        return log_open;
    }

    /**
     * @param enable 是否启用日志
     */
    public void enableLog(boolean enable) {
        LogCatUtil.log_open = enable;
    }

    /**
     * 详细信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public void v(String tag, String msg) {
        if (log_open) {
            Log.v(tag, msg);
        }
    }

    /**
     * 调试信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public void d(String tag, String msg) {
        if (log_open) {
            Log.d(tag, msg);
        }

    }

    /**
     * 提示信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public void i(String tag, String msg) {
        if (log_open) {
            Log.i(tag, msg);
        }

    }

    /**
     * 警告信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public void w(String tag, String msg, Exception exception) {
        if (log_open) {
            Log.w(tag, msg, exception);

        }

    }

    /**
     * 错误信息
     *
     * @param tag 标签
     * @param msg 消息
     */
    public void e(String tag, String msg, Exception exception) {
        if (log_open) {
            Log.e(tag, msg, exception);
        }

    }

    /**
     * 实例的容器  延迟加载 LogCatUtil instance
     */
    private static class Hodler {
        private static final LogCatUtil instance = new LogCatUtil();
    }

}
