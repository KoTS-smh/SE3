package com.sec.server.service;

import com.sec.server.domain.AnnotationInfo;

public interface AnnotationService {
    void updateAnnotation(String annotationInfo);

    String getAnnotation(long annotationId);

}
