package com.kk.future.safehelper.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.bean.AppInfo;
import com.kk.future.safehelper.dao.AppLockDao;
import com.kk.future.safehelper.utils.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:<br>
 * date: 2016/10/31  15:17.
 */
public class FragmentToolBoxAppLock extends Fragment {
    private TextView title;
    private ImageButton setting;

    private Button bt_unlock, bt_lock;
    private LinearLayout ll_unlock, ll_lock;
    private TextView tv_unlock, tv_lock;
    private ListView lv_unlock, lv_lock;
    private List<AppInfo> mAppInfoList;
    private List<AppInfo> mLockList;
    private List<AppInfo> mUnLockList;
    private AppLockDao mDao;
    private MyAdapter mLockAdapter;
    private MyAdapter mUnLockAdapter;
    private TranslateAnimation mTranslateAnimation;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //6.接收到消息,填充已加锁和未加锁的数据适配器
            mLockAdapter = new MyAdapter(true);
            lv_lock.setAdapter(mLockAdapter);

            mUnLockAdapter = new MyAdapter(false);
            lv_unlock.setAdapter(mUnLockAdapter);
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_tool_box_app_lock, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        iniAnim();
        initUI(view);
        initData();
    }

    private void initData() {
        new Thread() {
            public void run() {
                //1.获取所有手机中的应用
                mAppInfoList = AppInfoProvider.getAppInfoList(getContext());
                //2.区分已加锁应用和未加锁应用
                mLockList = new ArrayList<AppInfo>();
                mUnLockList = new ArrayList<AppInfo>();

                //3.获取数据库中已加锁应用包名的的结合
                mDao = AppLockDao.getInstance(getContext());
                List<String> lockPackageList = mDao.findAll();

                for (AppInfo appInfo : mAppInfoList) {
                    //4,如果循环到的应用的包名,在数据库中,则说明是已加锁应用
                    if (lockPackageList.contains(appInfo.packageName)) {
                        mLockList.add(appInfo);
                    } else {
                        mUnLockList.add(appInfo);
                    }
                }
                //5.告知主线程,可以使用维护的数据
                mHandler.sendEmptyMessage(0);
            }

            ;
        }.start();
    }

    // 初始化动画 添加 移动的动画
    private void iniAnim() {
        mTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        mTranslateAnimation.setDuration(500);
    }

    private void initUI(View view) {
        title = (TextView) view.findViewById(R.id.tv_action_title);
        title.setText("程序锁");
        setting = (ImageButton) view.findViewById(R.id.ib_actionbar_setting);
        setting.setVisibility(View.GONE);
        // 以下为 添加
        bt_unlock = (Button) view.findViewById(R.id.bt_unlock);
        bt_lock = (Button) view.findViewById(R.id.bt_lock);

        ll_unlock = (LinearLayout) view.findViewById(R.id.ll_unlock);
        ll_lock = (LinearLayout) view.findViewById(R.id.ll_lock);

        tv_unlock = (TextView) view.findViewById(R.id.tv_unlock);
        tv_lock = (TextView) view.findViewById(R.id.tv_lock);

        lv_unlock = (ListView) view.findViewById(R.id.lv_unlock);
        lv_lock = (ListView) view.findViewById(R.id.lv_lock);
        // 解锁的事件监听
        bt_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.已加锁列表隐藏,未加锁列表显示
                ll_lock.setVisibility(View.GONE);
                ll_unlock.setVisibility(View.VISIBLE);
                //2.未加锁变成深色图片,已加锁变成浅色图片
                bt_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                bt_lock.setBackgroundResource(R.drawable.tab_right_default);
            }
        });
        // 把锁的事件监听
        bt_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.已加锁列表显示,未加锁列表隐藏
                ll_lock.setVisibility(View.VISIBLE);
                ll_unlock.setVisibility(View.GONE);
                //2.未加锁变成浅色图片,已加锁变成深色图片
                bt_unlock.setBackgroundResource(R.drawable.tab_left_default);
                bt_lock.setBackgroundResource(R.drawable.tab_right_pressed);
            }
        });


    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_lock;
    }

    // 展示APP 信息 ListView 的适配器
    class MyAdapter extends BaseAdapter {
        private boolean isLock;

        /**
         * @param isLock 用于区分已加锁和未加锁应用的标示	true已加锁数据适配器	false未加锁数据适配器
         */
        public MyAdapter(boolean isLock) {
            this.isLock = isLock;
        }

        @Override
        public int getCount() {
            if (isLock) {
                tv_lock.setText("已加锁应用:" + mLockList.size());
                return mLockList.size();
            } else {
                tv_unlock.setText("未加锁应用:" + mUnLockList.size());
                return mUnLockList.size();
            }
        }

        @Override
        public AppInfo getItem(int position) {
            if (isLock) {
                return mLockList.get(position);
            } else {
                return mUnLockList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.listview_islock_item, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_lock);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final AppInfo appInfo = getItem(position);
            // 整个条目 需要 动画
            final View animationView = convertView;

            holder.iv_icon.setBackgroundDrawable(appInfo.icon);
            holder.tv_name.setText(appInfo.name);
            if (isLock) {
                holder.iv_lock.setBackgroundResource(R.drawable.lock);
            } else {
                holder.iv_lock.setBackgroundResource(R.drawable.unlock);
            }
            holder.iv_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加动画效果,动画默认是非阻塞的,所以执行动画的同时,动画以下的代码也会执行
                    animationView.startAnimation(mTranslateAnimation);//500毫秒
                    //对动画执行过程做事件监听,监听到动画执行完成后,再去移除集合中的数据,操作数据库,刷新界面
                    mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            //动画开始的是调用方法
                        }

                        //动画执行结束后调用方法
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (isLock) {
                                //已加锁------>未加锁过程
                                //1.已加锁集合删除一个,未加锁集合添加一个,对象就是getItem方法获取的对象
                                mLockList.remove(appInfo);
                                mUnLockList.add(appInfo);
                                //2.从已加锁的数据库中删除一条数据
                                mDao.delete(appInfo.packageName);
                            } else {
                                //未加锁------>已加锁过程
                                //1.已加锁集合添加一个,未加锁集合移除一个,对象就是getItem方法获取的对象
                                mLockList.add(appInfo);
                                mUnLockList.remove(appInfo);
                                //2.从已加锁的数据库中插入一条数据
                                mDao.insert(appInfo.packageName);
                            }
                            //3.刷新数据适配器
                            mLockAdapter.notifyDataSetChanged();
                            //3.刷新数据适配器
                            mUnLockAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            //动画重复时候调用方法
                        }
                    });
                }
            });
            return convertView;
        }
    }

}
