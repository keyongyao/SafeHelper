package com.kk.future.safehelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.signal.CommonSignal;
import com.kk.future.safehelper.utils.SPUtil;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:<br>
 * date: 2016/10/30  19:19.
 */

public class FragmentPhoneSafeGuide3 extends Fragment {
    private EditText etNum;
    private Button btnPrevious, btnNext, btnAddContact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_phone_safe_guide3, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        etNum = (EditText) view.findViewById(R.id.et_phoneGuard3rd_inputSafeNumber);
        etNum.setText(SPUtil.getString(getContext(), CommonSignal.PhoneSafe.SAFE_NUM));
        btnNext = (Button) view.findViewById(R.id.btn_PhoneGuard3st_next);
        btnPrevious = (Button) view.findViewById(R.id.btn_PhoneGuard3st_previous);
        btnAddContact = (Button) view.findViewById(R.id.btn_phoneGuard3rd_chooseContact);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 保存安全号码
                String safeNum = etNum.getText().toString().trim();
                SPUtil.putString(getContext(), CommonSignal.PhoneSafe.SAFE_NUM, safeNum);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.rl_container, new FragmentPhoneSafeGuide4(), "guide4");
                ft.addToBackStack("guide4");
                ft.commit();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "待完成此功能，调用系统联系人Provider", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
