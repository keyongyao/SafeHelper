package com.kk.future.safehelper.service;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.kk.future.safehelper.dao.BlackNumDao;
import com.kk.future.safehelper.utils.LogCatUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:挂断黑名单中的电话，并从通话记录总删除该记录<br>
 * date: 2016/10/31  17:11.
 */

public class BlackCallService extends Service {
    String incommingPhone;
    MyPhoneStateListener stateListener;
    TelephonyManager telephonyManager;

    @Override
    public void onCreate() {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        stateListener = new MyPhoneStateListener(getApplicationContext());
        telephonyManager.listen(stateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(stateListener, PhoneStateListener.LISTEN_NONE);
        stateListener.unRegisterContentObserver();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


class MyPhoneStateListener extends PhoneStateListener {
    private static final String TAG = "main";
    Context mContext;
    ContentResolver resolver;
    ContentObserver contentObserver;

    public MyPhoneStateListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCallStateChanged(int state, final String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if (state == TelephonyManager.CALL_STATE_RINGING) {
            LogCatUtil.getInstance().i(TAG, "有电话来了");
        }
        boolean block = new BlackNumDao(mContext).isInBlackNumList(incomingNumber);

        if (block) {
            LogCatUtil.getInstance().i(TAG, incomingNumber + "在黑名单中");
            Class<?> clazz = null;
            try {
                //1,获取ServiceManager字节码文件
                clazz = Class.forName("android.os.ServiceManager");
                //2,获取方法
                Method method = clazz.getMethod("getService", String.class);
                //3,反射调用此方法
                IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);

                ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
                iTelephony.endCall();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            resolver = mContext.getContentResolver();
            contentObserver = new ContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    LogCatUtil.getInstance().i(TAG, "最近联系人表发生了变化");
                    // 删除 黑明单的电话
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                        throw new IllegalStateException("没有读取和写入通话记录的权限");
                    }
                    resolver.delete(CallLog.Calls.CONTENT_URI, "number=?", new String[]{incomingNumber});
                }
            };
            resolver.registerContentObserver(CallLog.Calls.CONTENT_URI, true, contentObserver);
        }
    }

    /**
     * 取消注册 内容观察者
     */
    public void unRegisterContentObserver() {
        if (resolver != null)
            resolver.unregisterContentObserver(contentObserver);
    }

}


