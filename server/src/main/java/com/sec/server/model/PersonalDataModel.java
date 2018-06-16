package com.sec.server.model;

public class PersonalDataModel {
    private int point;
    private int tasknumber;
    private String rank;

    public PersonalDataModel() {
    }

    public PersonalDataModel(int point, int tasknumber, String rank) {
        this.point = point;
        this.tasknumber = tasknumber;
        this.rank = rank;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getTasknumber() {
        return tasknumber;
    }

    public void setTasknumber(int tasknumber) {
        this.tasknumber = tasknumber;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
