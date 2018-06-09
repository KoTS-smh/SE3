package com.sec.server.service;

public interface EvaluateService {

    double evaluateAnnotation(long taskOrderId);

    double evaluateAnnotationWithStandard(long taskOrderId, double points, long standardId);

    void evaluateTaskQuality(long taskId);

    double evaluateTaskRewards(long taskId);

}
