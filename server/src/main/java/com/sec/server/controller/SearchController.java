package com.sec.server.controller;


import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @RequestMapping("search/getTask")
    public Result searchTask(String message){
        //todo
        return ResultUtils.success();
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
