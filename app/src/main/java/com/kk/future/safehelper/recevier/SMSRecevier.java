package com.kk.future.safehelper.recevier;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.utils.LogCatUtil;
import com.kk.future.safehelper.utils.MyDeviceManager;
import com.kk.future.safehelper.utils.SMSUtil;


public class SMSRecevier extends BroadcastReceiver {
    private static final String TAG = "SMSRecevier";
    private MyDeviceManager mDM;

    public SMSRecevier() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String messageBody = SMSUtil.getLastestSMS(intent);
        LogCatUtil.getInstance().i(TAG, "SMSCmdRecevier.onReceive: 收到短信了");
        LogCatUtil.getInstance().i(TAG, messageBody);
        if (messageBody.contains("#*alarm*#")) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
            //  mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        if (messageBody.contains("#*location*#")) {
            // TODO: 2016/9/23  return location
        }
        if (messageBody.contains("#*locakscreen*#")) {
            DevicePolicyManager DM = (DevicePolicyManager) context.getSystemService(Service.DEVICE_POLICY_SERVICE);
            if (DM.isAdminActive(new ComponentName(context, DeviceAdminSampleRecevier.class))) {
                DM.lockNow();
            }
        }
        if (messageBody.contains("#*wipedata*#")) {
            // TODO: 2016/9/23 wipe data
        }
    }
}

