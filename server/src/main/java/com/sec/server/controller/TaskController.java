package com.sec.server.controller;

import com.sec.server.domain.Task;
import com.sec.server.model.TaskModel;
import com.sec.server.model.UserModel;
import com.sec.server.service.TaskService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TaskController {
//    @Resource(name = "taskService")
//    private TaskService taskService;
    @Resource(name = "taskService")
    private TaskService taskService;

    /**
     * 获得用户发起的所有任务
     * @param userModel 用户信息
     * @return 返回用户发起的所有任务
     */
    @RequestMapping("/task/getAllPost")
    public Result getAllPostTask(@RequestBody UserModel userModel){
//        long userId = userModel.getUserId();
//        List<Task> list = taskService.getAllPost(userId);
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

//        long taskId = taskModel.getTaskId();
//        Task task = taskService.getTaskInfo(taskId);
//
//        JSONObject object = new JSONObject(task);
//        return ResultUtils.success(task);
        long taskId = taskModel.getTaskId();
        Task task = taskService.getTask(taskId);

        return ResultUtils.success(task);
    }

    /**
     * 新建一个任务
     * @return 返回操作信息
     */
    @RequestMapping("/task/create")
    public Result createTask(@RequestBody TaskModel taskModel){
//        try {
//            taskService.createTask(task);
//        }catch (Exception e){
//            throw new ResultException(ResultCode.UNKNOWN_ERROR);
//        }
//        return ResultUtils.success();
        List<String> urlList = taskModel.getImgUrlList();
        //todo save urls

        Task task = new Task(taskModel);
        System.out.println(taskModel.getTaskInfo());
        System.out.println("in controller " + task.getTaskInfo());

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
        taskService.updataTask(task);
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

}