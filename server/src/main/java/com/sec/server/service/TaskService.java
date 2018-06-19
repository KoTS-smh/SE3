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
     * @describe 只有以下两种情况可以删除任务：
     *              1、开始了没有人接取
     *              2、还未开始且没人预约的任务
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
     * 系统定时检查
     * @describe
     *              1、有没有任务需要开始
     *              2、有没有任务需要结算
     *              3、审批工人任务质量
     *              4、有没有任务到达检查点  TODO
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
    void checkTaskAtCheckPoint(long taskId,boolean isEnd);

    JSONArray getMenuItems();

    void viewedTimeInc(long taskId);

    Task getSimpleTask(long taskId);

}
