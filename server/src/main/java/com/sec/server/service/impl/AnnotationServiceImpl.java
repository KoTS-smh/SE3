package com.sec.server.service.impl;

import com.sec.server.repository.AnnotationDao;
import com.sec.server.service.AnnotationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("annotationService")
public class AnnotationServiceImpl implements AnnotationService {
    @Resource(name = "annotationDao")
    private AnnotationDao annotationDao;

    @Override
    public void updateAnnotation(String annotationInfo) {
        annotationDao.updateAnnotation(annotationInfo);
    }

    @Override
    public String getAnnotation(long annotationId) {
        return annotationDao.getAnnotation(annotationId);
    }
}
