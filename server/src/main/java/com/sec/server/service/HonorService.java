package com.sec.server.service;

import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.AnnotationType;

import java.util.List;

/**
 *
 * 计算荣誉头衔
 *
 */
public interface HonorService {

    //标框标注
    void honorOfFrameTag(long userId);

    //分类标注
    void honorOfClassifyTag(long userId);

    //整体标注
    void honorOfWholeTag(long userId);

    //区域标注
    void honorRegionTag(long userId);

    //总荣誉
    void honorTotal(long userId);

    //计算积分
    double calculateTypePoint(long userId,AnnotationType annotationType);

    //创建荣誉信息
    void createHonorMessage(long userId);

    double getFramePoint(long userId);

    double getClassifyPoint(long userId);

    double getWholePoint(long userId);

    double getRegionPoint(long userId);
}
