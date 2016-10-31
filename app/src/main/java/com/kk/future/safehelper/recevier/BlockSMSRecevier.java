package com.kk.future.safehelper.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsMessage;

import com.kk.future.safehelper.dao.BlackNumDao;
import com.kk.future.safehelper.utils.LogCatUtil;


/**
 * Author: Future <br>
 * QQ: <br>
 * Description:<br>
 * date: 2016/10/31  17:05.
 */

public class BlockSMSRecevier extends BroadcastReceiver {
    private static final String TAG = "main";

    // TODO: 2016/9/27  好像有序广播改为无序广播了  服务接收不正常
    @Override
    public void onReceive(Context context, Intent intent) {
        LogCatUtil.getInstance().i(TAG, "收到短信了");

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        String originatingAddress = "";
        for (Object oo : pdus
                ) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) oo);
            originatingAddress = message.getOriginatingAddress();
        }
        boolean isOnList = new BlackNumDao(context).isInBlackNumList(originatingAddress);
        LogCatUtil.getInstance().i(TAG, "是否在黑名单中" + isOnList + "");
        if (isOnList) {
            abortBroadcast();
            Uri uri = Uri.parse("content://sms/");// 收信箱
            // TODO: 2016/10/31  无法删除指定的短信 可能是安全机制
            int delete = context.getContentResolver().delete(uri, "address=?", new String[]{originatingAddress});// 删除该短信
            LogCatUtil.getInstance().i(TAG, "delete:" + uri + originatingAddress + " " + delete);
            LogCatUtil.getInstance().i(TAG, "停止了广播");
        }


    }
}
