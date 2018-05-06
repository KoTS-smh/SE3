package com.sec.server.repository;

import com.sec.server.domain.User;

public interface UserDao {

    User login(String username,String password);

    void register(User user);

    void deleteUser(long userId);

    User update(User user);

    User getUserById(long ID);

    void logout(String username);
}
