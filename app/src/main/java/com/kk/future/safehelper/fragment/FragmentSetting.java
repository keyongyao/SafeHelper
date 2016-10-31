package com.kk.future.safehelper.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.activity.SetPositionActivity;
import com.kk.future.safehelper.service.BlackCallService;
import com.kk.future.safehelper.service.BlackSMSService;
import com.kk.future.safehelper.service.ShowLocationService;
import com.kk.future.safehelper.service.WatchDogService;
import com.kk.future.safehelper.signal.CommonSignal;
import com.kk.future.safehelper.utils.SPUtil;
import com.kk.future.safehelper.utils.ServiceUtil;
import com.kk.future.safehelper.view.SettingCheckBoxItemView;
import com.kk.future.safehelper.view.SettingChooseView;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description: 全局设置 <br>
 * date: 2016/10/28  15:48.
 */

public class FragmentSetting extends Fragment {
    private TextView title;
    private ImageButton setting;
    private SettingCheckBoxItemView autoUpdate, openlocation, blackNum, appLock;
    private SettingChooseView locationStyle, locationTipPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_setting, null);
    }

    // 此方法  在 onCreateView() 后执行
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initUI(view);
        setListeners();
    }

    private void setListeners() {
        // 自动更新
        autoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 更改UI 并且 保存配置
                autoUpdate.changeCheckBoxCheckedState();
                SPUtil.putboolean(getContext(), CommonSignal.SettingCenter.AUTO_UPDATE, autoUpdate.isChecked());
            }
        });
        // 是否 打开 来电显示
        openlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openlocation.changeCheckBoxCheckedState();
                SPUtil.putboolean(getContext(), CommonSignal.SettingCenter.INCOME_LOCATION, openlocation.isChecked());
                if (openlocation.isChecked()) {
                    getContext().startService(new Intent(getContext(), ShowLocationService.class));

                } else {
                    //先检测 服务是否在运行
                    if (ServiceUtil.checkRunning(getActivity(), "com.kk.future.safehelper.service.ShowLocationService")) {
                        getContext().stopService(new Intent(getContext(), ShowLocationService.class));

                    }
                }

            }
        });
        // 是否开启 黑名单
        blackNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blackNum.changeCheckBoxCheckedState();
                SPUtil.putboolean(getContext(), CommonSignal.SettingCenter.BLACK_NUM, blackNum.isChecked());
                if (blackNum.isChecked()) {
                    getContext().startService(new Intent(getContext(), BlackCallService.class));
                    getContext().startService(new Intent(getContext(), BlackSMSService.class));
                } else {
                    if (ServiceUtil.checkRunning(getActivity(), "com/kk/future/safehelper/service/BlackCallService.java")) {
                        getContext().stopService(new Intent(getContext(), BlackCallService.class));

                    }
                    if (ServiceUtil.checkRunning(getActivity(), "com.kk.future.safehelper.service.BlackSMSService")) {
                        getContext().stopService(new Intent(getContext(), BlackSMSService.class));

                    }
                }
            }
        });
        // 开启 程序锁
        appLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appLock.changeCheckBoxCheckedState();
                SPUtil.putboolean(getContext(), CommonSignal.SettingCenter.APP_LOCK, appLock.isChecked());
                if (appLock.isChecked()) {
                    getContext().startService(new Intent(getContext(), WatchDogService.class));
                } else {
                    if (ServiceUtil.checkRunning(getActivity(), "com.kk.future.safehelper.service.WatchDogService")) {
                        getContext().stopService(new Intent(getContext(), WatchDogService.class));

                    }
                }
            }
        });
        // x选择来电显示的样式
        locationStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("请选择来电显示样式");
                builder.setIcon(R.drawable.draw);
                builder.setNegativeButton("取消", null);
                builder.setSingleChoiceItems(CommonSignal.SettingCenter.STYLE, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 保存配置 刷新UI
                        SPUtil.putInt(getContext(), CommonSignal.SettingCenter.CHOOSETYPE, which);
                        locationStyle.changeSubTitleText(CommonSignal.SettingCenter.STYLE[which]);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        // 来电显示框 位置设置
        locationTipPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SetPositionActivity.class));
            }
        });
    }

    private void initUI(View view) {
        title = (TextView) view.findViewById(R.id.tv_action_title);
        title.setText("设置中心");
        setting = (ImageButton) view.findViewById(R.id.ib_actionbar_setting);
        setting.setVisibility(View.GONE);
        // 自动更新
        autoUpdate = (SettingCheckBoxItemView) view.findViewById(R.id.scbiv_autoupdate);
        autoUpdate.initUI(SPUtil.getBoolean(getContext(), CommonSignal.SettingCenter.AUTO_UPDATE, false));
        // 来电显示归属地
        openlocation = (SettingCheckBoxItemView) view.findViewById(R.id.scbiv_showLocation);
        openlocation.initUI(SPUtil.getBoolean(getContext(), CommonSignal.SettingCenter.INCOME_LOCATION, false));
        // 开启黑名单
        blackNum = (SettingCheckBoxItemView) view.findViewById(R.id.scbiv_balcknum);
        blackNum.initUI(SPUtil.getBoolean(getContext(), CommonSignal.SettingCenter.BLACK_NUM, false));
        // 程序锁开启
        appLock = (SettingCheckBoxItemView) view.findViewById(R.id.scbiv_applock);
        appLock.initUI(SPUtil.getBoolean(getContext(), CommonSignal.SettingCenter.APP_LOCK, false));
        // 来电显示风格
        locationStyle = (SettingChooseView) view.findViewById(R.id.scv_settingCenter_chooseStyle);
        int which = SPUtil.getInt(getContext(), CommonSignal.SettingCenter.CHOOSETYPE);
        locationStyle.changeSubTitleText(CommonSignal.SettingCenter.STYLE[which]);
        // 来电显示位置
        locationTipPosition = (SettingChooseView) view.findViewById(R.id.scv_settingCenter_setPhoneAddrTipPosition);

    }

}
