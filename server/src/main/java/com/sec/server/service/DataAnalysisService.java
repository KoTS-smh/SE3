package com.sec.server.service;

import com.sec.server.domain.User;

import java.util.HashMap;
import java.util.List;

import com.sec.server.domain.SystemAdministratorMessage;

import java.util.List;

import com.sec.server.domain.TaskRateMessage;
import com.sec.server.model.PersonalDataModel;

public interface DataAnalysisService {
//    List getAnalysisResult(String username);
//
//    List<User> getParticipant(long taskId);
//
//    int getTotalAmount(String path);

    HashMap<Integer,HashMap<String,Integer>> getAnnotationTag(long taskId);

    List<TaskRateMessage> getTaskMessage(long taskId);

    SystemAdministratorMessage getSystemMessage();

    PersonalDataModel getPersonalData(long userId);
}

