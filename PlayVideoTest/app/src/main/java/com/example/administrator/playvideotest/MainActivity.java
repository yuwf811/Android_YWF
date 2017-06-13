package com.example.administrator.playvideotest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;
    private Button play;
    private Button pause;
    private Button replay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        replay = (Button) findViewById(R.id.replay);
        videoView = (VideoView) findViewById(R.id.video_view);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                    ||ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                initVideoPath();
            }
        } else {
            initVideoPath();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initVideoPath();
                }
                else{
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 2);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"授权失败，无法打开视频", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"授权成功，可以打开视频了",Toast.LENGTH_SHORT).show();
                    initVideoPath();
                }
        }
    }

    private void initVideoPath(){
        File file = new File(Environment.getExternalStorageDirectory(),"/DCIM/Camera/金泰妍 - I.mp4");
        videoView.setVideoPath(file.getPath());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if(!videoView.isPlaying()){
                    videoView.start();
                }
                break;
            case R.id.pause:
                if(videoView.isPlaying()){
                   videoView.pause();
                }
                break;
            case R.id.replay:
                if(videoView.isPlaying()){
                    videoView.resume();
                }
                break;
            default:
                break;
        }
    }
}
