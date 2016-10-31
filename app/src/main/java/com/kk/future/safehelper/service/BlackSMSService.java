package com.kk.future.safehelper.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.kk.future.safehelper.recevier.BlockSMSRecevier;
import com.kk.future.safehelper.utils.LogCatUtil;


/**
 * Author: Future <br>
 * QQ: <br>
 * Description:拦截黑名单短信的服务，依靠广播接受者得知收到短信，在判断是否在黑名单中，在操作<br>
 * date: 2016/10/31  17:10.
 */

public class BlackSMSService extends Service {
    private static final String TAG = "main";
    BlockSMSRecevier blockSMS;
    IntentFilter filter;

    @Override
    public void onCreate() {
        super.onCreate();
        blockSMS = new BlockSMSRecevier();
        filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.provider.Telephony.SMS_RECEIVED_2");
        filter.addAction("android.provider.Telephony.GSM_SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(blockSMS, filter);
        LogCatUtil.getInstance().i("main", "registerReceiver(blockSMS,filter);");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(blockSMS);
        LogCatUtil.getInstance().i("main", "unregisterReceiver(blockSMS);;");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
