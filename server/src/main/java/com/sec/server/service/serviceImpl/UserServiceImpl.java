package com.sec.server.service.serviceImpl;

import com.sec.server.dao.UserDao;
import com.sec.server.domain.User;
import com.sec.server.enums.ResultCode;
import com.sec.server.enums.UserLevel;
import com.sec.server.exception.ResultException;
import com.sec.server.model.UserModel;
import com.sec.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private static List<Long> loginUsers = new ArrayList<Long>();

    @Autowired
    private UserDao userDao;

    @Override
    public User login(String username, String password) {
        User user = userDao.getUser(username);
        if(user == null ){
            throw new ResultException(ResultCode.UNKNOWN_USER_ERROR);
        }

        if(user.getPassword().equals(password)){
            if(loginUsers.contains(user.getUserId())) {
                System.out.println("already login");
                throw new ResultException("用户已登陆",12450);
            }else {
                loginUsers.add(user.getUserId());
                return user;
            }

        }else {
            throw new ResultException(ResultCode.PASSWORD_ERROR);
        }
    }

    @Override
    public void register(UserModel userModel) {
        User user = new User(userModel);
        user.setUserLevel(UserLevel.LEVEL1);
        user.setPoint(0);
        user.setBalance(0);
        try {
            userDao.insert(user);
        }catch (Exception e){
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public User getUserById(long userId) {
        User user = userDao.getUserById(userId);
        if(user == null) {
            throw new ResultException(ResultCode.UNKNOWN_USER_ERROR);
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        userDao.updateUser(user);
        User outUser = userDao.getUserById(user.getUserId());
        return outUser;
    }

    @Override
    public void deleteUser(long userId) {
        User user = userDao.getUserById(userId);
        if(user == null) {
            throw new ResultException(ResultCode.UNKNOWN_USER_ERROR);
        }
        userDao.deleteUser(userId);
    }

    @Override
    public void logout(long userId) {
        if(! loginUsers.contains(userId)) {
            throw new ResultException("用户尚未登陆", 12550);
        }
        loginUsers.remove(userId);
    }
}
