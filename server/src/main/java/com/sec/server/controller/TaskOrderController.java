package com.sec.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.ResultCode;
import com.sec.server.model.*;
import com.sec.server.service.AppointService;
import com.sec.server.service.TaskOrderService;
import com.sec.server.service.TaskService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TaskOrderController {
    @Resource(name = "taskOrderService")
    private TaskOrderService taskOrderService;

    @Resource(name = "taskService")
    private TaskService taskService;

    @Resource(name = "appointService")
    private AppointService appointService;


    /**
     * 获取一个任务订单
     * @param taskOrderId 任务订单ID
     * @return 任务订单详细信息
     */
    @RequestMapping("/taskOrder/orderInfo")
    public Result getTaskOrder(long taskOrderId){
        TaskOrder taskOrder = taskOrderService.getTaskOrderById(taskOrderId);
        return ResultUtils.success(taskOrder);
    }

    /**
     * 获取工人所有的任务订单
     * @param simpleUserModel 工人信息的一层封装
     * @return 所有的任务订单
     * @describe
     */
    @RequestMapping("/taskOrder/getAll")
    public Result getAllTaskOrder(@RequestBody SimpleUserModel simpleUserModel) {
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
        System.out.println(simpleTaskOrderModel.getAcceptUserId());
        //获取任务信息
        TaskModel task  = taskService.getTask(simpleTaskOrderModel.getTaskId());
        //判断任务状态
        switch (task.getState()){
            //任务处在预约期就预约任务
            case appoint:
                appointService.appointTask(simpleTaskOrderModel.getTaskId(),simpleTaskOrderModel.getAcceptUserId());
                return ResultUtils.success();
            //任务处于进行期
            case ongoing:
                //判断接取人数是否已经足够
                if(task.getMaxParticipator()==taskService.getWorkerNumber(simpleTaskOrderModel.getTaskId())){
                    //加入等待列表
                    appointService.appointTask(simpleTaskOrderModel.getTaskId(),simpleTaskOrderModel.getAcceptUserId());
                    return ResultUtils.success();
                }else{
                    //创建任务
                    TaskOrder taskOrder = new TaskOrder(simpleTaskOrderModel.getTaskId(), simpleTaskOrderModel.getAcceptUserId());
                    taskOrderService.createTaskOrder(taskOrder);
                    return ResultUtils.success();
                }
        }

        return ResultUtils.error(ResultCode.NO_IMAGE_ERROR);
    }

    /**
     * 更新一个任务订单信息
     * @param taskOrder 新的任务订单信息
     * @return 返回操作成功信息
     */
    @RequestMapping("/taskOrder/update")
    public Result updateTaskOrder(@RequestBody TaskOrder taskOrder){
        taskOrderService.updateTaskOrder(taskOrder);
        return ResultUtils.success();
    }

    /**
     * 删除一个任务订单信息
     * @param taskOrderId 任务订单ID
     * @return 返回操作成功信息
     */
    @RequestMapping("/taskOrder/delete")
    public Result deleteTaskOrder(long taskOrderId){
        taskOrderService.deleteTaskOrder(taskOrderId);
        return ResultUtils.success();
    }

    /**
     * 获得用户所有的任务订单信息
     * @param simpleUserModel 工人ID的一层封装
     * @return 返回所有任务订单信息
     * @describe 包括预约中和等待中的
     */
    @RequestMapping("/taskOrder/getAllSubmited")
    public Result getAllSubmitted(@RequestBody SimpleUserModel simpleUserModel){
        //获取所有的
        List<TaskOrder> list = taskOrderService.getAllSubmited(simpleUserModel.getAcceptUserId());
        return ResultUtils.success(list);
    }

    @RequestMapping("/taskOrder/appoint")
    public Result appointTask(long taskId,long userId){
        taskOrderService.appointTask(taskId,userId);
        return ResultUtils.success();
    }

    @RequestMapping("/taskOrder/getAppoint")
    public Result getAppointUser(long taskId){
        return ResultUtils.success(appointService.getAppointUser(taskId));
    }
}
