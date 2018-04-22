package com.sec.server.controller;

import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.service.TaskService;
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

@RestController
public class TaskController {
    @Resource(name = "taskService")
    private TaskService taskService;

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
    public Result createTask(@RequestBody String task){
//        System.out.println(task);
//        JSONObject object = new JSONObject(task);
//
//        File file = new File("src/data/task.json");
//        String content = null;
//
//        try {
//            content = FileUtils.readFileToString(file, "UTF-8");
//            JSONArray array = new JSONArray(content);
//            array.put(object);
//            FileUtils.write(file, array.toString(2));
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new ResultException(ResultCode.UNKNOWN_ERROR);
//        }
        System.out.println(task);
        try {
            taskService.createTask(task);
        }catch (Exception e){
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }


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