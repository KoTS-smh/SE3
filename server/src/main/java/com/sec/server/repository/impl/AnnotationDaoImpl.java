package com.sec.server.repository.impl;

import com.sec.server.repository.AnnotationDao;
import org.springframework.stereotype.Repository;

@Repository("annotationDao")
public class AnnotationDaoImpl implements AnnotationDao {
    @Override
    public void updateAnnotation(String annotationInfo) {

    }

    @Override
    public String getAnnotation(long annotationId) {
        return null;
    }
}
