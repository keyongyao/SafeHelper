package com.kk.future.safehelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kk.future.safehelper.R;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description: 流量管理的 布局<br>
 * date: 2016/10/28  15:48.
 */

public class FragmentNet extends Fragment {
    private TextView title;
    private ImageButton setting;
    private RadioGroup rgMenu;
    private FragmentManager childFragmentManager;
    private FragmentTransaction ft;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_net, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        childFragmentManager = getChildFragmentManager();
        ft = childFragmentManager.beginTransaction();
        ft.add(R.id.fl_net_childContainer, new FragmentNetMonitor(), "monitor");
        ft.commit();
        title = (TextView) view.findViewById(R.id.tv_action_title);
        title.setText("流量监控");
        setting = (ImageButton) view.findViewById(R.id.ib_actionbar_setting);
        setting.setVisibility(View.GONE);
        rgMenu = (RadioGroup) view.findViewById(R.id.rg_net_mennu);
        rgMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.rb_net_monitor:
                        ft = childFragmentManager.beginTransaction();
                        ft.add(R.id.fl_net_childContainer, new FragmentNetMonitor(), "monitor");

                        ft.commit();
                        break;
                    case R.id.rb_net_firewall:
                        ft = childFragmentManager.beginTransaction();
                        ft.add(R.id.fl_net_childContainer, new FragmentNetFirewall(), "firewall");

                        ft.commit();
                        break;
                    case R.id.rb_net_traffic_sort:
                        ft = childFragmentManager.beginTransaction();
                        ft.add(R.id.fl_net_childContainer, new FragmentNetSort(), "sort");

                        ft.commit();
                        break;
                }
            }
        });
    }


}
