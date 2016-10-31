package com.kk.future.safehelper.fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.bean.BlackNumBean;
import com.kk.future.safehelper.dao.BlackNumDao;
import com.kk.future.safehelper.utils.LogCatUtil;

import java.util.ArrayList;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description: 通信卫士 的 布局<br>
 * date: 2016/10/28  15:48.
 */

public class FragmentCommunication extends Fragment {
    private static final String TAG = "FragmentCommunication";
    private TextView title;
    private ListView lvbk;
    private ImageButton setting;
    private Button btnAdd;
    private boolean isLodingData;
    private ArrayList<BlackNumBean> blackNumBeanList = new ArrayList<>();
    private ListViewBlacknumAdapter listViewBlacknumAdapter;
    private BlackNumBean beanForAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_communication, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initUI(view);
        pagedData();
        //  initData();  // 填充 测试数据  测试  分页懒加载
        setListeners();

    }

    /**
     * 给 子控件  设置相应的 事件监视器
     */
    private void setListeners() {
        //添加黑名单
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beanForAdd = new BlackNumBean();
                showAddDialog();
            }
        });
        // ListView 添加适配器
        lvbk.setAdapter((listViewBlacknumAdapter = new ListViewBlacknumAdapter()));
        // listView 滚动监视器 动态加载 分页数据  当最后的可见条目为 黑名单集合的最后一条 则加载数据
        lvbk.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_TOUCH_SCROLL: {
                        LogCatUtil.getInstance().i(TAG, "SCROLL_STATE_TOUCH_SCROLL");
                        break;
                    }
                    case SCROLL_STATE_IDLE: {
                        if (lvbk.getLastVisiblePosition() == blackNumBeanList.size() - 1 && !isLodingData) {
                            // 分页加载数据
                            pagedData();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                LogCatUtil.getInstance().i(TAG, firstVisibleItem + "  " + visibleItemCount + "  " + totalItemCount);
            }
        });
    }

    /**
     * 添加黑名单对话框
     */
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog addDialog = builder.create();
        View dialogInflate = View.inflate(getContext(), R.layout.dialog_add_blacknum, null);
        addDialog.setView(dialogInflate);
        addDialog.show();
        // 初始化对话框中的控件
        final EditText etInputNum = (EditText) dialogInflate.findViewById(R.id.et_blacknum_inputnum);
        RadioGroup rgblockType = (RadioGroup) dialogInflate.findViewById(R.id.rg_blacknum_type);
        Button btnOK = (Button) dialogInflate.findViewById(R.id.btn_blacknum_addDialog_OK);
        Button btnCancel = (Button) dialogInflate.findViewById(R.id.btn_blacknum_addDialog_cancel);

        // 为控件设置事件监视器
        rgblockType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_blacknum_type_msg: {
                        beanForAdd.blockType = 1;
                        break;
                    }
                    case R.id.rb_blacknum_type_phone: {
                        beanForAdd.blockType = 2;

                        break;
                    }
                    case R.id.rb_blacknum_type_both: {
                        beanForAdd.blockType = 3;
                        break;
                    }
                }
            }
        });
        // 确定添加按钮
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etInputNum.getText().toString().trim();
                beanForAdd.phone = phone;
                blackNumBeanList.add(0, beanForAdd);
                Toast.makeText(getContext(), "" + beanForAdd.toString(), Toast.LENGTH_SHORT).show();
                new BlackNumDao(getContext()).insert(beanForAdd);
                listViewBlacknumAdapter.notifyDataSetChanged();
                addDialog.dismiss();
            }
        });
        // 取消添加按钮
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
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
        title.setText("通信卫士");
        setting = (ImageButton) view.findViewById(R.id.ib_actionbar_setting);
        setting.setVisibility(View.GONE);
        lvbk = (ListView) view.findViewById(R.id.lv_blacknum_show);
        btnAdd = (Button) view.findViewById(R.id.btn_blcknum_add);
    }

    /**
     * 分页添加黑名单数据 原理  查询语句 de  limit  start,end
     */
    private void pagedData() {
        isLodingData = true;
        // 每次 取出数据的集合
        ArrayList<BlackNumBean> addBeanList = new ArrayList<>();
        // 每次取15 条数据 ,15 由 查询语句决定
        Cursor cursor = new BlackNumDao(getContext()).paged(blackNumBeanList.size());
        while (cursor.moveToNext()) {
            BlackNumBean bean = new BlackNumBean();
            bean._id = cursor.getInt(0);
            bean.phone = cursor.getString(1);
            bean.blockType = cursor.getInt(2);
            addBeanList.add(bean);
        }
        // 往最终的集合 放数据
        blackNumBeanList.addAll(addBeanList);
        if (listViewBlacknumAdapter != null)
            listViewBlacknumAdapter.notifyDataSetChanged();

        isLodingData = false;
    }

    // 初始化 黑名单数据
    private void initData() {
        BlackNumDao dao = new BlackNumDao(getContext());
        dao.initData();
        Cursor cursor = dao.queryAll();
        while (cursor.moveToNext()) {
            BlackNumBean bean = new BlackNumBean();
            bean._id = cursor.getInt(0);
            bean.phone = cursor.getString(1);
            bean.blockType = cursor.getInt(2);
            blackNumBeanList.add(bean);
        }
    }

    /**
     * 黑名单适配器
     */
    public class ListViewBlacknumAdapter extends BaseAdapter {
        String[] blackType = {"拦截短信", "拦截来电", "拦截来电短信"};


        public ListViewBlacknumAdapter() {
        }

        @Override
        public int getCount() {
            return blackNumBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return blackNumBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.blacknum_item, null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_blacknum_item_title);
                holder.tvSubTitle = (TextView) convertView.findViewById(R.id.tv_blacknum_item_subtitle);
                holder.ivDel = (ImageView) convertView.findViewById(R.id.iv_blacknum_item_del);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvTitle.setText(blackNumBeanList.get(position).phone);
            holder.tvSubTitle.setText(blackType[blackNumBeanList.get(position).blockType - 1]);
            holder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("删除");
                    builder.setMessage("确定删除此黑名单？");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "删除操作" + blackNumBeanList.get(position).toString(), Toast.LENGTH_SHORT).show();
                            new BlackNumDao(getContext()).delete(blackNumBeanList.get(position));
                            blackNumBeanList.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    builder.create().show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tvTitle;
            TextView tvSubTitle;
            ImageView ivDel;
        }

    }
}
