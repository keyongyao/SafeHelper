package com.kk.future.safehelper.bean;

import android.graphics.drawable.Drawable;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:程序锁的信息<br>
 * date: 2016/10/31  15:28.
 */
public class AppInfo {
    //	名称,包名,图标,(内存,sd卡),(系统,用户)
    public String name;
    public String packageName;
    public Drawable icon;
    public boolean isSdCard;
    public boolean isSystem;

}
