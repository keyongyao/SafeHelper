package com.kk.future.safehelper.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk.future.safehelper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettingChooseStyle extends Fragment {


    public FragmentSettingChooseStyle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_setting_choose_style, container, false);
    }

}
