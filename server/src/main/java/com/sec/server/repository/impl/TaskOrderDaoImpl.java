package com.sec.server.repository.impl;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.TaskOrderDao;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository("taskOrderDao")
public class TaskOrderDaoImpl implements TaskOrderDao{
    @Override
    public List<TaskOrder> getAllTaskOrder(String username) {
        System.out.println("inDao " + username);
        File file = new File("src/data/taskOrder_" + username + ".json");
        String content = null;
        List<TaskOrder> list = null;
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);
            list = JSON.parseArray(array.toString(), TaskOrder.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }

        return list;
    }

}
