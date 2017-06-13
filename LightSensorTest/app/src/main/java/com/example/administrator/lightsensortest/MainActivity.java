package com.example.administrator.lightsensortest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetic;

    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];

//    private ImageView arrow;
    private float lastAngle;
    private TextView lightLevel;

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                accelerometerValues=event.values;
            }else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                magneticFieldValues = event.values;
            }

            calculateOrientation();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lightLevel = (TextView) findViewById(R.id.light_level);
        lastAngle =0;
//        arrow = (ImageView) findViewById(R.id.image_view_arrow);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(listener,accelerometer,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener,magnetic,SensorManager.SENSOR_DELAY_GAME);

    }

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);
        float rotateDegree = - values[0];
        if(Math.abs(rotateDegree-lastAngle)>1){
            RotateAnimation animation = new RotateAnimation(lastAngle,rotateDegree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation.setFillAfter(true);
//            arrow.startAnimation(animation);
            lastAngle = rotateDegree;
            lightLevel.setText(String.valueOf(rotateDegree));
        }


//        Log.i("---MainActivity---", values[0] + "");
//        if (values[0] >= -5 && values[0] < 5) {
//            lightLevel.setText("正北");
//        } else if (values[0] >= 5 && values[0] < 85) {
//            // Log.i(TAG, "东北");
//            lightLevel.setText("东北");
//        } else if (values[0] >= 85 && values[0] <= 95) {
//            // Log.i(TAG, "正东");
//            lightLevel.setText("正东");
//        } else if (values[0] >= 95 && values[0] < 175) {
//            // Log.i(TAG, "东南");
//            lightLevel.setText("东南");
//        } else if ((values[0] >= 175 && values[0] <= 180)
//                || (values[0]) >= -180 && values[0] < -175) {
//            // Log.i(TAG, "正南");
//            lightLevel.setText("正南");
//        } else if (values[0] >= -175 && values[0] < -95) {
//            // Log.i(TAG, "西南");
//            lightLevel.setText("西南");
//        } else if (values[0] >= -95 && values[0] < -85) {
//            // Log.i(TAG, "正西");
//            lightLevel.setText("正西");
//        } else if (values[0] >= -85 && values[0] < -5) {
//            // Log.i(TAG, "西北");
//            lightLevel.setText("西北");
//        }
    }

}
