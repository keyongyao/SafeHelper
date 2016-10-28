package com.kk.future.safehelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kk.future.safehelper.R;

import java.util.Date;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description: 缓存清理的 布局 <br>
 * date: 2016/10/28  15:48.
 */

public class FragmentClean extends Fragment {
    TextView tv;
    Button bt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_clean, null);
    }

    // 此方法  在 onCreateView() 后执行
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv = (TextView) view.findViewById(R.id.textView2);
        tv.setText(getClass().getName());
        bt = (Button) view.findViewById(R.id.button);
        setter();
    }

    /**
     * 设置点击事件
     */
    private void setter() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText(new Date().toString());
            }
        });
    }
}
