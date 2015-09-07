package com.cnwir.pedometer.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cnwir.pedometer.App;
import com.cnwir.pedometer.MainActivity;
import com.cnwir.pedometer.R;
import com.cnwir.pedometer.db.PedometerDB;
import com.cnwir.pedometer.domain.Step;
import com.cnwir.pedometer.domain.User;
import com.cnwir.pedometer.service.NormalPostRequest;
import com.cnwir.pedometer.service.StepDetector;
import com.cnwir.pedometer.service.StepService;
import com.cnwir.pedometer.ui.BaseFragment;
import com.cnwir.pedometer.ui.circle.CircleActivty;
import com.cnwir.pedometer.utils.DESPlus;
import com.cnwir.pedometer.utils.DateFormatUtils;
import com.cnwir.pedometer.utils.RequestUrl;
import com.cnwir.pedometer.utils.ShareUtils;
import com.cnwir.pedometer.utils.SharedPrefUtils;
import com.cnwir.pedometer.view.ScaleProgress;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by heaven on 2015/7/17.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    public static final String SPSENSORSTEPS = "pedometer";

    private Activity mActivity;
    private ScaleProgress scaleProgress;

    private SensorManager mSensorManager;

    private Sensor mSensor;

    private Button home_submit_data, home_circle_btn;

    private PedometerDB pedometerDB;

    private ProgressDialog pd;

    private Timer timer;

    /**
     * 用于指示传感器的回调函数是不是第一次被调用，如果是第一次调用，判断传感器的步数是不是为0，用于确定传感器是否被重置过
     */

    private boolean isFirstSensorMethedCalled = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View contentView = inflater.inflate(R.layout.home_layout, null);
        init();
        initView(contentView);
        updateStep();
        return contentView;
    }

    private void init(){
        pedometerDB = PedometerDB.getInstance(mActivity);
        if (Build.VERSION.SDK_INT >= 19) {
            startStepSensorKitKat();

        } else {
            startStepServiceUnderKitkat();

        }

    }

    private void initView(View view) {
        scaleProgress = (ScaleProgress) view.findViewById(R.id.scaleProgress);
        scaleProgress.setTargetNum(App.getInstance().getUser().getGoal());
        home_submit_data = (Button) view.findViewById(R.id.home_submit_data);
        home_circle_btn = (Button) view.findViewById(R.id.home_circle);


        home_circle_btn.setOnClickListener(this);
        home_submit_data.setOnClickListener(this);

    }

    /**
     * 描述：如果服务开启，更新步数
     */
    private void updateStep() {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        System.out.println("flag = " + StepService.flag);
        if (StepService.flag) {
            if(Build.VERSION.SDK_INT < 19){

                Step saveStep = pedometerDB.loadSteps(App.getInstance().getUser().getId(), date);


                if (saveStep != null) {
                    int tempSteps = saveStep.getNumber();
                    StepDetector.CURRENT_SETP = tempSteps;
                    System.out.println("start CURRENT_SETP = " + StepDetector.CURRENT_SETP);
                }
            }

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            scaleProgress.setStepNum(StepDetector.CURRENT_SETP);
                            System.out.println("转盘输出 = " + StepDetector.CURRENT_SETP);
                        }
                    });


                }
            }, 100, 1000);

        }


    }

    private void startStepServiceUnderKitkat() {
        StepService.flag = true;
        Intent intent = new Intent(mActivity, StepService.class);
        mActivity.startService(intent);

    }

    private void startStepSensorKitKat() {

        StepService.flag = true;


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        final boolean isToday = pedometerDB.loadSteps(App.getInstance().getUser().getId(), date) != null;


        mSensorManager = (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mSensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if (isFirstSensorMethedCalled) {

                    if (event.values[0] < 5) {
                        SharedPrefUtils.putSensorStepsLately(0, SPSENSORSTEPS, mActivity);
                    }
                    isFirstSensorMethedCalled = false;
                }

                System.out.println("enent[0] = " + event.values[0]);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.format(new Date());
                int sensorSteps = SharedPrefUtils.getSensorStepsLately(mActivity, SPSENSORSTEPS);
                User user = App.getInstance().getUser();


                String date = sdf.format(new Date());

                Step saveStep = pedometerDB.loadSteps(App.getInstance().getUser().getId(), date);
                System.out.println("saveStep = " + saveStep);

                System.out.println("sensorSteps = " + sensorSteps);

                if (saveStep != null) {
                    int tempSteps = saveStep.getNumber();
                    StepDetector.CURRENT_SETP = tempSteps + ((int) event.values[0] - sensorSteps);
                    saveStep.setNumber(StepDetector.CURRENT_SETP);
                    pedometerDB.updateStep(saveStep);
                    user.setToday_step_num(StepDetector.CURRENT_SETP);

                } else {
                    Step step = new Step();
                    step.setDate(date);
                    StepDetector.CURRENT_SETP = (int) event.values[0] - sensorSteps;
                    step.setNumber(StepDetector.CURRENT_SETP);
                    step.setUserId(App.getInstance().getUser().getId());
                    pedometerDB.saveStep(step);
                }


                SharedPrefUtils.putSensorStepsLately((int) event.values[0], SPSENSORSTEPS, mActivity);


            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, mSensor, mSensorManager.SENSOR_DELAY_FASTEST);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.home_submit_data:
                summitData();
                break;
            case R.id.home_circle:

                Intent intent = new Intent(mActivity, CircleActivty.class);
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                break;
            default:
                break;
        }

    }

    private void summitData() {
        pd = new ProgressDialog(mActivity);
        pd.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
        pd.setMessage("正在上传...");
        pd.show();
        String submitData = null;
        try {
            List<Date> sevenTimes = DateFormatUtils.dayToWeek(new Date());
            List<Step> sevenSteps = new ArrayList<Step>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            User user = App.getInstance().getUser();


            for (int i = 0; i < sevenTimes.size(); i++) {

                Step step = new Step();
                String tempDate = sdf.format(sevenTimes.get(i));
                step = pedometerDB.loadSteps(user.getId(), tempDate);
                if (step != null) {

                    sevenSteps.add(step);
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sevenSteps.size(); i++) {


                Date date = sdf.parse(sevenSteps.get(i).getDate());
                sb.append(date.getTime() + "," + sevenSteps.get(i).getNumber() + ";");

            }

            DESPlus d = new DESPlus();
            submitData = d.encrypt(sb.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", App.getInstance().getUser().getToken());
        map.put("d", submitData);
//
        NormalPostRequest request = new NormalPostRequest(RequestUrl.submitData(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                pd.dismiss();

                Toast.makeText(mActivity, "上传数据成功！", Toast.LENGTH_SHORT).show();
                System.out.println("上传数据 = " + jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Toast.makeText(mActivity, "上传数据失败！", Toast.LENGTH_SHORT).show();
                System.out.println("上传数据失败" + volleyError.toString());

            }
        }, map);

        executeRequest(request);


    }

    @Override
    public void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < 19) {
            saveDataUnderKitkat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT < 19) {
            saveDataUnderKitkat();
        }
        if (StepService.flag) {
            if(timer != null)
            timer.cancel();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (Build.VERSION.SDK_INT < 19) {
            saveDataUnderKitkat();
        }
    }

    /**
     * 保存4.4以下的数据
     */

    private void saveDataUnderKitkat() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.format(new Date());
        User user = App.getInstance().getUser();
        String date = sdf.format(new Date());
        //先到数据库查找今天是否有数据，如果有数据则更新数据，否则添加数据
        Step saveStep = pedometerDB.loadSteps(App.getInstance().getUser().getId(), date);

        System.out.println("save steps = " + StepDetector.CURRENT_SETP);
        if (saveStep != null) {
            saveStep.setNumber(StepDetector.CURRENT_SETP);
            pedometerDB.updateStep(saveStep);
            user.setToday_step_num(StepDetector.CURRENT_SETP);

        } else {
            Step step = new Step();
            step.setDate(date);
            step.setNumber(StepDetector.CURRENT_SETP);
            step.setUserId(App.getInstance().getUser().getId());
            pedometerDB.saveStep(step);
        }


    }
}
