package com.sec.server.model;

import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.utils.ReadFile;

import java.util.Date;

public class TaskOrderWraper {
    private long taskOrderId;
    private long taskId;
    private long acceptUserId;
    private boolean submited;
    private int lastPic;
    private String degreeOfCompletion;
    private int rate;
    private long annotationId;
    private Date beginDate;
    private Date endDate;

    private String taskName;

    public TaskOrderWraper(TaskOrder taskOrder) {
        Task task = ReadFile.getTask(taskOrder.getTaskId());
        this.taskOrderId = taskOrder.getTaskOrderId();
        this.taskId = taskOrder.getTaskId();
        this.acceptUserId = taskOrder.getAcceptUserId();
        this.submited = taskOrder.getSubmited();
        this.lastPic = taskOrder.getLastPic();

        double completion = taskOrder.getDegreeOfCompletion() / (double) task.getImgUrlList().size();
        this.degreeOfCompletion = completion * 100 + "%";

        this.rate = taskOrder.getRate();
        this.annotationId = taskOrder.getAnnotationId();
        this.beginDate = taskOrder.getBeginDate();
        this.endDate = taskOrder.getEndDate();


        this.taskName = task.getTaskname();
    }

    public long getTaskOrderId() {
        return taskOrderId;
    }

    public void setTaskOrderId(long taskOrderId) {
        this.taskOrderId = taskOrderId;
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

    public boolean isSubmited() {
        return submited;
    }

    public void setSubmited(boolean submited) {
        this.submited = submited;
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

    public long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(long annotationId) {
        this.annotationId = annotationId;
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
}
