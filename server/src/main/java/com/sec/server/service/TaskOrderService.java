package com.sec.server.service;

import com.sec.server.domain.TaskOrder;
import com.sec.server.model.TaskOrderWraper;

import java.util.List;

public interface TaskOrderService {
    //获取全部taskOrder方法
    List<TaskOrderWraper> getAllTaskOrder(long userId);

    void createTaskOrder(TaskOrder taskOrder);

    void updateTaskOrder(String taskOrder);

    void deleteTaskOrder(long taskOrderId,long userId);

    TaskOrder getTaskOrderById(long taskOrderId,long userId);
}
