package com.sec.server.model;

import java.util.Date;

public class TaskOrderModel {
    private long taskId;
    private long userId;


    public TaskOrderModel(){}

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
