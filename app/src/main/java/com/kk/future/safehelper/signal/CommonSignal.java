package com.kk.future.safehelper.signal;

import com.kk.future.safehelper.R;

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

    /**
     * 手机安全模块一些相关的常量
     */
    public static class PhoneSafe {
        public final static String IS_SET_PWD = "is_set_PWD";
        public final static String PWD = "pwd";
        public final static String IS_BIND_SIM = "is_bind_sim";
        public final static String SAFE_NUM = "safenum";
        public final static String IS_GUARD_OPEN = "is_guard_open";
        public final static String SIM_SNUM = "sim_snum";
    }

    /**
     * 设置中心
     */
    public static class SettingCenter {
        public static final String AUTO_UPDATE = "auto_update";
        public static final String BLACK_NUM = "black_num";
        public static final String INCOME_LOCATION = "income_location";
        public static final String APP_LOCK = "app_lock";
        public static final String CHOOSETYPE = "choose_type";
        public static final String[] STYLE = {"蓝色", "灰色", "青色", "橙色", "透明"};
        public static final int[] STYLEID = {R.drawable.call_locate_blue,
                R.drawable.call_locate_gray,
                R.drawable.call_locate_green,
                R.drawable.call_locate_orange,
                R.drawable.call_locate_white};
        public static final String LOCATIONX = "location_x";
        public static final String LOCATIONY = "location_y";
    }


}
