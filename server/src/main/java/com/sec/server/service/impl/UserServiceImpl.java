package com.sec.server.service.impl;

import com.sec.server.domain.User;
import com.sec.server.enums.UserLevel;
import com.sec.server.model.UserModel;
import com.sec.server.repository.UserDao;
import com.sec.server.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Resource(name = "userDao")
    private UserDao userDao;
    @Override
    public User login(String username, String password) {
        return userDao.login(username,password);
    }

    @Override
    public User register(UserModel userModel) {
        User user=new User();
        user.setPassword(userModel.getPassword());
        user.setPoint(0);
        user.setUsername(userModel.getUsername());
        user.setUserLevel(UserLevel.LEVEL1);
        return userDao.register(user);
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(Long userId) {

    }
}
