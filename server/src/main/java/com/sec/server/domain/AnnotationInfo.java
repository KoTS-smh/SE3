package com.sec.server.domain;

import java.util.HashMap;
import java.util.List;

public class AnnotationInfo {
    private long annotationId;
    private HashMap<String,List<Annotation>> annotationMap;

    public long getAnnotationId() {
        return annotationId;
    }

    public void setAnnotationId(long annotationId) {
        this.annotationId = annotationId;
    }

    public HashMap<String, List<Annotation>> getAnnotationMap() {
        return annotationMap;
    }

    public void setAnnotationMap(HashMap<String, List<Annotation>> annotationMap) {
        this.annotationMap = annotationMap;
    }
}
