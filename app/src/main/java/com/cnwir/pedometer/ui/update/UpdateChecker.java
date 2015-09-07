package com.cnwir.pedometer.ui.update;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;


import com.cnwir.pedometer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * Created by heaven on 2015/9/7.
 */
public class UpdateChecker{

    private static final String TAG = "UpdateChecker";
    private Context mContext;
    //检查版本信息的线程
    private Thread mThread;
    private AppVersion mAppVersion;
    //下载apk的对话框
    private ProgressDialog mProgressDialog;

    private File apkFile;

    public UpdateChecker(Context context) {
        mContext = context;
        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("正在下载");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void checkForUpdates(final String json) {
        final  Handler handler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    mAppVersion = (AppVersion) msg.obj;
                    try{
                        int versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
                        if (mAppVersion.getVersion() > versionCode) {
                            showUpdateDialog();
                        } else {
                            //Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
                        }
                    }catch (PackageManager.NameNotFoundException ignored) {
                        //
                    }
                }
            }
        };

        mThread = new Thread() {
            @Override
            public void run() {
                //if (isNetworkAvailable(mContext)) {
                Message msg = new Message();
                Log.i("jianghejie", "json = " + json);
                if(json!=null){
                    AppVersion appVersion = parseJson(json);
                    msg.what = 1;
                    msg.obj = appVersion;
                    handler.sendMessage(msg);
                }else{
                    Log.e(TAG, "can't get app update json");
                }
            }
        };
        mThread.start();
    }
    private AppVersion parseJson(String json) {
        AppVersion appVersion = new AppVersion();
        try {
            JSONObject obj = new JSONObject(json);
            String apkUrl = obj.getString(AppVersion.APK_DOWNLOAD_URL);
            int apkCode = obj.getInt(AppVersion.APK_VERSION_CODE);
            appVersion.setVersion(apkCode);
            appVersion.setUrl(apkUrl);
        } catch (JSONException e) {
            Log.e(TAG, "parse json error", e);
        }
        return appVersion;
    }

    public void showUpdateDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("有新版本");
        builder.setMessage(mAppVersion.getUpdateMessage());
        builder.setPositiveButton("立刻下载",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        downLoadApk();
                    }
                });
        builder.setNegativeButton("以后再说",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
        builder.show();
    }

    public void downLoadApk() {
        String apkUrl = mAppVersion.getUrl();
        String dir = mContext.getExternalFilesDir( "apk").getAbsolutePath();
        File folder = Environment.getExternalStoragePublicDirectory(dir);
        if(folder.exists() && folder.isDirectory()) {
            //do nothing
        }else {
            folder.mkdirs();
        }
//        String filename = apkUrl.substring(apkUrl.lastIndexOf("/"),apkUrl.length());
        String filename = "zuji.apk";
        String destinationFilePath =  dir + "/" + filename;
        apkFile = new File(destinationFilePath);
        mProgressDialog.show();
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra("url", apkUrl);
        intent.putExtra("dest", destinationFilePath);
        intent.putExtra("receiver", new DownloadReceiver(new Handler()));
        mContext.startService(intent);
    }


    private class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    mProgressDialog.dismiss();
                    //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
                    String[] command = {"chmod","777",apkFile.toString()};
                    try{
                        ProcessBuilder builder = new ProcessBuilder(command);
                        builder.start();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                        mContext.startActivity(intent);
                    }catch (Exception e){

                    }
                }
            }
        }
    }


}
