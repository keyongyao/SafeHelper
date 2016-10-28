package com.kk.future.safehelper.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:关闭一些 closeable<br>
 * date: 2016/10/27  22:17.
 */

public class IOutil {
    public static void closAll(Closeable... ios) {
        for (Closeable io : ios
                ) {
            if (io != null) {
                try {
                    io.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
