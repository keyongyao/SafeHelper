package com.kk.future.safehelper.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.signal.CommonSignal;
import com.kk.future.safehelper.utils.MyDeviceManager;
import com.kk.future.safehelper.utils.SPUtil;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:手机安全的布局<br>
 * date: 2016/10/28  15:48.
 */

public class FragmentPhoneSafe extends Fragment {
    private ImageButton setting;  // acationbar 的 右侧的设置 按钮
    private TextView title;   // actionbar 的标题
    private TextView safeNum, tvLockState, tvDM;  // 安全号码 防盗状态 设备管理器
    private ImageView ivLock;  // 防盗锁图片
    private Button btnReset;  // 重置 防盗设置
    private MyDeviceManager mDM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_phone_safe, null);
        setting = (ImageButton) view.findViewById(R.id.ib_actionbar_setting);
        setting.setVisibility(View.GONE);
        title = (TextView) view.findViewById(R.id.tv_action_title);
        title.setText("手机防盗");
        mDM = new MyDeviceManager(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        safeNum = (TextView) view.findViewById(R.id.tv_phonesafe_safenum);
        safeNum.setText(SPUtil.getString(getContext(), CommonSignal.PhoneSafe.SAFE_NUM));
        tvLockState = (TextView) view.findViewById(R.id.tv_phonesafe_lockstate);
        if (SPUtil.getBoolean(getContext(), CommonSignal.PhoneSafe.IS_GUARD_OPEN, false)) {
            tvLockState.setText("防盗保护开启状态");
        }
        tvDM = (TextView) view.findViewById(R.id.tv_phonesafe_DM);
        ivLock = (ImageView) view.findViewById(R.id.iv_phonesafe_lockstate);
        if (SPUtil.getBoolean(getContext(), CommonSignal.PhoneSafe.IS_GUARD_OPEN, false)) {
            ivLock.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lock));
        }
        btnReset = (Button) view.findViewById(R.id.btn_phonesafe_reset);
        // 进入设置 向导
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.rl_container, new FragmentPhoneSafeGuide1(), "guide1");
                ft.addToBackStack("guide1");
                ft.commit();
            }
        });
        // 引用用户 启用 相应的 设备管理器
        tvDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDM.activte();

            }
        });
        // 检测设备管理器的激活状态
        if (mDM.isActived()) {
            tvDM.setBackgroundColor(Color.GREEN);
            tvDM.setText("以下功能已被激活");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDM.isActived()) {
            tvDM.setBackgroundColor(Color.GREEN);
            tvDM.setText("以下功能已被激活");
        }
    }


}
