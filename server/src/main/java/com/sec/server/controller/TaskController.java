package com.sec.server.controller;

import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/task/{taskId}")
    public Result getTaskInfo(@PathVariable long taskId){
        //todo
        return ResultUtils.success();
    }

    @RequestMapping("/taskInfo")

    public Result getInfo(){
        return ResultUtils.success();
    }



}