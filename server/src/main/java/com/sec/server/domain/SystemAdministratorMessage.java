package com.sec.server.domain;

public class SystemAdministratorMessage {
     private long userNumber;
     private long taskNumber;
     private long finishedTaskNumber;
     private long unfinishedTaskNumber;

    public long getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(long userNumber) {
        this.userNumber = userNumber;
    }

    public long getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(long taskNumber) {
        this.taskNumber = taskNumber;
    }

    public long getFinishedTaskNumber() {
        return finishedTaskNumber;
    }

    public void setFinishedTaskNumber(long finishedTaskNumber) {
        this.finishedTaskNumber = finishedTaskNumber;
    }

    public long getUnfinishedTaskNumber() {
        return unfinishedTaskNumber;
    }

    public void setUnfinishedTaskNumber(long unfinishedTaskNumber) {
        this.unfinishedTaskNumber = unfinishedTaskNumber;
    }
}
