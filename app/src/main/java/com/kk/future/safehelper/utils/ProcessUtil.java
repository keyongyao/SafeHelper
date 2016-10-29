package com.kk.future.safehelper.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.bean.ProcessInfoBean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:进程模块相关的一些工具<br>
 * date: 2016/10/29  18:23.
 */

public class ProcessUtil {
    private final ActivityManager mAm;
    private final List<ActivityManager.RunningAppProcessInfo> mRunningAppProcesses;
    private Context mContext;
    private PackageManager mPm;

    public ProcessUtil(Context mContext) {
        this.mContext = mContext;
        mAm = (ActivityManager) mContext.getSystemService(Service.ACTIVITY_SERVICE);
        mRunningAppProcesses = mAm.getRunningAppProcesses();
        mPm = mContext.getPackageManager();
    }

    /**
     * 杀死列表里的进程
     *
     * @param killList
     */
    public void killProgess(ArrayList<ProcessInfoBean> killList) {
        for (ProcessInfoBean bean : killList
                ) {
            mAm.killBackgroundProcesses(bean.pkgName);
        }
    }

    /**
     * 获取可用的内存
     *
     * @return
     */
    public long getAvailableMemory() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mAm.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    /**
     * 获取总共的内存
     *
     * @return
     */
    public long getTotalMemory() {
        long mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息

        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content);
        return mTotal * 1024;
    }

    /**
     * @return 所有进程的数量
     */
    public int getAllProcessCount() {
        return mRunningAppProcesses.size();
    }

    /**
     * 分类进程集合  分为 系统  和 用户
     *
     * @param userProcessList
     * @param sysProcessList
     */
    public void loadClassfiyProcessInfoList(ArrayList<ProcessInfoBean> userProcessList, ArrayList<ProcessInfoBean> sysProcessList) {
        ArrayList<ProcessInfoBean> allProcessInfo = getAllProcessInfo();
        for (ProcessInfoBean bean : allProcessInfo
                ) {
            if (bean.isSystem) {
                sysProcessList.add(bean);
            } else {
                userProcessList.add(bean);
            }
        }
    }

    /**
     * 获取所有的进程信息
     *
     * @return
     */
    public ArrayList<ProcessInfoBean> getAllProcessInfo() {
        ArrayList<ProcessInfoBean> processInfoBeens = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo pi : mRunningAppProcesses
                ) {
            ProcessInfoBean bean = new ProcessInfoBean();
            // 获取进程包名
            bean.pkgName = pi.processName;
            // 获取进程 消耗的内存
            Debug.MemoryInfo[] processMemoryInfo = mAm.getProcessMemoryInfo(new int[]{pi.pid});
            Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            bean.memSize = memoryInfo.getTotalSharedDirty() * 1024;
            try {
                // 获取 APP label 和 icon
                ApplicationInfo applicationInfo = mPm.getApplicationInfo(bean.pkgName, PackageManager.GET_META_DATA);
                bean.name = applicationInfo.loadLabel(mPm).toString();
                bean.icon = applicationInfo.loadIcon(mPm);
                // 是否为系统进程
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    bean.isSystem = true;
                } else {
                    bean.isSystem = false;
                }
            } catch (PackageManager.NameNotFoundException e) {
                //   PackageManager.getApplicationInfo()  的名字 没有找到
                bean.name = pi.processName;
                bean.icon = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
                bean.isSystem = true;
                e.printStackTrace();
            }
            processInfoBeens.add(bean);
        }
        return processInfoBeens;
    }
}
