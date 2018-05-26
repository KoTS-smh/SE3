package com.sec.server.controller;

import com.sec.server.domain.Task;
import com.sec.server.model.Picture_CardModel;
import com.sec.server.service.TaskService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HomePageController {
    @Resource(name = "taskService")
    private TaskService taskService;

    @RequestMapping("/getContent")
    public Result getContent() {
//        List<Picture_CardModel> modelList = ReadFile.getAllUnFinishTasks();
//        JSONArray array = new JSONArray(modelList);
//        return ResultUtils.success(array.toString());
        List<Task> taskList = taskService.getEveryUnFinishedTask();
        List<Picture_CardModel> modelList = new ArrayList<>();
        for(Task tmp : taskList){
            Picture_CardModel model = new Picture_CardModel();
            System.out.println("this is taskId " + tmp.getTaskId());
            model.setId(tmp.getTaskId());
            model.setName(tmp.getTaskname());
            model.setDescription(tmp.getTaskInfo());
            model.setUrl(tmp.getImgUrls().get(0));

            modelList.add(model);
        }

        JSONArray array = new JSONArray(modelList);

        return ResultUtils.success(array.toString());
    }
}
