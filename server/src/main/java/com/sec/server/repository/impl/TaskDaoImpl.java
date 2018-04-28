package com.sec.server.repository.impl;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.Task;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.TaskDao;
import com.sec.server.utils.DateFormatConverter;
import com.sec.server.utils.ReadFile;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository("taskDao")
public class TaskDaoImpl implements TaskDao{
    private static String path = "src/data/task.json";
    @Override
    public void createTask(String task) {
        //System.out.println(task);
        JSONObject object = new JSONObject(task);

        File file = new File(path);
        String content = null;

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);
            array.put(object);
            FileUtils.write(file, array.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public void updateTask(String task) {
        System.out.println(task);
        JSONObject object = new JSONObject(task);
        long inId = object.getLong("taskId");
        File file = new File(path);
        String content = null;

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            for(int i = 0;i < array.length();++i){
               JSONObject tmp = (JSONObject) array.get(i);
               if(tmp.getLong("taskId") == inId){
                   array.remove(i);
               }
            }

            //修改时间格式
            String beginDate = object.getString("beginDate");
//            beginDate = DateFormatConverter.simpleDateConvert(beginDate);
            beginDate = DateFormatConverter.convert(beginDate);
            object.remove("beginDate");
            System.out.println("beginDate " + beginDate);
            object.put("beginDate", beginDate);
            String endDate = object.getString("endDate");
            //endDate = DateFormatConverter.simpleDateConvert(endDate);
            endDate = DateFormatConverter.convert(endDate);
            object.remove("endDate");
            System.out.println("endDate " + endDate);
            object.put("endDate", endDate);

            array.put(object);
            FileUtils.write(file, array.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public void deleteTask(long taskId) {
        File file = new File(path);
        String content = null;

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            for(int i = 0;i < array.length();++i){
                JSONObject tmp = (JSONObject) array.get(i);
                if(tmp.getLong("taskId") == taskId){
                    array.remove(i);
                }
            }

            FileUtils.write(file, array.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.UNKNOWN_ERROR);

        }
    }

    @Override
    public Task getTaskInfo(long taskId) {
        File file = new File(path);
        String content = null;

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            for(int i = 0;i < array.length();++i){
                JSONObject tmp = (JSONObject) array.get(i);
                if(tmp.getLong("taskId") == taskId){
                    Task task = (Task) JSON.parse(tmp.toString());
                    return task;
                }
            }



            throw new ResultException(ResultCode.TASK_NOT_FOUND);
        } catch (IOException e) {

            e.printStackTrace();
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public List<Task> getAllPost(long userId) {
        return ReadFile.getAllPost(userId);
    }
}
