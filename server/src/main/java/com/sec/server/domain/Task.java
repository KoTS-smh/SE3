package com.sec.server.domain;

import com.sec.server.enums.AnnotationType;
import com.sec.server.exception.ResultException;
import com.sec.server.model.TaskModel;
import com.sec.server.utils.StringList2String;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Task {
    private long taskId;
    private long postUserId;
    private String taskname;
    private String taskInfo;
    private AnnotationType annotationType;
    private String classifiedInfo;//改成str
    private Date beginDate;
    private Date endDate;
    private int totalPoints;
    private int taskLevel;
    private int maxParticipator;
    private boolean isFinished;
    private int viewedTimes;
    private double reward;
    private List<String> imgUrls;
    private String upRate;

    public Task(){}

    public Task(TaskModel taskModel) {
        this.postUserId = taskModel.getPostUserId();
        this.taskname = taskModel.getTaskname();
        this.taskInfo = taskModel.getTaskInfo();
        this.annotationType = taskModel.getAnnotationType();

        if(taskModel.getClassifiedInfo().get(0).equals("")){
            this.classifiedInfo = "";
        }
        else
        {
            this.classifiedInfo = StringList2String.join(",", taskModel.getClassifiedInfo());
        }

        try {
            this.beginDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(taskModel.getBeginDate());
            this.endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(taskModel.getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ResultException("时间格式错误", 12120);
        }

        this.totalPoints = taskModel.getTotalPoints();
        this.taskLevel = taskModel.getTaskLevel();
        this.maxParticipator = taskModel.getMaxParticipator();
        this.isFinished = taskModel.isFinished();
        this.viewedTimes = 0;
        this.reward = 0;
        this.imgUrls = taskModel.getImgUrlList();

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

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(AnnotationType annotationType) {
        this.annotationType = annotationType;
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

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getViewedTimes() {
        return viewedTimes;
    }

    public void setViewedTimes(int viewedTimes) {
        this.viewedTimes = viewedTimes;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public String getClassifiedInfo() {
        return classifiedInfo;
    }

    public void setClassifiedInfo(String classifiedInfo) {
        this.classifiedInfo = classifiedInfo;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }


    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getUpRate() {
        return upRate;
    }

    public void setUpRate(String upRate) {
        this.upRate = upRate;
    }
}
