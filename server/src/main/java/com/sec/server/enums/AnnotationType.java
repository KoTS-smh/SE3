package com.sec.server.enums;

public enum AnnotationType {
    option1(0),//标框
    option2(1),//分类
    option3(2),//区域
    option4(3);//整体

    private int code;

    AnnotationType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
