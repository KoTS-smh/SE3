package com.sec.server.repository;

public interface AnnotationDao {
    void updateAnnotation(String annotationInfo);

    String getAnnotation(long annotationId);

}
