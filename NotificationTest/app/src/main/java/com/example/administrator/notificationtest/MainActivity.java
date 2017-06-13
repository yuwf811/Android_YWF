package com.example.administrator.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sendNotice;

    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendNotice = (Button) findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(pendingIntent==null){
            pendingIntent=PendingIntent.getActivity(this,0, new Intent(this,NotificationActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        }

        switch (v.getId()){
            case R.id.send_notice:
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Notification notification = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    notification = new Notification.Builder(this)
                            .setContentTitle("This is content title")
                            .setContentText("This is content text")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setVibrate(new long[]{0,1000,1000,1000}).build();
                    notification.ledARGB = Color.argb(127,255,0,255);
                    notification.ledOnMS = 1000;
                    notification.ledOffMS = 1000;
                    notification.flags = Notification.FLAG_SHOW_LIGHTS;
//                    notification.defaults = Notification.DEFAULT_ALL;
                }

                Random random=new Random();
                int r =  random.nextInt(100);
                manager.notify(1,notification);
                break;
            default:
                break;
        }
    }
}
