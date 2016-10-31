package com.kk.future.safehelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.signal.CommonSignal;

/**
 * 包含 对应 fragment 的 容器 Activity
 */
public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        initContainerView();
    }

    /**
     * 从 对应的 fragment 加载 对应 的布局
     */
    private void initContainerView() {
        Intent intent = getIntent();
        int flags = intent.getFlags();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (flags) {
            case CommonSignal.FunArray.CLEAN:
                transaction.replace(R.id.rl_container, new FragmentClean(), "clean");
                transaction.commit();
                break;
            case CommonSignal.FunArray.NET:
                transaction.replace(R.id.rl_container, new FragmentNet(), "net");
                transaction.commit();
                break;
            case CommonSignal.FunArray.PROCESS:
                transaction.replace(R.id.rl_container, new FragmentProcess(), "process");
                transaction.commit();
                break;
            case CommonSignal.FunArray.PHONE_SAFE:
                transaction.replace(R.id.rl_container, new FragmentPhoneSafe(), "phonesafe");
                transaction.commit();

                break;
            case CommonSignal.FunArray.TOOL_BOX:
                transaction.replace(R.id.rl_container, new FragmentToolBox(), "toolbox");
                transaction.commit();
                break;
            case CommonSignal.FunArray.COMMUNICATION:
                transaction.replace(R.id.rl_container, new FragmentCommunication(), "communication");
                transaction.commit();
                break;
            case CommonSignal.FunArray.SETTING:
                transaction.replace(R.id.rl_container, new FragmentSetting(), "setting");
                transaction.commit();
                break;

        }

    }


}
