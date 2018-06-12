package com.sec.server.domain;

public class HonorMessage {
    //用户ID
    private long userId;
    //标框标注等级
    private int frameTagLevel;
    //分类标注等级
    private int classifyTagLevel;
    //整体标注等级
    private int wholeTagLevel;
    //区域标注等级
    private int regionTagLevel;
    //总评等级
    private int totalLevel;
    //标框标注分数
    private double frameTagPoint;
    //分类标注分数
    private double classifyTagPoint;
    //整体标注分数
    private double wholeTagPoint;
    //区域标注分数
    private double regionTagPoint;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getFrameTagLevel() {
        return frameTagLevel;
    }

    public void setFrameTagLevel(int frameTagLevel) {
        this.frameTagLevel = frameTagLevel;
    }

    public int getClassifyTagLevel() {
        return classifyTagLevel;
    }

    public void setClassifyTagLevel(int classifyTagLevel) {
        this.classifyTagLevel = classifyTagLevel;
    }

    public int getWholeTagLevel() {
        return wholeTagLevel;
    }

    public void setWholeTagLevel(int wholeTagLevel) {
        this.wholeTagLevel = wholeTagLevel;
    }

    public int getRegionTagLevel() {
        return regionTagLevel;
    }

    public void setRegionTagLevel(int regionTagLevel) {
        this.regionTagLevel = regionTagLevel;
    }

    public int getTotalLevel() {
        return totalLevel;
    }

    public void setTotalLevel(int totalLevel) {
        this.totalLevel = totalLevel;
    }

    public double getFrameTagPoint() {
        return frameTagPoint;
    }

    public void setFrameTagPoint(double frameTagPoint) {
        this.frameTagPoint = frameTagPoint;
    }

    public double getClassifyTagPoint() {
        return classifyTagPoint;
    }

    public void setClassifyTagPoint(double classifyTagPoint) {
        this.classifyTagPoint = classifyTagPoint;
    }

    public double getWholeTagPoint() {
        return wholeTagPoint;
    }

    public void setWholeTagPoint(double wholeTagPoint) {
        this.wholeTagPoint = wholeTagPoint;
    }

    public double getRegionTagPoint() {
        return regionTagPoint;
    }

    public void setRegionTagPoint(double regionTagPoint) {
        this.regionTagPoint = regionTagPoint;
    }
}
