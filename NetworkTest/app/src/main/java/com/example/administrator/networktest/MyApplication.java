package com.example.administrator.networktest;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class MyApplication extends Application {
    private static Context context;

    public void onCreate(){
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
