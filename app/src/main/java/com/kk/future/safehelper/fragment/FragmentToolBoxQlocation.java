package com.kk.future.safehelper.fragment;


import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.dao.QueryLocationDao;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:查询 归属地 的布局<br>
 * date: 2016/10/31  11:22.
 */
public class FragmentToolBoxQlocation extends Fragment {
    public static final int QUERYRESULT = 22;
    private TextView title, qResult;
    private ImageButton setting;
    private EditText etNum;
    private Button btnQ;
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_tool_box_qlocation, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initUi(view);
        // 处理 QueryLocationDao 发回来的查询结果
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == QUERYRESULT) {
                    qResult.setText((String) msg.obj);
                }
            }
        };
        setListeners();
    }

    private void setListeners() {
        btnQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenum = etNum.getText().toString().trim();
                // 输入的号码为空时
                if (TextUtils.isEmpty(phonenum)) {
                    etNum.setError("输入号码");
                    Vibrator vibrator = (Vibrator) getContext().getSystemService(Service.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                }
                new QueryLocationDao().query(getActivity(), phonenum, mHandler);
            }
        });
        // 监视 号码输入框 内容改变
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phonenum = etNum.getText().toString().trim();
                QueryLocationDao.query(getActivity(), phonenum, mHandler);
            }
        });

    }

    private void initUi(View view) {
        title = (TextView) view.findViewById(R.id.tv_action_title);
        title.setText("归属地查询");
        setting = (ImageButton) view.findViewById(R.id.ib_actionbar_setting);
        setting.setVisibility(View.GONE);
        etNum = (EditText) view.findViewById(R.id.et_atool_inputPhone);
        btnQ = (Button) view.findViewById(R.id.btn_atool_queryPhone);
        qResult = (TextView) view.findViewById(R.id.tv_atool_queryResult);
    }
}
