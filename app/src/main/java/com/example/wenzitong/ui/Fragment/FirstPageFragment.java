package com.example.wenzitong.ui.Fragment;

import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wenzitong.R;
import com.example.wenzitong.activity.firstpage.CameraActivity;
import com.example.wenzitong.activity.firstpage.ImgShowActivity;
import com.example.wenzitong.ui.GlideImgaeLoader;
import com.example.wenzitong.untils.FontCache;
import com.soundcloud.android.crop.Crop;
import com.youth.banner.Banner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 邹特强 on 2017/11/22.
 * 首页对应fragment
 */

public class FirstPageFragment extends Fragment implements View.OnClickListener {
    private View view;//fragment总布局
    private Button photoBt;//拍照按钮
    private Button uploadBt;//上传按钮
    private final int CHOOSE_PHOTO = 2;
    private Uri outputUri = null;
    private File outputFile = null;
    private String afterImagePath = null;
    private Boolean isPhotoBtClicked = false;
    private Boolean isUploadBtClicked = false;
    /**
     * word:"文字通让生活更便捷！"
     */
    private TextView tv1Banner;
    private TextView tv2Banner;
    private TextView tv3Banner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_firstpage, container, false);
        initView();
        initListeners();
        return view;
    }

    /**
     * 初始化布局
     */
    private void initView() {
        photoBt = view.findViewById(R.id.photo_bt);
        uploadBt = view.findViewById(R.id.upload_bt);
//        tv1Banner = view.findViewById(R.id.banner_1_tv);
//        tv2Banner = view.findViewById(R.id.banner_2_tv);
//        tv3Banner = view.findViewById(R.id.banner_3_tv);
//        try {
//            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/writing_brush.ttf");
//            tv1Banner.setTypeface(FontCache.getTypeface("fonts/writing_brush.ttf",getContext()),Typeface.NORMAL);
//            tv2Banner.setTypeface(typeface);
//            tv3Banner.setTypeface(typeface);
//        }catch (NullPointerException ex){
//            ex.printStackTrace();
//        }

    }

//    /**
//     * 初始化广播轮播页
//     */
//    private void initBanner() {
//        Banner banner = (Banner) view.findViewById(R.id.banner);
//        //设置图片加载器
//        banner.setImageLoader(new GlideImgaeLoader());
//        Integer[] images = {R.drawable.baner_page1,
//                R.drawable.banner_page2,
//                R.drawable.banner_page3,
//                R.drawable.banner_page4,
//                R.drawable.banner_page5};
//        //设置图片集合
//        banner.setImages(Arrays.asList(images));
//        //banner设置方法全部调用完毕时最后调用
//        banner.start();
//    }

    /**
     * 初始化监听事件
     */
    private void initListeners() {
        photoBt.setOnClickListener(this);
        uploadBt.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 拍照
             */
            case R.id.photo_bt:
                if (!isPhotoBtClicked) {
                    photoBt.setTextColor(getResources().getColor(R.color.white));
                    photoBt.setBackground(getResources().getDrawable(R.drawable.round_rect_blue_content));
                    isPhotoBtClicked = true;
                } else {
                    photoBt.setTextColor(getResources().getColor(R.color.theme_color));
                    photoBt.setBackground(getResources().getDrawable(R.drawable.round_rect_white_content));
                    isPhotoBtClicked = false;
                }
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);
//                try{
//                    getActivity().onBackPressed();
//                }catch (NullPointerException ex){
//                    ex.printStackTrace();
//                }
                break;
            /**
             * 上传
             */
            case R.id.upload_bt:
                if (!isUploadBtClicked) {
                    uploadBt.setTextColor(getResources().getColor(R.color.white));
                    uploadBt.setBackground(getResources().getDrawable(R.drawable.round_rect_blue_content));
                    isUploadBtClicked = true;
                } else {
                    uploadBt.setTextColor(getResources().getColor(R.color.theme_color));
                    uploadBt.setBackground(getResources().getDrawable(R.drawable.round_rect_white_content));
                    isUploadBtClicked = false;
                }
                //检查权限，权限合格就打开相册
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 1);//第二个是请求码
                } else {
                    openAlbum();
                }
//                try{
//                    getActivity().onBackPressed();
//                }catch (NullPointerException ex){
//                    ex.printStackTrace();
//                }
                break;
        }
    }

    /**
     * 打开相册//TODO 有空进行封装,把requestCode写成固定的
     */
    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");//选择照片后毁掉onActivityResult方法
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    /**
     * 启动活动的回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //打开相册回调
            case CHOOSE_PHOTO:
                String beforeImagePath = null;
                afterImagePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/WordRecognition_picture" + "_temp_" + ".jpg";
                outputFile = new File(afterImagePath);
                if (outputFile.exists()) {
                    outputFile.delete();
                    try {
                        outputFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT > 19) {
                        beforeImagePath = handlerImgOnNewVersion(data);
                    } else {
                        beforeImagePath = handlerImgOnOldVersion(data);
                    }
                    Uri inputUri = Uri.fromFile(new File(beforeImagePath));
                    outputUri = Uri.fromFile(outputFile);
                    //Crop专门为Fragment设置的方法
                    Crop.of(inputUri, outputUri).asSquare().start(getActivity(), this);//裁剪api,将裁剪后的图片存入outputUri所代表的临时文件
                }
                break;
            //裁剪图片回调
            case Crop.REQUEST_CROP:
                String imgPath = null;
                if (resultCode == RESULT_OK) {
                    if (outputUri != null) {
                        System.out.println("imgPath" + imgPath);
                        Intent intent = new Intent(getActivity(), ImgShowActivity.class);
                        intent.putExtra("cropImgUri", outputUri);//TODO uri竟然可以直接传递！！！它是Parcelable的！！
                        startActivity(intent);
                        outputUri = null;
                    }
                }
                break;
        }
    }

    /**
     * 以老版本方法处理图片
     *
     * @param data
     * @return
     */
    private String handlerImgOnOldVersion(Intent data) {
        Uri uri = data.getData();
        String imgPath = getImagePath(uri, null);
        return imgPath;
    }

    /**
     * 以新版本方法处理图片
     *
     * @param data 相册信息
     * @return
     */
    private String handlerImgOnNewVersion(Intent data) {
        String imgPath = null;//选择的图片的路径
        Uri uri = data.getData();//选择图片的结果,即图片地址的封装，接下来对其进行解析
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {//判断是否是document类型,DocumentProvider
            String docId = DocumentsContract.getDocumentId(uri);
            switch (uri.getAuthority())//就是获取uri的最开头部分
            {
                case "com.android.providers.media.documents":
                    String id = docId.split(":")[1];//解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imgPath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                    break;
                case "com.android.providers.downloads.documents":
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imgPath = getImagePath(contentUri, null);
                    break;
                default:
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {//ContentProvider
            imgPath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {//FileProvider,相当于4.4之前的版本
            imgPath = uri.getPath();
        }
        return imgPath;
    }

    /**
     * 获取图片绝对地址
     *
     * @param uri       图片uri
     * @param selection 筛选条件
     * @return 图片绝对地址
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //TODO 不知道会不会出错，运用getActivity()
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onResume() {
        super.onResume();
        photoBt.setTextColor(getResources().getColor(R.color.theme_color));
        photoBt.setBackground(getResources().getDrawable(R.drawable.round_rect_white_content));
        uploadBt.setTextColor(getResources().getColor(R.color.theme_color));
        uploadBt.setBackground(getResources().getDrawable(R.drawable.round_rect_white_content));
    }
}
