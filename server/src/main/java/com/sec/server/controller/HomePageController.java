package com.sec.server.controller;

import com.sec.server.domain.Task;
import com.sec.server.model.Picture_CardModel;
import com.sec.server.model.UserModel;
import com.sec.server.service.TaskService;
import com.sec.server.service.UserService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HomePageController {
    @Resource(name = "taskService")
    private TaskService taskService;

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping("/getContent")
    public Result getContent() {
        List<Task> taskList = taskService.getEveryUnFinishedTask();
        List<Picture_CardModel> modelList = new ArrayList<>();
        for(Task tmp : taskList){
            Picture_CardModel model = new Picture_CardModel(tmp);

            modelList.add(model);
        }

        JSONArray array = new JSONArray(modelList);

        return ResultUtils.success(array.toString());
    }

    @RequestMapping("/getPersonalData")
    public Result getPersonalData(@RequestBody UserModel userModel) {
        //任务数量数据
        //雷达图数据
        long userId = userModel.getUserId();
        JSONArray array = new JSONArray();


        return null;
    }
}
