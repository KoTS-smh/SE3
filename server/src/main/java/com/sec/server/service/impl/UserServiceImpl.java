package com.sec.server.service.impl;

import com.sec.server.domain.User;
import com.sec.server.service.UserService;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Override
    public User login(String username, String password) {
        return null;
    }

    @Override
    public User register(User user) {
        return null;
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(Long userId) {

    }
}
