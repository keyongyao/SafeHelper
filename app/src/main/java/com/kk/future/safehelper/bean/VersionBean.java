package com.kk.future.safehelper.bean;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:保存更新版本信息<br>
 * date: 2016/10/27  22:07.
 */

public class VersionBean {
    public String description;
    public String downloadUrl;
    public int versionCode;
    public String versionName;

    @Override
    public String toString() {
        return "VersionBean{" +
                "description='" + description + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                '}';
    }
}
