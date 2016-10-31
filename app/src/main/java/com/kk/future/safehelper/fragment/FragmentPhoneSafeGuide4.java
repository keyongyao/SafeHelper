package com.kk.future.safehelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.signal.CommonSignal;
import com.kk.future.safehelper.utils.SPUtil;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:<br>
 * date: 2016/10/30  19:19.
 */

public class FragmentPhoneSafeGuide4 extends Fragment {
    private Button btnPrevious, btnFinish;
    private CheckBox cbOpenGuard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_phone_safe_guide4, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnPrevious = (Button) view.findViewById(R.id.btn_PhoneGuard4st_previous);
        btnFinish = (Button) view.findViewById(R.id.btn_PhoneGuard4st_finish);
        cbOpenGuard = (CheckBox) view.findViewById(R.id.cb_phonguard4th_openGuard);
        cbOpenGuard.setChecked(SPUtil.getBoolean(getContext(), CommonSignal.PhoneSafe.IS_GUARD_OPEN, false));
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        // 保存 是否 开启 手机防盗服务
        cbOpenGuard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SPUtil.putboolean(getContext(), CommonSignal.PhoneSafe.IS_GUARD_OPEN, b);
            }
        });
        // 开启或者关闭 相应的服务 
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.rl_container, new FragmentPhoneSafe(), "phonesafe");
                ft.commit();

            }
        });
    }
}
