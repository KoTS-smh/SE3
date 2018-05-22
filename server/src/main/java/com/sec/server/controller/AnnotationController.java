package com.sec.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AnnotationController {
//    @Resource(name = "annotationService")
//    private AnnotationService annotationService;

    /**
     * 获得一个图片所有的标注信息
     * @param annotationId 标注ID
     * @return 所有图片标注信息
     */
    @RequestMapping("/annotation/getAll")
    public Result getAllAnnotation(long annotationId){
//        String annotationInfo = annotationService.getAnnotation(annotationId);
//        return ResultUtils.success(JSON.parse(annotationInfo));
        return null;
    }

    /**
     * 更改一个图片的标注信息
     * @param annotationInfo 图片标注信息
     */
    @RequestMapping("/annotation/update")
    public Result saveAnnotation(@RequestBody String annotationInfo){
//        JSONObject jsonObject = JSON.parseObject(annotationInfo);
//        annotationService.updateAnnotation(JSON.toJSONString(jsonObject.getJSONObject("annotationInfo")));
//        return ResultUtils.success();
        return null;
    }

}
