package com.sec.server.service;


import com.sec.server.domain.Annotation;

import java.util.HashMap;
import java.util.List;

public interface AnnotationService {
    void updateAnnotation(Annotation annotation);

    Annotation getAnnotation(long taskOrderId, int pictureNum);

    List<Annotation> getAnnotations(long taskOrderId);

    void deleteOne(long taskOrderId, int pictureNum);

    void deleteAll(long taskOrderId);

    HashMap<String,Integer> getTags(long taskId, int pictureNum);
}
