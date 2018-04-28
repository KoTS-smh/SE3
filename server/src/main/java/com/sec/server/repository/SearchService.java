package com.sec.server.repository;

import com.sec.server.domain.Task;
import com.sec.server.domain.User;

import java.util.List;

public interface SearchService {

    List<Task> searchTask(String message);

    List<User> searchUser(String message);

}
