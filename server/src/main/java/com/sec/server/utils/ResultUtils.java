package com.sec.server.utils;


import com.sec.server.enums.ResultCode;

public class ResultUtils {
    public static Result success(Object data){
        return new Result<>(ResultCode.SUCCESS,data);
    }

    public static Result success(){
        return new Result(ResultCode.SUCCESS);
    }

    public static Result error(int code, String msg) {
        Result result = new Result();
        result.setMsg(msg);
        result.setCode(code);
        return result;
    }

    public static Result error(ResultCode resultCode) {
        return new Result(resultCode);
    }
}
