package com.sec.server.repository.impl;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.TaskOrderDao;
import com.sec.server.utils.DateFormatConverter;
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
    public List<TaskOrder> getAllTaskOrder(long userId) {
        System.out.println("inDao " + userId);
        File file = new File("src/data/taskOrder_" + userId + ".json");
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

    /**
     * 新建taskOrder的方法
     * @param taskOrder
     */
    @Override
    public void createTaskOrder(TaskOrder taskOrder) {
        long userId = taskOrder.getAcceptUserId();
        System.out.println("this is userID" + userId);
        long taskId = taskOrder.getTaskId();
        JSONObject object = new JSONObject(taskOrder);
        String nativeBeginDate = object.getString("beginDate");
        String nativeEndDate = object.getString("endDate");
        object.put("beginDate", DateFormatConverter.convert(nativeBeginDate));
        object.put("endDate", DateFormatConverter.convert(nativeEndDate));

        String content = null;

        //先判断是否存在json文件
        File file = new File("src/data/taskOrder_" + userId + ".json");
        if(file.exists()){
            try {
                content = FileUtils.readFileToString(file, "UTF-8");
                JSONArray array = new JSONArray(content);

                //check if the task is already accepted
                List<TaskOrder> taskOrderList = JSON.parseArray(array.toString(), TaskOrder.class);
                for(TaskOrder tmpTaskOrder : taskOrderList){
                    if(tmpTaskOrder.getTaskId() == taskId){
                        //重复
                        throw new ResultException(ResultCode.TASK_ALREADY_ACCEPT);
                    }
                }

                //不重复
                array.put(object);
                FileUtils.write(file, array.toString(2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                file.createNewFile();
                JSONArray array = new JSONArray();
                array.put(object);
                FileUtils.write(file, array.toString(2));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void updateTaskOrder(String taskOrder) {

    }

    @Override
    public void deleteTaskOrder(long taskOrderId) {

    }

}
