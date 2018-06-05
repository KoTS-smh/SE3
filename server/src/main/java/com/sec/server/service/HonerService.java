package com.sec.server.service;

/**
 *
 * 计算荣誉头衔
 *
 */
public interface HonerService {

    //标框标注
    void honerOfFrameTag(long userId);

    //分类标注
    void honerOfClassifyTag(long userId);

    //整体标注
    void honerOfWholeTag(long userId);

    //区域标注
    void honerRegionTag(long userId);

    //总荣誉
    void honerTotal(long userId);


}
