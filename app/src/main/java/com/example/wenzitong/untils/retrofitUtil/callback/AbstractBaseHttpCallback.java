package com.example.wenzitong.untils.retrofitUtil.callback;

import com.example.wenzitong.application.MyApp;
import com.example.wenzitong.untils.ToastUtil;
import com.example.wenzitong.untils.retrofitUtil.AppNetWorkUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 邹特强 on 2018/3/22.
 * @author 邹特强
 * retrofit对应的基础callback，实现对一些基础的网路问题的回调
 * 所有抽象方法部分应该给调用方自己处理
 * TODO:优化：一般在与服务器连接时都需要有个进度条，防止在与服务器交互时，用户重复网络请求
 */

public abstract class AbstractBaseHttpCallback<T extends Object> implements Callback<T> {
    private final int SERVE_ERROR = 500;
    private boolean internetState = true;
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        internetState =true;
        int code = response.raw().code();
        /**
         * 处理常见错误
         */
        if(!response.isSuccessful()){
            if(code >= SERVE_ERROR){
                onServerError();
            }
        }
    }

    /**
     * onFailuer方法：response.code都无法获取的情况
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        /**
         * 优先检查网络
         */
        if(!AppNetWorkUtil.isNetWorkAvailable(MyApp.getGlobalContext())){
            onInternetBroken();
        }
    }
    /**
     * 通用的网络连接中断回调
     * 这时应该提醒用户"网络不可用"
     * code:
     */
    private void onInternetBroken(){
        internetState = false;
        ToastUtil.ToastShortShow("网络不可用!",MyApp.getGlobalContext());
    }
    /**
     * 服务器内部错误回调
     * 这时应提醒用户"服务器繁忙，请稍后再试"
     * code:5开头
     */
    private void onServerError(){
        ToastUtil.ToastShortShow("服务器繁忙,请稍后再试!",MyApp.getGlobalContext());
    }

    /**
     * 提供给子类检测网络状况的接口，防止重复检测
     * @return 网络状态：正常：true,中断： false
     */
    public boolean getInternetState(){
        return internetState;
    }

    /**
     * 未知错误接口，节省子类代码
     */
    public void onUnknownError(){
        ToastUtil.ToastShortShow("未知错误！",MyApp.getGlobalContext());
    }
}
