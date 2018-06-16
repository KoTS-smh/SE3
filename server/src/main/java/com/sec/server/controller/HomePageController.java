package com.sec.server.controller;

import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.AnnotationType;
import com.sec.server.model.PersonalPointModel;
import com.sec.server.model.PersonalTaskNumModel;
import com.sec.server.model.Picture_CardModel;
import com.sec.server.model.UserModel;
import com.sec.server.service.HonorService;
import com.sec.server.service.TaskOrderService;
import com.sec.server.service.TaskService;
import com.sec.server.service.UserService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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

    @Resource(name = "honorService")
    private HonorService honorService;

    @Resource(name = "taskOrderService")
    private TaskOrderService taskOrderService;

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
        PersonalTaskNumModel pModel = userService.getPersonalTaskNum(userId);

        double framePoint = honorService.getFramePoint(userId);
        double classifyPoint = honorService.getClassifyPoint(userId);
        double wholePoint = honorService.getWholePoint(userId);
        double regionPoint = honorService.getRegionPoint(userId);

        PersonalPointModel pointModel = new PersonalPointModel(framePoint, classifyPoint, wholePoint, regionPoint);

        int num = taskOrderService.getNumOfDiffenentType(6, AnnotationType.option1);
        System.out.println("this is num " + num);

        JSONObject object1 = new JSONObject(pModel);
        JSONObject object2 = new JSONObject(pointModel);

        array.put(object1);
        array.put(object2);

        return ResultUtils.success(array.toString());
    }
}
