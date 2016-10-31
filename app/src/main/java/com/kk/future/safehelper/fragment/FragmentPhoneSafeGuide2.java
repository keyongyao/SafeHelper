package com.kk.future.safehelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.signal.CommonSignal;
import com.kk.future.safehelper.utils.SIMutil;
import com.kk.future.safehelper.utils.SPUtil;
import com.kk.future.safehelper.view.SettingCheckBoxItemView;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:<br>
 * date: 2016/10/30  19:19.
 */

public class FragmentPhoneSafeGuide2 extends Fragment {
    private SettingCheckBoxItemView cbBind;
    private Button btnNext, btnPrevious;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_phone_safe_guide2, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        cbBind = (SettingCheckBoxItemView) view.findViewById(R.id.scbiv_bindSIMcard);
        cbBind.initUI(SPUtil.getBoolean(getContext(), CommonSignal.PhoneSafe.IS_BIND_SIM, false));
        cbBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbBind.changeCheckBoxCheckedState(); // 改变相应的 文字 和 checkbox 的状态
                // 从 checkbox 获取状态 并保存到 conifg.xml
                SPUtil.putboolean(getContext(), CommonSignal.PhoneSafe.IS_BIND_SIM, cbBind.isChecked());
                // 获取 保存  SIM serial number
                SPUtil.putString(getContext(), CommonSignal.PhoneSafe.SIM_SNUM, SIMutil.getSimSerialNumber(getContext()));
            }
        });
        btnNext = (Button) view.findViewById(R.id.btn_PhoneGuard2st_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.rl_container, new FragmentPhoneSafeGuide3(), "guide3");
                ft.addToBackStack("guide3");
                ft.commit();
            }
        });
        btnPrevious = (Button) view.findViewById(R.id.btn_PhoneGuard2st_previous);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

    }
}
