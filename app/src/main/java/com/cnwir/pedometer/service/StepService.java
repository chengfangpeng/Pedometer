package com.cnwir.pedometer.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;

/**
 * Created by heaven on 2015/7/17.
 */

public class StepService extends Service {
	public static Boolean flag = false;
	private SensorManager sensorManager;
	private StepDetector stepDetector;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		startStepDetector();

	}

	private void startStepDetector() {


		System.out.println("StepService start");
		flag = true;
		stepDetector = new StepDetector(this);
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);//获取传感器管理器的实�?
		Sensor sensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//获得传感器的类型，这里获得的类型是加速度传感�?
		//此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
		sensorManager.registerListener(stepDetector, sensor,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
		System.out.println("StepService onDestroy");
		if (stepDetector != null) {
			sensorManager.unregisterListener(stepDetector);
		}

	}
}
