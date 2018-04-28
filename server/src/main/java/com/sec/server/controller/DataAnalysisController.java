package com.sec.server.controller;

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
}
