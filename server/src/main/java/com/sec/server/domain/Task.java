package com.sec.server.domain;

import com.sec.server.enums.AnnotationType;

import java.util.Date;
import java.util.List;

public class Task {
    private long taskId;
    private long postUserId;

    private String taskname;
    private String taskInfo;
    private List<String> imgUrlList;
    private AnnotationType annotationType;
    private List<String> classifiedInfo;
    private Date beginDate;
    private Date endDate;
    private int totalPoints;
//    private List<Long> acceptUserIds;
    private int taskLevel;
    private int maxParticipator;
    private List<Long> acceptUserIds;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(long postUserId) {
        this.postUserId = postUserId;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public List<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(AnnotationType annotationType) {
        this.annotationType = annotationType;
    }

    public List<String> getClassifiedInfo() {
        return classifiedInfo;
    }

    public void setClassifiedInfo(List<String> classifiedInfo) {
        this.classifiedInfo = classifiedInfo;
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

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

//    public List<Long> getAcceptUserIds() {
//        return acceptUserIds;
//    }
//
//    public void setAcceptUserIds(List<Long> acceptUserIds) {
//        this.acceptUserIds = acceptUserIds;
//    }
//
    public int getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(int taskLevel) {
        this.taskLevel = taskLevel;
    }

    public int getMaxParticipator() {
        return maxParticipator;
    }

    public void setMaxParticipator(int maxParticipator) {
        this.maxParticipator = maxParticipator;
    }

    public List<Long> getAcceptUserIds() {
        return acceptUserIds;
    }

    public void setAcceptUserIds(List<Long> acceptUserIds) {
        this.acceptUserIds = acceptUserIds;
    }
}
