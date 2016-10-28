package com.kk.future.safehelper.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description: 管理 操纵 APK <br>
 * date: 2016/10/28  11:16.
 */

public class APKUtil {
    /**
     * 安装APK
     *
     * @param context 上下文
     * @param apk     需要安装的APK 最终由用户决定是否安装
     */
    public static void installAPK(Context context, File apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(apk),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("apk"));
        context.startActivity(intent);
    }
}
