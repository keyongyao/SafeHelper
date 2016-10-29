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

    /**
     * 主界面的功能排列
     */
    public static class FunArray {
        public final static int CLEAN = 1;
        public final static int PROCESS = 2;
        public final static int NET = 3;
        public final static int PHONE_SAFE = 4;
        public final static int COMMUNICATION = 5;
        public final static int TOOL_BOX = 6;
        public final static int SETTING = 9;
    }

    /**
     * 进程模块一些相关的常量
     */
    public static class Process {
        public final static String IS_HIDDEN_SYSPROCESS = "is_hidden_sysProcess";
        public final static String LOCK_SCREEN_CLEAR = "lock_screen_clear";
    }
}
