package com.kk.future.safehelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kk.future.safehelper.R;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description: 缓存清理的 布局 <br>
 * date: 2016/10/28  15:48.
 */

public class FragmentClean extends Fragment {
    private ImageButton setting;
    private TextView title;
    private FragmentTabHost mTabHost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_clean, null);
        // 找到 fragmentTabHost
        mTabHost = (FragmentTabHost) view.findViewById(R.id.fth_clean);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.fl_clean_container);
        mTabHost.addTab(mTabHost.newTabSpec("cache").setIndicator("缓存清理"),
                FragmentCleanCache.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("sdcard").setIndicator("SD卡清理"),
                FragmentCleanSDcard.class, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setting = (ImageButton) view.findViewById(R.id.ib_actionbar_setting);
        setting.setVisibility(View.GONE);
        title = (TextView) view.findViewById(R.id.tv_action_title);
        title.setText("缓存清理");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }

}
