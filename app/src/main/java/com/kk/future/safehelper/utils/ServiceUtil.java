package com.kk.future.safehelper.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;

import java.util.List;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:检测 服务是否已经在运行 <br>
 * date: 2016/10/31  16:57.
 */

public class ServiceUtil {
    /**
     * @param activity
     * @param clazzPkg 服务的类名
     * @return true: running
     */
    public static boolean checkRunning(Activity activity, String clazzPkg) {
        ActivityManager service = (ActivityManager) activity.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = service.getRunningServices(500);
        for (ActivityManager.RunningServiceInfo ss : runningServices
                ) {
            if (ss.service.getClassName().equals(clazzPkg)) {
                return true;
            }
        }

        return false;
    }
}
