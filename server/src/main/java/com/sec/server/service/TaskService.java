package com.sec.server.service;

import com.sec.server.domain.Task;

public interface TaskService {
    //创建任务方法
    void createTask(String task);

    void updateTask(String task);

    void deleteTask(long taskId);

    Task getTaskInfo(long taskId);
}
