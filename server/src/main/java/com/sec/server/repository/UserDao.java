package com.sec.server.repository;

import com.sec.server.domain.User;

public interface UserDao {

    User login(String username,String password);

    User register(User user);

}
