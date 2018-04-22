package com.sec.server.repository.impl;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.User;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.UserDao;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
    @Override
    public User login(String username, String password) {
        File file = new File("src/data/user.json");
        String content = null;
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);
            for(int i = 0;i < array.length();i++){
                String thisUsername = array.getJSONObject(i).getString("username");
                if(username.equals(thisUsername)){
                    String thisPassword = array.getJSONObject(i).getString("password");
                    if(password.equals(thisPassword)){
                        String temp = array.get(i).toString();
                        return JSON.parseObject(temp,User.class);
                    }else{
                        throw new ResultException(ResultCode.PASSWORD_ERROR);
                    }
                }
            }
        } catch (IOException e) {
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
        throw new ResultException(ResultCode.UNKNOWN_USER_ERROR);
    }

    @Override
    public void register(User user){
        File file = new File("src/data/user.json");

        String content = null;
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new ResultException(ResultCode.WEAK_NET_WORK);
        }


        JSONArray array = new JSONArray(content);
        JSONObject object = new JSONObject();
        object.put("username", user.getUsername());
        object.put("password", user.getPassword());
        object.put("userLevel", user.getUserLevel());
        object.put("point", user.getPoint());

        if(array.length() > 0){
            JSONObject lastObject = array.optJSONObject(array.length() - 1);
            int lastid = lastObject.getInt("userId");
            object.put("userId", lastid + 1);
        }else
            object.put("userId", 0);

        array.put(object);

        try {
            FileUtils.write(file, array.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
            //todo
            //需要定义一种新的ResultCode
            throw  new ResultException(ResultCode.WEAK_NET_WORK);


        }


    }
}
