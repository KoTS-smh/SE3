package com.sec.server.controller;

import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskOrderController {

    @RequestMapping("/taskOrder/orderInfo")
    public Result getAllTaskOrder(long taskOrderId){
        return ResultUtils.success();
    }
}
