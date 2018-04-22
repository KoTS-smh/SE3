package com.sec.server.repository.impl;

import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.TaskDao;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;

@Repository("taskDao")
public class TaskDaoImpl implements TaskDao{
    @Override
    public void createTask(String task) {
        //System.out.println(task);
        JSONObject object = new JSONObject(task);

        File file = new File("src/data/task.json");
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
}
