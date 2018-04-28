package com.sec.server.repository.impl;

import com.alibaba.fastjson.JSONArray;
import com.sec.server.domain.Task;
import com.sec.server.domain.User;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.SearchDao;
import com.sec.server.utils.Path;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchDaoImpl implements SearchDao {
    @Override
    public List<Task> searchTask(String message) {
        String[] keys = message.split("/s+/");
        File file = new File(Path.taskPath);
        try {
            List<Task> tasks= JSONArray.parseArray(FileUtils.readFileToString(file,"utf-8"),Task.class);
            for (String key : keys) {
                tasks = getRelateTask(tasks, key);
            }
            return tasks;
        } catch (IOException e) {
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public List<User> searchUser(String message) {
        String[] keys = message.split("/s+/");
        File file = new File(Path.userPath);
        try {
            List<User> users = JSONArray.parseArray(FileUtils.readFileToString(file,"utf-8"),User.class);
            for(String key:keys){
                users = getRelatedUser(users,key);
            }
            return users;
        } catch (IOException e) {
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    private List<Task> getRelateTask(List<Task> tasks,String key){
        List<Task> result = new ArrayList<>();
        for(Task t:tasks){
            if(String.valueOf(t.getTaskId()).equals(key)){
                result.add(t);
                continue;
            }
            if(String.valueOf(t.getPostUserId()).equals(key)){
                result.add(t);
                continue;
            }
            if(t.getTaskname().contains(key)){
                result.add(t);
                continue;
            }
            for(String s:t.getTaskInfo().split("/s+/")){
                if(s.equals(key)){
                    result.add(t);
                    break;
                }
            }
        }
        return result;
    }

    private List<User> getRelatedUser(List<User> users,String key){
        List<User> result = new ArrayList<>();
        for(User user:users){
            if(String.valueOf(user.getUserId()).equals(key)){
                result.add(user);
                continue;
            }
            if(user.getUsername().contains(key)){
                result.add(user);
            }
        }
        return result;
    }
}
