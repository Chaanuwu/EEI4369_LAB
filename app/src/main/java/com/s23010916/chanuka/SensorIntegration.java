package com.s23010916.chanuka;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class SensorIntegration extends AppCompatActivity implements SensorEventListener {
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor tempSensor;
    private Boolean isTempSensorAvailable;
    ImageView tempAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_integration);

        textView = findViewById(R.id.textView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isTempSensorAvailable = true;
        } else {
            isTempSensorAvailable = false;
            tempAlert = findViewById(R.id.tempAlert);
            tempAlert.setVisibility(View.GONE);
            textView.setText("Temperature sensor not available.");
        }
    }

    private boolean isPlaying = false;
    MediaPlayer mediaPlayer;

    @Override
    public void onSensorChanged(SensorEvent event) {
        textView.setText("Temperature: " + event.values[0] + "C");

        if (event.values[0] > 16) {
            if (!isPlaying) {
                isPlaying = true;
                mediaPlayer = MediaPlayer.create(this, R.raw.alert_sound);
                mediaPlayer.start();

                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    isPlaying = false;
                });
            }
        } else {
            if (isPlaying && mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                mediaPlayer.release();
                isPlaying = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        if (isTempSensorAvailable) {
            sensorManager.registerListener((SensorEventListener) this,tempSensor, sensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTempSensorAvailable) {
            sensorManager.unregisterListener((SensorEventListener) this);
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}