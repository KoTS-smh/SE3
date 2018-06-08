package com.sec.server.enums;

public enum TaskOrderState {
    //提交状态      等待审批中
    submitted,
    //未提交状态    工人工作中
    unSubmitted,
    //失败状态      低于最低要求水平被踢出任务序列
    fail,
    //完成状态      发布者审批通过
    finish,
    //预约状态      等待预约结果中
    appoint
}
