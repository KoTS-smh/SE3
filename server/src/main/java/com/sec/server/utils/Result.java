package com.sec.server.utils;

import com.sec.server.enums.ResultCode;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public Result() {
    }

    public Result(ResultCode resultCode, T data) {
        this.code=resultCode.getCode();
        this.msg=resultCode.getMsg();
        this.data = data;
    }

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
