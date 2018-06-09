package com.sec.server.service;

import com.sec.server.domain.TaskOrder;
import com.sec.server.model.TaskOrderModel;

import java.util.List;

public interface TaskOrderService {

    TaskOrder getTaskOrderById(long taskOrderId);

    List<TaskOrderModel> getAllTaskOrder(long userId);

    void createTaskOrder(TaskOrder taskOrder);

    void updateTaskOrder(TaskOrder taskOrder);

    void deleteTaskOrder(long taskOrderId);

    List<TaskOrder> getAllSubmited(long userId);

    void endAppointment(long taskId);

}
