package com.sec.server.service.serviceImpl;

import com.sec.server.domain.HonorMessage;
import com.sec.server.domain.Message;
import com.sec.server.repository.MessageDao;
import com.sec.server.repository.UserDao;
import com.sec.server.domain.User;
import com.sec.server.enums.ResultCode;
import com.sec.server.enums.UserLevel;
import com.sec.server.exception.ResultException;
import com.sec.server.model.UserModel;
import com.sec.server.service.HonorService;
import com.sec.server.service.MessageService;
import com.sec.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private static List<Long> loginUsers = new ArrayList<Long>();

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    private HonorService honorService;

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 用户 user
     */
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

    /**
     * 注册新用户
     * @param userModel 用户信息
     */
    @Override
    public void register(UserModel userModel) {
        User user = new User(userModel);
        user.setUserLevel(UserLevel.LEVEL1);
        user.setPoint(0);
        user.setBalance(0);

        userDao.insertUser(user);
            //获得刚刚新建的用户
            List<User> list = userDao.getAllUsers();
            //新建荣誉信息
            honorService.createHonorMessage(user.getUserId());
            //提示完善个人信息
        Message message = new Message(user.getUserId(), "请您早日完善个人信息", "提示通知");
        message.setRead(false);
        messageDao.insertMessage(message);

    }

    /**
     * 通过Id获取用户信息
     * @param userId 用户Id
     * @return 用户信息 user
     */
    @Override
    public User getUserById(long userId) {
        User user = userDao.getUserById(userId);
        if(user == null) {
            throw new ResultException(ResultCode.UNKNOWN_USER_ERROR);
        }
        return user;
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return  ？
     */
    @Override
    public User updateUser(User user) {
        userDao.updateUser(user);
        return userDao.getUserById(user.getUserId());
    }

    @Override
    public void deleteUser(long userId) {
        User user = userDao.getUserById(userId);
        if(user == null) {
            throw new ResultException(ResultCode.UNKNOWN_USER_ERROR);
        }
        userDao.deleteUser(userId);
    }

    /**
     * 用户登出
     * @param userId 用户Id
     */
    @Override
    public void logout(long userId) {
        if(! loginUsers.contains(userId)) {
            throw new ResultException("用户尚未登陆", 12550);
        }
        loginUsers.remove(userId);
    }

    @Override
    public void recharge(int num, long userId) {
        userDao.recharge(num, userId);
    }

    @Override
    public double getBalance(long userId) {
        return userDao.getBalance(userId);
    }
}
