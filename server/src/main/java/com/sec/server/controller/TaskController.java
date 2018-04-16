package com.sec.server.controller;

import com.sec.server.domain.Task;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

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

    @RequestMapping("/task/addTask")
    public Result saveTask(@RequestBody Task task) throws IOException{
        File file = new File("src/data/task.json");
        String content = FileUtils.readFileToString(file, "UTF-8");
        JSONArray array = new JSONArray(content);


        return null;
    }



}