package com.kk.future.safehelper.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:覆盖前面的toast 不用等待前面的Toast 消失 再 弹出下一个Toast<br>
 * date: 2016/10/28  11:44.
 */

public class SuperToast {
    public static Toast toast;

    public static void show(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
