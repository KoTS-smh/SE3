package com.sec.server.exception;


import com.sec.server.enums.ResultCode;

/**
 * 结果异常，会被 ExceptionHandler 捕捉并返回给前端
 */
public class ResultException extends RuntimeException {

    private int code;

    public ResultException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public ResultException(String message,int code){
        super(message);
        this.code=code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
