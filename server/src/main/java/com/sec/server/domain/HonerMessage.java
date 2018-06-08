package com.sec.server.domain;

public class HonerMessage {

    private long userId;
    private int frameTagLevel;
    private int classifyTagLevel;
    private int wholeTagLevel;
    private int regionTagLevel;
    private int totalLevel;

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
}
