package com.sec.server.model;

import com.sec.server.domain.Task;
import com.sec.server.enums.AnnotationType;
import com.sec.server.enums.TaskState;

import java.util.Arrays;
import java.util.List;

public class TaskModel {
    private long taskId;
    private long postUserId;
    private String taskname;
    private String beginDate;
    private String endDate;
    private String taskInfo;
    private int totalPoints;
    private int maxParticipator;
    private int taskLevel;
    private List<String> imgUrlList;
    private TaskState state;
    private List<String> classifiedInfo;
    private AnnotationType annotationType;
    private List<Long> acceptUserIds;
    private double reward;
    private String upRate;
    private List<String> dynamicTags;

    public TaskModel() {}

    public TaskModel(Task task) {
        this.taskId = task.getTaskId();
        this.postUserId = task.getPostUserId();
        this.taskname = task.getTaskname();
        this.beginDate = task.getBeginDate().toString();
        this.endDate = task.getEndDate().toString();
        this.taskInfo = task.getTaskInfo();
        this.totalPoints = task.getTotalPoints();
        this.maxParticipator = task.getMaxParticipator();
        this.taskLevel = task.getTaskLevel();
        this.imgUrlList = task.getImgUrls();
        this.state = task.getState();

        if(task.getClassifiedInfo() != null) {
            this.classifiedInfo = Arrays.asList(task.getClassifiedInfo().split(","));
        }

        this.annotationType = task.getAnnotationType();
        this.reward = task.getReward();
        this.upRate = task.getUpRate();
    }

    public List<Long> getAcceptUserIds() {
        return acceptUserIds;
    }

    public void setAcceptUserIds(List<Long> acceptUserIds) {
        this.acceptUserIds = acceptUserIds;
    }

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

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getMaxParticipator() {
        return maxParticipator;
    }

    public void setMaxParticipator(int maxParticipator) {
        this.maxParticipator = maxParticipator;
    }

    public int getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(int taskLevel) {
        this.taskLevel = taskLevel;
    }

    public List<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public List<String> getClassifiedInfo() {
        return classifiedInfo;
    }

    public void setClassifiedInfo(List<String> classifiedInfo) {
        this.classifiedInfo = classifiedInfo;
    }

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(AnnotationType annotationType) {
        this.annotationType = annotationType;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public String getUpRate() {
        return upRate;
    }

    public void setUpRate(String upRate) {
        this.upRate = upRate;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public List<String> getDynamicTags() {
        return dynamicTags;
    }

    public void setDynamicTags(List<String> dynamicTags) {
        this.dynamicTags = dynamicTags;
    }
}
