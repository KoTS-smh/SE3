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
     * 获取任务当前工作人数
     * @param taskId 任务Id
     * @return 当前工作人数
     */
    int getWorkerNumber(long taskId);

    /**
     * 任务激励方法
     */
    void checkTaskEveryDay();

    List<Task> getRecommendTask(long userId);

    List<Picture_CardModel> searchTask(String message, String taskType, String tag);

    /**
     * 任务结算方法
     * @param taskId 任务Id
     */
    void finishTask(long taskId);

    /**
     * 定时遍历数据库的任务
     * @describe
     *              运行时间：每天午夜十二点
     *              目的： 1、检查有没有任务需要开始
     *                    2、检查有没有任务需要
     */
    void timeTask();

    /**
     * 创建检查点
     */
    void createCheckPoint(long taskId);

    /**
     * 到达检查点开始检查任务订单完成质量
     * @param taskId 任务Id
     * @param isEnd 是否是最后一个检查点
     */
    void checkTask(long taskId,boolean isEnd);

    JSONArray getMenuItems();

    void viewedTimeInc(long taskId);

}
