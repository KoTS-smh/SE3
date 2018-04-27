package com.sec.server.controller;

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

    //获取工人积分信息
    public long getPoint(long userId){
        return 0;
    }

    /**
     * 获取工人积分信息
     * @param userId 众包工人ID
     * @return point 积分信息
     */

    /**
     * 获取工人群体排名信息
     * @param userId 众包工人ID
     * @return rank 排名信息
     */

    /**
     * 众包发起者获取任务的完成情况
     * @param taskId 任务ID
     * @return List<taskRateMessage> 所有的任务完成信息
     * 其中 taskRateMessage 包括 acceptUserId 工人ID，acceptUserName 工人姓名, rate 工人进度
     */

    /**
     * 系统管理员获得当前所有用户数目
     * @return userNumber 所有用户数目
     */

    /**
     * 系统管理员获得当前所有任务信息
     * @return List<taskMessage> 所有的任务基本信息
     * 其中 taskMessage 包括 taskId 任务Id, taskName 任务名称, taskState 任务完成状态
     */

    @RequestMapping("/analysis")
    public Result getAnalysisResult(@RequestBody UserModel userModel){
        return ResultUtils.success();
    }
}
