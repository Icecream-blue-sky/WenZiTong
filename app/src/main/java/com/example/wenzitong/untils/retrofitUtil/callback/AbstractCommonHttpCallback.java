package com.example.wenzitong.untils.retrofitUtil.callback;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 邹特强 on 2018/3/23.
 *
 * @author 邹特强
 *         解决最常见问题的回调
 *         code:200-300:就是成功，其它都算失败
 */

public abstract class AbstractCommonHttpCallback<T extends Object> extends AbstractBaseHttpCallback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        super.onResponse(call, response);
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            onFail();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        super.onFailure(call, t);
        onFail();
    }

    /**
     * code:200-300
     */
    public abstract void onSuccess(T result);

    /**
     * code不属于 200-300
     */
    public abstract void onFail();
}
