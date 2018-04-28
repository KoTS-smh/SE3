package com.sec.server.utils;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.domain.User;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {

    private static String taskPath = "src/data/task.json";
    private static String taskOrderPath = "src/data/taskOrder_";
    private static String userPath = "src/data/user.json";
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
        File file = new File(taskPath);
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

    /**
     * 从task.json文件中获取所有已经发布的任务的方法
     * @param userId 用户编号
     * @return task列表
     */
    public static List<Task> getAllPost(long userId){
        File file = new File(taskPath);
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

    public static List<Task> getAllFinished(long userId) {
        File file = new File(taskPath);
        String content = null;
        List<Task> retTaskList = new ArrayList<>();

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            List<Task> taskList = JSON.parseArray(array.toString(), Task.class);

            for(Task tmpTask : taskList) {
                if(tmpTask.getPostUserId() == userId && tmpTask.isFinished()) {
                    retTaskList.add(tmpTask);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.TASK_NOT_FOUND);
        }

        return retTaskList;
    }

    public static List<Task> getAllunFinished(long userId){
        File file = new File(taskPath);
        String content = null;
        List<Task> retTaskList = new ArrayList<>();

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            List<Task> taskList = JSON.parseArray(array.toString(), Task.class);

            for(Task tmpTask : taskList) {
                if(tmpTask.getPostUserId() == userId && !tmpTask.isFinished()) {
                    retTaskList.add(tmpTask);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.TASK_NOT_FOUND);
        }

        return retTaskList;
    }

    public static List<TaskOrder> getAllSubmited(long userId){
        File file = new File(taskOrderPath + userId + ".json");
        String content = null;
        List<TaskOrder> retList = new ArrayList<>();

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            List<TaskOrder> taskOrderList = JSON.parseArray(array.toString(), TaskOrder.class);

            for(TaskOrder tmpTaskOrder : taskOrderList) {

                if(tmpTaskOrder.isSubmited()){
                    retList.add(tmpTaskOrder);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.TASK_NOT_FOUND);
        }

        return retList;
    }

    public static List<TaskOrder> getAllunSubmited(long userId) {
        File file = new File(taskOrderPath + userId + ".json");
        String content = null;
        List<TaskOrder> retList = new ArrayList<>();

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            List<TaskOrder> taskOrderList = JSON.parseArray(array.toString(), TaskOrder.class);

            for(TaskOrder tmpTaskOrder : taskOrderList) {

                if(!tmpTaskOrder.isSubmited()){
                    retList.add(tmpTaskOrder);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.TASK_NOT_FOUND);
        }

        return retList;
    }

    public static int getUserPoint(long userId) {
        File file = new File(userPath);
        String content = null;
        int retPoint = -1;
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            List<User> userList = JSON.parseArray(array.toString(), User.class);

            for(User tmpUser : userList) {
                if(tmpUser.getUserId() == userId)
                    retPoint = tmpUser.getPoint();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retPoint;
    }

    public static int getNumberOfTaskInProcess(long userId) {
        File file = new File(taskOrderPath + userId + ".json");
        String content = null;
        int taskNumber = 0;
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            List<TaskOrder> taskOrderList = JSON.parseArray(array.toString(), TaskOrder.class);

            for(TaskOrder tmpTaskOrder : taskOrderList) {
                if(tmpTaskOrder.getAcceptUserId() == userId)
                    taskNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return taskNumber;
    }

    /**
     * 计算众包工人群体排名
     */

    public static String getRank(long userId) {
        File file = new File(userPath);
        String content = null;
        List<User> userList = new ArrayList<>();
        List<Integer> pointList = new ArrayList<>();
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);
            String tmpstr = array.toString();
            userList = JSON.parseArray(tmpstr, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get current user point
        int currentPoint = 0;
        for(User tmpuser : userList) {
            if(tmpuser.getUserId() == userId){
                currentPoint = tmpuser.getPoint();
            }
        }

        for(int i = 0;i < userList.size();++i) {
            pointList.add(userList.get(i).getPoint());
        }

        if(userList.size() <= 1) {
            return "1";
        }

        //排名
        double rank = 0;

        for(User tmpUser : userList) {
            if(tmpUser.getPoint() > currentPoint)
                rank++;
        }

        if(userList.size() < 10) {
            return rank + "";
        }else {
            double percentage = rank / userList.size();
            DecimalFormat df = new DecimalFormat("0.00");
            return "前" + df.format(percentage * 100) + " %";
        }
    }
}
