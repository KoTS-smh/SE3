package com.sec.server.repository;

import com.sec.server.domain.Task;
import com.sec.server.domain.User;

import java.util.List;

public interface SearchDao {
    List<Task> searchTask(String message);

    List<User> searchUser(String message);
}
