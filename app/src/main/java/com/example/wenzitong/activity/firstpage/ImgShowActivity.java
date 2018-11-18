package com.example.wenzitong.activity.firstpage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private Button sendBt;//发送按钮
    PredictTF preTF;
    private static final String MODEL_FILE = "file:///android_asset/tensor_model.pb"; //模型存放路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_show);
        preTF = new PredictTF(getAssets(), MODEL_FILE);//输入模型存放路径，并加载TensoFlow模型
        hideActionBar();
        getImgUri();
        initViews();
        initListeners();
        loadImg();
    }

    /**
     * 隐藏标题栏
     */
    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * 从上个活动获取图片的uri
     */
    public void getImgUri() {
        Intent intent = getIntent();
        imgUri = intent.getParcelableExtra("cropImgUri");
    }

    /**
     * 初始化布局控件
     */
    public void initViews() {
        imgshowImg = findViewById(R.id.img_show);
        cancelBt = findViewById(R.id.cancel_bt);
        sendBt = findViewById(R.id.send_bt);
    }

    /**
     * 初始化监听事件
     */
    public void initListeners() {
        cancelBt.setOnClickListener(this);
        sendBt.setOnClickListener(this);
    }

    /**
     * 加载图片
     */
    public void loadImg() {
        imgshowImg.setImageURI(imgUri);
    }

    /**
     * 识别事件函数
     */
    public void click(Bitmap bitmap) {
        String res = "预测结果为：";
        float[] result = preTF.getPredict(bitmap);
        for (int i = 0; i < result.length; i++) {
            System.out.println(res + result[i]);
            res = res + String.valueOf(result[i]) + " ";
        }
    }

    /**
     * 通过一个Uri获取一个Bitmap对象
     */
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_bt:
                finish();
                break;
            case R.id.send_bt:
                Bitmap bitmap = getBitmapFromUri(imgUri);
//                click(bitmap);
                break;
            default:
        }
    }
}
