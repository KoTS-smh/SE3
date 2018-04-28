package com.sec.server.service.impl;

import com.sec.server.domain.SystemAdministratorMessage;
import com.sec.server.domain.TaskRateMessage;
import com.sec.server.model.PersonalDataModel;
import com.sec.server.repository.DataAnalysisDao;
import com.sec.server.service.DataAnalysisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "dataAnalysisService")
public class DataAnalysisServiceImpl implements DataAnalysisService {
    @Resource(name = "dataAnalysisDao")
    private DataAnalysisDao dataAnalysisDao;

    @Override
    public List<TaskRateMessage> getTaskMessage(long taskId) {
        return dataAnalysisDao.getTaskMessage(taskId);
    }

    @Override
    public SystemAdministratorMessage getSystemMessage() {
        return dataAnalysisDao.getSystemMessage();
    }

    @Override
    public PersonalDataModel getPersonalData(long userId) {
        return dataAnalysisDao.getPersonalData(userId);
    }
}
