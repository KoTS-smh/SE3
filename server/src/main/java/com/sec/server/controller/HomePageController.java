package com.sec.server.controller;

import com.sec.server.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    @RequestMapping("/getContent")
    public Result getContent() {
//        List<Picture_CardModel> modelList = ReadFile.getAllUnFinishTasks();
//        JSONArray array = new JSONArray(modelList);
//        return ResultUtils.success(array.toString());
        return null;
    }
}
