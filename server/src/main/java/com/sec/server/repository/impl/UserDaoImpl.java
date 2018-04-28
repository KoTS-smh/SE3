package com.sec.server.repository.impl;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.User;
import com.sec.server.enums.Education;
import com.sec.server.enums.ResultCode;
import com.sec.server.enums.Sex;
import com.sec.server.enums.UserLevel;
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

//    ios todo
//    private String pathname = "src/data/user.json";
//    windows
    private String pathname = "src/data/user.json";
    private File file = new File(pathname);
    @Override
    public User login(String username, String password) {
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
        String content = null;
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new ResultException(ResultCode.WEAK_NET_WORK);
        }

        //判断用户名是否已经注册过

        //注册用户名
        JSONArray array = new JSONArray(content);
        JSONObject object = putUser(user,new JSONObject());
        ;
        //赋予userId
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

    @Override
    public User upate(User user) {
        String content = null;
        try {
            content = FileUtils.readFileToString(file,"UTF-8");
            JSONArray array = new JSONArray(content);
            //判断是否已经放入过
            boolean isPush = false;
            for (int i = 0; i < array.length() ; i++) {
                String username = array.getJSONObject(i).getString("username");
                if(user.getUsername().equals(username)){
                    isPush = true;
                    //todo 看下能不能修改里面的值
                    putUser(user,array.getJSONObject(i));
                    break;
                }
            }
            //如果没放入过就新建一个
            if(!isPush){
                JSONObject jsonObject =putUser(user,new JSONObject());
                array.put(jsonObject);
            }
            //整体覆盖写入文件
            FileUtils.write(file,array.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserById(Long ID) {
        String content = null;
        try {
            content = FileUtils.readFileToString(file);
            JSONArray array = new JSONArray(content);
            for(int i = 0;i<array.length();i++){
                if(array.getJSONObject(i).getLong("userId")==ID){
                    JSONObject object = array.getJSONObject(i);
                    User user = new User();
                    user.setUserLevel(translateToUserLevel(object.getString("userLevel")));
                    user.setEducation(translateToEducation(object.getString("education")));
                    user.setPoint(object.getInt("point"));
                    user.setUsername(object.getString("username"));
                    user.setTel_phone(object.getString("tel_phone"));
                    user.setEmail(object.getString("email"));
                    user.setSex(translateToSex(object.getString("sex")));
                    user.setDescription(object.getString("description"));
                    return user;
                }
            }

            //一个新的用户
            User user = new User();
            user.setUserLevel(translateToUserLevel("LEVEL1"));
            user.setEducation(null);
            user.setPoint(0);
            user.setUsername(null);
            user.setTel_phone(null);//todo
            user.setSex(null);
            user.setDescription(null);
            user.setUserId(ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //将String转化为Education枚举类
    private Education translateToEducation(String content){
        switch (content){
            case "juniorMiddle":
                return Education.juniorMiddle;
            case "seniorMiddle":
                return Education.seniorMiddle;
            case "juniorCollege":
                return Education.juniorCollege;
            case "undergraduate":
                return Education.undergraduate;
            case "master":
                return Education.master;
            case "doctor":
                return Education.doctor;
        }
        return null;
    }

    //将String类转化为Sex枚举类
    private Sex translateToSex(String content){
        switch (content){
            case "Man":
                return Sex.Man;
            case "Woman":
                return Sex.Woman;
        }
        return null;
    }

    //将String类转化为UserLevel类
    private UserLevel translateToUserLevel(String content){
        switch (content){
            case "LEVEL1":
                return UserLevel.LEVEL1;
            case "LEVEL2":
                return UserLevel.LEVEL2;
            case "LEVEL3":
                return UserLevel.LEVEL3;
            case "LEVEL4":
                return UserLevel.LEVEL4;
            case "LEVEL5":
                return UserLevel.LEVEL5;
        }
        return null;
    }

    //将User放入JSONObject中
    private JSONObject putUser (User user,JSONObject jsonObject){
        jsonObject.put("password",user.getPassword());
        jsonObject.put("userId",user.getUserId());
        jsonObject.put("username",user.getUsername());
        jsonObject.put("description",user.getDescription());
        jsonObject.put("education",user.getEducation());
        jsonObject.put("email",user.getEmail());
        jsonObject.put("sex",user.getSex());
        jsonObject.put("tel_phone",user.getTel_phone());
        jsonObject.put("userId",user.getUserId());
        jsonObject.put("point",user.getPoint());
        jsonObject.put("userLevel",user.getUserLevel());
        return jsonObject;
    }
}
