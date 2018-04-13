package com.sec.server.controller;

import com.sec.server.domain.User;
import com.sec.server.model.UserModel;
import com.sec.server.service.UserService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping("/user/login")
    public Result login(@RequestBody UserModel userModel){
        User user=userService.login(userModel.getUsername(),userModel.getPassword());
        return ResultUtils.success(user);
    }

    @RequestMapping("/user/register")
    public Result register(@RequestBody UserModel userModel){

        //userService.register();
        return ResultUtils.success();
    }

    @RequestMapping("/user/update")
    public Result update(@RequestBody UserModel userModel){
        return ResultUtils.success();
    }
}
