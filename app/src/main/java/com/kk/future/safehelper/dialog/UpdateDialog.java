package com.kk.future.safehelper.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.bean.VersionBean;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description: 检查更新的对话框 考虑到手动更新和自动更新需要重用 所以抽取<br>
 * date: 2016/10/28  8:23.
 */

public class UpdateDialog {
    /**
     * 需要跟新 有更新的APK
     */
    private boolean isUpdate;
    private Context mContext;
    private VersionBean mVersionBean;
    private ClickCallBack mClickCallBack;

    public UpdateDialog(boolean isUpdate, Context mContext, VersionBean bean, ClickCallBack callBack) {
        this.isUpdate = isUpdate;
        this.mContext = mContext;
        mVersionBean = bean;
        mClickCallBack = callBack;
    }

    public void show() {
        if (!isUpdate) {
            Toast.makeText(mContext, "没有更新版本", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("发现更新版本");
            builder.setMessage(mVersionBean.description);
            builder.setPositiveButton("跟新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mClickCallBack != null) {
                        mClickCallBack.onBtnPositiveClick(mVersionBean.downloadUrl);
                    }
                }
            });
            builder.setNegativeButton("取消", null);
            builder.setIcon(R.drawable.head);
            builder.create().show();
        }
    }

    /**
     * 回调接口
     */
    public interface ClickCallBack {
        void onBtnPositiveClick(String apkUrl);
    }
}
