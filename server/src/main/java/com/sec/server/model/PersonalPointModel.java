package com.sec.server.model;

public class PersonalPointModel {
    private double framePoint;
    private double classifyPoint;
    private double wholePoint;
    private double regionPoint;

    public PersonalPointModel() {
    }

    public PersonalPointModel(double framePoint, double classifyPoint, double wholePoint, double regionPoint) {
        this.framePoint = framePoint;
        this.classifyPoint = classifyPoint;
        this.wholePoint = wholePoint;
        this.regionPoint = regionPoint;
    }

    public double getFramePoint() {
        return framePoint;
    }

    public void setFramePoint(double framePoint) {
        this.framePoint = framePoint;
    }

    public double getClassifyPoint() {
        return classifyPoint;
    }

    public void setClassifyPoint(double classifyPoint) {
        this.classifyPoint = classifyPoint;
    }

    public double getWholePoint() {
        return wholePoint;
    }

    public void setWholePoint(double wholePoint) {
        this.wholePoint = wholePoint;
    }

    public double getRegionPoint() {
        return regionPoint;
    }

    public void setRegionPoint(double regionPoint) {
        this.regionPoint = regionPoint;
    }
}
