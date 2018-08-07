package com.example.wenzitong.entity;

import java.io.Serializable;

/**
 * Created by 邹特强 on 2018/3/6.
 * 总的回复泛型javabean类
 */

public class BaseResponse<T> implements Serializable {
    /**
     * 服务器返回的状态码，0为成功，1为失败
     */
    private int code;
    /**
     * 服务器反馈的状态信息
     */
    private String msg;
    /**
     * 服务器返回的具体信息
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
