package com.sec.server.repository;

import com.sec.server.domain.TaskOrder;

import java.util.List;

public interface TaskOrderDao {

    List<TaskOrder> getAllTaskOrder(String username);
}
