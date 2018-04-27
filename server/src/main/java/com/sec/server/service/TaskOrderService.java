package com.sec.server.service;

import com.sec.server.domain.TaskOrder;

import java.util.List;

public interface TaskOrderService {
    //获取全部taskOrder方法
    List<TaskOrder> getAllTaskOrder(long userId);

    void createTaskOrder(TaskOrder taskOrder);

    void updateTaskOrder(String taskOrder);

    void deleteTaskOrder(long taskOrderId,long userId);
}
