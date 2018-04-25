package com.sec.server.utils;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.Task;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    /**
     * 读取json文件，返回文件内容
     */

    public static String readFile(String path) throws IOException{
        File file = new File(path);

        if(!file.exists() || file.isDirectory()){
            throw new FileNotFoundException();
        }

        StringBuffer sb = new StringBuffer();

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineText = null;
            while((lineText = bufferedReader.readLine()) != null) {
                sb.append(lineText);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 从任务JSON文件获取 标注积分奖励
     * @param path 文件路径
     * @return
     */
    public static int getPointFromTask(String path, long taskId){
        File file = new File(path);
        String content = null;
        List<Integer> list = new ArrayList();

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            for(int i = 0;i < array.length();++i){
                JSONObject tmp = (JSONObject) array.get(i);
                if(tmp.getLong("taskId") == taskId){
                    int points = tmp.getInt("totalPoints");
                    return points;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.TASK_NOT_FOUND);
        }

        return -1;

    }

    public static int getNumOfPics(String path, long taskId){
        File file = new File(path);
        String content = null;

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            for(int i = 0;i < array.length();++i){
                JSONObject tmp = (JSONObject) array.get(i);
                if(tmp.getLong("taskId") == taskId){
                    int numOfPics = tmp.getJSONArray("imgList").length();
                    System.out.println("in function " + numOfPics);
                    return numOfPics;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.TASK_NOT_FOUND);
        }

        return -1;
    }

    /**
     * 根据任务ID从文件中获取任务对象的方法
     * @param taskId 任务ID
     * @return 对应ID的任务对象
     */
    public static Task getTask(long taskId){
        File file = new File("src/data/task.json");
        String content = null;

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            List<Task> taskList = JSON.parseArray(array.toString(), Task.class);
            for(Task task : taskList){
                if(task.getTaskId() == taskId){
                    return task;
                }
            }

            throw new ResultException(ResultCode.TASK_NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    public static List<Task> getAllPost(long userId){
        File file = new File("src/data/task.json");
        String content = null;
        List<Task> retTaskList = new ArrayList<>();

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            List<Task> taskList = JSON.parseArray(array.toString(), Task.class);
            for(Task task : taskList){
                if(task.getPostUserId() == userId)
                    retTaskList.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retTaskList;
    }
}
