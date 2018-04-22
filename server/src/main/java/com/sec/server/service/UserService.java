package com.sec.server.service;

import com.sec.server.domain.User;
import com.sec.server.model.UserModel;

public interface UserService {
    //登录方法
    User login(String username,String password);
    //注册方法
    void register(UserModel userModel);
    //更新用户信息
    void updateUser(User user);
    //注销用户
    void deleteUser(Long userId);
}
