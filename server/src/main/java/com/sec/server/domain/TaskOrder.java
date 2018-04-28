package com.sec.server.domain;


import com.sec.server.repository.DataAnalysisDao;
import com.sec.server.repository.impl.DataAnalysisDaoImpl;
import com.sec.server.utils.ReadFile;


import java.io.File;
import java.util.Date;

public class TaskOrder {
    private long taskOrderId;
    private long taskId;
    private long acceptUserId;
    private boolean isSubmited;
    private int lastPic;
    private int degreeOfCompletion;
    private int rate;
    private long annotationId;
    private Date beginDate;
    private Date endDate;

    public TaskOrder(){}

    public TaskOrder(long taskId, long acceptUserId){
        this.taskId = taskId;
        this.acceptUserId = acceptUserId;

        //获取现在该用户taskOrder文件下taskOrder的数目，以确定编号
        //todo 实际运行时路径前没有server，测试时需要有
        String path = "src/data/taskOrder_" + acceptUserId + ".json";
        File file = new File(path);
        if(file.exists()){
            DataAnalysisDao finder = new DataAnalysisDaoImpl();
            int length = finder.getTotalAmount(path);
            this.taskOrderId = length + 1;
        }else {
            this.taskOrderId = 0;
        }

        this.isSubmited = false;
        this.lastPic = 0;
        this.degreeOfCompletion = 0;
        this.rate = -1;
        this.annotationId = 0;

        Task task = ReadFile.getTask(taskId);
        this.beginDate = task.getBeginDate();
        this.endDate = task.getEndDate();
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

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

    public boolean isSubmited() {
        return isSubmited;
    }

    public void setSubmited(boolean submited) {
        isSubmited = submited;
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

    public int getDegreeOfCompletion() {
        return degreeOfCompletion;
    }

    public void setDegreeOfCompletion(int degreeOfCompletion) {
        this.degreeOfCompletion = degreeOfCompletion;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
}
