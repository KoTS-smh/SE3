package com.sec.server.model;

import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.TaskOrderState;

import java.util.Date;

public class TaskOrderModel {
    private long taskId;
    private long acceptUserId;
    private long taskOrderId;
    private TaskOrderState submited;
    private int lastPic;
    private String degreeOfCompletion;
    private int rate;
    private Date beginDate;
    private Date endDate;
    private String taskName;

    public TaskOrderModel(){}

    public TaskOrderModel(TaskOrder taskOrder) {
        this.taskId = taskOrder.getTaskId();
        this.acceptUserId = taskOrder.getAcceptUserId();
        this.taskOrderId = taskOrder.getTaskOrderId();
        this.submited = taskOrder.getSubmited();
        this.lastPic = taskOrder.getLastPic();
        this.rate = taskOrder.getRate();
        this.beginDate = taskOrder.getBeginDate();
        this.endDate = taskOrder.getEndDate();
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

    public long getTaskOrderId() {
        return taskOrderId;
    }

    public void setTaskOrderId(long taskOrderId) {
        this.taskOrderId = taskOrderId;
    }

    public int getLastPic() {
        return lastPic;
    }

    public void setLastPic(int lastPic) {
        this.lastPic = lastPic;
    }

    public String getDegreeOfCompletion() {
        return degreeOfCompletion;
    }

    public void setDegreeOfCompletion(String degreeOfCompletion) {
        this.degreeOfCompletion = degreeOfCompletion;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskOrderState getSubmited() {
        return submited;
    }

    public void setSubmited(TaskOrderState submited) {
        this.submited = submited;
    }
}
