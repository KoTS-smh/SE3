package com.sec.server.service.impl;

import com.sec.server.repository.TaskDao;
import com.sec.server.service.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "taskService")
public class TaskServiceImpl implements TaskService {
    @Resource(name = "taskDao")
    private TaskDao taskDao;

    @Override
    public void createTask(String task) {
        taskDao.createTask(task);
    }
}
