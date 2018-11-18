package com.example.wenzitong.activity.firstpage;

/**
 * Created by TANG on 2018/8/6.
 */

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


public class PredictTF {
    private static final String TAG = "PredictionTF";
    //设置模型输入/输出节点的数据维度
    private static final int IN_COL = 128;
    private static final int IN_ROW = 128;
    private static final int OUT_COL = 100;
    private static final int OUT_ROW = 100;
    //模型中输入变量的名称
    private static final String inputName = "input_1_9";
    //模型中输出变量的名称
    private static final String outputName = "activation_1_9/Softmax";
    private static final int IMAGE_SIZE = 128;

    TensorFlowInferenceInterface inferenceInterface;

    static {
        //加载libtensorflow_inference.so库文件
        System.loadLibrary("tensorflow_inference");
        Log.e(TAG, "libtensorflow_inference.so库加载成功");
    }

    PredictTF(AssetManager assetManager, String modePath) {
        //初始化TensorFlowInferenceInterface对象
        inferenceInterface = new TensorFlowInferenceInterface(assetManager, modePath);
        Log.e(TAG, "TensoFlow模型文件加载成功");
    }

    /**
     * 利用训练好的TensoFlow模型预测结果
     *
     * @param bitmap 输入被测试的bitmap图
     * @return 返回预测结果，int数组
     */
    public float[] getPredict(Bitmap bitmap) {
        float[] inputdata = bitmapToFloatArray(bitmap, 128, 128);//需要将图片缩放带128*128
        //将数据feed给tensorflow的输入节点
        inferenceInterface.feed(inputName, inputdata, 1,IN_COL, IN_ROW,1);
        //运行tensorflow
        String[] outputNames = new String[]{outputName};
        inferenceInterface.run(outputNames);
        ///获取输出节点的输出信息
        float[] outputs = new float[OUT_COL * OUT_ROW]; //用于存储模型的输出数据
        inferenceInterface.fetch(outputName, outputs);
        return outputs;
    }

    /**
     * 将bitmap转为（按行优先）一个float数组，并且每个像素点都归一化到0~1之间。
     *
     * @param bitmap 输入被测试的bitmap图片
     * @param rx     将图片缩放到指定的大小（列）->128
     * @param ry     将图片缩放到指定的大小（行）->128
     * @return 返回归一化后的一维float数组 ->128*128
     */
    public static float[] bitmapToFloatArray(Bitmap bitmap, int rx, int ry) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        // 计算缩放比例
        float scaleWidth = ((float) rx) / width;
        float scaleHeight = ((float) ry) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        Log.i(TAG, "bitmap width:" + bitmap.getWidth() + ",height:" + bitmap.getHeight());
        Log.i(TAG, "bitmap.getConfig():" + bitmap.getConfig());
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        float[] result = new float[height * width];
        int k = 0;
        //行优先
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int argb = bitmap.getPixel(i, j);
                int r = Color.red(argb);
                int g = Color.green(argb);
                int b = Color.blue(argb);
                int a = Color.alpha(argb);
                //由于是灰度图，所以r,g,b分量是相等的。
                assert (r == g && g == b);
//                Log.i(TAG,i+","+j+" : argb = "+argb+", a="+a+", r="+r+", g="+g+", b="+b);
                result[k++] = r / 255.0f;
            }
        }
        return result;
    }

    //convert bitmap to array
    private float[] getPixels(Bitmap bitmap) {


        int[] intValues = new int[IMAGE_SIZE * IMAGE_SIZE];
        float[] floatValues = new float[IMAGE_SIZE * IMAGE_SIZE * 3];

        if (bitmap.getWidth() != IMAGE_SIZE || bitmap.getHeight() != IMAGE_SIZE) {
            // rescale the bitmap if needed
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, IMAGE_SIZE, IMAGE_SIZE);
        }

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i * 3] = Color.red(val) / 255.0f;
            floatValues[i * 3 + 1] = Color.green(val) / 255.0f;
            floatValues[i * 3 + 2] = Color.blue(val) / 255.0f;
        }
        return floatValues;
    }

    //缩放图片,使用openCV，缩放方法采用area interpolation法
    private Bitmap scaleImage(Bitmap bitmap, int width, int height)
    {

        Mat src = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(bitmap, src);
        //new Size(width, height)
        Imgproc.resize(src, dst, new Size(width,height),0,0,Imgproc.INTER_AREA);
        Bitmap bitmap1 = Bitmap.createBitmap(dst.cols(),dst.rows(),Bitmap.Config.RGB_565);
        Utils.matToBitmap(dst, bitmap1);
        return bitmap1;
    }

}