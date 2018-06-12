package com.sec.server.domain;

import com.sec.server.enums.AnnotationType;
import com.sec.server.enums.TaskTag;
import com.sec.server.exception.ResultException;
import com.sec.server.model.TaskModel;
import com.sec.server.utils.StringList2String;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {
    //任务Id
    private long taskId;
    //发布者Id
    private long postUserId;
    //任务名称
    private String taskname;
    //任务信息
    private String taskInfo;
    //任务标注类型
    private AnnotationType annotationType;
    //分类标注信息（只有分类标注才有，其他的null）
    private String classifiedInfo;
    //开始时间
    private Date beginDate;
    //结束时间
    private Date endDate;
    //完成任务获得的积分
    private int totalPoints;
    //任务需要的最低工人等级
    private int taskLevel;
    //最大同时标注工人数
    private int maxParticipator;
    //是否完成
    private boolean isFinished;
    //被访问次数
    private int viewedTimes;
    //报酬（等级为1时的基础，如果更高会多给报酬，按照等级由网站支付）
    private double reward;
    private List<String> imgUrls;
    private String upRate;
    private List<TaskTag> taskTags;
    private String taskTagString;
    private double quality;

    public Task(){
        this.taskTags = new ArrayList<>();
    }

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
        this.taskTagString = "";
        this. taskTags = new ArrayList<>();

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

    public List<TaskTag> getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(List<TaskTag> taskTags) {
        this.taskTags = taskTags;
    }

    public String getTaskTagString() {
        return taskTagString;
    }

    public void setTaskTagString(String taskTagString) {
        this.taskTagString = taskTagString;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public void insertTag(TaskTag tag) {
        this.taskTags.add(tag);
    }
}
