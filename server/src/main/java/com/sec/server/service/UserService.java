package com.sec.server.service;

import com.sec.server.domain.User;

public interface UserService {
    //登录方法
    User login(String username,String password);
    //注册方法
    User register(User user);
    //更新用户信息
    void updateUser(User user);
    //注销用户
    void deleteUser(Long userId);
}
