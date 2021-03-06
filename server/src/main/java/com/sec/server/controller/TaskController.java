package com.sec.server.controller;

import com.sec.server.domain.Task;
import com.sec.server.enums.ResultCode;
import com.sec.server.model.Recommend_CardModel;
import com.sec.server.model.TaskModel;
import com.sec.server.model.UserModel;
import com.sec.server.service.DataAnalysisService;
import com.sec.server.service.TaskService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {
    @Resource(name = "taskService")
    private TaskService taskService;
    @Resource(name = "dataAnalysisService")
    private DataAnalysisService dataAnalysisService;

    /**
     * 获得用户发起的所有任务
     * @param userModel 用户信息
     * @return 返回用户发起的所有任务
     */
    @RequestMapping("/task/getAllPost")
    public Result getAllPostTask(@RequestBody UserModel userModel){
        long postUserId = userModel.getUserId();
        List<Task> list = taskService.getAllPostTask(postUserId);
        return ResultUtils.success(list);
    }

    /**
     * 获得用户所有完成的任务
     * @param userModel 用户信息
     * @return 返回任务信息
     */
    @RequestMapping("/task/getAllFinished")
    public Result getAllFinishedTask(@RequestBody UserModel userModel) {
        long postUserId = userModel.getUserId();
        List<Task> list = taskService.getAllFinishedTask(postUserId);
        return ResultUtils.success(list);
    }

    /**
     * 获得用户所有未完成的任务
     * @param userModel 用户信息
     * @return 返回任务信息
     */
    @RequestMapping("/task/getAllunFinished")
    public Result getAllunFinishedTask(@RequestBody UserModel userModel) {
        long postUserId = userModel.getUserId();
        List<Task> list = taskService.getAllunFinishedTask(postUserId);
        return ResultUtils.success(list);
    }

    /**
     * 获取一个任务的信息
     * @param taskModel 任务ID的一层封装
     * @return 返回任务具体信息
     */
    @RequestMapping("/task/taskInfo")
    public Result getTaskInfo(@RequestBody TaskModel taskModel){
        long taskId = taskModel.getTaskId();
        TaskModel taskModel1 = taskService.getTask(taskId);
        return ResultUtils.success(taskModel1);
    }

    /**
     * 新建一个任务
     * @return 返回操作信息
     */
    @RequestMapping("/task/create")
    public Result createTask(@RequestBody TaskModel taskModel){
        List<String> urlList = taskModel.getImgUrlList();
        Task task = new Task(taskModel);
        taskService.createTask(task);

        return ResultUtils.success();
    }

    /**
     * 修改任务信息
     * @param taskModel 任务信息
     * @return 返回操作信息
     */
    @RequestMapping("/task/update")
    public Result updateTask(@RequestBody TaskModel taskModel){
        Task task = new Task(taskModel);
        taskService.updateTask(task);
        return ResultUtils.success();
    }

    /**
     * 删除一个任务
     * @param taskId 任务ID
     * @return 返回操作信息
     */
    @RequestMapping("/task/delete")
    public Result deleteTask(long taskId){
        taskService.deleteTask(taskId);
        return ResultUtils.success();
    }

    @RequestMapping("/task/recommend")
    public Result getRecommendTasks(@RequestBody UserModel userModel){
        long userId = userModel.getUserId();
        List<Task> recommendList = taskService.getRecommendTask(userId);
        List<Recommend_CardModel> modelList = new ArrayList<>();
        for(Task tmp : recommendList) {
            Recommend_CardModel model = new Recommend_CardModel(tmp);
            modelList.add(model);
        }
        return ResultUtils.success(modelList);
    }

    @RequestMapping("/task/getReward")
    public Result getMinReward(@RequestBody TaskModel taskModel){
        //获得任务推荐最小金额
        double reward = dataAnalysisService.calculateMinimumMoneyOfTask(new Task(taskModel));
        //保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");
        return ResultUtils.success(df.format(reward));
    }

    @RequestMapping("/task/viewedTimeInc")
    public Result viewedTimeInc(long taskId) {
        taskService.viewedTimeInc(taskId);

        return ResultUtils.success();
    }

}