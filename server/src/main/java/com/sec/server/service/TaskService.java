package com.sec.server.service;

import com.sec.server.domain.Task;

import java.util.List;


public interface TaskService {
    void createTask(Task task);

    void updataTask(Task task);

    void deleteTask(long taskId);

    List<Task> getAllPostTask(long postUserId);

    List<Task> getAllFinishedTask(long postUserId);

    List<Task> getAllunFinishedTask(long postUserId);

    List<Task> getEveryUnFinishedTask();

    Task getTask(long taskId);
}
