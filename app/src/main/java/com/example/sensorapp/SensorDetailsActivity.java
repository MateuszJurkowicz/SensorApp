package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorLight;
    private Sensor sensorStepDetector;
    private Sensor sensorStepCounter;
    private TextView sensorTextView;
    private int selectedSensorType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        Intent intent = getIntent();
        if (intent.hasExtra("sensorType")) {
            selectedSensorType = intent.getIntExtra("sensorType", -1);
        }

        sensorTextView = findViewById(R.id.sensor_light_label);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        if (sensorLight == null) {
            sensorTextView.setText(R.string.missing_sensor);
        }
        if (sensorStepDetector == null) {
            sensorTextView.setText(R.string.missing_sensor);
        }
        if (sensorStepCounter == null) {
            sensorTextView.setText(R.string.missing_sensor);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sensorLight != null) {
            sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (sensorStepDetector != null) {
            sensorManager.registerListener(this, sensorStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (sensorStepCounter != null) {
            sensorManager.registerListener(this, sensorStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("OnAccuracyChanged", "Użyto onAccuracyChanged");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float[] values = sensorEvent.values;

        if (selectedSensorType != sensorType) {
            return;
        }

        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                sensorTextView.setText(getResources().getString(R.string.light_sensor_label, values[0]));
                break;
            case Sensor.TYPE_STEP_COUNTER:
                sensorTextView.setText(getResources().getString(R.string.step_counter_sensor_label, values[0]));
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                if (values[0] == 1.0)
                    sensorTextView.setText(getResources().getString(R.string.step_detector_sensor_label));
                else
                    sensorTextView.setText(getResources().getString(R.string.not_step_detector_sensor_label));
                break;
        }
    }
}