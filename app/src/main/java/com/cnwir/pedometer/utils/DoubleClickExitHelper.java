package com.cnwir.pedometer.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import com.cnwir.pedometer.AppManager;
import com.cnwir.pedometer.R;


/**
 * 双击退出应用
 * Created by heaven on 2015/5/26.
 */
public class DoubleClickExitHelper {

    private Handler mHander;
    private Toast mToast;

    private Activity activity;

    private boolean isBackClicking;

    public DoubleClickExitHelper(Activity activity){

        this.activity = activity;
        mHander = new Handler(Looper.getMainLooper());
    }

    public boolean doubleClickExit(int keyCode, KeyEvent keyEvent){

        if(keyCode != KeyEvent.KEYCODE_BACK){

            return false;
        }
        if(isBackClicking){
            mHander.removeCallbacks(onBackTimeRunnable);
            if(mToast != null){
                mToast.cancel();
                mToast = null;
            }
            AppManager.getAppManager().appExit(activity);
            return true;

        }else {
            isBackClicking = true;
            if(mToast == null){
                mToast = Toast.makeText(activity, R.string.double_click_exit, Toast.LENGTH_LONG);
            }
            mToast.show();
            mHander.postDelayed(onBackTimeRunnable, 2000);


            return true;
        }

    }
    private Runnable onBackTimeRunnable = new Runnable() {

        @Override
        public void run() {
            isBackClicking = false;
            if(mToast != null){
                mToast.cancel();
            }
        }
    };
}
