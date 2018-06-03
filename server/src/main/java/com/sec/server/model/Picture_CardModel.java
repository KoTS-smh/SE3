package com.sec.server.model;

/**
 * 与前端主页中的picture_card 结构对应
 * name
 * id
 * url
 * description
 * views 待完成
 * comments 待完成
 */
public class Picture_CardModel {
    private String name;
    private long id;
    private String url;
    private String description;
    private int viewedTimes;
    private double reward;
    private String upRate;


    public Picture_CardModel(){}

    public Picture_CardModel(long id, String name, String url, String description, int viewedTimes,
                             double reward, String upRate) {
        this.name = name;
        this.id = id;
        this.url = url;
        this.description = description;
        this.viewedTimes = viewedTimes;
        this.reward = reward;
        this.upRate = upRate;
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
}
