package com.sec.server.enums;

public enum AnnotationType {
    option1(1),//标框
    option2(2),//分类
    option3(3),//区域
    option4(4);//整体

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
