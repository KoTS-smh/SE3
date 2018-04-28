package com.sec.server.controller;

import com.sec.server.domain.User;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.model.UserModel;
import com.sec.server.service.UserService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    /**
     * 判断用户登陆情况
     * @param userModel 用户名和密码的一层封装
     * @return  如果用户名密码错误，会抛出exception由ExceptionHandler处理，如果登陆成功，会返回
     * 成功的消息和该用户具体信息（待实现）
     */
    @RequestMapping("/user/login")
    public Result login(@RequestBody UserModel userModel) {
        User user = userService.login(userModel.getUsername(),userModel.getPassword());
        System.out.println("the userId " + userModel.getUserId());
        return ResultUtils.success(user);
    }

    @RequestMapping("/user/register")
    public Result register(@RequestBody UserModel userModel) throws IOException{
        try {
            System.out.println("controller" + userModel.getUsername());
            userService.register(userModel);
        }catch (Exception e){
            throw new ResultException(ResultCode.UNKNOWN_USER_ERROR);
        }
        return ResultUtils.success();
    }

    @RequestMapping("/user/getUser")
    public Result getUser(long userId){
        System.out.println(userId);
        User user = userService.get(userId);
        System.out.print(user.getUserId());
        return ResultUtils.success(user);
    }
    @RequestMapping("/user/update")
    public Result update(@RequestBody UserModel userModel){
        User user = userService.updateUser(userModel);
        return ResultUtils.success(user);
    }

    @RequestMapping("/user/delete")
    public Result delete(long userId){
        userService.deleteUser(userId);
        return ResultUtils.success();
    }

    @RequestMapping("/user/getById")
    public Result getCommonUserInfo(long userId){
        User user=userService.get(userId);
        return ResultUtils.success(user);
    }
}
