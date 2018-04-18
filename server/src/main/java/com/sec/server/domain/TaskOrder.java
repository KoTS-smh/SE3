package com.sec.server.domain;


public class TaskOrder {
    private long taskOrderId;
    private long taskId;
    private long acceptUserId;
    private boolean isFinished;
    private int lastPic;
    private int isDone;
    private int rate;
    private long annotationId;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(long acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public int getLastPic() {
        return lastPic;
    }

    public void setLastPic(int lastPic) {
        this.lastPic = lastPic;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(long annotationId) {
        this.annotationId = annotationId;
    }

    public long getTaskOrderId() {
        return taskOrderId;
    }

    public void setTaskOrderId(long taskOrderId) {
        this.taskOrderId = taskOrderId;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}
