package com.kk.future.safehelper.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kk.future.safehelper.R;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:工具箱的布局<br>
 * date: 2016/10/28  15:48.
 */

public class FragmentToolBox extends Fragment {
    private TextView title;
    private ImageButton setting;
    private TextView tvQlocation, tvBack, tvCommonNum, tvAppLock, tvWhere;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_tool_box, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initUI(view);
        setListeners();
    }

    /**
     * 设置 事件点击 监视器
     */
    private void setListeners() {

    }

    /**
     * 初始化 UI
     *
     * @param view
     */
    private void initUI(View view) {
        title = (TextView) view.findViewById(R.id.tv_action_title);
        title.setText("高级工具");
        setting = (ImageButton) view.findViewById(R.id.ib_actionbar_setting);
        setting.setVisibility(View.GONE);
        tvQlocation = (TextView) view.findViewById(R.id.tv_atool_queryLocation);
        tvBack = (TextView) view.findViewById(R.id.tv_atool_SMSbackup);
        tvCommonNum = (TextView) view.findViewById(R.id.tv_atool_query_phoneNum);
        tvAppLock = (TextView) view.findViewById(R.id.tv_atool_applock);
        tvWhere = (TextView) view.findViewById(R.id.tv_atool_whereIam);
    }

}
