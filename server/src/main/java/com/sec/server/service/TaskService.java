package com.sec.server.service;

import com.sec.server.domain.Task;
import com.sec.server.model.Picture_CardModel;
import com.sec.server.model.TaskModel;

import java.util.List;


public interface TaskService {
    void createTask(Task task);

    void updataTask(Task task);

    void deleteTask(long taskId);

    List<Task> getAllPostTask(long postUserId);

    List<Task> getAllFinishedTask(long postUserId);

    List<Task> getAllunFinishedTask(long postUserId);

    List<Task> getEveryUnFinishedTask();

    TaskModel getTask(long taskId);

    void checkTaskEveryDay();

    List<Task> getRecommendTask(long userId);

    List<Picture_CardModel> searchTask(String message, String taskType, String tag);
}
