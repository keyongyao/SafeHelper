package com.kk.future.safehelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kk.future.safehelper.bean.VersionBean;
import com.kk.future.safehelper.dialog.UpdateDialog;
import com.kk.future.safehelper.fragment.ContainerActivity;
import com.kk.future.safehelper.signal.CommonSignal;
import com.kk.future.safehelper.utils.SuperToast;
import com.kk.future.safehelper.utils.VersionUtil;

import java.net.MalformedURLException;
import java.net.URL;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private Context mContext;
    private URL mUpdateURL;
    private ImageButton mIb_setting;
    private TextView tv_clean, tv_process, tv_net, tv_phonesafe, tv_communication, tv_toolbox;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 判断 检查更新的结果
                if (msg.what == CommonSignal.CheckUpdate.HAS_lATEST_VERSION_YES) {
                    // 有更新包
                    new UpdateDialog(true, mContext, (VersionBean) msg.obj, new UpdateDialog.ClickCallBack() {
                        @Override
                        public void onBtnPositiveClick(String apkUrl) {
                            VersionUtil.downloadAPK(apkUrl, mContext);
                        }
                    }).show();
                } else if (msg.what == CommonSignal.CheckUpdate.HAS_lATEST_VERSION_NO) {
                    //么有更新包

                    new UpdateDialog(false, mContext, null, null).show();
                }

            }
        };
        // 检查更新
        try {
            mUpdateURL = new URL("http://192.168.199.216:8080/version.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        VersionUtil.checkUpdate(mUpdateURL, mHandler, this);
        // 找到 6 个 功能 TextView 和  设置 ImageButton
        initView();
        // 设置事件监听
        setListener();
    }

    private void setListener() {
        mIb_setting.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
        tv_communication.setOnClickListener(this);
        tv_net.setOnClickListener(this);
        tv_toolbox.setOnClickListener(this);
        tv_phonesafe.setOnClickListener(this);
        tv_process.setOnClickListener(this);
    }

    private void initView() {
        mIb_setting = (ImageButton) findViewById(R.id.ib_actionbar_setting);
        tv_clean = (TextView) findViewById(R.id.tv_home_clean);
        tv_process = (TextView) findViewById(R.id.tv_home_process);
        tv_net = (TextView) findViewById(R.id.tv_home_net);
        tv_communication = (TextView) findViewById(R.id.tv_home_communication);
        tv_phonesafe = (TextView) findViewById(R.id.tv_home_phonesafe);
        tv_toolbox = (TextView) findViewById(R.id.tv_home_toolbox);
    }

    /**
     * 处理 本页面的点击事件
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_actionbar_setting:
                SuperToast.show(HomeActivity.this, "设置被点击了");
                intent = new Intent(this, ContainerActivity.class);
                intent.setFlags(CommonSignal.FunArray.SETTING);
                startActivityForResult(intent, CommonSignal.FunArray.SETTING);
                break;
            case R.id.tv_home_clean:
                SuperToast.show(mContext, "缓存清理被点击了");
                intent = new Intent(this, ContainerActivity.class);
                intent.setFlags(CommonSignal.FunArray.CLEAN);
                startActivityForResult(intent, CommonSignal.FunArray.CLEAN);
                break;
            case R.id.tv_home_phonesafe:
                SuperToast.show(mContext, "手机安全被点击了");
                intent = new Intent(this, ContainerActivity.class);
                intent.setFlags(CommonSignal.FunArray.PHONE_SAFE);
                startActivityForResult(intent, CommonSignal.FunArray.PHONE_SAFE);
                break;
            case R.id.tv_home_communication:
                SuperToast.show(mContext, "通信卫士被点击了");
                intent = new Intent(this, ContainerActivity.class);
                intent.setFlags(CommonSignal.FunArray.COMMUNICATION);
                startActivityForResult(intent, CommonSignal.FunArray.COMMUNICATION);
                break;
            case R.id.tv_home_toolbox:
                SuperToast.show(mContext, "工具箱被点击了");
                intent = new Intent(this, ContainerActivity.class);
                intent.setFlags(CommonSignal.FunArray.TOOL_BOX);
                startActivityForResult(intent, CommonSignal.FunArray.TOOL_BOX);
                break;
            case R.id.tv_home_net:
                SuperToast.show(mContext, "网络管理被点击了");
                intent = new Intent(this, ContainerActivity.class);
                intent.setFlags(CommonSignal.FunArray.NET);
                startActivityForResult(intent, CommonSignal.FunArray.NET);
                break;
            case R.id.tv_home_process:
                SuperToast.show(mContext, "进程管理被点击了");
                intent = new Intent(this, ContainerActivity.class);
                intent.setFlags(CommonSignal.FunArray.PROCESS);
                startActivityForResult(intent, CommonSignal.FunArray.PROCESS);
                break;
        }
    }


}
