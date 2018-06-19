package com.sec.server.service;

public interface EvaluateService {

    /**
     * 计算评分
     * @param taskOrderId 任务Id
     * @return 评分
     * @describe 每天十二点要调用一次
     */
    void evaluateAnnotation(long taskOrderId);


    /**
     * 计算任务质量
     * @param taskId 任务Id
     */
    void evaluateTaskQuality(long taskId);

    void evaluateAnnotation();
}
