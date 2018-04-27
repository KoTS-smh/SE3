package com.sec.server.controller;

import com.sec.server.domain.Task;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.model.UserModel;
import com.sec.server.service.TaskService;
import com.sec.server.utils.ReadFile;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class TaskController {
    @Resource(name = "taskService")
    private TaskService taskService;

    @RequestMapping("/task/getAllPost")
    public Result getAllPostTask(@RequestBody UserModel userModel){
        long userId = userModel.getUserId();
        List<Task> list = taskService.getAllPost(userId);
        JSONArray array = new JSONArray(list);

        return ResultUtils.success(array.toString());
    }

    @RequestMapping("/task/getAllFinished")
    public Result getAllFinishedTask(@RequestBody UserModel userModel) {
        long userId = userModel.getUserId();
        List<Task> list  = ReadFile.getAllFinished(userId);

        JSONArray array = new JSONArray(list);
        return ResultUtils.success(array.toString());
    }

    @RequestMapping("/task/getAllunFinished")
    public Result getAllunFinishedTask(@RequestBody UserModel userModel) {
        long userId = userModel.getUserId();
        List<Task> list  = ReadFile.getAllunFinished(userId);
        JSONArray array = new JSONArray(list);
        return ResultUtils.success(array.toString());
    }

    @RequestMapping("/task/getAllAccept")
    public Result getAllAcceptTask(long userId){
        //todo
        return ResultUtils.success();
    }

    @RequestMapping("/task/taskInfo")
    public Result getTaskInfo(long taskId){
        Task task = taskService.getTaskInfo(taskId);
        return ResultUtils.success(task);
    }

    @RequestMapping("/task/create")
    public Result createTask(@RequestBody String task){
        System.out.println(task);
        try {
            taskService.createTask(task);
        }catch (Exception e){
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }


        return ResultUtils.success();
    }

    @RequestMapping("/task/update")
    public Result updateTask(@RequestBody String task){
        task = task.substring(1, task.length() - 1);
        try {
            taskService.updateTask(task);
        }catch (Exception e){
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
        return ResultUtils.success();
    }

    @RequestMapping("/task/delete")
    public Result deleteTask(long taskId){
        try {
            taskService.deleteTask(taskId);
        }catch (Exception e){
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
        return ResultUtils.success();
    }

}