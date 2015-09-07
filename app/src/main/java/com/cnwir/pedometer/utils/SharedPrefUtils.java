package com.cnwir.pedometer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

/**
 * Created by heaven on 2015/6/26.
 */
public class SharedPrefUtils {

    /**
     * 获取版本号
     *
     * @param context
     * @param prefName
     * @return
     */


    public static int getVersionCode(Context context, String prefName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        int versionCode = -1;
        if (sp != null && context != null) {

            versionCode = sp.getInt(prefName, -1);
        }

        return versionCode;

    }

    /**
     * 保存版本号
     *
     * @param versionCode
     * @param prefName
     * @param context
     */
    public static void putVersionCode(int versionCode, String prefName, Context context) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(prefName, versionCode);
        if (Build.VERSION.SDK_INT > 9) {

            editor.apply();
        } else {

            editor.commit();
        }

    }

    /**
     * 保存记步传感器最近的一次步数
     *
     * @param steps
     * @param prefName
     * @param context
     */

    public static void putSensorStepsLately(int steps, String prefName, Context context) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(prefName, steps);
        if (Build.VERSION.SDK_INT > 9) {

            editor.apply();
        } else {

            editor.commit();
        }

    }

    /**
     * 获取传感器最近一次的步数
     * @param context
     * @param prefName
     * @return
     */

    public static int getSensorStepsLately(Context context, String prefName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        int steps = 0;
        if (sp != null && context != null) {

            steps = sp.getInt(prefName, 0);
        }

        return steps;

    }

}
