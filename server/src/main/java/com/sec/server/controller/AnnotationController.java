package com.sec.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.domain.Annotation;
import com.sec.server.domain.AnnotationInfo;
import com.sec.server.domain.Coordinate;
import com.sec.server.service.AnnotationService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class AnnotationController {
    @Resource(name = "annotationService")
    private AnnotationService annotationService;

    @RequestMapping("annotation/getAll")
    public Result getAllAnnotation(long annotationId){
        AnnotationInfo annotationInfo=new AnnotationInfo();
        annotationInfo.setAnnotationId(544);
        HashMap<Integer,List<Annotation>> map = new HashMap<>();
        List<Annotation> list=new ArrayList<>();
        Annotation annotation=new Annotation();
        annotation.setSentence("dadsads");
        List<String> strings = new ArrayList<>(){};
        strings.add("aa");
        strings.add("adsdas");
        annotation.setWords(strings);
        List<Coordinate> coordinates =new ArrayList<>();
        coordinates.add(new Coordinate(0,0));
        coordinates.add(new Coordinate(100,100));
        annotation.setCoordinates(coordinates);
        list.add(annotation);
        map.put(1,list);
        annotationInfo.setAnnotationMap(map);
        return ResultUtils.success(annotationInfo);
    }

    @RequestMapping("annotation/update")
    public Result saveAnnotation(@RequestBody String annotationInfo){
        JSONObject jsonObject = JSON.parseObject(annotationInfo);
        annotationService.updateAnnotation(JSON.toJSONString(jsonObject.getJSONObject("annotationInfo")));
        return ResultUtils.success();
    }

}
