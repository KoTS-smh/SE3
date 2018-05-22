package com.sec.server.controller;


import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @RequestMapping("search/getTask")
    public Result searchTask(String message, String taskType){
//        System.out.println("ffffffhere");
//        System.out.println(message);
//        System.out.println(taskType);
//        List<Picture_CardModel> list = ReadFile.searchTask(message, taskType);
//        JSONArray array = new JSONArray(list);
//        return ResultUtils.success(array.toString());
        return null;
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
