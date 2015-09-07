package com.cnwir.pedometer.ui.loadding;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cnwir.pedometer.MainActivity;
import com.cnwir.pedometer.R;
import com.cnwir.pedometer.ui.BaseActivity;
import com.cnwir.pedometer.utils.SharedPrefUtils;
import com.cnwir.pedometer.utils.VersionManager;

/**
 * Created by heaven on 2015/6/26.
 */
public class LoadingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadding);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CheckToGuide();
            }
        }, 1000);

    }

    private void toMain() {

        Intent intent = new Intent();
        intent.setClass(LoadingActivity.this, MainActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_left_in,
                R.anim.slide_right_out);

        finish();
    }


    /**
     * 检测是不是第一次进入程序
     */

    private void CheckToGuide() {
        int oldVersionCode = SharedPrefUtils.getVersionCode(this,
                "version_code");

        int currentVersionCode = VersionManager.getCurrentVersionCode(this);

        if (currentVersionCode > oldVersionCode) {
            Intent guideIntent = new Intent(this, GuideAcitity.class);

            startActivity(guideIntent);

            overridePendingTransition(R.anim.slide_left_in,
                    R.anim.slide_right_out);
            SharedPrefUtils.putVersionCode(currentVersionCode, "version_code",
                    this);
            finish();

        } else {
            toMain();
        }
    }

}
