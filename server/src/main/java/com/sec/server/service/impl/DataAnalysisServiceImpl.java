package com.sec.server.service.impl;

import com.sec.server.model.PersonalDataModel;
import com.sec.server.repository.DataAnalysisDao;
import com.sec.server.service.DataAnalysisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "dataAnalysisService")
public class DataAnalysisServiceImpl implements DataAnalysisService {
    @Resource(name = "dataAnalysisDao")
    private DataAnalysisDao dataAnalysisDao;

    @Override
    public PersonalDataModel getPersonalData(long userId) {
        return dataAnalysisDao.getPersonalData(userId);
    }
}
