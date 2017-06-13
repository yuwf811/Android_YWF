package com.example.administrator.androidthreadtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int UPDATE_TEXT = 1;
    private Button changeText;
    private TextView text;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    text.setText("Nice to meet you");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        changeText = (Button) findViewById(R.id.change_text);
        progressDialog = new ProgressDialog(MainActivity.this);
        changeText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_text:
                new DownloadTask().execute();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    class DownloadTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                for(int i=0;i<=100;i+=10){
                    Thread.sleep(2000);
                    publishProgress(i);
                }
            }catch(Exception e){
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("Download" + values[0] + "%");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();

            if(aBoolean){
                Toast.makeText(MainActivity.this,"Download succeeded",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,"Download failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
}


