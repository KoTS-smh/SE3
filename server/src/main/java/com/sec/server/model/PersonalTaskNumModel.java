package com.sec.server.model;

public class PersonalTaskNumModel {
    private int appointNum;
    private int ongoingNum;
    private int finishedNum;
    private int point;

    public int getAppointNum() {
        return appointNum;
    }

    public void setAppointNum(int appointNum) {
        this.appointNum = appointNum;
    }

    public int getOngoingNum() {
        return ongoingNum;
    }

    public void setOngoingNum(int ongoingNum) {
        this.ongoingNum = ongoingNum;
    }

    public int getFinishedNum() {
        return finishedNum;
    }

    public void setFinishedNum(int finishedNum) {
        this.finishedNum = finishedNum;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
