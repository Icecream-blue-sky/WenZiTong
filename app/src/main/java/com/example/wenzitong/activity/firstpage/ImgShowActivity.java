package com.example.wenzitong.activity.firstpage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.wenzitong.R;

/**
 * 裁剪完展示图片的活动
 */
public class ImgShowActivity extends AppCompatActivity implements View.OnClickListener {
    private Uri imgUri;//裁剪图片后的uri
    private ImageView imgshowImg;//展示图片的imageView
    private Button cancelBt;//取消按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_show);
        hideActionBar();
        getImgUri();
        initViews();
        initListeners();
        loadImg();
    }

    /**
     * 隐藏标题栏
     */
    public void hideActionBar(){
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
    }
    /**
     * 从上个活动获取图片的uri
     */
    public void getImgUri(){
        Intent intent=getIntent();
        imgUri=intent.getParcelableExtra("cropImgUri");
    }
    /**
     * 初始化布局控件
     */
    public void initViews(){
        imgshowImg=findViewById(R.id.img_show);
        cancelBt=findViewById(R.id.cancel_bt);
    }
    /**
     * 初始化监听事件
     */
    public void initListeners(){
        cancelBt.setOnClickListener(this);
    }
    /**
     * 加载图片
     */
    public void loadImg(){
        imgshowImg.setImageURI(imgUri);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.cancel_bt:
                finish();
                break;
        }
    }
}
