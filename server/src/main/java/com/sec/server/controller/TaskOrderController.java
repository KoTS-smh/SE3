package com.sec.server.controller;

import com.sec.server.domain.TaskOrder;
import com.sec.server.model.SimpleTaskOrderModel;
import com.sec.server.model.SimpleUserModel;
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
        TaskOrder taskOrder = taskOrderService.getTaskOrderById(taskOrderId);
        return ResultUtils.success(taskOrder);
    }

    /**
     * 获取工人所有的任务订单
     * @param simpleUserModel 工人信息的一层封装
     * @return 所有的任务订单
     */
    @RequestMapping("/taskOrder/getAll")
    public Result getAllTaskOrder(@RequestBody SimpleUserModel simpleUserModel) {
//        long userId = userModel.getAcceptUserId();
        //List<TaskOrderWraper> list = taskOrderService.getAllTaskOrder(userId);
//        return ResultUtils.success(list);
        List<TaskOrderModel> list = taskOrderService.getAllTaskOrder(simpleUserModel.getAcceptUserId());
        return ResultUtils.success(list);
    }

    /**
     * 创建一个任务订单
     * @param simpleTaskOrderModel 任务订单信息
     * @return 返回操作成功信息
     */
    @RequestMapping("/taskOrder/createTaskOrder")
    public Result createTaskOrder(@RequestBody SimpleTaskOrderModel simpleTaskOrderModel){
//        long taskId = taskOrderModel.getTaskId();
//        long userId = taskOrderModel.getAcceptUserId();
//        TaskOrder taskOrder = new TaskOrder(taskId, userId);
//        taskOrderService.createTaskOrder(taskOrder);
//        return ResultUtils.success();

        TaskOrder taskOrder = new TaskOrder(simpleTaskOrderModel.getTaskId(), simpleTaskOrderModel.getAcceptUserId());
        taskOrderService.createTaskOrder(taskOrder);
        return ResultUtils.success();
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
        taskOrderService.deleteTaskOrder(taskOrderId);
        return ResultUtils.success();
    }

    /**
     * 获得用户提交的所有任务订单信息
     * @param simpleUserModel 工人ID的一层封装
     * @return 返回所有任务订单信息
     */
    @RequestMapping("/taskOrder/getAllSubmited")
    public Result getAllSubmitted(@RequestBody SimpleUserModel simpleUserModel){
//        long userId = userModel.getAcceptUserId();
//        List<TaskOrder> list = ReadFile.getAllSubmited(userId);
//        return ResultUtils.success(list);
        List<TaskOrder> list = taskOrderService.getAllSubmited(simpleUserModel.getAcceptUserId());
        return ResultUtils.success(list);
    }


}
