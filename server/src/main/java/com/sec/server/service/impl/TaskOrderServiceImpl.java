package com.sec.server.service.impl;

import com.sec.server.domain.TaskOrder;
import com.sec.server.repository.TaskOrderDao;
import com.sec.server.service.TaskOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "taskOrderService")
public class TaskOrderServiceImpl implements TaskOrderService{

    @Resource(name = "taskOrderDao")
    private TaskOrderDao taskOrderDao;


    @Override
    public List<TaskOrder> getAllTaskOrder(long userId) {
        return taskOrderDao.getAllTaskOrder(userId);
    }

    @Override
    public void createTaskOrder(TaskOrder taskOrder) {
        taskOrderDao.createTaskOrder(taskOrder);
    }

    @Override
    public void updateTaskOrder(String taskOrder) {
        taskOrderDao.updateTaskOrder(taskOrder);
    }

    @Override
    public void deleteTaskOrder(long taskOrderId,long userId) {
        taskOrderDao.deleteTaskOrder(taskOrderId,userId);
    }

    @Override
    public TaskOrder getTaskOrderById(long taskOrderId,long userId) {
        return taskOrderDao.getTaskOrderById(taskOrderId,userId);
    }
}
