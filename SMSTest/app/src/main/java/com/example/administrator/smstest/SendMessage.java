package com.example.administrator.smstest;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/11/1 0001.
 */

public class SendMessage extends AppCompatActivity {
    private IntentFilter sendFilter;
    private SendStatusReceiver sendStatusReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_send);
        Button sendMsg = (Button) findViewById(R.id.sendMsg);

        sendFilter = new IntentFilter();
        sendFilter.addAction("SENT_SMS_ACTION");
        sendStatusReceiver = new SendStatusReceiver();
        registerReceiver(sendStatusReceiver,sendFilter);

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText msgContent = (EditText) findViewById(R.id.message);
                SmsManager smsManager = SmsManager.getDefault();

                Intent sentIntent = new Intent("SENT_SMS_ACTION");
                PendingIntent pi = PendingIntent.getBroadcast(SendMessage.this,0,sentIntent,0);

                String[] mPermission = {"android.permission.RECEIVE_SMS","android.permission.SEND_SMS"};
                if (ActivityCompat.checkSelfPermission(SendMessage.this, mPermission[0]) != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(SendMessage.this, mPermission[1]) != MockPackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SendMessage.this, mPermission, 1);
                }

                while (ActivityCompat.checkSelfPermission(SendMessage.this, mPermission[0]) != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(SendMessage.this, mPermission[1]) != MockPackageManager.PERMISSION_GRANTED) {
                    Thread.currentThread();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                smsManager.sendTextMessage("123",null,msgContent.getText().toString(),pi,null);
            }
        });
    }

    class SendStatusReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(getResultCode()==RESULT_OK){
                Toast.makeText(context,"Send succeeded",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"Send failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
