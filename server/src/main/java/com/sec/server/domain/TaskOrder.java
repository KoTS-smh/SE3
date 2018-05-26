package com.sec.server.domain;


import java.util.Date;

public class TaskOrder {
    private long taskOrderId;
    private long taskId;
    private long acceptUserId;
    private boolean submited;
    private int lastPic;
    private int finishedPics;//zuihaogaimingzi
    private int rate;
    private Date beginDate;
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

    public boolean getSubmited() {
        return submited;
    }

    public void setSubmited(boolean submited) {
        this.submited = submited;
    }

}
