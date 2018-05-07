package com.sec.server.repository;

import com.sec.server.domain.TaskOrder;
import com.sec.server.model.TaskOrderWraper;

import java.util.List;

public interface TaskOrderDao {

    List<TaskOrderWraper> getAllTaskOrder(long userId);

    void createTaskOrder(TaskOrder taskOrder);

    void updateTaskOrder(String taskOrder);

    void deleteTaskOrder(long taskOrderId,long userId);

    TaskOrder getTaskOrderById(long taskOrder,long userId);
}
