package com.sec.server.model;

import com.sec.server.domain.TaskOrder;
import com.sec.server.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskRateMessage {
    private String acceptUserName;
    private double rate;
    private String state;
    private boolean isSubmitted;
    private long taskOrderId;

    public String getAcceptUserName() {
        return acceptUserName;
    }

    public void setAcceptUserName(String acceptUserName) {
        this.acceptUserName = acceptUserName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTaskOrderId(Long taskOrderId) {
        this.taskOrderId = taskOrderId;
    }

    public Long getTaskOrderId() {
        return taskOrderId;
    }
}

