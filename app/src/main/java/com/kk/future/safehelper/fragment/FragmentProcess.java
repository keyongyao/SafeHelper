package com.kk.future.safehelper.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.bean.ProcessInfoBean;
import com.kk.future.safehelper.signal.CommonSignal;
import com.kk.future.safehelper.utils.ProcessUtil;
import com.kk.future.safehelper.utils.SPUtil;

import java.util.ArrayList;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:进程管理的布局<br>
 * date: 2016/10/28  15:48.
 */

public class FragmentProcess extends Fragment {
    private final int LOADINGDATAOK = 11;
    private TextView tvProcessCounts;
    private TextView tvMem;
    private ListView lvPrcesss;
    private TextView tvProcessType;
    private Button btnAll, btnInverse, btnClear, btnSetting;
    private ProcessUtil mProcessUtil;
    private ArrayList<ProcessInfoBean> mUserProcessList;
    private ArrayList<ProcessInfoBean> mSysProcessList;
    private Handler mHandler;
    private ProcessAdapter mAdapter;
    private ProgressDialog mProgressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mProcessUtil = new ProcessUtil(getContext());
        mProgressBar = new ProgressDialog(getContext());
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 处理 加载 进程信息 则 设置 适配器
                if (msg.what == LOADINGDATAOK) {
                    mAdapter = new ProcessAdapter();
                    lvPrcesss.setAdapter(mAdapter);
                    tvProcessType.setText("用户进程：" + mUserProcessList.size());

                }
            }
        };
        return inflater.inflate(R.layout.layout_process, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initUI(view);
        fillData();
        setListener();
    }

    /**
     * 设置事件监视器
     */
    private void setListener() {
        // 列表设置滚动监听事件  需要 更改 浮动 的 显示 进程类的数
        lvPrcesss.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem <= mUserProcessList.size()) {
                    tvProcessType.setText("用户进程：" + mUserProcessList.size());
                } else if (firstVisibleItem == mUserProcessList.size() + 1) {
                    tvProcessType.setText("系统进程：" + mSysProcessList.size());
                }
            }
        });
        // 全选按钮
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ProcessInfoBean pi : mUserProcessList) {
                    pi.isCheck = true;
                }
                for (ProcessInfoBean pi : mSysProcessList) {
                    pi.isCheck = true;
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        //反选按钮
        btnInverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ProcessInfoBean pi : mUserProcessList) {
                    pi.isCheck = !pi.isCheck;
                }
                for (ProcessInfoBean pi : mSysProcessList) {
                    pi.isCheck = !pi.isCheck;
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startMem = mProcessUtil.getAvailableMemory();
                mProcessUtil.killProgess(mUserProcessList);
                mProcessUtil.killProgess(mSysProcessList);
                long endMem = mProcessUtil.getAvailableMemory();
                mProgressBar.show();


                fillData();
                mAdapter.notifyDataSetChanged();
                mProgressBar.dismiss();
                Toast.makeText(getContext(), "释放内存:" + Formatter.formatFileSize(getContext(), endMem - startMem), Toast.LENGTH_SHORT).show();
            }
        });
        // 设置按钮的 点击事件 监听
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // 获取用户的配置
                boolean b1 = SPUtil.getBoolean(getContext(), CommonSignal.Process.IS_HIDDEN_SYSPROCESS, false);
                boolean b2 = SPUtil.getBoolean(getContext(), CommonSignal.Process.LOCK_SCREEN_CLEAR, false);
                boolean[] checkedItem = {b1, b2};

                builder.setMultiChoiceItems(new String[]{"隐藏系统进程", "锁屏清理进程"}, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        Toast.makeText(getContext(), "" + i + b, Toast.LENGTH_SHORT).show();
                        // 保存用户的配置
                        SPUtil.putboolean(getContext(), i == 0 ? CommonSignal.Process.IS_HIDDEN_SYSPROCESS :
                                CommonSignal.Process.LOCK_SCREEN_CLEAR, b);
                    }
                });
                builder.setTitle("设置");
                builder.setIcon(R.drawable.head);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.create().show();
            }
        });
    }

    /**
     * 填充数据
     */
    private void fillData() {
        tvProcessCounts.setText("进程总数：" + new ProcessUtil(getContext()).getAllProcessCount());
        String availableMem = Formatter.formatFileSize(getContext(), mProcessUtil.getAvailableMemory());
        String totalMem = Formatter.formatFileSize(getContext(), mProcessUtil.getTotalMemory());
        tvMem.setText(String.format("剩余/总共：%s/%s", availableMem, totalMem));
        // 加载 进程信息 并且 分成 用户进程 和系统进程 
        mUserProcessList = new ArrayList<>();
        mSysProcessList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mProcessUtil.loadClassfiyProcessInfoList(mUserProcessList, mSysProcessList);
                Message msg = Message.obtain();
                msg.what = LOADINGDATAOK;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 找出控件
     */
    private void initUI(View viewContainer) {
        tvProcessCounts = (TextView) viewContainer.findViewById(R.id.tv_processManager_counts);
        tvMem = (TextView) viewContainer.findViewById(R.id.tv_processManager_memorystatus);
        lvPrcesss = (ListView) viewContainer.findViewById(R.id.lv_process_list);
        tvProcessType = (TextView) viewContainer.findViewById(R.id.tv_processManager_type);
        btnAll = (Button) viewContainer.findViewById(R.id.bt_process_all);
        btnInverse = (Button) viewContainer.findViewById(R.id.bt_process_reverse);
        btnClear = (Button) viewContainer.findViewById(R.id.bt_process_clear);
        btnSetting = (Button) viewContainer.findViewById(R.id.bt_process_setting);
    }

    /**
     * ListView 的 两种 类型 适配器  关键  getItemViewType getViewTypeCount
     */
    private class ProcessAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (SPUtil.getBoolean(getContext(), CommonSignal.Process.IS_HIDDEN_SYSPROCESS, false)) {
                return mUserProcessList.size() + 1;
            }
            return mUserProcessList.size() + mSysProcessList.size() + 2;
        }

        // 要点 如果是 进程类别 条目 则返回 空；如果是 正常条目 则需要返回 真正的条目 位置
        @Override
        public ProcessInfoBean getItem(int position) {
            if (position == 0 || position == mUserProcessList.size() + 1) {
                return null;
            }
            if (position < mUserProcessList.size() + 1) {
                return mUserProcessList.get(position - 1);
            }
            return mSysProcessList.get(position - mUserProcessList.size() - 2);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            if (type == 0) {
                final Holder holder;
                if (convertView == null) {
                    convertView = View.inflate(getContext(), R.layout.processinfo_listview_item, null);
                    holder = new Holder();
                    holder.icon = (ImageView) convertView.findViewById(R.id.iv_processManager_listItem_icon);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_processManager_listItem_title);
                    holder.subTitle = (TextView) convertView.findViewById(R.id.tv_processManager_listItem_subtitle);
                    holder.cbIsCheck = (CheckBox) convertView.findViewById(R.id.cb_processmanager_listItem_checkbox);
                    convertView.setTag(holder);
                } else {
                    holder = (Holder) convertView.getTag();
                }
                holder.icon.setImageDrawable(getItem(position).icon);
                holder.title.setText(getItem(position).name);
                holder.subTitle.setText("内存占用：" + Formatter.formatFileSize(getContext(), getItem(position).memSize));
                // 全选按钮
                holder.cbIsCheck.setChecked(getItem(position).isCheck);
                holder.cbIsCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.cbIsCheck.isChecked()) {
                            getItem(position).isCheck = true;
                        } else if (!holder.cbIsCheck.isChecked()) {
                            getItem(position).isCheck = false;
                        }
                    }
                });
                return convertView;
            }
            // 剩下的情况是特殊的 文本
            HolderText holderText;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.layout_textview_process, null);
                holderText = new HolderText();
                holderText.showClassfiyProcessCount = (TextView) convertView.findViewById(R.id.tv_processManager_showprocessCount);
                convertView.setTag(holderText);
            } else {
                holderText = (HolderText) convertView.getTag();
            }
            if (position == 0) {
                holderText.showClassfiyProcessCount.setText("用户进程：" + mUserProcessList.size());
            }
            holderText.showClassfiyProcessCount.setText("系统进程：" + mSysProcessList.size());

            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mUserProcessList.size() + 1) {
                return 1; // 进程类型的标注 文本类型
            }
            return 0; // 正常 item
        }

        @Override
        public int getViewTypeCount() {
            // 条目的种类
            return 2;
        }

        private class Holder {
            ImageView icon;
            TextView title, subTitle;
            CheckBox cbIsCheck;
        }

        private class HolderText {
            TextView showClassfiyProcessCount;
        }
    }
}


