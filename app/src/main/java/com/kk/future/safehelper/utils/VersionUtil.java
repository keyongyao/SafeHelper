package com.kk.future.safehelper.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:封装一些版本的常用方法<br>
 * date: 2016/10/27  15:40.
 */

public class VersionUtil {
    private static final String TAG = "VersionUtil";

    /**
     * @param mContext 上下文
     * @return versionCode  在 build.gradle中配置
     */
    public static int getLocalVersionCode(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param mContext 上下文
     * @return versionName 在 build.gradle 中配置
     */
    public static String getLocalVersionName(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "未知";
    }
}
