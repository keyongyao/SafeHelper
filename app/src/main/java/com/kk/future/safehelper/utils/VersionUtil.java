package com.kk.future.safehelper.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.kk.future.safehelper.R;
import com.kk.future.safehelper.bean.VersionBean;
import com.kk.future.safehelper.signal.CommonSignal;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:封装一些版本的常用方法<br>
 * date: 2016/10/27  15:40.
 */

public class VersionUtil {
    private static final String TAG = "VersionUtil";

    /**
     *  检查更新的结果状态
     */

    /**
     * @param mContext 上下文
     * @return versionName 在 build.gradle 中配置
     */
    public static String getLocalVersionName(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "未知";
    }

    /**
     * AsyncTask  异步执行更新操作
     * @param updateURL app 版本 Json 的URL
     * @param handler  发送 处理完的 VersionBean
     */
    public static void checkUpdate(URL updateURL, final Handler handler, final Context context) {
        class Check extends AsyncTask<URL, Void, String> {
            VersionBean bean = new VersionBean();

            @Override
            protected String doInBackground(URL... params) {
                return downloadJson(params[0]);
            }

            @Override
            protected void onPostExecute(String jsonstr) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonstr);
                    bean.description = jsonObject.getString("description");
                    bean.downloadUrl = jsonObject.getString("downloadUrl");
                    bean.versionCode = jsonObject.getInt("versionCode");
                    bean.versionName = jsonObject.getString("versionName");
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                LogCatUtil.getInstance().i(TAG, bean.toString());
                // 如果有更新版本，则发送 VersionBean
                if (bean.versionCode > getLocalVersionCode(context)) {
                    Message msg = Message.obtain();
                    msg.what = CommonSignal.CheckUpdate.HAS_lATEST_VERSION_YES;
                    msg.obj = bean;
                    handler.sendMessage(msg);
                } else {
                    Message msg = Message.obtain();
                    msg.what = CommonSignal.CheckUpdate.HAS_lATEST_VERSION_NO;
                    handler.sendMessage(msg);
                }
            }
        }
        new Check().execute(updateURL);
    }

    /**
     * @param mContext 上下文
     * @return versionCode  在 build.gradle中配置
     */
    public static int getLocalVersionCode(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 运行在 AsyncTask WorkThred
     *
     * @param url app 更新的 服务器 URL
     * @return 版本信息
     */
    private static String downloadJson(URL url) {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        String result = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                outputStream = new ByteArrayOutputStream();
                byte[] flush = new byte[4096];
                int len;
                while (-1 != (len = inputStream.read(flush))) {
                    outputStream.write(flush, 0, len);
                }
                outputStream.flush();
                result = outputStream.toString();
            } else {
                LogCatUtil.getInstance().e(TAG, "检查更新错误码：" + new Date() + conn.getResponseCode(), null);
            }

        } catch (IOException e) {
            LogCatUtil.getInstance().e(TAG, "检查更新错误：" + new Date() + e.getLocalizedMessage() + "\n" + e.getCause(), null);
        } finally {
            IOutil.closeAll(outputStream, inputStream);
        }
        return result;
    }

    /**
     * 下载 更新包 APK
     *
     * @param apkUrl  下载APK 的 URL
     * @param context 上下文
     */
    public static void downloadAPK(String apkUrl, final Context context) {
        // 设置 进度条
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("下载进度");
        dialog.setIcon(R.drawable.icon);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
        // 保存 APK 的路径
        File downloadDir = context.getExternalFilesDir("download");
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        final String filePath = downloadDir.getAbsolutePath() + apkUrl.substring(apkUrl.lastIndexOf("/"), apkUrl.length());
        RequestParams params = new RequestParams(apkUrl);
        params.setSaveFilePath(filePath);
        Callback.ProgressCallback<File> callback = new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
                LogCatUtil.getInstance().i(TAG, "onWaiting");
            }

            @Override
            public void onStarted() {
                LogCatUtil.getInstance().i(TAG, "onStarted");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogCatUtil.getInstance().i(TAG, "total:" + total + "  current:" + current + " isDownloading: " + isDownloading);
                dialog.setMax((int) (total / 1024));
                dialog.setProgress((int) (current / 1024));

            }

            @Override
            public void onSuccess(File result) {
                LogCatUtil.getInstance().i(TAG, "onSuccess");
                // 弹出安装 界面
                dialog.dismiss();
                APKUtil.installAPK(context, result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                LogCatUtil.getInstance().i(TAG, "onFinished");

            }
        };

        x.http().get(params, callback);
    }
}
