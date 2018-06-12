package com.sec.server.service;

import com.sec.server.domain.Task;
import com.sec.server.model.Picture_CardModel;
import com.sec.server.model.TaskModel;
import org.json.JSONArray;

import java.util.List;


public interface TaskService {

    /**
     * 创建任务
     * @param task 任务信息
     */
    void createTask(Task task);

    /**
     * 更新任务
     * @param task 任务信息
     */
    void updateTask(Task task);

    /**
     * 删除任务
     * @param taskId 任务Id
     */
    void deleteTask(long taskId);

    /**
     * 获得发布者所有发布的任务
     * @param postUserId 发布者Id
     * @return 任务列表 list
     */
    List<Task> getAllPostTask(long postUserId);

    /**
     * 获得发布者所有发布完成的任务
     * @param postUserId 发布者Id
     * @return 任务列表 list
     */
    List<Task> getAllFinishedTask(long postUserId);

    /**
     * 获得发布者所有发布未完成的任务
     * @param postUserId 发布者Id
     * @return 任务列表 list
     */
    List<Task> getAllunFinishedTask(long postUserId);

    /**
     * 获得所有未完成的任务
     * @return 任务列表 list
     */
    List<Task> getEveryUnFinishedTask();

    /**
     * 获取任务
     * @param taskId 任务Id
     * @return 任务信息
     */
    TaskModel getTask(long taskId);

    /**
     * 任务激励方法
     */
    void checkTaskEveryDay();

    List<Task> getRecommendTask(long userId);

    List<Picture_CardModel> searchTask(String message, String taskType, String tag);

    /**
     * 结束预约方法
     * @param taskId 任务Id
     */
    void endAppointment(long taskId);

    /**
     * 任务结算方法
     * @param taskId 任务Id
     */
    boolean finishTask(long taskId);

    // todo  每到十二点就运行一次这个方法判断有没有任务到了DDL
    void endTask();

    JSONArray getMenuItems();
}
