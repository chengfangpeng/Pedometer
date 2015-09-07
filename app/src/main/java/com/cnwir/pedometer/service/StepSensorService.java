package com.cnwir.pedometer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cnwir.pedometer.App;
import com.cnwir.pedometer.db.PedometerDB;
import com.cnwir.pedometer.domain.Step;
import com.cnwir.pedometer.domain.User;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heaven on 2015/8/14.
 * <p/>
 * kitkat以上的系统使用
 */
public class StepSensorService extends Service implements SensorEventListener {

    private int steps;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.format(new Date());


        String date = sdf.format(new Date());

        if (event.values[0] > Integer.MAX_VALUE) {
            //可能不是一个真实的值

            return;
        } else {
            steps = (int) event.values[0];
            if (steps > 0) {
//                Database db = Database.getInstance(this);
                PedometerDB pedometerDB = PedometerDB.getInstance(this);
                Step oldStep = pedometerDB.loadSteps(App.getInstance().getUser().getId(), date);
                if (oldStep != null) {
                    Step step = new Step();

                    String curDate = sdf.format(new Date());
                    step.setDate(curDate);

                    User user = App.getInstance().getUser();

                    user.setToday_step_num(steps);
                    App.getInstance().setUser(user);
                    step.setNumber(App.getInstance().getUser().getToday_step_num());
                    step.setUserId(App.getInstance().getUser().getId());
                    pedometerDB.updateStep(step);

                } else {



                }


//                if (db.getSteps(Util.getToday()) == Integer.MIN_VALUE) {
//                    int pauseDifference = steps -
//                            getSharedPreferences("pedometer", Context.MODE_MULTI_PROCESS)
//                                    .getInt("pauseCount", steps);
//                    db.insertNewDay(Util.getToday(), steps - pauseDifference);
//                    if (pauseDifference > 0) {
//                        // update pauseCount for the new day
//                        getSharedPreferences("pedometer", Context.MODE_MULTI_PROCESS).edit()
//                                .putInt("pauseCount", steps).commit();
//                    }
//                    reRegisterSensor();
//                }
//                db.saveCurrentSteps(steps);
//                db.close();
//                updateNotificationState();
//                startService(new Intent(this, WidgetUpdateService.class));
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void saveData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.format(new Date());

        User user = App.getInstance().getUser();

//        user.setToday_step_num(StepDetector.CURRENT_SETP);
        App.getInstance().setUser(user);

        Step step = new Step();

        String date = sdf.format(new Date());
        step.setDate(date);
        step.setNumber(App.getInstance().getUser().getToday_step_num());
        step.setUserId(App.getInstance().getUser().getId());


//        pedometerDB.updateStep(step);
//        if (pedometerDB.loadSteps(App.getInstance().getUser().getId(), date) != null) {
//
//            pedometerDB.updateStep(step);
//
//        } else {
//
//            pedometerDB.saveStep(step);
//        }


    }
}
