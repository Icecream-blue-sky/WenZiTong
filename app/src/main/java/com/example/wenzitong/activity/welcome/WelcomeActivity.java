package com.example.wenzitong.activity.welcome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.wenzitong.R;
import com.example.wenzitong.activity.main.LoginActivity;
import com.example.wenzitong.activity.main.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        hideActionBar();
        jumpToGuidance();
    }

    /**
     * 隐藏ActionBar
     */
    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
    }
    /**
     * 跳转到引导界面
     */
    private void jumpToGuidance(){
        final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Guide();
            }
        };
        handler.postDelayed(runnable,2000);
    }
    /**
     * 引导界面启动方法
     */
    private void Guide() {
        boolean isFirstIn;//用户是否第一次进入
        SharedPreferences getInMemory;
        //每次启动从文件中获知用户是否是第一次进入应用
        getInMemory = this.getSharedPreferences("getInMemory", 0);
        isFirstIn = getInMemory.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            //将用户是否进入记录至文件中
            getInMemory = this.getSharedPreferences("getInMemory", 0);
            SharedPreferences.Editor editor = getInMemory.edit();
            editor.putBoolean("isFirstIn", false);
            editor.apply();
            //第一次进入应用启动引导界面
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
        }else{
            //Intent intent=new Intent(WelcomeActivity.this, LoginActivity.class);
            //TODO 注册登录测试未完全完成，先跳过
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
