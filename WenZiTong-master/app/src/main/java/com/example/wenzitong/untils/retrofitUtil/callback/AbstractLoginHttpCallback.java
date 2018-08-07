package com.example.wenzitong.untils.retrofitUtil.callback;

import com.example.wenzitong.application.MyApp;
import com.example.wenzitong.untils.ToastUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 邹特强 on 2018/3/22.
 *
 * @author 邹特强
 *         登录活动专用的网络回调类
 */

public abstract class AbstractLoginHttpCallback<T extends Object> extends AbstractBaseHttpCallback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        /**
         * 先调用父类的方法实现基本功能回调
         * 然后针对特殊的错误，独立处理
         */
        super.onResponse(call, response);
        int code = response.raw().code();
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else if (code == 400) {
            onAccountOrPasswordError();
        } else if (code == 404) {
            onNotRegister();
        } else{
            onUnknownError();
        }
        onFinal();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        super.onFailure(call, t);
        onFinal();
    }

    /**
     * 网络请求成功的统一回调
     *
     * @param result:传递Retrofit自动解析后的对象
     */
    public abstract void onSuccess(T result);

    /**
     * 用户输入的账号密码错误
     */
    public abstract void onAccountOrPasswordError();

    /**
     * 用户尚未注册
     */
    public abstract void onNotRegister();

    /**
     * 未知错误接口
     */
    @Override
    public void onUnknownError() {
        ToastUtil.ToastShortShow("登录失败,未知错误!", MyApp.getGlobalContext());
    }

    /**
     * 不管成功或失败都调用的方法
     * 可以节省代码
     */
    public abstract void onFinal();
}
