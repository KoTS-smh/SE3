package com.sec.server.repository.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.domain.User;
import com.sec.server.domain.*;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.model.PersonalDataModel;
import com.sec.server.repository.DataAnalysisDao;
import com.sec.server.utils.Path;
import com.sec.server.utils.ReadFile;
import com.sec.server.utils.Stopwords;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Repository("dataAnalysisDao")
public class DataAnalysisDaoImpl implements DataAnalysisDao {

    @Override
    public List<TaskOrder> getAnalysisResult(String username) {
        File file = new File(Path.taskOrderPath + username + ".json");
        String content = null;

        List<TaskOrder> taskOrderList = new ArrayList<>();

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
        File file = new File(Path.taskPath);
        String content;
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
        File userFile = new File(Path.userPath);
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
        String content;
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
    public HashMap<Integer, HashMap<String, Integer>> getAnnotationTag(long taskId) {
        File file = new File(Path.taskPath);
        String content;
        List<Long> userIds = new ArrayList<>();
        List<Long> annotationIds = new ArrayList<>();
        int len = 0;
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray array = new JSONArray(content);
            for(int i=0;i<array.length();i++){
                if(taskId == array.getJSONObject(i).getLong("taskId")){
                    userIds = JSON.parseArray(array.getJSONObject(i).getJSONArray("acceptUserIds").toString(),Long.class);
                    len = array.getJSONObject(i).getJSONArray("imgUrlList").length();
                    break;
                }
            }
            for (Long userId : userIds) {
                File thisFile = new File(Path.taskOrderPath + userId + ".json");
                String temp = FileUtils.readFileToString(thisFile, "UTF-8");
                JSONArray taskOrders = new JSONArray(temp);
                for (int j = 0; j < taskOrders.length(); j++) {
                    if (taskId == taskOrders.getJSONObject(j).getLong("taskId")) {
                        annotationIds.add(taskOrders.getJSONObject(j).getLong("annotationId"));
                        break;
                    }
                }
            }
            if(annotationIds.size()==0){
                throw new ResultException("无标注信息",123);
            }
            HashMap<Integer,HashMap<String,Integer>> result = getOneAnnotationTag(annotationIds.get(0),len);
            HashMap<Integer,HashMap<String,Integer>> temp;
            for(int i=1;i<annotationIds.size();i++){
                temp = getOneAnnotationTag(annotationIds.get(i),len);
                for(int j=1;j<=len;j++){
                    Set<String> keys = temp.get(j).keySet();
                    for(String s:keys){
                        if(!result.get(j).containsKey(s)){
                            result.get(i).put(s,temp.get(j).get(s));
                        }else {
                            result.get(j).put(s,result.get(j).get(s)+temp.get(j).get(s));
                        }
                    }
                }
            }
            for(int i=1;i<=len;i++){
                List<Map.Entry<String,Integer>> list = new ArrayList<>(result.get(i).entrySet());
                list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
            }
            return result;
        } catch (IOException e) {
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    private HashMap<Integer,HashMap<String,Integer>> getOneAnnotationTag(long annotationId,int len){
        File file = new File(Path.annotationPath+"/"+annotationId+".txt");
        HashMap<Integer,HashMap<String,Integer>> result = new HashMap<>();
        try {
            String content = FileUtils.readFileToString(file,"utf-8");
            JSONObject jsonObject = JSON.parseObject(content);
            HashMap<String,Integer> tags;
            for(int i=1;i<=len;i++){
                if(jsonObject.getJSONObject("annotationMap").getJSONArray(i+"").size() == 0){
                    tags = new HashMap<>();
                }else {
                    List<String> words = JSON.parseArray(JSON.toJSONString(jsonObject.getJSONObject("annotationMap").getJSONArray(i + "").getJSONObject(0).getJSONArray("words")), String.class);
                    tags = getTags(words);
                }
                result.put(i,tags);
            }
            return result;
        } catch (IOException e) {
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    private HashMap<String,Integer> getTags(List<String> words){
        HashMap<String,Integer> result =new HashMap<>();
        if(words == null){
            return result;
        }
        for (String word : words) {
            String temp = isValue(word);
            if (temp != null) {
                if (!result.containsKey(temp)) {
                    result.put(temp, 1);
                } else {
                    result.put(temp, result.get(temp) + 1);
                }
            }
        }
        return result;
    }

    private String isValue(String word){
        word=word.toLowerCase();
        if(word.length()>20){
            return null;
        }else {
                if(word.startsWith("'")){
                    word = word.substring(1);
                }
                if(word.endsWith("'")){
                    word = word.substring(0,word.length()-1);
                }
                if(Pattern.matches("^[a-z]+[']?[-]?[a-z]+$",word)) {
                    String pattern = new Stopwords().getStopwordsRegex();
                    if (Pattern.matches(pattern, word)) {
                        return null;
                    } else {
                        return word;
                    }
                }else {
                    return null;
                }
        }
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

        return taskOrder.getDegreeOfCompletion() / (double) numOfPics;
    }

    /**
     *
     */
    public List<TaskRateMessage> getTaskMessage(long taskId){

        //获取所有的userId
        long userNumber = getTotalAmount(Path.userPath);

        //遍历所有的json文件，找出对应的jsonObject信息
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (int i = 0;i<userNumber;i++) {
            File file = new File(Path.taskOrderPath + i + ".json");
            if (file.exists()) {
                String content = null;
                try {
                    content = FileUtils.readFileToString(file, "UTF-8");
                    com.alibaba.fastjson.JSONArray array = JSON.parseArray(content);
                    for (int j = 0; j < array.size(); j++) {
                        JSONObject object = array.getJSONObject(j);
                        long id = 0;
                        id = object.getLong("taskId");
                        if (id == taskId) {
                            jsonObjectList.add(object);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //将jsonObject信息转为taskMessage返回
        List<TaskRateMessage> list = new ArrayList<>();
        for (JSONObject aJsonObjectList : jsonObjectList) {
            TaskRateMessage taskRateMessage = new TaskRateMessage();
            taskRateMessage.setAcceptUserId(aJsonObjectList.getLong("acceptUserId"));
            taskRateMessage.setAcceptUserName(aJsonObjectList.getString("acceptUserName"));
            taskRateMessage.setRate(getCOD(new TaskOrder(aJsonObjectList.getLong("taskId"), aJsonObjectList.getLong("acceptUserId"))));
//            taskRateMessage.setRate(aJsonObjectList.getDouble("rate"));
            if(aJsonObjectList.getBoolean("submited"))
                taskRateMessage.setState("待评审");
            else{
                if(taskRateMessage.getRate()==1)
                    taskRateMessage.setState("待提交");
                else
                    taskRateMessage.setState("未完成");
            }
            list.add(taskRateMessage);
        }

        return list;
    }

    /**
     *
     * @return systemAdministratorMessage 系统信息
     */
    @Override
    public SystemAdministratorMessage getSystemMessage() {
        SystemAdministratorMessage systemAdministratorMessage = new SystemAdministratorMessage();

        //计算用户数目
        int userNumber = 0;
        userNumber = getTotalAmount(Path.userPath);
        systemAdministratorMessage.setUserNumber(userNumber);

        //计算任务数目
        int taskNumber = 0;
        int unfinishedTaskNumber = 0;

        //遍历所有的json文件，找出任务信息
        List<Task> taskList = new ArrayList<>();
        File file = new File(Path.taskPath);


            if (file.exists()) {
                String content = null;
                try {
                    content = FileUtils.readFileToString(file, "UTF-8");
                    JSONArray array = new JSONArray(content);
                    taskList = JSON.parseArray(array.toString(), Task.class);
                    taskNumber = taskList.size();

                    for(Task tmpTask : taskList) {

                        if(!tmpTask.isFinished()){
                            unfinishedTaskNumber++;
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        systemAdministratorMessage.setFinishedTaskNumber(taskNumber-unfinishedTaskNumber);
        systemAdministratorMessage.setTaskNumber(taskNumber);
        systemAdministratorMessage.setUnfinishedTaskNumber(unfinishedTaskNumber);

        return systemAdministratorMessage;
    }

}
