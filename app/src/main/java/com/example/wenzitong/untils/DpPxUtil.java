package com.example.wenzitong.untils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
 /**
 * dip和px转化工具类
 */
public class DpPxUtil {
    /**
     * dipתΪ px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     *  px תΪ dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static <T> boolean compareTwoList(List<T> newData, List<T> mData) {
        List<T> diffAdd = new ArrayList<>();
        List<T> diffDeduct = new ArrayList<>();
        if (newData != null && mData != null) {
            for (T t : newData){
                if (!mData.contains(t)){
                    diffAdd.add(t);
                }
            }
            for (T t2 : mData){
                if (!newData.contains(t2)){
                    diffDeduct.add(t2);
                }
            }
            mData.removeAll(diffDeduct);
            mData.addAll(diffAdd);
        }else {
            Log.d("HIS_PRESENTATION", "ERROR INTERNET");
            return false;
        }
        diffAdd.clear();
        diffDeduct.clear();
        return true;
    }
}
