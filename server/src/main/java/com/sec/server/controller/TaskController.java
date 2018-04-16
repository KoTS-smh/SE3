package com.sec.server.controller;

import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @RequestMapping("/task/getAllPost")
    public Result getAllPostTask(long userId){
        // todo
        return ResultUtils.success();
    }

    @RequestMapping("/task/getAllAccept")
    public Result getAllAcceptTask(long userId){
        //todo
        return ResultUtils.success();
    }

    @RequestMapping("/task/taskInfo")
    public Result getTaskInfo(long taskId){
        //todo
        return ResultUtils.success();
    }

    @RequestMapping("/task/create")
    public Result createTask(String task){
        return ResultUtils.success();
    }

    @RequestMapping("/task/update")
    public Result updateTask(String task){
        return ResultUtils.success();
    }

    @RequestMapping("/task/delete")
    public Result deleteTask(long taskId){
        return ResultUtils.success();
    }

}