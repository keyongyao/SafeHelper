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

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:<br>
 * date: 2016/10/30  19:19.
 */

public class FragmentPhoneSafeGuide1 extends Fragment {
    private Button nextPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_phone_safe_guide1, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nextPage = (Button) view.findViewById(R.id.btn_PhoneSafeGuide1_next);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.rl_container, new FragmentPhoneSafeGuide2(), "guide2");
                ft.addToBackStack("guide2");
                ft.commit();

            }
        });
    }
}
