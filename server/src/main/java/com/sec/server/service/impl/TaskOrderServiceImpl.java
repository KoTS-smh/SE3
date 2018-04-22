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
    public List<TaskOrder> getAllTaskOrder(String username) {
        System.out.println("inService" + username);
        return taskOrderDao.getAllTaskOrder(username);
    }
}
