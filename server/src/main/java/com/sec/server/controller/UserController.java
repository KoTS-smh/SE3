package com.sec.server.controller;

import com.sec.server.domain.User;
import com.sec.server.exception.ResultException;
import com.sec.server.model.UserModel;
import com.sec.server.service.UserService;
import com.sec.server.utils.ReadFile;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    /**
     * 判断用户登陆情况
     * @param userModel 用户名和密码的一层封装
     * @return  如果用户名密码错误，会抛出exception由ExceptionHandler处理，如果登陆成功，会返回
     * 成功的消息和该用户具体信息（待实现）
     * @throws IOException
     */
    @RequestMapping("/user/login")
    public Result login(@RequestBody UserModel userModel) throws IOException{
        String inUsername = userModel.getUsername();
        String inPassword = userModel.getPassword();
        File file = new File("src/data/user.json");
        String content = FileUtils.readFileToString(file, "UTF-8");
        //JSONObject object = new JSONObject(content);
        JSONArray array = new JSONArray(content);
        for(int i = 0;i < array.length();i++){
            String username = array.getJSONObject(i).getString("username");
            String password = array.getJSONObject(i).getString("password");
            if(username.equals(inUsername)){
                User user = new User();
                return ResultUtils.success(user);
            }
        }

        //User user=userService.login(userModel.getUsername(),userModel.getPassword());
        //return ResultUtils.success(user);
        //用户名密码错误，抛出异常
        throw new ResultException("用户名或密码错误", 10001);
    }

    /**
     *
     * @param userModel
     * @return
     * @throws IOException
     */
    @RequestMapping("/user/register")
    public Result register(@RequestBody UserModel userModel) throws IOException{
        String inUsername = userModel.getUsername();
        String inPassword = userModel.getPassword();
        File file = new File("src/data/user.json");
        String content = FileUtils.readFileToString(file, "UTF-8");

        JSONArray array = new JSONArray(content);
        JSONObject object = new JSONObject();
        object.put("username", userModel.getUsername());
        object.put("password", userModel.getPassword());
        object.put("tel_number", userModel.getTel_number());
        array.put(object);

        FileUtils.write(file, array.toString());


        //userService.register();
        System.out.println(userModel.getUsername());
        System.out.println(userModel.getPassword());
        return ResultUtils.success();
    }

    @RequestMapping("/user/update")
    public Result update(@RequestBody UserModel userModel){
        return ResultUtils.success();
    }


    public static void main(String[] args){

    }
}
