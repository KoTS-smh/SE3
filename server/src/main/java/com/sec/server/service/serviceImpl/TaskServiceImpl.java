package com.sec.server.service.serviceImpl;

import com.sec.server.dao.ImgUrlDao;
import com.sec.server.dao.TaskDao;
import com.sec.server.domain.Task;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "taskService")
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ImgUrlDao imgUrlDao;

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
    public Task getTask(long taskId) {
        Task task = taskDao.getTask(taskId);
        return task;
    }
}
