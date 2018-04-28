package com.sec.server.repository.impl;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.domain.User;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.model.PersonalDataModel;
import com.sec.server.repository.DataAnalysisDao;
import com.sec.server.utils.ReadFile;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository("dataAnalysisDao")
public class DataAnalysisDaoImpl implements DataAnalysisDao {
    @Override
    public List<TaskOrder> getAnalysisResult(String username) {
        File file = new File("src/data/taskOrder_" + username + ".json");
        String content = null;

        List<TaskOrder> taskOrderList = new ArrayList();

        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            taskOrderList = JSON.parseArray(array.toString(), TaskOrder.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return analysis(taskOrderList);
    }

    @Override
    public List<User> getParticipant(long taskId) {
        List<User> participantList = new ArrayList<>();
        List<Task> currentTasks = new ArrayList<>();
        List<User> currentUsers = new ArrayList<>();
        List<Long> participantIdList = new ArrayList<>();
        File file = new File("src/data/task.json");
        String content = null;
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);

            currentTasks = JSON.parseArray(array.toString(), Task.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Task task : currentTasks){
            if(task.getTaskId() == taskId){
                participantIdList = task.getAcceptUserIds();
            }
        }

        //read the user.json file
        File userFile = new File("src/data/user.json");
        try {
            content = FileUtils.readFileToString(userFile, "UTF-8");
            JSONArray array = new JSONArray(content);
            currentUsers = JSON.parseArray(array.toString(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(long tmpId : participantIdList){
            for(User tmpUser : currentUsers){
                if(tmpId == tmpUser.getUserId()){
                    participantList.add(tmpUser);
                }
            }
        }

        return participantList;
    }

    /**
     * 用于查询json列表文件中总数目的方法
     * @return 对应对象列表的长度
     */
    @Override
    public int getTotalAmount(String path) {
        File file = new File(path);
        String content = null;
        int totalAmount = 0;
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);
            totalAmount = array.length();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
        return totalAmount;
    }

    @Override
    public PersonalDataModel getPersonalData(long userId) {
        int point = ReadFile.getUserPoint(userId);
        int taskNumber = ReadFile.getNumberOfTaskInProcess(userId);
        //todo 算排名
        String rank = ReadFile.getRank(userId);

        PersonalDataModel personalDataModel = new PersonalDataModel();
        personalDataModel.setPoint(point);
        personalDataModel.setTasknumber(taskNumber);
        personalDataModel.setRank(rank);
        return personalDataModel;
    }


    private List<TaskOrder> analysis(List<TaskOrder> taskOrderList){
        double averageRate = 0;
        int totalPoints = 0;
        double degreeOfCompletion = 0;

        for (TaskOrder taskOrder : taskOrderList){
            averageRate += taskOrder.getRate();
            long taskId = taskOrder.getTaskId();
            int point = ReadFile.getPointFromTask("src/data/task.json", taskId);
            totalPoints += point;

            int numOfPics = ReadFile.getNumOfPics("src/data/task.json", taskId);
            degreeOfCompletion = taskOrder.getDegreeOfCompletion() / (double) numOfPics;

        }
        averageRate /= taskOrderList.size();
        System.out.println("the average rate is " + averageRate);
        System.out.println("the total points is " + totalPoints);
        System.out.println("the number of my tasks is " + taskOrderList.size());

        return taskOrderList;
    }

    /**
     * 获得用户任务平均评分的私有方法
     * @param taskOrderList 根据用户名或者ID从taskOrder json中取出的taskOrder列表
     * @return 平均评分
     */
    private double getAverageRate(List<TaskOrder> taskOrderList){
        double averageRate = 0;
        for(TaskOrder taskOrder : taskOrderList){
            averageRate += taskOrder.getRate();
        }

        averageRate /= taskOrderList.size();
        return averageRate;
    }

    /**
     * 获得用户总积分的私有方法
     * @param taskOrderList 根据用户名或者ID从taskOrder json中取出的taskOrder列表
     * @return 用户总积分
     */
    private int getTotalPoints(List<TaskOrder> taskOrderList) {
        int totalPoints = 0;
        for(TaskOrder taskOrder : taskOrderList){
            long taskId = taskOrder.getTaskId();
            int point = ReadFile.getPointFromTask("src/data/task.json", taskId);
            totalPoints += point;
        }

        return totalPoints;
    }

    /**
     * 计算用户的一个任务完成度的方法
     * @param taskOrder taskOrder对象
     * @return 任务完成度
     */
    private double getCOD(TaskOrder taskOrder) {
        long taskId = taskOrder.getTaskId();
        int numOfPics = ReadFile.getNumOfPics("src/data/task.json", taskId);
        if(numOfPics == 0){
            throw new ResultException(ResultCode.NO_IMAGE_ERROR);
        }
        double degreeOfCompletion = taskOrder.getDegreeOfCompletion() / (double) numOfPics;

        return degreeOfCompletion;
    }



    public static void main(String[] args){
        String username = "illiant";
        DataAnalysisDao test = new DataAnalysisDaoImpl();

        System.out.println(test.getParticipant(3).get(0).getUserId());
    }
}
