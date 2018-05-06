package com.sec.server.repository.impl;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.TaskOrderDao;
import com.sec.server.utils.DateFormatConverter;
import com.sec.server.utils.Path;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.sec.server.repository.impl.AnnotationDaoImpl.createAnnotationInfo;

@Repository("taskOrderDao")
public class TaskOrderDaoImpl implements TaskOrderDao{
    @Override
    public List<TaskOrder> getAllTaskOrder(long userId) {
        File file = new File(Path.taskOrderPath + userId + ".json");
        if(!file.exists()){
            try {
                file.createNewFile();
                JSONArray array = new JSONArray();
                FileUtils.write(file, array.toString(2));
            } catch (IOException e) {
                throw new ResultException(ResultCode.UNKNOWN_ERROR);
            }
        }
        String content;
        List<TaskOrder> list;
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
        File file= new File(Path.taskPath);
        try {
            long taskId = taskOrder.getTaskId();
            long userId = taskOrder.getAcceptUserId();
            int len = 0;
            String content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray tasks = new JSONArray(content);
            for(int i=0;i<tasks.length();i++){
                if(tasks.getJSONObject(i).getLong("taskId")==taskId){
                    len =tasks.getJSONObject(i).getJSONArray("imgUrlList").length();
                    tasks.getJSONObject(i).getJSONArray("acceptUserIds").put(userId);
                    break;
                }
            }
            FileUtils.write(file, tasks.toString(2));
            long annotationId = createAnnotationInfo(len);
            taskOrder.setAnnotationId(annotationId);
            taskOrder.setLastPic(1);
            JSONObject object = new JSONObject(taskOrder);
            String nativeBeginDate = object.getString("beginDate");
            String nativeEndDate = object.getString("endDate");
            object.put("beginDate", DateFormatConverter.convert(nativeBeginDate));
            object.put("endDate", DateFormatConverter.convert(nativeEndDate));
            //先判断是否存在json文件
            file = new File(Path.taskOrderPath + userId + ".json");
            if (file.exists()) {
                content = FileUtils.readFileToString(file, "UTF-8");
                JSONArray array = new JSONArray(content);
                //check if the task is already accepted
                List<TaskOrder> taskOrderList = JSON.parseArray(array.toString(), TaskOrder.class);
                for (TaskOrder tmpTaskOrder : taskOrderList) {
                    if (tmpTaskOrder.getTaskId() == taskId) {
                        //重复
                        throw new ResultException(ResultCode.TASK_ALREADY_ACCEPT);
                    }
                }
                //不重复
                array.put(object);
                FileUtils.write(file, array.toString(2));
            } else {
                file.createNewFile();
                JSONArray array = new JSONArray();
                array.put(object);
                FileUtils.write(file, array.toString(2));
            }
        }catch (IOException e){
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public void updateTaskOrder(String taskOrder) {
        JSONObject newObject = new JSONObject(taskOrder);
        long userId = newObject.getLong("acceptUserId");
        File file = new File(Path.taskOrderPath + userId + ".json");
        if(file.exists()){
            try {
                String content = FileUtils.readFileToString(file,"UTF-8");
                JSONArray array = new JSONArray(content);
                for(int i = 0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    if(object.getLong("taskId")==newObject.getLong("taskId")){
                        array.put(i,newObject);
                        break;
                    }
                }
                FileUtils.write(file,array.toString(2));
            } catch (IOException e) {
                throw new ResultException(ResultCode.UNKNOWN_ERROR);
            }
        }
    }

    @Override
    public void deleteTaskOrder(long taskOrderId,long userId) {
        //todo
        File file = new File(Path.taskOrderPath + userId + ".json");
        if(file.exists()){
            try {
                String content = FileUtils.readFileToString(file);
                JSONArray array = new JSONArray(content);
                for(int i = 0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    if(object.getLong("taskOrderId")==taskOrderId){
                        File file2 = new File(Path.taskPath);
                        String s = FileUtils.readFileToString(file2, "UTF-8");
                        JSONArray tasks = new JSONArray(content);
                        for(int j=0;j<tasks.length();j++){
                            if(tasks.getJSONObject(j).getLong("taskId")==object.getLong("taskId")){
                                JSONArray ids = tasks.getJSONObject(j).getJSONArray("acceptUserIds");
                                for (int n=0;n<ids.length();n++){
                                    if(ids.getLong(n)==userId){
                                        tasks.getJSONObject(j).getJSONArray("acceptUserIds").remove(n);
                                        FileUtils.write(file2, tasks.toString(2));
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        array.remove(i);
                        break;
                    }
                }
                FileUtils.write(file,array.toString(2));
            } catch (IOException e) {
                throw new ResultException(ResultCode.UNKNOWN_ERROR);
            }

        }
    }

    @Override
    public TaskOrder getTaskOrderById(long taskOrder, long userId) {
        File file =new File(Path.taskOrderPath+userId+".json");
        try {
            JSONArray array = new JSONArray(FileUtils.readFileToString(file));
            for(int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                if(object.getLong("taskOrderId")==taskOrder){
                    return JSON.parseObject(object.toString(),TaskOrder.class);
                }
            }
        } catch (IOException e) {
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
        throw new ResultException("任务订单不存在",2);
    }

}
