package com.sec.server.repository;

import com.sec.server.domain.Task;

import java.util.List;

public interface TaskDao {
    void createTask(String task);

    void updateTask(String task);

    void deleteTask(long taskId);

    Task getTaskInfo(long taskId);

    List<Task> getAllPost(long userId);
}
