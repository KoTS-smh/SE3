package com.sec.server.service.impl;

import com.sec.server.domain.User;
import com.sec.server.enums.Education;
import com.sec.server.enums.Sex;
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
    public void register(UserModel userModel) {
        User user=new User();
        user.setPassword(userModel.getPassword());
        user.setPoint(0);
        user.setUsername(userModel.getUsername());
        user.setUserLevel(UserLevel.LEVEL1);
        user.setDescription("");
        user.setTel_phone("");
        user.setSex(Sex.Man);
        user.setEducation(Education.undergraduate);
        user.setEmail("");

        System.out.println("Service" + user.getUsername());
        userDao.register(user);

    }

    @Override
    public User updateUser(UserModel userModel) {
        User user = new User();
        user.setUserId(userModel.getUserId());
        user.setDescription(userModel.getDescription());
        user.setEducation(userModel.getEducation());
        user.setEmail(userModel.getEmail());
        user.setSex(userModel.getSex());
        user.setTel_phone(userModel.getTel_phone());
        user.setUsername(userModel.getUsername());
        user.setPoint(userModel.getPoint());
        user.setUserLevel(userModel.getUserLevel());
//        System.out.println(userModel.getEmail());
        return userDao.upate(user);
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public User get(Long userId) {
        return userDao.getUserById(userId);
    }
}
