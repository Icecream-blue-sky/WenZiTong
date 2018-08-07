package com.example.wenzitong.activity.welcome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Window;

import com.example.wenzitong.R;
import com.example.wenzitong.activity.main.MainActivity;
import com.example.wenzitong.ui.SampleSlide;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * 引导界面
 */
public class GuideActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        addSlide(SampleSlide.newInstance(R.layout.guide_page1));
        addSlide(SampleSlide.newInstance(R.layout.guide_page2));
        addSlide(SampleSlide.newInstance(R.layout.guide_page3));
        setBarColor(Color.parseColor("#00000000"));
        setSeparatorColor(Color.parseColor("#00000000"));
        showSkipButton(false);
        setDoneText("跳转");
        setColorDoneText(Color.parseColor("#47BAFE"));
    }
    //完成按钮监听
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent=new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

