package com.kk.future.safehelper.signal;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description: 全局的信号量 供 handle message.what 使用<br>
 * date: 2016/10/27  22:56.
 */

public class CommonSignal {
    /**
     * 检查更新结果
     */
    public static class CheckUpdate {
        public final static int HAS_lATEST_VERSION_YES = 1000;
        public final static int HAS_lATEST_VERSION_NO = 1001;
    }
}
