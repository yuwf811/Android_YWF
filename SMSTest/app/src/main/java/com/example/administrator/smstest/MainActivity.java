package com.example.administrator.smstest;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView sender;
    private TextView content;
    private IntentFilter receiveFilter;
    private MessageReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String[] mPermission = {"android.permission.RECEIVE_SMS","android.permission.RECEIVE_SMS"};
//        if (ActivityCompat.checkSelfPermission(MainActivity.this, mPermission[0]) != MockPackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(MainActivity.this, mPermission[1]) != MockPackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, mPermission, 1);
//        }
//
//        while (ActivityCompat.checkSelfPermission(MainActivity.this, mPermission[0]) != MockPackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(MainActivity.this, mPermission[1]) != MockPackageManager.PERMISSION_GRANTED) {
//            Thread.currentThread();
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
            }
        }
        sender = (TextView) findViewById(R.id.sender);
        content=(TextView) findViewById(R.id.content);
        receiveFilter = new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver,receiveFilter);

        Button btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SendMessage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }

    class MessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[])bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for(int i=0;i<messages.length;i++){
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
            }
            String address = messages[0].getOriginatingAddress();
            String fullMessage = "";
            for(SmsMessage message:messages){
                fullMessage+=message.getMessageBody();
            }
            sender.setText(address);
            content.setText(fullMessage);
        }
    }
}
