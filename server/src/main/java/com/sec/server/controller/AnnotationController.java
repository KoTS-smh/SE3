package com.sec.server.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.domain.Annotation;
import com.sec.server.exception.ResultException;
import com.sec.server.model.AnnotationModel;
import com.sec.server.model.Coordinate;
import com.sec.server.service.AnnotationService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AnnotationController {
    @Resource(name = "annotationService")
    private AnnotationService annotationService;

    /**
     * 获得一个图片所有的标注信息
     * @return 图片标注信息
     */
    @RequestMapping("/annotation/get")
    public Result getAllAnnotation(long taskOrderId, int pictureNum){
        Annotation annotation = annotationService.getAnnotation(taskOrderId,pictureNum);
        if(annotation == null){
            throw new ResultException("还未标注！",122);
        }else {
            String tmp = annotation.getCoordinates();
            List<Coordinate> coordinates = JSONArray.parseArray(tmp,Coordinate.class);
            AnnotationModel annotationModel = new AnnotationModel();
            annotationModel.setSentence(annotation.getSentence());
            annotationModel.setCoordinates(coordinates);
            annotationModel.setWords(annotation.getWords());
            return ResultUtils.success(annotationModel);
        }
}

    /**
     * 更改一个图片的标注信息
     * @param annotation 图片标注信息
     */
    @RequestMapping("/annotation/update")
    public Result saveAnnotation(@RequestBody Annotation annotation){
        annotationService.updateAnnotation(annotation);
        return ResultUtils.success();
    }

    @RequestMapping("/annotation/delete")
    public Result deleteAnnotation(long taskOrderId,int pictureNum){
        annotationService.deleteAnnotation(taskOrderId,pictureNum);
        return ResultUtils.success();
    }

    /**
     * 获取一个任务中每张图片被标注的tag信息
     * @return 一个任务每张图片被标注的tag信息
     */
    @RequestMapping("/annotation/tags")
    public Result getTags(long taskId, int pictureNum){
        return ResultUtils.success(annotationService.getTags(taskId,pictureNum));
    }

}
