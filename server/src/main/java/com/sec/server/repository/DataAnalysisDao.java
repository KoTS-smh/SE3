package com.sec.server.repository;

import com.sec.server.domain.User;
import com.sec.server.model.PersonalDataModel;

import java.util.List;

public interface DataAnalysisDao {
    List getAnalysisResult(String username);

    List<User> getParticipant(long taskId);

    int getTotalAmount(String path);

    PersonalDataModel getPersonalData(long userId);
}
