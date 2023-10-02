package com.example.foodtoqu;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimerService extends Service {
    private Handler handler;
    private Runnable updateTimeRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateTimerText();
                handler.postDelayed(this, 1000); // Update every second (1000 milliseconds)
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(updateTimeRunnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateTimerText() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = timeFormat.format(new Date());
        // Broadcast the updated time to the activity
        Intent intent = new Intent("TIMER_UPDATE");
        intent.putExtra("current_time", currentTime);
        sendBroadcast(intent);
    }
}

