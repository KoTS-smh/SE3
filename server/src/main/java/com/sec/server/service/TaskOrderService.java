package com.sec.server.service;

import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.AnnotationType;
import com.sec.server.model.TaskOrderModel;

import java.util.List;

public interface TaskOrderService {

    /**
     * 根据Id获取任务订单
     * @param taskOrderId 任务订单Id
     * @return 任务订单 taskOrder
     */
    TaskOrder getTaskOrderById(long taskOrderId);

    /**
     * 获得一个工人所有的任务订单
     * @param userId 工人Id
     * @return 任务订单列表 list
     */
    List<TaskOrderModel> getAllTaskOrder(long userId);

    /**
     * 创建任务订单
     * @param taskOrder 任务订单
     */
    void createTaskOrder(TaskOrder taskOrder);

    /**
     * 更新任务订单
     * @param taskOrder 任务订单
     */
    void updateTaskOrder(TaskOrder taskOrder);

    /**
     * 删除任务订单
     * @param taskOrderId 任务订单Id
     */
    void deleteTaskOrder(long taskOrderId);

    /**
     * 获得一个工人所有提交的订单
     * @param userId 工人Id
     * @return 任务订单列表 list
     */
    List<TaskOrder> getAllSubmited(long userId);

    /**
     * 提交任务订单
     * @param taskOrderId 任务订单Id
     */
    void submittedTaskOrder(long taskOrderId);

    void appointTask(long taskId,long userId);

    int getNumOfDiffenentType(long userId, AnnotationType annotationType);
}
