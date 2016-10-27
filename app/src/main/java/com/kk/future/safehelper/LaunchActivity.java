package com.kk.future.safehelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kk.future.safehelper.utils.VersionUtil;

public class LaunchActivity extends AppCompatActivity {
    private TextView tv_version, tv_skipWaite, tv_checkingVersion;
    private Handler mHandler;

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
    }

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

    //  初始化 子 View
    private void initView() {
        tv_version = (TextView) findViewById(R.id.tv_launch_version);
        tv_version.setText("版本：" + VersionUtil.getLocalVersionName(this));
        tv_skipWaite = (TextView) findViewById(R.id.tv_launch_skip_waite);
    }


}
