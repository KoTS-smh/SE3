package com.sec.server.controller;

import com.sec.server.model.Picture_CardModel;
import com.sec.server.utils.ReadFile;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomePageController {

    @RequestMapping("/getContent")
    public Result getContent() {
        List<Picture_CardModel> modelList = ReadFile.getAllUnFinishTasks();
        JSONArray array = new JSONArray(modelList);
        return ResultUtils.success(array.toString());
    }
}
