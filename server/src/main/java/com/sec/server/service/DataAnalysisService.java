package com.sec.server.service;

import com.sec.server.domain.Task;

import java.util.List;

public interface DataAnalysisService {

    double calculateMinimumMoneyOfTask(Task task);

    void modifyCurrency(double changeCurrency,long userId);

    void replaceWorker(long taskId, List<Long> replacedWorkerList);

}

