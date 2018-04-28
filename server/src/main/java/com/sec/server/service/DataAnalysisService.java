package com.sec.server.service;

import com.sec.server.domain.SystemAdministratorMessage;

import java.util.List;

import com.sec.server.domain.TaskRateMessage;
import com.sec.server.model.PersonalDataModel;

public interface DataAnalysisService {

    List<TaskRateMessage> getTaskMessage(long taskId);

    SystemAdministratorMessage getSystemMessage();

    PersonalDataModel getPersonalData(long userId);
}
