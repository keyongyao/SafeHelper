package com.kk.future.safehelper.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.utils.LogCatUtil;


/**
 * 一个设置中心菜单的条目，方便重复使用
 * Created by Administrator on 2016/9/21.
 */
public class SettingCheckBoxItemView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.kk.future.safehelper";
    TextView tv_setting_center_item_title, tv_closeORopen;
    CheckBox cb_switch;
    String itemTitle, open, close;
    Context mContext;

    public SettingCheckBoxItemView(Context context) {
        this(context, null);
    }

    public SettingCheckBoxItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingCheckBoxItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 使当前的 View 充实内容
        View.inflate(context, R.layout.setting_center_item, this);
        mContext = context;
        tv_setting_center_item_title = (TextView) findViewById(R.id.tv_setting_item_title);
        tv_closeORopen = (TextView) findViewById(R.id.tv_closeOropen);
        cb_switch = (CheckBox) findViewById(R.id.cb_switch);
        // 取消 checkBox 从 父View中获取点击事件
        // cb_switch.setClickable(false);
        //  cb_switch.setFocusableInTouchMode(false);
        LogCatUtil.getInstance().i("main", "SettingCenterItemView running");
        initAttrs(attrs);
        //  initUI();

    }

    /**
     * @param attrs 初始化从XML传进来的书信
     */
    private void initAttrs(AttributeSet attrs) {
        itemTitle = attrs.getAttributeValue(NAMESPACE, "itemTitle");
        open = attrs.getAttributeValue(NAMESPACE, "open");
        close = attrs.getAttributeValue(NAMESPACE, "close");
    }

    /**
     * 初始化 UI checkbox 是否打勾状态
     *
     * @param isChecked 应该从陪配置文件读取配置
     */

    public void initUI(boolean isChecked) {
        tv_setting_center_item_title.setText(itemTitle);
        tv_setting_center_item_title.setTextColor(Color.BLACK);
        cb_switch.setChecked(isChecked);

        if (isChecked) {
            tv_closeORopen.setText(open);
            tv_closeORopen.setTextColor(Color.BLACK);
        } else {
            tv_closeORopen.setText(close);
        }

    }

    /**
     * 改变checkbox的状态
     */
    public void changeCheckBoxCheckedState() {
        cb_switch.setChecked(!cb_switch.isChecked());
        if (cb_switch.isChecked()) {
            tv_closeORopen.setText(open);
        } else {
            tv_closeORopen.setText(close);
        }
    }

    public boolean isChecked() {
        return cb_switch.isChecked();
    }


}
