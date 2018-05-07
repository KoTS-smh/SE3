package com.sec.server.domain;

public class SystemAdministratorMessage {
     private int userNumber;
     private int taskNumber;
     private int finishedTaskNumber;
     private int unfinishedTaskNumber;

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public int getFinishedTaskNumber() {
        return finishedTaskNumber;
    }

    public void setFinishedTaskNumber(int finishedTaskNumber) {
        this.finishedTaskNumber = finishedTaskNumber;
    }

    public int getUnfinishedTaskNumber() {
        return unfinishedTaskNumber;
    }

    public void setUnfinishedTaskNumber(int unfinishedTaskNumber) {
        this.unfinishedTaskNumber = unfinishedTaskNumber;
    }
}
