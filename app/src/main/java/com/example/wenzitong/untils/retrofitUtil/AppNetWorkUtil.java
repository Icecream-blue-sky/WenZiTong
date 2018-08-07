package com.example.wenzitong.untils.retrofitUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 邹特强 on 2018/3/17.
 * 检查网络状况的工具类
 */

public class AppNetWorkUtil {
    /**
     * 检查网络是否连接
     *
     * @return 网络连接情况
     */
    public static boolean isNetWorkAvailable(Context context) {
        if (context != null) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();
            } catch (NullPointerException nex) {
                nex.printStackTrace();
            }

        }
        return false;
    }
}