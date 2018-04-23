package com.sec.server.repository;

import com.sec.server.domain.User;

import java.util.List;

public interface DataAnalysisDao {
    List getAnalysisResult(String username);

    List<User> getParticipant(long taskId);

    int getTotalAmount(String path);
}
