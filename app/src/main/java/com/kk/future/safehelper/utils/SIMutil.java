package com.kk.future.safehelper.utils;

import android.app.Service;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:SIM 卡 的 工具类<br>
 * date: 2016/10/30  22:13.
 */

public class SIMutil {

    public static String getSimSerialNumber(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        return manager.getSimSerialNumber();
    }

}
