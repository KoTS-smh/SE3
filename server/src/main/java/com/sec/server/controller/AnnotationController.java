package com.sec.server.controller;

import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnnotationController {

    @RequestMapping("annotation/getAll")
    public Result getAllAnnotation(long annotationId){
        // todo
        return ResultUtils.success();
    }

    public Result saveAnnotation(String annotationInfo){
        return ResultUtils.success();
    }


}
