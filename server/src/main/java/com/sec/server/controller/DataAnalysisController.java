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
     * 众包发起者获取任务的完成情况
     * @param taskId 任务ID
     * @return List<taskRateMessage> 任务完成信息
     * taskRateMessage 包括 acceptUserId 工人ID，acceptUserName 工人姓名, rate 工人进度
     */
    @RequestMapping("/getTaskMessage")
    public Result getTaskMessage(long taskId){
        List<TaskRateMessage> list = new ArrayList<>();
        //todo
//        list = dataAnalysisService.getTaskMessage(taskId);

        TaskRateMessage t1 = new TaskRateMessage();
        t1.setRate(0.9);
        t1.setAcceptUserId(0);
        t1.setAcceptUserName("wrs000");
        t1.setSubmitted(false);
        t1.setState("未完成");

        TaskRateMessage t2 = new TaskRateMessage();
        t2.setRate(0.8);
        t2.setAcceptUserId(1);
        t2.setAcceptUserName("wrs111");
        t2.setSubmitted(false);
        t2.setState("未完成");

        TaskRateMessage t3 = new TaskRateMessage();
        t3.setRate(1);
        t3.setAcceptUserId(2);
        t3.setAcceptUserName("wrs222");
        t3.setSubmitted(false);
        t3.setState("待提交");

        TaskRateMessage t4 = new TaskRateMessage();
        t4.setSubmitted(true);
        t4.setAcceptUserName("wrs333");
        t4.setAcceptUserId(3);
        t4.setRate(1);
        t4.setState("待评审");

        list.add(t1);
        list.add(t2);
        list.add(t3);
        list.add(t4);

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

    /**
     * 获取用户相关统计数据
     * @param userModel 用户信息
     * @return 用户统计数据
     */
    @RequestMapping("/personalData")
    public Result getPersonalData(@RequestBody UserModel userModel) {
        long userId = userModel.getUserId();
        PersonalDataModel personalDataModel = dataAnalysisService.getPersonalData(userId);
        return ResultUtils.success(personalDataModel);
    }

    /**
     * 获取一个任务中每张图片被标注的tag信息
     * @param taskId 任务ID
     * @return 一个任务每张图片被标注的tag信息
     */
    @RequestMapping("/annotation/tags")
    public Result getTags(long taskId){
        return ResultUtils.success(dataAnalysisService.getAnnotationTag(taskId));
    }
}
