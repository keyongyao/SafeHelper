package com.kk.future.safehelper.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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

    /**
     * 备份全部短信
     *
     * @return 是否备份成功
     */
    public static boolean backupSMS(Context context, File dest) {
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), new String[]{"address",
                "date_sent", "body"}, null, null, null);
        final ArrayList<SMSBackupBean> smsBeanList = new ArrayList<>();
        while (cursor.moveToNext()) {
            SMSBackupBean bean = new SMSBackupBean();
            bean.address = cursor.getString(0);
            bean.date_sent = cursor.getString(1);
            bean.body = cursor.getString(2);
            smsBeanList.add(bean);
        }
        // 写出到文件
        XmlSerializer xmlSerializer = Xml.newSerializer();

        try {
            OutputStream outputStream = new FileOutputStream(dest);
            xmlSerializer.setOutput(outputStream, "utf-8");
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "SMSs");
            for (SMSBackupBean bean : smsBeanList
                    ) {
                xmlSerializer.startTag(null, "sms");

                xmlSerializer.startTag(null, "address");
                xmlSerializer.text(bean.address);
                xmlSerializer.endTag(null, "address");

                xmlSerializer.startTag(null, "date_sent");
                xmlSerializer.text(bean.date_sent);
                xmlSerializer.endTag(null, "date_sent");

                xmlSerializer.startTag(null, "body");
                xmlSerializer.text(bean.body);
                xmlSerializer.endTag(null, "body");
                xmlSerializer.endTag(null, "sms");

            }
            xmlSerializer.endTag(null, "SMSs");
            xmlSerializer.endDocument();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private static class SMSBackupBean {
        public String address;
        public String date_sent;
        public String body;
    }
}
