package com.kk.future.safehelper;

import android.app.Application;

import com.kk.future.safehelper.utils.LogCatUtil;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:自定义一些app全局的 参数 配置  对象<br>
 * date: 2016/10/27  16:13.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 启用 LogCatUtil
        LogCatUtil.getInstance().enableLog(true);
        // 配置 xUtil3
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);  // 是否开启 xUtil3 的调试

    }
}
