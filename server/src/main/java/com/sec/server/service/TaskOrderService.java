package com.sec.server.service;

import com.sec.server.domain.TaskOrder;

import java.util.List;

public interface TaskOrderService {
    //获取全部taskOrder方法
    List<TaskOrder> getAllTaskOrder(String username);
}
