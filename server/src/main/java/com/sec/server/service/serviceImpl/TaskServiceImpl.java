package com.sec.server.service.serviceImpl;

import com.sec.server.dao.ImgUrlDao;
import com.sec.server.dao.TaskDao;
import com.sec.server.dao.TaskOrderDao;
import com.sec.server.domain.Task;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.model.TaskModel;
import com.sec.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service(value = "taskService")
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ImgUrlDao imgUrlDao;

    @Autowired
    private TaskOrderDao taskOrderDao;

    @Override
    @Transactional
    public void createTask(Task task) {
        List<String> urlLists = task.getImgUrls();
        try {
            taskDao.addTask(task);
            imgUrlDao.insertUrlList(urlLists, task.getTaskId());
        }catch (Exception e) {
            throw new ResultException("数据库错误", 11100);
        }


    }

    @Override
    public void updataTask(Task task) {
        taskDao.updateTask(task);
    }

    @Override
    public void deleteTask(long taskId) {
        try {
            //taskDao.deleteTask(taskId);
            throw new ResultException("任务已被接取，无法删除", 12580);
        }catch (Exception e) {
            throw new ResultException("任务已被接取，无法删除", 12580);
        }
    }

    @Override
    public List<Task> getAllPostTask(long postUserId) {
        List<Task> taskList = taskDao.getAllPostTask(postUserId);
        return taskList;
    }

    @Override
    public List<Task> getAllFinishedTask(long postUserId) {
        List<Task> taskList = taskDao.getAllFinishedTask(postUserId);
        return taskList;
    }

    @Override
    public List<Task> getAllunFinishedTask(long postUserId) {
        List<Task> taskList = taskDao.getAllunFinishedTask(postUserId);
        return taskList;
    }

    @Override
    public List<Task> getEveryUnFinishedTask() {
        List<Task> taskList = taskDao.getEveryUnFinishedTask();
        return taskList;
    }

    @Override
    @Transactional
    public TaskModel getTask(long taskId) {
        Task task;
        List<Long> acceptUserList;

        try {
            task = taskDao.getTask(taskId);
            acceptUserList = taskOrderDao.getAcceptUserIds(taskId);
            taskDao.increaseViewedTimes(taskId);
        }catch (Exception e){
            throw new ResultException("任务不存在！", 1111);
        }

        TaskModel taskModel = new TaskModel(task);
        taskModel.setAcceptUserIds(acceptUserList);
        return taskModel;
    }

    @Override
    public void checkTaskEveryDay() {
        List<Task> taskList = taskDao.getAllTask();
        for(Task tmp : taskList) {
            checkTask(tmp);
        }
    }

    private Task checkTask(Task task) {
        Date beginDate = task.getBeginDate();
        Date endDate = task.getEndDate();
        Date today = new Date();

        //任务已经结束
        if(today.getTime() >= endDate.getTime()) {
            task.setFinished(true);
            return task;
        }

        //任务已经被多人接取
        int acceptNum = taskOrderDao.getAcceptNum(task.getTaskId());
        double acceptPercent = (acceptNum * 1.0) / task.getMaxParticipator();
        if(acceptPercent > 0.3){
            task.setUpRate("");
            return task;
        }

        //已经长时间无人接取
        int taskLastDays = (int) ((endDate.getTime() - beginDate.getTime()) / (1000*60*60*24));
        int dayUntilNow = (int) ((today.getTime() - beginDate.getTime()) / (1000*60*60*24));
        double percent = (dayUntilNow * 1.0) / taskLastDays;

        //接近任务结束时间
        if((taskLastDays - dayUntilNow) <= 3){
            return task;
        }

        //更改任务奖励，起到激励效果
        if(percent > 0.3) {
            task.setReward(task.getReward() * 1.2);
            task.setUpRate("+20%");
        }else if(percent > 0.5) {
            task.setReward(task.getReward() * 1.4);
            task.setUpRate("+40%");
        }else if(percent > 0.8) {
            task.setReward(task.getReward() * 1.6);
            task.setUpRate("+60%");
        }

        return task;

    }
}
