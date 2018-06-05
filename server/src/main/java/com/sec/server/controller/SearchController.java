package com.sec.server.controller;


import com.sec.server.model.Picture_CardModel;
import com.sec.server.service.TaskService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class SearchController {

    @Resource(name = "taskService")
    private TaskService taskService;

    @RequestMapping("search/getTask")
    public Result searchTask(String message, String taskType, String taskTag){
        System.out.println(message);
        System.out.println(taskType);
        System.out.println(taskTag);
//        System.out.println("ffffffhere");
//        System.out.println(message);
//        System.out.println(taskType);
        List<Picture_CardModel> list = taskService.searchTask(message, taskType, taskTag);
        System.out.println(list.size());
        return ResultUtils.success(list);
    }

    @RequestMapping("search/getUser")
    public Result searchUser(String message){
        // todo
        return ResultUtils.success();
    }

    @RequestMapping("search/all")
    public Result search(){
        return ResultUtils.success();
    }

}
