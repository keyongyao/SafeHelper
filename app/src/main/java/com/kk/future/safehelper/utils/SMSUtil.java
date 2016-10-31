package com.kk.future.safehelper.utils;

import android.content.Intent;
import android.telephony.SmsMessage;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:短信工具类<br>
 * date: 2016/10/31  9:13.
 */

public class SMSUtil {

    private static String originatingAddress;

    /**
     * @param intent 收到短信时 广播的Intent
     * @return 收到 短信的内容
     */
    public static String getLastestSMS(Intent intent) {
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        StringBuilder msg = new StringBuilder();
        for (Object oo : pdus
                ) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) oo);
            String originatingAddress = sms.getOriginatingAddress();
            msg.append(sms.getMessageBody());
        }
        return msg.toString();
    }

    /**
     * @param intent 收到短信时 随广播的 intent
     * @return 发送人 的 手机号
     */
    public static String getSender(Intent intent) {
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        StringBuilder msg = new StringBuilder();
        for (Object oo : pdus
                ) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) oo);
            originatingAddress = sms.getOriginatingAddress();
        }
        return originatingAddress;
    }
}
