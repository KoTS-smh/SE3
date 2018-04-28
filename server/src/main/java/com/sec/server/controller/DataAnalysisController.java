package com.sec.server.controller;

import com.sec.server.domain.SystemAdministratorMessage;
import com.sec.server.domain.TaskRateMessage;
import com.sec.server.model.PersonalDataModel;
import com.sec.server.model.UserModel;
import com.sec.server.service.DataAnalysisService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DataAnalysisController {
    @Resource(name = "dataAnalysisService")
    private DataAnalysisService dataAnalysisService;

    /**
     * 获取工人积分信息
     * @param userId 众包工人ID
     * @return workerMessage 该工人的信息
     * 包括 point 积分 rank 排名
     */

    /**
     * 众包发起者获取任务的完成情况
     * @param taskId 任务ID
     * @return List<taskRateMessage> 任务完成信息
     * taskRateMessage 包括 acceptUserId 工人ID，acceptUserName 工人姓名, rate 工人进度
     */
    @RequestMapping("/getTaskMessage")
    public Result getTaskMessage(long taskId){
        List<TaskRateMessage> list = new ArrayList<>();
        list = dataAnalysisService.getTaskMessage(taskId);
        return ResultUtils.success(list);
    }

    /**
     * 系统管理员获得信息
     * @return systemAdministratorMessage 系统管理员信息
     * 包括 userNumber 用户数
     *      taskNumber 任务数
     *      finishedTaskNumber 已完成任务数
     *      unfinishedTaskNumber 未完成任务数
     */
    @RequestMapping("/getSystemMessage")
    public Result getSystemMessage(){
        SystemAdministratorMessage message = dataAnalysisService.getSystemMessage();
        return ResultUtils.success(message);
    }

    @RequestMapping("/analysis")
    public Result getAnalysisResult(@RequestBody UserModel userModel){
        return ResultUtils.success();
    }

    @RequestMapping("/personalData")
    public Result getPersonalData(@RequestBody UserModel userModel) {
        long userId = userModel.getUserId();
        PersonalDataModel personalDataModel = dataAnalysisService.getPersonalData(userId);
        return ResultUtils.success(personalDataModel);
    }

    @RequestMapping("/annotation/tags")
    public Result getTags(long taskId){
        return ResultUtils.success(dataAnalysisService.getAnnotationTag(taskId));
    }
}
