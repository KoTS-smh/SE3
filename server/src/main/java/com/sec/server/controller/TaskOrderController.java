package com.sec.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.domain.TaskOrder;
import com.sec.server.model.TaskOrderModel;
import com.sec.server.model.TaskOrderWraper;
import com.sec.server.model.UserModel;
import com.sec.server.utils.ReadFile;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TaskOrderController {
//    @Resource(name = "taskOrderService")
//    private TaskOrderService taskOrderService;

    /**
     * 获取一个任务订单
     * @param taskOrderId 任务订单ID
     * @param userId 工人ID
     * @return 任务订单详细信息
     */
    @RequestMapping("/taskOrder/orderInfo")
    public Result getTaskOrder(long taskOrderId,long userId){
//        TaskOrder taskOrder = taskOrderService.getTaskOrderById(taskOrderId,userId);
//        return ResultUtils.success(taskOrder);
        return null;
    }

    /**
     * 获取工人所有的任务订单
     * @param userModel 工人信息的一层封装
     * @return 所有的任务订单
     */
    @RequestMapping("/taskOrder/getAll")
    public Result getAllTaskOrder(@RequestBody UserModel userModel) {
//        long userId = userModel.getUserId();
//        List<TaskOrderWraper> list = taskOrderService.getAllTaskOrder(userId);
//        return ResultUtils.success(list);
        return null;
    }

    /**
     * 创建一个任务订单
     * @param taskOrderModel 任务订单信息
     * @return 返回操作成功信息
     */
    @RequestMapping("/taskOrder/createTaskOrder")
    public Result createTaskOrder(@RequestBody TaskOrderModel taskOrderModel){
//        long taskId = taskOrderModel.getTaskId();
//        long userId = taskOrderModel.getUserId();
//        TaskOrder taskOrder = new TaskOrder(taskId, userId);
//        taskOrderService.createTaskOrder(taskOrder);
//        return ResultUtils.success();
        return null;
    }

    /**
     * 更新一个任务订单信息
     * @param taskOrder 新的任务订单信息
     * @return 返回操作成功信息
     */
    @RequestMapping("/taskOrder/update")
    public Result updateTaskOrder(@RequestBody String taskOrder){
//        JSONObject jsonObject = JSON.parseObject(taskOrder);
//        taskOrderService.updateTaskOrder(JSON.toJSONString(jsonObject.getJSONObject("taskOrder")));
//        return ResultUtils.success();
        return null;
    }

    /**
     * 删除一个任务订单信息
     * @param taskOrderId 任务订单ID
     * @param userId 工人ID
     * @return 返回操作成功信息
     */
    @RequestMapping("/taskOrder/delete")
    public Result deleteTaskOrder(long taskOrderId,long userId){
//        taskOrderService.deleteTaskOrder(taskOrderId,userId);
//        return ResultUtils.success();
        return null;
    }

    /**
     * 获得用户提交的所有任务订单信息
     * @param userModel 工人ID的一层封装
     * @return 返回所有任务订单信息
     */
    @RequestMapping("/taskOrder/getAllSubmited")
    public Result getAllSubmitted(@RequestBody UserModel userModel){
//        long userId = userModel.getUserId();
//        List<TaskOrder> list = ReadFile.getAllSubmited(userId);
//        return ResultUtils.success(list);
        return null;
    }


}
