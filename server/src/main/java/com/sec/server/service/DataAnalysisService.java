package com.sec.server.service;

import com.sec.server.model.PersonalDataModel;

public interface DataAnalysisService {
    PersonalDataModel getPersonalData(long userId);
}
