package com.sec.server.enums;

public enum ResultCode {

    //userModule error
    UNREGISTERED_ERROR(10000,"用户不存在"),

    SUCCESS(0, "请求成功"),
    UNKNOWN_ERROR(-1,"未知错误"),
    WEAK_NET_WORK(1, "网络异常，请稍后重试"),
    PASSWORD_ERROR(10001, "密码错误"),
    UNKNOWN_USER_ERROR(10010,"用户名不存在"),
    PARAMETER_ERROR(10101, "参数错误"),
    TASK_NOT_FOUND(10110, "任务不存在"),
    NO_IMAGE_ERROR(10111, "任务中无图片"),
    TASK_ALREADY_ACCEPT(11000, "任务已经被接取");

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    public int getCode(){
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
