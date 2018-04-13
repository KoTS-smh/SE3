package com.sec.server.domain;

import java.util.HashMap;
import java.util.List;

public class AnnotationInfo {
    private long annotationId;
    private HashMap<Integer,List<Annotation>> annotationMap;

    public long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(long annotationId) {
        this.annotationId = annotationId;
    }

    public HashMap<Integer, List<Annotation>> getAnnotationMap() {
        return annotationMap;
    }

    public void setAnnotationMap(HashMap<Integer, List<Annotation>> annotationMap) {
        this.annotationMap = annotationMap;
    }
}
