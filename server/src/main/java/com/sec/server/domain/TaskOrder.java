package com.sec.server.domain;


import com.sec.server.enums.TaskOrderState;

import java.util.Date;


public class TaskOrder {

    //任务订单Id
    private long taskOrderId;
    //所属任务的Id
    private long taskId;
    //接取任务工人Id
    private long acceptUserId;
    //任务当前状态
    // 包括:     0、提交状态
    //           1、未提交状态
    //           2、失败状态
    //           3、完成状态
    //           4、预约状态
    private TaskOrderState submited;
    //
    private int lastPic;
    //完成的标注张数
    private int finishedPics;//zuihaogaimingzi
    //
    private int rate;
    //开始时间
    private Date beginDate;
    //结束时间
    private Date endDate;

    public TaskOrder(){}

    public TaskOrder(long taskId, long acceptUserId) {
        this.taskId = taskId;
        this.acceptUserId = acceptUserId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(long acceptUserId) {
        this.acceptUserId = acceptUserId;
    }


    public int getLastPic() {
        return lastPic;
    }

    public void setLastPic(int lastPic) {
        this.lastPic = lastPic;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public long getTaskOrderId() {
        return taskOrderId;
    }

    public void setTaskOrderId(long taskOrderId) {
        this.taskOrderId = taskOrderId;
    }

    public int getFinishedPics() {
        return finishedPics;
    }

    public void setFinishedPics(int finishedPics) {
        this.finishedPics = finishedPics;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public TaskOrderState getSubmited() {
        return submited;
    }

    public void setSubmited(TaskOrderState submited) {
        this.submited = submited;
    }
}
