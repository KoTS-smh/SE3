package com.sec.server.controller;

import com.sec.server.model.UserModel;
import com.sec.server.utils.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataAnalysisController {
//    @Resource(name = "dataAnalysisService")
//    private DataAnalysisService dataAnalysisService;

    /**
     * 众包发起者获取任务的完成情况
     * @param taskId 任务ID
     * @return List<taskRateMessage> 任务完成信息
     * taskRateMessage 包括 acceptUserId 工人ID，acceptUserName 工人姓名, rate 工人进度
     */
    @RequestMapping("/getTaskMessage")
    public Result getTaskMessage(long taskId){

//        List<TaskRateMessage> list = new ArrayList<>();
//        list = dataAnalysisService.getTaskMessage(taskId);
//        System.out.println(list.size());
//        return ResultUtils.success(list);
        return null;
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
//        SystemAdministratorMessage message = dataAnalysisService.getSystemMessage();
//        return ResultUtils.success(message);
        return null;
    }

    /**
     * 获取用户相关统计数据
     * @param userModel 用户信息
     * @return 用户统计数据
     */
    @RequestMapping("/personalData")
    public Result getPersonalData(@RequestBody UserModel userModel) {
//        long userId = userModel.getAcceptUserId();
//        PersonalDataModel personalDataModel = dataAnalysisService.getPersonalData(userId);
//        return ResultUtils.success(personalDataModel);
        return null;
    }

}
