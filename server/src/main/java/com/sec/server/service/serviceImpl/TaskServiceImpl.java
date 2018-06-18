package com.sec.server.service.serviceImpl;

import com.sec.server.domain.*;
import com.sec.server.repository.*;
import com.sec.server.enums.*;
import com.sec.server.exception.ResultException;
import com.sec.server.model.Picture_CardModel;
import com.sec.server.model.TaskModel;
import com.sec.server.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "taskService")
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ImgUrlDao imgUrlDao;

    @Autowired
    private TaskOrderDao taskOrderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private AppointDao appointDao;

    @Autowired
    private CheckPointDao checkPointDao;

    @Resource(name = "dataAnalysisService")
    private DataAnalysisService dataAnalysisService;

    @Resource(name = "appointService")
    private AppointService appointService;

    @Resource(name = "evaluateService")
    private EvaluateService evaluateService;

    @Resource(name = "userService")
    private UserService userService;

    /**
     * 创建任务
     * @param task 任务信息
     * @describe
     *          1、创建任务后任务处于预约状态
     *          2、创建任务后需要调用修改金额方法修改发布者的金额
     */
    @Override
    public void createTask(Task task) {
        List<String> urlLists = task.getImgUrls();
        int maxParticipator = task.getMaxParticipator();
        double cost = 0;
//        if(urlLists.size() * maxParticipator * 0.02 < 10){
//            cost = 10;
//        }else {
//            cost = urlLists.size() * maxParticipator * 0.02;
//        }
        cost = task.getReward();


        long userId = task.getPostUserId();
        double currentBalance = userDao.getBalance(userId);
        if(currentBalance < cost) {
            throw new ResultException("余额不足", 12222);
        }
        userDao.consume(cost, userId);
        taskDao.addTask(task);
        imgUrlDao.insertUrlList(urlLists, task.getTaskId());

        //计算任务质量 todo 现在会报错
//        evaluateService.evaluateTaskQuality(task.getTaskId());

        //创建检查点
        System.out.println("taskId = "+task.getTaskId());
        createCheckPoint(task.getTaskId());
    }

    @Override
    public void updateTask(Task task) {
        taskDao.updateTask(task);
    }

    @Override
    public void deleteTask(long taskId) {
        try {
            //taskDao.deleteTask(taskId);
            throw new ResultException("任务已被接取，无法删除", 12580);
        }catch (Exception e) {
            throw new ResultException("任务已被接取，无法删除", 12580);
        }
    }

    @Override
    public List<Task> getAllPostTask(long postUserId) {
        List<Task> taskList = taskDao.getAllPostTask(postUserId);
        for(Task tmp : taskList) {
            tmp = placeTaskTag(tmp);
        }
        return taskList;
    }

    @Override
    public List<Task> getAllFinishedTask(long postUserId) {
        List<Task> taskList = taskDao.getAllFinishedTask(postUserId);
        for(Task tmp : taskList) {
            tmp = placeTaskTag(tmp);
        }
        return taskList;
    }

    @Override
    public List<Task> getAllunFinishedTask(long postUserId) {
        List<Task> taskList = taskDao.getAllunFinishedTask(postUserId);
        for(Task tmp : taskList)
            tmp = placeTaskTag(tmp);

        return taskList;
    }

    @Override
    public List<Task> getEveryUnFinishedTask() {
        List<Task> taskList = taskDao.getEveryUnFinishedTask();
        for(Task tmp : taskList)
            tmp = placeTaskTag(tmp);

        return taskList;
    }

    @Override
    @Transactional
    public TaskModel getTask(long taskId) {
        Task task;
        List<Long> acceptUserList;

        try {
            task = taskDao.getTask(taskId);
            acceptUserList = taskOrderDao.getAcceptUserIds(taskId);
        }catch (Exception e){
            throw new ResultException("任务不存在！", 1111);
        }

        int appointNum = appointDao.getAppointUser(taskId).size();
        int annoNum = taskOrderDao.getAcceptNum(taskId);
        TaskModel taskModel = new TaskModel(task);
        taskModel.setAcceptUserIds(acceptUserList);
        taskModel.setAnnoNum(annoNum);
        taskModel.setAppointNum(appointNum);
        return taskModel;
    }

    @Override
    public int getWorkerNumber(long taskId) {
        return taskOrderDao.getAcceptNum(taskId);
    }

    @Override
    public void checkTaskEveryDay() {
        List<Task> taskList = taskDao.getAllTask();
        for(Task tmp : taskList) {
            checkTask(tmp);
        }
    }

    @Override
    public List<Task> getRecommendTask(long userId) {
        List<Task> recommmendTasks = new ArrayList<>();

        User user = userDao.getUserById(userId);
        List<TaskOrder> taskOrderList = taskOrderDao.getAllTaskOrder(userId);
        //用户之前完成过的任务
        List<Task> taskList = new ArrayList<>();
        for(TaskOrder item : taskOrderList) {
            if(item.getSubmited().equals(TaskOrderState.submitted)) {
                Task task = taskDao.getTask(item.getTaskId());
                task = placeTaskTag(task);
                taskList.add(task);
            }
        }

        List<TaskTag> recommendTags = new ArrayList<>();
        int[] tagCounts = new int[TaskTag.values().length];

        //统计之前标注任务的类型
        for(Task item : taskList) {
            List<TaskTag> taskTags = item.getTaskTags();
            for (TaskTag taskTag : taskTags) {
                tagCounts[taskTag.ordinal()]++;
            }
        }

        List<Integer> mostTagged = getMostTagged(tagCounts);

        List<Long> acceptedTasks = taskOrderDao.getAcceptedTasks(userId);
        List<Task> tasksNotAccept = taskDao.getAllTask();

        if(tasksNotAccept.size() == 0){
            return recommmendTasks;
        }

        //所有用户没有接过的任务
        List<Task> taskToDelete = new ArrayList<>();
        for(Task tmp : tasksNotAccept) {
            tmp = placeTaskTag(tmp);
            if(acceptedTasks.contains(tmp.getTaskId())){
                taskToDelete.add(tmp);
            }
        }

        tasksNotAccept.removeAll(taskToDelete);
        
        //根据职业推荐
        recommmendTasks = recommendByProfession(recommmendTasks, user);

        if(mostTagged.size() <= 10) {
            //用户之前进行的标注任务较少，此时推荐接取次数比较少的任务
            for(Task tmp : tasksNotAccept) {
                if(recommmendTasks.size() > 10)
                    break;
                if(tmp.getUpRate() != null && tmp.getUpRate().length() > 0) {
                    recommmendTasks.add(tmp);
                }
            }
            return recommmendTasks;
        }

        for(Task tmp : tasksNotAccept) {
            if(recommmendTasks.size() > 10)
                break;

            List<TaskTag> taskTags = tmp.getTaskTags();
            for(TaskTag taskTag : taskTags){
               if(mostTagged.contains(taskTag.ordinal()))
                   recommmendTasks.add(tmp);
            }
        }

        return recommmendTasks;
    }

    @Override
    public List<Picture_CardModel> searchTask(String message, String taskType, String tag) {
        List<Picture_CardModel> list = new ArrayList<>();
        List<Task> taskList = new ArrayList<>();
        if(taskType.equals("all")) {
            taskList = taskDao.searchForAllTasks(message);
        }else{
            int typeNum = (taskType.charAt(taskType.length() - 1) - '1');
            taskList = taskDao.searchForTypedTasks(message, typeNum);
        }
            for(Task tmp : taskList) {
                tmp = placeTaskTag(tmp);
            }

            if(tag.equals("请选择") || tag.equals("all") || tag.trim().equals("")) {
                //do nothing
            }else {
                taskList = taskFilter(taskList, tag);
            }
            if(taskList.size() > 0) {
                for(Task task : taskList) {
                    list.add(new Picture_CardModel(task));
                }
            }


       return list;
    }

    /**
     * 任务结算方法  todo
     * @param taskId 任务Id
     * @describe
     *              1、每天十二点判断是否有任务到达DDL，有则会调用此方法
     *              2、发布者可以进入任务完成情况界面选择订单全部提交的任务结束
     *
     * 工人预约任务-> 预约成功开始工作 -> 任务提交等待审批 -> 任务订单审批通过 -> 任务结算 -> 获取佣金
     * 任务到期之后停止订单的修改和提交操作
     * 1、所有人都提交了，检查点到最后一个阶段（即到达任务结束时间），需要结算任务
     *          问题：
     *              a、因为在日常的检查点会有专门的方法去检查每个任务评分是否达到要求，那在最后一个检查点如果没达到要求是直接变成fail?
     * 结算方法主要是为了付钱和更改任务状态
     */
    @Override
    public void finishTask(long taskId) {
        //最后一次质量审核任务
        checkTaskAtCheckPoint(taskId,true);

        //获取任务信息
        Task task = taskDao.getTask(taskId);

        //更改任务状态
        taskDao.finishTask(taskId);

        //计算通过的人数
        int passNumber = 0;

        //通知发布者任务结束
        Message message = new Message();
        message.setUserId(task.getPostUserId());
        message.setMessageInfo("您的任务已经成功结束。任务名称："+task.getTaskname());
        message.setTitle("任务通知");
        message.setRead(false);
        messageDao.insertMessage(message);

        //获得任务等待列表
        List<Long> waitingList = appointDao.getAppointUser(taskId);
        //通知工人们任务已经结束，删除数据库中等待信息
        for(Long aUser:waitingList){
            appointDao.deleteAppointMessage(taskId,aUser);
            Message message1 = new Message();
            message1.setUserId(aUser);
            message1.setMessageInfo("您等待的任务已经成功结束，感谢您的等待。任务名称："+task.getTaskname());
            message1.setTitle("任务通知");
            message1.setRead(false);
            messageDao.insertMessage(message1);
        }

        //获取所有的工人任务
        TaskOrder taskOrder;
        List<TaskOrder> list = taskOrderDao.getAllTaskOrderOfATask(taskId);

        for (TaskOrder aList : list) {
            taskOrder = aList;

            //操作通过的任务订单
            switch (taskOrder.getSubmited()) {
                case submitted:
                    //把任务状态修改为success
                    taskOrder.setSubmited(TaskOrderState.finish);
                    taskOrderDao.updateTaskOrder(taskOrder);
                    //通知工人通过
                    Message messageToWorker = new Message();
                    messageToWorker.setRead(false);
                    messageToWorker.setUserId(aList.getAcceptUserId());
                    messageToWorker.setMessageInfo("您的任务订单成功完成。任务名称："+task.getTaskname());
                    messageToWorker.setTitle("任务通知");
                    messageDao.insertMessage(messageToWorker);
                    //结算佣金
                    dataAnalysisService.modifyCurrency(task.getReward(),taskOrder.getAcceptUserId());
                    //计算得分
                    userDao.increaseUserPoint(taskOrder.getAcceptUserId(),task.getTotalPoints());
                    passNumber++;
                    break;
                case fail:
                    //通知任务失败
                    Message messageToFailWorker = new Message();
                    messageToFailWorker.setUserId(aList.getAcceptUserId());
                    messageToFailWorker.setMessageInfo("您没有通过检查点的审批，感谢您曾经为此任务做出的贡献。"+"任务名称："+task.getTaskname());
                    messageToFailWorker.setTitle("任务通知");
                    messageToFailWorker.setUserId(aList.getAcceptUserId());
                    messageToFailWorker.setRead(false);
                    messageDao.insertMessage(messageToFailWorker);
                    //计算得分
                    userService.pointDrop(aList.getAcceptUserId());
                    break;
                default:
                    break;
            }

            //修改发布者余额 todo
            double number = task.getUpRate().charAt(0)/10+1;
            dataAnalysisService.modifyCurrency(-(task.getReward()/number)*passNumber,task.getPostUserId());

        }

    }

    /**
     * 系统定时检查
     * @describe
     *              1、有没有任务需要开始
     *              2、有没有任务需要结算
     *              3、审批工人任务质量
     *              4、有没有任务到达检查点
     */
    @Override
//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "0 47 17 * * ?")
    public void timeTask() {
        //获取所有未完成的任务订单 todo
//        List<TaskOrder> taskOrderList = taskOrderDao.getTaskNeedEvaluate();
        //给任务订单评分 todo
//        for(TaskOrder taskOrder:taskOrderList){
//            evaluateService.evaluateAnnotation(taskOrder.getTaskOrderId());
//        }
        //获取所有预约中未开始的任务
        List<Task> appointTaskList = taskDao.getAllAppointTask();
        //获取所有进行中未结算的任务
        List<Task> ongoingTaskList = taskDao.getAllOngoingTask();
        //获取当前时间df
        Date nowTime = new Date();
        Date compareTime;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            nowTime = df.parse(df.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //判断是否到达时间需要预约开始
        for(Task aTask:appointTaskList){
            compareTime = aTask.getBeginDate();
            //如果当前时间超过开始时间
            if(compareTime.getTime()<=nowTime.getTime()){
                //开始任务
                appointService.endAppointment(aTask.getTaskId());
            }
        }

        //判断是否到达时间需要任务结算
        for(Task aTask:ongoingTaskList){
            compareTime = aTask.getEndDate();
            //如果当前时间超过结束时间
            if(compareTime.getTime()<=nowTime.getTime()){
                //结算任务
                finishTask(aTask.getTaskId());
            }
        }

        //获取到达检查点的任务 todo
        List<Long> checkList = new ArrayList<>();
             checkList = checkPointDao.getTaskIdByDate(nowTime);
//        checkList.add((long) 20);
        //判断是否有任务需要检查
        if(checkList.size()!=0){
            for (Long taskId:checkList){
                //检查任务是否需要替换工人
                checkTaskAtCheckPoint(taskId,false);
            }
        }
    }

    /**
     * 创建检查点
     * @describe
     *              DDL也是一次检查点
     *              5天要检查一次
     *              大于等于十天的任务中间设一次检查点
     *              大于等于十五天的任务中间设两次检查点
     */
    @Override
//    @Scheduled(cron = "0 50 15 * * ?")
    public void createCheckPoint(long taskId) {
        //获取任务
        Task task = taskDao.getTask(taskId);
        //获取任务时间长度
        long difference = (task.getEndDate().getTime()-task.getBeginDate().getTime())/86400000;
        double time = Math.abs(difference);
        //设置检查点
        double judge = time/5;
        Calendar calendar = new GregorianCalendar();
        for(int i = 1;i<=judge;i++){
            //天数+5
            calendar.setTime(task.getBeginDate());
            calendar.add(Calendar.DATE,5*i);
            System.out.println(taskId);
            checkPointDao.createCheckPoint(taskId,calendar.getTime(),false);
        }
        //创建最后一个检查点
        checkPointDao.createCheckPoint(taskId,task.getEndDate(),true);
    }

    /**
     * 到达检查点开始检查当前任务订单的质量
     * @param taskId 任务Id
     * @param isEnd  是否是最后一个检查点
     * @describe 任务完成也是一个检查点
     */
    @Override
    public void checkTaskAtCheckPoint(long taskId,boolean isEnd) {
        //获取任务订单信息
        List<TaskOrder> taskOrderList = taskOrderDao.getAllTaskOrderOfATask(taskId);
        //将被替换的名单
        List<Long> replaceList = new ArrayList<>();
        //获取评分信息
        for(TaskOrder taskOrder:taskOrderList){
            //小于60为不合格，将被替换
            if(taskOrder.getRate()<60){
                //如果是最后一个检查点，则不需要替换工人，直接fail
                if(isEnd){
                    taskOrder.setSubmited(TaskOrderState.fail);
                    taskOrderDao.updateTaskOrder(taskOrder);
                }
                //如果不是最后一个检查点，则调用替换算法
                else
                    replaceList.add(taskOrder.getAcceptUserId());
            }
        }
        //如果是最后一个检查点，replaceList会是空的
        if (replaceList.size()>0) {
            //获取任务的等待列表
            List<Long> temp = appointDao.getAppointUser(taskId);
            //如果等待列表中有人则调用替换算法
            if(temp.size()>0)
                dataAnalysisService.replaceWorker(taskId, replaceList);
        }
    }

    @Override
    public JSONArray getMenuItems() {
        JSONObject homepage = new JSONObject();
        homepage.put("title", "首页");
        homepage.put("num", 0);
        homepage.put("home", true);

        int recTagNum = taskDao.getNumOfRecTask();
        JSONObject recTag = new JSONObject();
        recTag.put("title", "标框标注");
        recTag.put("num", recTagNum);

        int classifiedTagNum = taskDao.getNumOfClassifyTask();
        JSONObject classifyTag = new JSONObject();
        classifyTag.put("title", "分类标注");
        classifyTag.put("num", classifiedTagNum);

        int regionTagNum = taskDao.getNumOfRegionTask();
        JSONObject regionTag = new JSONObject();
        regionTag.put("title", "区域标注");
        regionTag.put("num", regionTagNum);

        int wholeTagNum = taskDao.getNumOfWholeTask();
        JSONObject wholeTag = new JSONObject();
        wholeTag.put("title", "整体标注");
        regionTag.put("num", wholeTagNum);

        int allTaskNum = taskDao.getNumOfAllTask();
        JSONObject allTask =  new JSONObject();
        allTask.put("title", "全部任务");
        allTask.put("num", allTaskNum);

        JSONObject instruction = new JSONObject();
        instruction.put("title", "使用教程");
        instruction.put("num", 0);
        instruction.put("sequare", true);

        JSONObject personSpace = new JSONObject();
        personSpace.put("title", "个人空间");
        personSpace.put("num", 0);
        personSpace.put("live", true);
        personSpace.put("link", "http://localhost:8888/#/personalSpace");

        JSONArray array = new JSONArray();
        array.put(homepage);
        array.put(recTag);
        array.put(classifyTag);
        array.put(regionTag);
        array.put(wholeTag);
        array.put(allTask);
        array.put(instruction);
        array.put(personSpace);

        return array;

    }

    @Override
    public void viewedTimeInc(long taskId) {
        taskDao.increaseViewedTimes(taskId);
    }

    @Override
    public Task getSimpleTask(long taskId) {
        return taskDao.getTask(taskId);
    }


    /**
     * 任务激励方法
     * @param task 需要被激励的任务信息
     * @return 修改后的任务信息 task
     * @describe 当任务长时间接取人数不到要求值的时候会调用此方法
     */
    private Task checkTask(Task task) {
        Date beginDate = task.getBeginDate();
        Date endDate = task.getEndDate();
        Date today = new Date();

        //任务已经结束
        if(today.getTime() >= endDate.getTime()) {
            task.setState(TaskState.finish);
            return task;
        }

        //任务已经被多人接取
        int acceptNum = taskOrderDao.getAcceptNum(task.getTaskId());
        double acceptPercent = (acceptNum * 1.0) / task.getMaxParticipator();
        if(acceptPercent > 0.3){
            task.setUpRate("");
            return task;
        }

        //已经长时间无人接取
        int taskLastDays = (int) ((endDate.getTime() - beginDate.getTime()) / (1000*60*60*24));
        int dayUntilNow = (int) ((today.getTime() - beginDate.getTime()) / (1000*60*60*24));
        double percent = (dayUntilNow * 1.0) / taskLastDays;

        //接近任务结束时间
        if((taskLastDays - dayUntilNow) <= 3){
            return task;
        }

        //更改任务奖励，起到激励效果
        if(percent > 0.3) {
            task.setReward(task.getReward() * 1.2);
            task.setUpRate("+20%");
        }else if(percent > 0.5) {
            task.setReward((task.getReward() / 1.2) * 1.4);
            task.setUpRate("+40%");
        }else if(percent > 0.8) {
            task.setReward((task.getReward() / 1.4) * 1.6);
            task.setUpRate("+60%");
        }

        return task;

    }

    private Task placeTaskTag(Task task) {
        String taskTagString = task.getTaskTagString();

        if(taskTagString == null || taskTagString.length() == 0)
            return task;

        String[] tagList = taskTagString.split(",");
        for(String tmpString : tagList) {
            switch (tmpString) {
                case "ANIMAL":
                    task.getTaskTags().add(TaskTag.ANIMAL);
                    break;
                case "HUMAN":
                    task.insertTag(TaskTag.HUMAN);
                    break;
                case "NATURE":
                    task.getTaskTags().add(TaskTag.NATURE);
                    break;
                case "DAILYSTUFF":
                    task.getTaskTags().add(TaskTag.DAILYSTUFF);
                    break;
                case "BUILDING":
                    task.getTaskTags().add(TaskTag.BUILDING);
                    break;
                case "TECHNOLOGY":
                    task.getTaskTags().add(TaskTag.TECHNOLOGY);
                    break;
                case "FOOD":
                    task.getTaskTags().add(TaskTag.FOOD);
                    break;
                case "TRAFFIC":
                    task.getTaskTags().add(TaskTag.TRAFFIC);
                    break;
                case "FURNITURE":
                    task.getTaskTags().add(TaskTag.FURNITURE);
                    break;
                case "INDUSTRY":
                    task.getTaskTags().add(TaskTag.INDUSTRY);
                    break;
                default:
                    task.getTaskTags().add(TaskTag.OTHER);
                    break;
            }
        }

        return task;
    }

    private List<Integer> getMostTagged(int input[]) {
        int out[] = new int[3];
        List<Integer> outList = new ArrayList<>();
        for(int i = 0; i < 3; ++i) {
            int k = 0;
            for (int j = 0; j < input.length; ++j) {
                if(k < input[j]){
                    k = input[j];
                    out[i] = j;
                }
            }

            input[out[i]] = 0;
        }

        for(int j = 0;j < 3;j++){
            outList.add(out[j]);
        }

        return outList;
    }

    private List<Task> taskFilter(List<Task> tasks, String tagNum) {
        TaskTag taskTag = null;
        if(tagNum.equals("0"))
            taskTag = TaskTag.ANIMAL;
        else if(tagNum.equals("1"))
            taskTag = TaskTag.HUMAN;
        else if(tagNum.equals("2"))
            taskTag = TaskTag.NATURE;
        else if(tagNum.equals("3"))
            taskTag = TaskTag.DAILYSTUFF;
        else if(tagNum.equals("4"))
            taskTag = TaskTag.BUILDING;
        else if(tagNum.equals("5"))
            taskTag = TaskTag.TECHNOLOGY;
        else if(tagNum.equals("6"))
            taskTag = TaskTag.FOOD;
        else if(tagNum.equals("7"))
            taskTag = TaskTag.TRAFFIC;
        else if(tagNum.equals("8"))
            taskTag = TaskTag.FURNITURE;
        else if(tagNum.equals("9"))
            taskTag = TaskTag.INDUSTRY;


        List<Task> retList = new ArrayList<>();
        for(Task tmp : tasks) {
            if(tmp.getTaskTags().contains(taskTag))
                retList.add(tmp);
        }

        return retList;

    }

    /**
     *
     * @param tasks
     * @param user
     * @return
     */
    private List<Task> recommendByProfession(List<Task> tasks, User user) {
        List<TaskTag> taskTagList = new ArrayList<>();
        List<Task> retlist = new ArrayList<>();
        if(user.getProfession() == null) {
            return retlist;
        }
        if(user.getProfession().equals(UserProfession.ENGINEER)) {
            taskTagList.add(TaskTag.INDUSTRY);
            taskTagList.add(TaskTag.BUILDING);
        }else if(user.getProfession().equals(UserProfession.DESIGNER)) {
            taskTagList.add(TaskTag.DAILYSTUFF);
            taskTagList.add(TaskTag.FURNITURE);
            taskTagList.add(TaskTag.TECHNOLOGY);
        }else if(user.getProfession().equals(UserProfession.CHEF)) {
            taskTagList.add(TaskTag.FOOD);
        }else {

            return retlist;
        }

        for(Task task : tasks) {
            if(retlist.size() >= 5)
                return retlist;

            List<TaskTag> tagsOfTask = task.getTaskTags();
            if(have_common_tag(tagsOfTask, taskTagList))
                retlist.add(task);
        }

        return retlist;
    }

    /**
     * 判断两个taskTag的列表是否有交集
     * @param list1 列表1
     * @param list2 列表2
     * @return 判断结果
     */
    private static boolean have_common_tag(List<TaskTag> list1, List<TaskTag> list2) {
        for(TaskTag taskTag : list2) {
            if(list1.contains(taskTag))
                return true;
        }

        return false;
    }

}
