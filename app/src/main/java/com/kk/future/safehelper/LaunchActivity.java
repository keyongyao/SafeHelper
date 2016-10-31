package com.kk.future.safehelper;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kk.future.safehelper.activity.HomeActivity;
import com.kk.future.safehelper.service.BlackCallService;
import com.kk.future.safehelper.service.BlackSMSService;
import com.kk.future.safehelper.service.WatchDogService;
import com.kk.future.safehelper.signal.CommonSignal;
import com.kk.future.safehelper.utils.CopyFileUtil;
import com.kk.future.safehelper.utils.SPUtil;
import com.kk.future.safehelper.utils.ServiceUtil;
import com.kk.future.safehelper.utils.VersionUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LaunchActivity extends AppCompatActivity {
    private TextView tv_version, tv_skipWaite;
    private Handler mHandler;
    private TextView checkingUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 请求不要 ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_launch);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                // 0 秒时 自动跳转 到 HomeActivity
                if (msg.what == 0) {
                    mHandler.removeCallbacksAndMessages(null);
                    startActivity(new Intent(LaunchActivity.this, HomeActivity.class));
                    LaunchActivity.this.finish();
                }
                tv_skipWaite.setText(msg.what + "秒 跳过");
                sendEmptyMessageDelayed(--msg.what, 1000);
            }
        };
        initView();
        setter();
        initDatabases();
        startServices();
    }

    // 根据用户的设置开启相应的服务
    private void startServices() {
        // 自动更新一些细节处理
        if (SPUtil.getBoolean(this, CommonSignal.SettingCenter.AUTO_UPDATE, false)) {
            checkingUpdate.setVisibility(View.VISIBLE);
        }
        // 黑名单 之 电话拦截
        if (SPUtil.getBoolean(this, CommonSignal.SettingCenter.BLACK_NUM, false)
                && !ServiceUtil.checkRunning(this, "com/kk/future/safehelper/service/BlackCallService")) {
            startService(new Intent(this, BlackCallService.class));
        }
        // 黑名单 之 短信拦截
        if (SPUtil.getBoolean(this, CommonSignal.SettingCenter.BLACK_NUM, false)
                && !ServiceUtil.checkRunning(this, "com.kk.future.safehelper.service.BlackSMSService")) {
            startService(new Intent(this, BlackSMSService.class));
        }
        // app lock  服务
        if (SPUtil.getBoolean(this, CommonSignal.SettingCenter.APP_LOCK, false)
                && !ServiceUtil.checkRunning(this, "com.kk.future.safehelper.service.WatchDogService")) {
            startService(new Intent(this, WatchDogService.class));
        }

    }

    // 初始化 数据库 手机号码  常用号码
    private void initDatabases() {
        // 手机号码数据库
        if (!new File(getFilesDir(), "address.db").exists()) {
            AssetManager am = getResources().getAssets();
            try {
                InputStream open = am.open("address.db");
                File dir = getFilesDir();
                File file = new File(dir, "address.db");
                FileOutputStream out = new FileOutputStream(file);
                CopyFileUtil.copy(open, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 常用号码数据库
        if (!new File(getFilesDir(), "usefullNum.db").exists()) {
            AssetManager am = getResources().getAssets();
            try {
                InputStream open = am.open("usefullNum.db");
                File dir = getFilesDir();
                File file = new File(dir, "usefullNum.db");
                FileOutputStream out = new FileOutputStream(file);
                CopyFileUtil.copy(open, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 设置一些控件的监视器
     */
    private void setter() {
        // 5秒后自动跳转 到 HomeActivity
        mHandler.sendEmptyMessage(5);
        // 用户 点击马上 跳转 HomeActivity
        tv_skipWaite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacksAndMessages(null);
                startActivity(new Intent(LaunchActivity.this, HomeActivity.class));
                LaunchActivity.this.finish();
            }
        });

    }

    /**
     * 初始化 子 View
     */

    private void initView() {
        tv_version = (TextView) findViewById(R.id.tv_launch_version);
        tv_version.setText("版本：" + VersionUtil.getLocalVersionName(this));
        tv_skipWaite = (TextView) findViewById(R.id.tv_launch_skip_waite);
        checkingUpdate = (TextView) findViewById(R.id.tv_checking_version);
        checkingUpdate.setVisibility(View.INVISIBLE);
    }


}
