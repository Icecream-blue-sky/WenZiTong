package com.example.wenzitong.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by 邹特强 on 2017/11/21.
 * 建立这个类方便在任意类中获取context
 */

public class MyApp extends Application{
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
    }
    public static Context getGlobalContext(){
        return mContext;
    }
}
