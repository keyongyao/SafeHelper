package com.kk.future.safehelper.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description: shareprefernce 工具类<br>
 * date: 2016/10/29  19:29.
 */

public class SPUtil {
    public static String CONFIG = "config";

    public static boolean getBoolean(Context context, String key, boolean def) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getBoolean(key, def);
    }

    public static void putboolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
