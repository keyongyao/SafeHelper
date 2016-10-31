package com.kk.future.safehelper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.future.safehelper.R;


/**
 * Author: Future <br>
 * QQ: <br>
 * Description:带有选择的菜单条目<br>
 * date: 2016/10/31  16:24.
 */
public class SettingChooseView extends RelativeLayout {
    final String NAMESPACE = "http://schemas.android.com/apk/res/com.kk.future.safehelper";
    TextView tvTitle, tvSubTitle;

    public SettingChooseView(Context context) {
        this(context, null);
    }

    public SettingChooseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.setting_center_choose_menu, this);
        tvTitle = (TextView) view.findViewById(R.id.tv_setting_choose_sub_menu_title);
        tvSubTitle = (TextView) view.findViewById(R.id.tv_setting_choose_sub_menu_subTitle);
        // 从XML文件获取  title 和 subTitle 的文字
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        String title = attrs.getAttributeValue(NAMESPACE, "title");
        String subTitle = attrs.getAttributeValue(NAMESPACE, "subTitle");
        initUI(title, subTitle);
    }

    // 设置UI 文字
    private void initUI(String title, String subTitle) {
        tvTitle.setText(title);
        tvSubTitle.setText(subTitle);
    }

    public void changeSubTitleText(String subTitle) {
        tvSubTitle.setText(subTitle);
    }


}
