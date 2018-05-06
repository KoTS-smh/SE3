package com.sec.server.domain;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.utils.Path;
import com.sec.server.utils.ReadFile;
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
    private int degreeOfCompletion;
    private int rate;
    private long annotationId;
    private Date beginDate;
    private Date endDate;

    public TaskOrder(){}

    public TaskOrder(long taskId, long acceptUserId){
        this.taskId = taskId;
        this.acceptUserId = acceptUserId;
        String path = Path.taskOrderPath + acceptUserId + ".json";
        File file = new File(path);
        if(file.exists()){
            try {
                String content = FileUtils.readFileToString(file,"utf-8");
                com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(content);
                JSONObject object =jsonArray.getJSONObject(jsonArray.size()-1);
                long length = object.getLong("taskOrderId");
                this.taskOrderId = length + 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            this.taskOrderId = 0;
        }

        this.submited = false;
        this.lastPic = 0;
        this.degreeOfCompletion = 0;
        this.rate = -1;
        this.annotationId = 0;

        Task task = ReadFile.getTask(taskId);
        this.beginDate = task.getBeginDate();
        this.endDate = task.getEndDate();
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
