package com.kk.future.safehelper.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.utils.SuperToast;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:工具箱的布局<br>
 * date: 2016/10/28  15:48.
 */

public class FragmentToolBox extends Fragment {
    private TextView title;
    private ImageButton setting;
    private TextView tvQlocation, tvBack, tvCommonNum, tvAppLock, tvWhere, tvMan;
    private Button btnBKsms, btnBKcontact, btnRsms, btnRcontact;
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
        // 归属地查询
        tvQlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack("toolbox");
                ft.replace(R.id.rl_container, new FragmentToolBoxQlocation(), "Qlocation");
                ft.addToBackStack("Qlocation");
                ft.commit();
            }
        });
        // 备份还原功能
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 显示 选择 备份 和 还原的 对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final AlertDialog dialog = builder.create();
                View inflate = View.inflate(getContext(), R.layout.layout_tool_box_backup, null);
                btnBKsms = (Button) inflate.findViewById(R.id.btn_atool_backupSMS);
                btnBKcontact = (Button) inflate.findViewById(R.id.btn_atool_backupContact);
                btnRsms = (Button) inflate.findViewById(R.id.btn_atool_restoreSMS);
                btnRcontact = (Button) inflate.findViewById(R.id.btn_atool_restoreContact);
                dialog.setView(inflate);
                dialog.show();
                // 备份 短信
                btnBKsms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SuperToast.show(getContext(), "备份短信");
                        dialog.dismiss();
                    }
                });
                // 备份联系人
                btnBKcontact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SuperToast.show(getContext(), "备份联系人");
                        dialog.dismiss();
                    }
                });
                //还原短信
                btnRsms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SuperToast.show(getContext(), "还原短信");
                        dialog.dismiss();
                    }
                });
                // 还原联系人
                btnRcontact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SuperToast.show(getContext(), "还原联系人");
                        dialog.dismiss();
                    }
                });


            }
        });
        // 常用号码查询
        tvCommonNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack("toolbox");
                ft.replace(R.id.rl_container, new FragmentToolBoxCnum(), "Cmun");
                ft.addToBackStack("Cnum");
                ft.commit();
            }
        });
        // 程序锁
        tvAppLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack("toolbox");
                ft.replace(R.id.rl_container, new FragmentToolBoxAppLock(), "applock");
                ft.addToBackStack("applock");
                ft.commit();
            }
        });
        // 我的定位
        tvWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuperToast.show(getContext(), "将会加入百度地图SDK");
            }
        });
        tvMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuperToast.show(getContext(), "将会加入简单程序管理功能");
            }
        });
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
        tvMan = (TextView) view.findViewById(R.id.tv_atool_appMan);
    }

}
