package com.kk.future.safehelper.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.bean.VersionBean;
import com.kk.future.safehelper.dialog.UpdateDialog;
import com.kk.future.safehelper.fragment.ContainerActivity;
import com.kk.future.safehelper.signal.CommonSignal;
import com.kk.future.safehelper.utils.SPUtil;
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
        // 检测配置 在决定是否 检查升级
        if (SPUtil.getBoolean(HomeActivity.this, CommonSignal.SettingCenter.AUTO_UPDATE, false)) {
            VersionUtil.checkUpdate(mUpdateURL, mHandler, this);
        }

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
                // 需要 弹出 密码验证框
                showDialog();

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


    /**
     * 进入 phonesafe 模块 需要 密码
     * 弹出密码 输入框 分 初次进入 和 非初次进入 两种对话框
     */
    private void showDialog() {
        // 准备对话框的 内容
        View view = View.inflate(this, R.layout.layout_phonesafe_pwd, null);
        final EditText pwd1, pwd2;
        Button btnOK, btnCancel;
        pwd1 = (EditText) view.findViewById(R.id.et_phonesafe_setPwd);
        pwd2 = (EditText) view.findViewById(R.id.et_phonesafe_chkpwd);
        btnOK = (Button) view.findViewById(R.id.btn_phonesafe_ok);
        btnCancel = (Button) view.findViewById(R.id.btn_phonesafe_cancel);
        final boolean checkPwd = SPUtil.getBoolean(this, CommonSignal.PhoneSafe.IS_SET_PWD, false);
        // 不是在设置密码，而是在验证密码
        if (checkPwd) {
            pwd1.setVisibility(View.GONE);
        }
        // 生成对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入密码");
        builder.setIcon(R.drawable.head);
        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.show();
        // 取消输入密码
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 首次 设置密码
                if (!checkPwd) {
                    if (TextUtils.isEmpty(pwd1.getText().toString().trim())) {
                        pwd1.setError("密码不能为空");
                    }
                    if (TextUtils.isEmpty(pwd2.getText().toString().trim())) {
                        pwd2.setError("密码不能为空");
                    }
                    if (!TextUtils.equals(pwd1.getText().toString().trim(), pwd2.getText().toString().trim())) {
                        pwd1.setError("密码不一致");
                    }
                    // 两次 输入密码 一致
                    if (TextUtils.equals(pwd1.getText().toString().trim(), pwd2.getText().toString().trim())) {
                        // 保存密码 并且 进入相应的 功能模块
                        SPUtil.putboolean(getApplicationContext(), CommonSignal.PhoneSafe.IS_SET_PWD, true);
                        SPUtil.putString(getApplicationContext(), CommonSignal.PhoneSafe.PWD, pwd1.getText().toString().trim());
                        dialog.dismiss();
                        intent = new Intent(getApplicationContext(), ContainerActivity.class);
                        intent.setFlags(CommonSignal.FunArray.PHONE_SAFE);
                        startActivityForResult(intent, CommonSignal.FunArray.PHONE_SAFE);
                    }
                } else {
                    // 密码验证正确
                    String pwd = SPUtil.getString(getApplicationContext(), CommonSignal.PhoneSafe.PWD);
                    if (!TextUtils.equals(pwd, pwd2.getText().toString().trim())) {
                        pwd2.setError("密码错误！");
                    }
                    if (TextUtils.equals(pwd, pwd2.getText().toString().trim())) {
                        dialog.dismiss();
                        intent = new Intent(HomeActivity.this, ContainerActivity.class);
                        intent.setFlags(CommonSignal.FunArray.PHONE_SAFE);
                        startActivityForResult(intent, CommonSignal.FunArray.PHONE_SAFE);
                    }
                }
            }
        });
    }

}
