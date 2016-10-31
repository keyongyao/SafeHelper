package com.kk.future.safehelper.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.bean.UsefullGroupBean;
import com.kk.future.safehelper.bean.UsefullGroupContextBean;
import com.kk.future.safehelper.dao.UsefullNumDao;

import java.util.ArrayList;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:重用号码预览<br>
 * date: 2016/10/31  14:38.
 */
public class FragmentToolBoxCnum extends Fragment {
    private TextView title;
    private ImageButton setting;
    private ExpandableListView elNum;
    private ArrayList<UsefullGroupBean> mGroupBeenList;
    private ArrayList<ArrayList<UsefullGroupContextBean>> allUsefullNumList;
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.layout_tool_box_cnum, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    elNum.setAdapter(new MyAdapter());
                }
            }
        };
        initUI(view);
        initData();
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                UsefullNumDao dao = new UsefullNumDao(getContext());
                mGroupBeenList = dao.getGroups();
                allUsefullNumList = new ArrayList<>();
                ArrayList<UsefullGroupContextBean> temp;
                // 查询每组的内容
                for (int i = 1; i <= mGroupBeenList.size(); i++) {
                    temp = dao.getGroupContext(i);
                    allUsefullNumList.add(temp);
                }
                Message msg = Message.obtain();
                msg.what = 0;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void initUI(View view) {
        title = (TextView) view.findViewById(R.id.tv_action_title);
        title.setText("常用号码大全");
        setting = (ImageButton) view.findViewById(R.id.ib_actionbar_setting);
        setting.setVisibility(View.GONE);
        elNum = (ExpandableListView) view.findViewById(R.id.el_atool_usefullNum);
    }

    // 适配器
    class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mGroupBeenList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return allUsefullNumList.get(groupPosition).size();
        }

        @Override
        public UsefullGroupBean getGroup(int groupPosition) {
            return mGroupBeenList.get(groupPosition);
        }

        @Override
        public UsefullGroupContextBean getChild(int groupPosition, int childPosition) {
            return allUsefullNumList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View inflate = View.inflate(getContext(), R.layout.usefullnumgroup_layout, null);
            TextView view = (TextView) inflate.findViewById(R.id.tv_atool_usefullGroup);
            view.setText(mGroupBeenList.get(groupPosition).name);
            return inflate;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View inflate = View.inflate(getContext(), R.layout.usefullnum_groupchild_layout, null);
            TextView name = (TextView) inflate.findViewById(R.id.tv_atool_usefullnum_item_name);
            TextView number = (TextView) inflate.findViewById(R.id.tv_atool_usefullnum_item_number);
            name.setText(getChild(groupPosition, childPosition).name);
            number.setText(getChild(groupPosition, childPosition).number);
            LinearLayout ll = (LinearLayout) inflate.findViewById(R.id.ll_atool_usefullNumItem);
            // 点击拨打电话
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) v.findViewById(R.id.tv_atool_usefullnum_item_number);
                    String number = tv.getText().toString().trim();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);
                }
            });
            return inflate;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
