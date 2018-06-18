package com.sec.server.model;

import com.sec.server.domain.TaskOrder;
import com.sec.server.domain.User;
import com.sec.server.enums.TaskOrderState;
import com.sec.server.enums.TaskState;
import com.sec.server.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskOrderMessage {
    private long taskOrderId;
    private String acceptUsername;
    private double percentage;
    private TaskOrderState state;

    public TaskOrderMessage() {}

    public long getTaskOrderId() {
        return taskOrderId;
    }

    public void setTaskOrderId(long taskOrderId) {
        this.taskOrderId = taskOrderId;
    }

    public String getAcceptUsername() {
        return acceptUsername;
    }

    public void setAcceptUsername(String acceptUsername) {
        this.acceptUsername = acceptUsername;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public TaskOrderState getState() {
        return state;
    }

    public void setState(TaskOrderState state) {
        this.state = state;
    }
}
