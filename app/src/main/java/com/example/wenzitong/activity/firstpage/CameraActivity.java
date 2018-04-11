package com.example.wenzitong.activity.firstpage;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.*;
import org.opencv.features2d.*;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenzitong.R;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class CameraActivity extends AppCompatActivity {
    private static final int CROP_IMAGE = -999;
    private static final String TAG = "WordRecognition";
    private CameraManager mCameraManager;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceViewHolder;
    private Handler mHandler;
    private String mCameraId;
    private ImageReader mImageReader;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private int mState;
    private CameraCaptureSession mSession;
//    private ImageView open_album;
    private Button cancel_bt;
    private ImageView take_picture_bt;
    private Handler mainHandler;
    private ImageView img_show;
    private int pictureId;
    private final int CHOOSE_PHOTO = 2;
    private String filePath;
//    private ImageView result_img;
    private Button send_bt;
    private boolean hasStopPreview = true;
    private Uri outputUri = null;
    private File outputFile = null;
    private String afterImagePath = null;
    private boolean isPhoto = false;//判断是否是单纯的照片
    private int keypointsObject1;
    Mat src1, src1_gray;
    //static int ACTION_MODE = 0;
    private boolean src1Selected = false;
    static int REQUEST_READ_EXTERNAL_STORAGE = 11;
    static int REQUEST_WRITE_EXTERNAL_STORAGE = 12;
    static boolean read_external_storage_granted = false;
    static boolean write_external_storage_granted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        hideActionBar();
        checkPermissions();
        initSurfaceView();//初始化SurfaceView
        initViews();
        initListeners();

    }

    /**
     * 隐藏actionBar
     */
    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * 检查权限
     */
    public void checkPermissions() {
        //申请写入权限，暂时这样写，虽然很难看
        if (ActivityCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }
    }

    /**
     * 初始化各个控件
     */
    public void initViews() {
        //展示图片控件
        img_show = findViewById(R.id.img_show);
        //拍照按钮
        take_picture_bt = findViewById(R.id.take_picture);
        //取消按钮St
        cancel_bt=findViewById(R.id.camera_cancel_bt);
        send_bt=findViewById(R.id.camera_send_bt);
//        //打开相册按钮
//        open_album = (ImageView) findViewById(R.id.open_album);
//
//        result_img = (ImageView) findViewById(R.id.recognition_result);
    }

    /**
     * 初始化各个控件的监听
     */
    public void initListeners() {
        /**
         * 拍照按钮监听
         */
        take_picture_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPhoto) {
                    mSurfaceView.setVisibility(View.VISIBLE);
                    img_show.setVisibility(View.GONE);
                    isPhoto = false;
                    return;
                }
                takePicture();
            }
        });
        /**
         * 取消按钮监听
         */
        cancel_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
//        /**
//         * 打开相册监听
//         */
//        open_album.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
//                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    }, 1);//第二个是请求码
//                } else {
//                    openAlbum();
//                }
//            }
//        });
//        /**
//         * 识别结果监听
//         */
//        result_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    /**
     * 打开相册
     */
    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");//选择照片后毁掉onActivityResult方法
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }



    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //LOAD OpenCV engine and init OpenCV library
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_4, getApplicationContext(), mLoaderCallback);
        Log.i(TAG, "onResume success load OpenCV...");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                String beforeImagePath = null;
                if (afterImagePath == null)
                    afterImagePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/WordRecognition_picture" + "_temp_" + ".jpg";
                outputFile = new File(afterImagePath);
                img_show.setImageURI(null);
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
                    if (outputUri == null)
                        outputUri = Uri.fromFile(outputFile);
                    Crop.of(inputUri, outputUri).asSquare().start(this);
                }
                break;
            case Crop.REQUEST_CROP:
                String imgPath = null;
                if (resultCode == RESULT_OK) {
                    if (outputUri != null) {
                        img_show.setImageURI(outputUri);
                        if (mSurfaceView.getVisibility() == View.VISIBLE)
                            mSurfaceView.setVisibility(View.GONE);
                        img_show.setVisibility(View.VISIBLE);
                        isPhoto = true;
                        System.out.println("imgPath" + imgPath);
                    }
                    break;
                }
            default:
        }
    }

    //OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            // TODO Auto-generated method stub
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    System.loadLibrary("nonfree");
                    System.loadLibrary("opencv_java");
                    Log.i(TAG, "成功加载");
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.i(TAG, "加载失败");
                    break;
            }

        }
    };

    private void FeatureSurfBruteforce(Mat src)
    {
        FeatureDetector detector;
        MatOfKeyPoint keypoints1;
        DescriptorExtractor descriptorExtractor;
        Mat descriptors1;
        String res="上传成功";
        keypoints1 = new MatOfKeyPoint();
        descriptors1 = new Mat();
        Log.i("APP", "before detact");
        detector = FeatureDetector.create(FeatureDetector.ORB);
        descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
//      descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);

        detector.detect(src, keypoints1);
        Log.i("APP", keypoints1.toArray().length+" keypoints");
        Log.i("APP", "Detect");

        keypointsObject1 = keypoints1.toArray().length;

        descriptorExtractor.compute(src, keypoints1, descriptors1);

        Log.i("APP"," descriptorExtractor");
    }

    private String handlerImgOnOldVersion(Intent data) {
        Uri uri = data.getData();
        String imgPath = getImagePath(uri, null);
        return imgPath;
    }

    private String handlerImgOnNewVersion(Intent data) {
        String imgPath = null;//选择的图片的路径
        Uri uri = data.getData();//选择图片的结果,即图片地址的封装，接下来对其进行解析
        if (DocumentsContract.isDocumentUri(this, uri)) {//判断是否是document类型,DocumentProvider
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

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void initSurfaceView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.mFirstSurfaceView);
        //通过SurfaceViewHolder可以对SurfaceView进行管理
        mSurfaceViewHolder = mSurfaceView.getHolder();
        mSurfaceViewHolder.addCallback(new SurfaceHolder.Callback() {
            //SurfaceView被成功创建
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCameraAndPreview();
                Log.d(TAG, "SUCCEED");
            }

            //SurfaceView被销毁
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //释放camera
                if (mCameraDevice != null) {
                    mCameraDevice.close();
                    mCameraDevice = null;
                }
            }

            //SurfaceView内容发生改变
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });
    }

    public void initCameraAndPreview() {
        HandlerThread handlerThread = new HandlerThread("My First Camera2");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());//用来处理ui线程的handler，即ui线程
        try {
            mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;
            mImageReader = ImageReader.newInstance(mSurfaceView.getWidth(), mSurfaceView.getHeight(), ImageFormat.JPEG,/*maxImages*/7);
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mainHandler);//这里必须传入mainHandler，因为涉及到了Ui操作
            mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{android.Manifest.permission.CAMERA}, 2);
            } else {
                mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler);
            }
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            //进行相片存储 mCameraDevice.close();
            Uri inputUri;//原图uri
            byte[] imgBytes;
            imgBytes = getImagBytes(reader.acquireNextImage());//获取img的bytes数据格式
            int count = imgBytes.length;
            filePath = savePicture(imgBytes);
            inputUri = Uri.fromFile(new File(filePath));
            if (afterImagePath == null)
                afterImagePath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/WordRecognition_picture" + "_temp_" + ".jpg";
            if (outputUri == null)
                outputUri = Uri.fromFile(new File(afterImagePath));
            Crop.of(inputUri, outputUri).asSquare().start(CameraActivity.this);
            Mat image1 = new Mat(240, 320, CvType.CV_8UC1);
            image1.put(0, 0, getBytesFromBitmap(getBitmapFromUri(outputUri)));
            FeatureSurfBruteforce(image1);
            Log.i("APP", "Before Execute");
        }
    };



    //获取图片的byte数据格式
    public byte[] getImagBytes(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];//buffer.capacity，返回buffer的容量
        buffer.get(bytes);//将image对象转化为byte，再转化为bitmap
        return bytes;
    }

    //通过一个Uri获取一个Bitmap对象
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

    //Bitmap → byte[]
    private byte[] getBytesFromBitmap(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            try {
                takePreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Toast.makeText(CameraActivity.this, "打开摄像头失败", Toast.LENGTH_SHORT).show();
        }
    };

    public void takePreview() throws CameraAccessException {
        mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        mPreviewBuilder.addTarget(mSurfaceViewHolder.getSurface());
        mState = 0;
        mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceViewHolder.getSurface(), mImageReader.getSurface()), mSessionPreviewStateCallback, mHandler);
    }

    private CameraCaptureSession.StateCallback mSessionPreviewStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mSession = session;
            //配置完毕开始预览
            try {
                /**
                 * 设置你需要配置的参数
                 */
                //自动对焦
                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                //打开闪光灯
                mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                //无限次的重复获取图像
                mSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);//作者将这里改为了null存疑，应该存在的mSessionCaptureCallback
                hasStopPreview = false;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Toast.makeText(CameraActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
        }
    };
    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        //可以用来提示图片保存的位置
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            mSession = session;
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            mSession = session;
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            CameraActivity.this.finish();
        }
    };

    //将照片存储在相机照片存储位置,这里采用bitmap方式保存
    public String savePicture(byte[] imgBytes) {
        pictureId++;
        String imgPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/WordRecognition_picture" + pictureId + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);//图像数据被转化为bitmap
        File outputImage = new File(imgPath);
        FileOutputStream outputStream = null;
        try {
            if (outputImage.exists()) {
                outputImage.delete();//存在就删除
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream = new FileOutputStream(outputImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);//第二个参数为压缩质量
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//及时更新到系统相册
        MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory() + "//DCIM//Camera//WordRecogniton_picture" + pictureId + ".jpg"}, null, null);//"//"可以用File.separator代替
        return imgPath;
    }

    public void takePicture() {
        try {
            CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);//用来设置拍照请求的request
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getJpegOrientation(cameraCharacteristics, rotation));//使图片做顺时针旋转
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            if (!hasStopPreview) {
                mSession.stopRepeating();
                hasStopPreview = true;
            } else {
                mSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);
                hasStopPreview = false;
            }
            mSession.capture(mCaptureRequest, null, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "权限未获取！", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && ActivityCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "权限未获取，无法使用本应用！", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
            default:
        }
    }

    //获取图片应该旋转的角度，使图片竖直
    private int getJpegOrientation(CameraCharacteristics c, int deviceOrientation) {
        if (deviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN)
            return 0;
        int sensorOrientation = c.get(CameraCharacteristics.SENSOR_ORIENTATION);

        // Round device orientation to a multiple of 90
        deviceOrientation = (deviceOrientation + 45) / 90 * 90;

        // LENS_FACING相对于设备屏幕的方向,LENS_FACING_FRONT相机设备面向与设备屏幕相同的方向
        boolean facingFront = c.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT;
        if (facingFront) deviceOrientation = -deviceOrientation;

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        int jpegOrientation = (sensorOrientation + deviceOrientation + 360) % 360;

        return jpegOrientation;
    }

    //释放资源待会处理
    @Override
    protected void onDestroy() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (outputFile != null && outputFile.exists()) {
            outputFile.delete();
        }
        super.onDestroy();
    }
}
