package com.kk.future.safehelper.utils;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.kk.future.safehelper.recevier.DeviceAdminSampleRecevier;


/**
 * 设备管理器功能呢
 * Created by Administrator on 2016/9/23.
 */

public class MyDeviceManager {
    private static final String TAG = "main";
    Context mContext;
    ComponentName mDeviceAdminSample;
    DevicePolicyManager mDevice;

    public MyDeviceManager(Context context) {
        this.mContext = context;
        mDeviceAdminSample = new ComponentName(mContext, DeviceAdminSampleRecevier.class);
        mDevice = (DevicePolicyManager) mContext.getSystemService(Service.DEVICE_POLICY_SERVICE);

    }


    /**
     * 开启设备管理器的activity
     */
    public void activte() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "设备管理器");
        mContext.startActivity(intent);
    }

    /**
     * 锁屏
     */
    public void lockScreen() {
        if (mDevice.isAdminActive(mDeviceAdminSample)) {
            mDevice.lockNow();
        }
    }

    /**
     * 清楚数据
     */
    public void wipeData() {
        if (mDevice.isAdminActive(mDeviceAdminSample)) {
            mDevice.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        }

    }

    /**
     * @return 设备管理器是否被激活了
     */
    public boolean isActived() {
        return mDevice.isAdminActive(mDeviceAdminSample);
    }
}

