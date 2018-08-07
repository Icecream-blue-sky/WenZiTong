package com.example.wenzitong.untils.retrofitUtil.callback;

import com.example.wenzitong.application.MyApp;
import com.example.wenzitong.untils.ToastUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 邹特强 on 2018/3/22.
 *
 * @author 邹特强
 *         注册活动专用的网路回调接口
 */

public abstract class AbstractRegisterHttpCallback<T extends Object> extends AbstractBaseHttpCallback<T> {
    /**
     * 不常见的错误都归为未知错误
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        super.onResponse(call, response);
        int code = response.raw().code();
        if (response.isSuccessful()) {
            if (response.body() != null) {
                onSuccess(response.body());
            }
        } else if (code == 400) {
            onVerCodeError();
        } else {
            onUnknownError();
        }
        onFinal();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        super.onFailure(call, t);
        if (super.getInternetState()) {
            onUnknownError();
        }
        onFinal();
    }

    /**
     * 请求成功的回调
     *
     * @param result:传递Retrofit解析后的对象
     */
    public abstract void onSuccess(T result);

    /**
     * 验证码错误处理
     */
    private void onVerCodeError() {
        ToastUtil.ToastShortShow("验证码错误!", MyApp.getGlobalContext());
    }

    @Override
    /**
     * 未知错误的回调
     */
    public void onUnknownError() {
        ToastUtil.ToastShortShow("注册失败,未知错误!", MyApp.getGlobalContext());
    }

    /**
     * 不管请求成功或失败都调用的方法
     */
    public abstract void onFinal();

}
