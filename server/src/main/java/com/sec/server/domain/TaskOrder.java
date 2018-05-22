package com.sec.server.domain;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.utils.Path;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class TaskOrder {
    private long taskOrderId;
    private long taskId;
    private long acceptUserId;
    private boolean submited;
    private int lastPic;
    private int degreeOfCompletion;//zuihaogaimingzi
    private int rate;
    private long annotationId;
    private Date beginDate;
    private Date endDate;

    public TaskOrder(){}


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

    public long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(long annotationId) {
        this.annotationId = annotationId;
    }

    public long getTaskOrderId() {
        return taskOrderId;
    }

    public void setTaskOrderId(long taskOrderId) {
        this.taskOrderId = taskOrderId;
    }

    public int getDegreeOfCompletion() {
        return degreeOfCompletion;
    }

    public void setDegreeOfCompletion(int degreeOfCompletion) {
        this.degreeOfCompletion = degreeOfCompletion;
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
