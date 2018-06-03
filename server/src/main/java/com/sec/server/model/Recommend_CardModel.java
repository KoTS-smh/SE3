package com.sec.server.model;

import com.sec.server.domain.Task;
import com.sec.server.enums.TaskTag;

import java.util.List;

/**
 * 推荐任务界面的信息包装类
 */
public class Recommend_CardModel {
    private String name;
    private long id;
    private String url;
    private String description;
    private int viewedTimes;
    private double reward;
    private String upRate;
    private List<TaskTag> tagList;

    public Recommend_CardModel(){}

    public Recommend_CardModel(Task task) {
        this.name = task.getTaskname();
        this.id = task.getTaskId();
        this.url = task.getImgUrls().get(0);
        this.description = task.getTaskInfo();
        this.viewedTimes = task.getViewedTimes();
        this.reward = task.getReward();
        this.upRate = task.getUpRate();
        this.tagList = task.getTaskTags();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUpRate() {
        return upRate;
    }

    public void setUpRate(String upRate) {
        this.upRate = upRate;
    }

    public List<TaskTag> getTagList() {
        return tagList;
    }

    public void setTagList(List<TaskTag> tagList) {
        this.tagList = tagList;
    }
}
