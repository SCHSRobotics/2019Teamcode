package org.firstinspires.ftc.teamcode.drivers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class SensorUtil implements SensorEventListener {
    public static SensorManager sensorService;
    public static Sensor magSensor, accelerometer, gyroscope;
    public static SensorUtil util;

    public float[] acellVal;
    public float[] gyroVal;


    public void setup(HardwareMap hardwareMap){
        sensorService = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        magSensor = sensorService.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorService.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorService.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorService.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorService.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        util = new SensorUtil();
    }
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            for (int i = 0; i <event.values.length; i++){
                acellVal[i] = event.values[i] + acellVal[i];
            }
        }else if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
            for (int i = 0; i <event.values.length; i++){
                gyroVal[i] = event.values[i] + gyroVal[i];
            }
        }
    }
    public float[] getacellVal() {
        float[] tmp = acellVal;
        acellVal = new float[acellVal.length];
        return tmp;

    }
    public float[] getGyroVal() {
        float[] tmp = gyroVal;
        gyroVal = new float[gyroVal.length];
        return tmp;
    }
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
