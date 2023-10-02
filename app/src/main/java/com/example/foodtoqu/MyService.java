package com.example.foodtoqu;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {
    public MyService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                removeOldDiaryEntriesForCurrentUser();
                // Schedule the task to run again after one minute
                handler.postDelayed(this, 60 * 1000); // 60,000 milliseconds = 1 minute
            }
        }, 60 * 1000); // Initial delay of 1 minute

        return START_STICKY;
    }


    private void removeOldDiaryEntriesForCurrentUser() {
        // Get the current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userUid = currentUser.getUid();
            DatabaseReference diaryRef = FirebaseDatabase.getInstance().getReference("diary").child(userUid);

            // Get the current time in milliseconds
            long currentTimeMillis = System.currentTimeMillis();

            // Calculate one minute ago
            long oneMinuteAgoMillis = currentTimeMillis - (60 * 1000);

            // Construct a date string for one minute ago
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            String oneMinuteAgoDateString = dateFormat.format(new Date(oneMinuteAgoMillis));

            // Query and remove entries older than one minute
            diaryRef.orderByChild("timestamp").endAt(oneMinuteAgoDateString)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                childSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors here
                        }
                    });
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }
}