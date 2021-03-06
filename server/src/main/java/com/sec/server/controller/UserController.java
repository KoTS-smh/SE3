package com.sec.server.controller;

import com.sec.server.repository.MessageDao;
import com.sec.server.domain.Message;
import com.sec.server.domain.User;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.model.MessageModel;
import com.sec.server.model.UserModel;
import com.sec.server.service.UserService;
import com.sec.server.utils.CreateToken;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @Autowired
    private MessageDao messageDao;

    /**
     * 判断用户登陆情况
     * @param userModel 用户名和密码的一层封装
     * @return  如果用户名密码错误，会抛出exception由ExceptionHandler处理，如果登陆成功，会返回
     * 成功的消息和该用户具体信息（待实现）
     */
    @RequestMapping("/user/login")
    public Result login(@RequestBody UserModel userModel) {
        String username = userModel.getUsername();
        String password = userModel.getPassword();
        User user = userService.login(username, password);

        if(user != null) {
            return ResultUtils.success(user);
        }else{
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 用户注册使用方法
     * @param userModel 用户名和密码的一层封装
     * @return 如果已经用户名已经被注册过，会抛出异常
     */
    @RequestMapping("/user/register")
    public Result register(@RequestBody UserModel userModel){
        boolean result = userService.register(userModel);
        if(result)
            return ResultUtils.success();
        else
            return ResultUtils.error(ResultCode.USERNAME_EXIST_ERROR);
    }

    /**
     * 获取一个用户
     * @param userId 用户ID
     * @return 返回用户详细信息
     */
    @RequestMapping("/user/getUser")
    public Result getUser(long userId){
        User user = userService.getUserById(userId);
        return ResultUtils.success(user);
    }

    /**
     * 更新用户信息
     * @param userModel 新的用户信息
     * @return 操作成功信息
     */
    @RequestMapping("/user/update")
    public Result update(@RequestBody UserModel userModel){
        User user = new User(userModel);
        User outUser = userService.updateUser(user);
        return ResultUtils.success(outUser);
    }

    /**
     * 删除用户信息
     * @param userId 用户ID
     * @return 操作成功信息
     */
    @RequestMapping("/user/delete")
    public Result delete(long userId){
//        userService.deleteUser(userId);
//        return ResultUtils.success();
        userService.deleteUser(userId);
        return ResultUtils.success();
    }

    @RequestMapping("/user/getToken")
    public Result getToken() {
        String bucket = "mrgs-bucket";
        String accessKey = "j0dwMMGFcKPhncC7vb_PWXshbpiSMEWB69NiKhn4";
        String secretKey = "2vWVIw3WJfk314YN3e24ZnixdJMbyoZ14FXiqF--";
        String token = CreateToken.uploadToken(bucket, accessKey, secretKey);
        return ResultUtils.success(token);
    }

    @RequestMapping("/user/logout")
    public Result logout(long userId){
//        System.out.println("here");
//        userService.logout(username);
//        return ResultUtils.success();
        userService.logout(userId);
        return ResultUtils.success();
    }

    @RequestMapping("/user/getMessageNum")
    public Result getMessageNum(@RequestBody UserModel userModel) {
        long userId = userModel.getUserId();
        List<Message> messages = messageDao.getMessages(userId);

        return ResultUtils.success(messages.size());
    }

    @RequestMapping("/user/getMessage")
    public Result getMessage(@RequestBody UserModel userModel) {
        long userId = userModel.getUserId();
        List<Message> messages = messageDao.getMessages(userId);
        System.out.println(messages.size());
        return ResultUtils.success(messages);
    }

    @RequestMapping("/user/setRead")
    public Result setRead(@RequestBody MessageModel messageModel) {
        long messageId = messageModel.getMessageId();
        messageDao.setAsReaded(messageId);
        return ResultUtils.success();
    }

    @RequestMapping("/user/deleteMessage")
    public Result deleteMessage(@RequestBody MessageModel messageModel) {
        long messageId = messageModel.getMessageId();
        messageDao.deleteMessage(messageId);
        return ResultUtils.success();
    }

    @RequestMapping("/user/recharge")
    public Result recharge(@RequestBody UserModel userModel) {
        long userId = userModel.getUserId();
        int rechargeNum = userModel.getRechargeNum();
        userService.recharge(rechargeNum, userId);
        return ResultUtils.success();
    }

    @RequestMapping("/user/getBalance")
    public Result getBalance(@RequestBody UserModel userModel) {
        long userId = userModel.getUserId();
        double balance = userService.getBalance(userId);
        return ResultUtils.success(balance);
    }

}