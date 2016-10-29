package com.kk.future.safehelper.fragment;

import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kk.future.safehelper.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description: 清除 缓存cache 的布局 <br>
 * date: 2016/10/29  10:23.
 */

public class FragmentCleanCache extends Fragment {
    private final int UPDATE_CACHE_APP = 22;
    private final int CHECK_CACHE_APP = 33;
    private final int CHECK_FINISH = 44;
    private final int CLEAR_CACHE = 55;
    /**
     * 清理缓存的 按钮
     */
    private Button btn_clear;
    /**
     * 扫描 进度 条
     */
    private ProgressBar progressBar;
    /**
     * 扫描过程中的 显示信息
     */
    private TextView tv_clearing_app;
    /**
     * 存档 有 缓存的 APP  展示 View
     */
    private LinearLayout ll_app_container;
    private PackageManager mPm;
    private Handler mHandler;
    /**
     * 正在 扫描 APP 的位置
     */
    private int mIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_clean_cache, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPm = getActivity().getPackageManager();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_CACHE_APP:
                        View view = View.inflate(getContext(), R.layout.linearlayout_cache_item, null);

                        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                        TextView tv_item_name = (TextView) view.findViewById(R.id.tv_name);
                        TextView tv_memory_info = (TextView) view.findViewById(R.id.tv_memory_info);
                        ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);

                        final CacheInfo cacheInfo = (CacheInfo) msg.obj;
                        iv_icon.setBackgroundDrawable(cacheInfo.icon);
                        tv_item_name.setText(cacheInfo.name);
                        tv_memory_info.setText(Formatter.formatFileSize(getContext(), cacheInfo.cacheSize));

                        ll_app_container.addView(view, 0);

                        break;
                    case CHECK_CACHE_APP:
                        tv_clearing_app.setText((String) msg.obj);
                        break;
                    case CHECK_FINISH:
                        tv_clearing_app.setText("扫描完成");
                        btn_clear.setEnabled(true);
                        break;
                    case CLEAR_CACHE:
                        ll_app_container.removeAllViews();
                        break;
                }
            }
        };
        btn_clear = (Button) view.findViewById(R.id.btn_cache_clear);
        btn_clear.setEnabled(false);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_cache);
        tv_clearing_app = (TextView) view.findViewById(R.id.tv_cache_clearing);
        ll_app_container = (LinearLayout) view.findViewById(R.id.ll_cache_appname_container);
        // 清理 缓存 的 方法  炫耀 aidl  反射调用 私有方法
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Class<?> clazz = Class.forName("android.content.pm.PackageManager");
                    Method method = clazz.getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);
                    method.invoke(mPm, Long.MAX_VALUE, new IPackageDataObserver.Stub() {

                        @Override
                        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                            //清除缓存完成后调用的方法(考虑权限)
                            Message msg = Message.obtain();
                            msg.what = CLEAR_CACHE;
                            mHandler.sendMessage(msg);
                        }
                    });

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        mIndex = 0;
    }

    /**
     * 获取 有缓存的 APP
     */
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                List<PackageInfo> installedPackages = mPm.getInstalledPackages(PackageManager.GET_CONFIGURATIONS);
                progressBar.setMax(installedPackages.size());
                for (PackageInfo pi : installedPackages
                        ) {
                    // 获取 APP 的缓存  并且显示到 ll_app_container
                    getAppCache(pi.packageName);
                    try {
                        Thread.sleep(100 + new Random().nextInt(50));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mIndex++;
                    progressBar.setProgress(mIndex);
                    //每循环一次就将检测应用的名称发送给主线程显示
                    Message msg = Message.obtain();
                    msg.what = CHECK_CACHE_APP;
                    String name = null;
                    try {
                        name = "正在扫描缓存：" + mPm.getApplicationInfo(pi.packageName, 0).loadLabel(mPm).toString();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    msg.obj = name;
                    mHandler.sendMessage(msg);
                }
                // 扫描完 APP
                Message msg = Message.obtain();
                msg.what = CHECK_FINISH;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 获取 APP 的缓存
     *
     * @param pkgName
     */
    protected void getAppCache(String pkgName) {
        // aidl 接口
        IPackageStatsObserver mPkgstatsObserver = new IPackageStatsObserver.Stub() {

            @Override
            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                long cacheSize = pStats.cacheSize;
                if (cacheSize > 0) {
                    Message msg = Message.obtain();
                    msg.what = UPDATE_CACHE_APP;
                    CacheInfo cacheInfo = new CacheInfo();
                    cacheInfo.cacheSize = cacheSize;
                    cacheInfo.packagename = pStats.packageName;
                    try {
                        cacheInfo.icon = mPm.getApplicationInfo(pStats.packageName, PackageManager.GET_META_DATA).loadIcon(mPm);
                        cacheInfo.name = mPm.getApplicationInfo(pStats.packageName, PackageManager.GET_META_DATA).loadLabel(mPm).toString();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 发送 有缓存 的 APP 的一些基本信息
                    msg.obj = cacheInfo;
                    mHandler.sendMessage(msg);
                }
            }
        };

        /**
         *  根据包名来获取缓存  ，由于方法是私有 需要反射
         */
        //1.获取指定类的字节码文件
        try {
            Class<?> clazz = Class.forName("android.content.pm.PackageManager");
            //2.获取调用方法对象
            Method method = clazz.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            //3.获取对象调用方法
            method.invoke(mPm, pkgName, mPkgstatsObserver);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 保存 一些 需要 清理 缓存的 APP 的信息
     */
    private class CacheInfo {
        public String name;
        public Drawable icon;
        public String packagename;
        public long cacheSize;
    }


}
