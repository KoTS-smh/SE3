package com.sec.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.domain.TaskOrder;
import com.sec.server.model.TaskOrderModel;
import com.sec.server.model.UserModel;
import com.sec.server.service.TaskOrderService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TaskOrderController {
    @Resource(name = "taskOrderService")
    private TaskOrderService taskOrderService;

    @RequestMapping("/taskOrder/orderInfo")
    public Result getAllTaskOrder(long taskOrderId){
        System.out.print(taskOrderId);
        TaskOrder taskOrder = new TaskOrder();
        taskOrder.setLastPic(1);
        taskOrder.setAcceptUserId(1000);
        taskOrder.setAnnotationId(100);
        taskOrder.setSubmited(false);
        taskOrder.setDegreeOfCompletion(1);
        taskOrder.setTaskId(10000);
        taskOrder.setTaskOrderId(111);
        return ResultUtils.success(taskOrder);
    }

    @RequestMapping("/taskOrder/getAll")
    public Result getAllTaskOrder(@RequestBody UserModel userModel) {
        String username = userModel.getUsername();
        long userId = userModel.getUserId();
        System.out.println("inController" + username);
        List<TaskOrder> list = taskOrderService.getAllTaskOrder(userId);
        return ResultUtils.success(list);
    }

    @RequestMapping("/taskOrder/createTaskOrder")
    public Result createTaskOrder(@RequestBody TaskOrderModel taskOrderModel){
        long taskId = taskOrderModel.getTaskId();
        long userId = taskOrderModel.getUserId();
        TaskOrder taskOrder = new TaskOrder(taskId, userId);
        taskOrderService.createTaskOrder(taskOrder);
        return ResultUtils.success();
    }

    @RequestMapping("taskOrder/update")
    public Result updateTaskOrder(@RequestBody String taskOrder){
        JSONObject jsonObject = JSON.parseObject(taskOrder);
        System.out.print(jsonObject.getJSONObject("taskOrder"));
        return ResultUtils.success();
    }

    @RequestMapping("taskOrder/delete")
    public Result deleteTaskOrder(long taskOrderId){
        System.out.print(taskOrderId);
        return ResultUtils.success();
    }
}
