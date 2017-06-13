package com.example.administrator.servicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Administrator on 2016/11/9 0009.
 */

public class MyService extends Service {

    private DownloadBinder mBinder = new DownloadBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Notification comes");
        builder.setContentText("123");
        builder.setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
//        Intent notificationIntent = new Intent(this,MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class DownloadBinder extends Binder {
        private int progress;

        public void startDownload() {
            for (progress = 0; progress <= 100; progress += 10) {

            }
        }

        public int getProgress() {
            return progress;
        }
    }
}
