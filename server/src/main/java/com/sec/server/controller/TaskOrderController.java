package com.sec.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.domain.TaskOrder;
import com.sec.server.model.TaskOrderModel;
import com.sec.server.model.UserModel;
import com.sec.server.service.TaskOrderService;
import com.sec.server.utils.ReadFile;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.json.JSONArray;
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
    public Result getTaskOrder(long taskOrderId,long userId){
        TaskOrder taskOrder = taskOrderService.getTaskOrderById(taskOrderId,userId);
        return ResultUtils.success(taskOrder);
    }

    @RequestMapping("/taskOrder/getAll")
    public Result getAllTaskOrder(@RequestBody UserModel userModel) {
        String username = userModel.getUsername();
        long userId = userModel.getUserId();
        List<TaskOrder> list = taskOrderService.getAllTaskOrder(userId);
        JSONArray array = new JSONArray(list);


        //todo 从task.json获取任务名称
//        for(int i = 0;i < list.size();++i){
//            long taskId = list.get(i).getTaskId();
//            Task tmpTask = ReadFile.getTask(taskId);
//            JSONObject tmpObject = (JSONObject) array.get(i);
//            tmpObject.put("taskname", tmpTask.getTaskname());
//            array.remove(i);
//            array.put(tmpObject);
//        }

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
        taskOrderService.updateTaskOrder(JSON.toJSONString(jsonObject.getJSONObject("taskOrder")));
        return ResultUtils.success();
    }

    @RequestMapping("taskOrder/delete")
    public Result deleteTaskOrder(long taskOrderId,long userId){
        taskOrderService.deleteTaskOrder(taskOrderId,userId);
        return ResultUtils.success();
    }

    @RequestMapping("/taskOrder/getAllSubmited")
    public Result getAllSubmitted(@RequestBody UserModel userModel){
        long userId = userModel.getUserId();
        List<TaskOrder> list = ReadFile.getAllSubmited(userId);
        JSONArray array = new JSONArray(list);
        return ResultUtils.success(array.toString());
    }

}
