package com.sec.server.service.serviceImpl;

import com.sec.server.domain.*;
import com.sec.server.repository.*;
import com.sec.server.enums.*;
import com.sec.server.exception.ResultException;
import com.sec.server.model.Picture_CardModel;
import com.sec.server.model.TaskModel;
import com.sec.server.service.DataAnalysisService;
import com.sec.server.service.MessageService;
import com.sec.server.service.TaskService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private HonorDao honorDao;

    @Autowired
    private WaitingDao waitingDao;

    @Autowired
    private MessageService messageService;

    private DataAnalysisService dataAnalysisService;

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
        if(urlLists.size() * maxParticipator * 0.02 < 10){
            cost = 10;
        }else {
            cost = urlLists.size() * maxParticipator * 0.02;
        }


        long userId = task.getPostUserId();
        double currentBalance = userDao.getBalance(userId);
        if(currentBalance < cost) {
            throw new ResultException("余额不足", 12222);
        }
        userDao.consume(cost, userId);
        taskDao.addTask(task);
        imgUrlDao.insertUrlList(urlLists, task.getTaskId());


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
            taskDao.increaseViewedTimes(taskId);
        }catch (Exception e){
            throw new ResultException("任务不存在！", 1111);
        }
        TaskModel taskModel = new TaskModel(task);
        taskModel.setAcceptUserIds(acceptUserList);
        return taskModel;
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

        //所有用户没有接过的任务
        for(Task tmp : tasksNotAccept) {
            tmp = placeTaskTag(tmp);
            if(acceptedTasks.contains(tmp.getTaskId()))
                tasksNotAccept.remove(tmp);
        }

        List<Task> recommmendTasks = new ArrayList<>();

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
        if(taskType.equals("all")){

            List<Task> taskList = taskDao.searchForAllTasks(message);

            for(Task tmp : taskList) {
                tmp = placeTaskTag(tmp);
            }

            if(tag.equals("请选择")) {
                //do nothing
            }else {
                taskList = taskFilter(taskList, tag);
            }
            if(taskList.size() > 0) {
                for(Task task : taskList) {
                    list.add(new Picture_CardModel(task));
                }
            }

        }
       return list;
    }

    /**
     * 任务结算方法 todo
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
        messageService.addMessage(message);

        //获取所有的工人任务
        TaskOrder taskOrder;
        List<TaskOrder> list = taskOrderDao.getAllTaskOrderOfATask(taskId);

        for (TaskOrder aList : list) {
            taskOrder = aList;

            //质量审核 todo 如果不达标submited会变成fail，否则就会是submitted

            //操作通过的任务订单
            switch (taskOrder.getSubmited()) {
                case submitted:
                    //把任务状态修改为success
                    taskOrder.setSubmited(TaskOrderState.finish);
                    taskOrderDao.updateTaskOrder(taskOrder);
                    //通知工人通过
                    Message messageToWorker = new Message();
                    messageToWorker.setMessageInfo("您的任务订单成功完成。任务名称："+task.getTaskname());
                    messageToWorker.setTitle("任务通知");
                    messageService.addMessage(messageToWorker);
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
                    messageService.addMessage(messageToFailWorker);
                    //计算得分 todo
                    break;
                default:
                    break;
            }

            //修改发布者余额
            dataAnalysisService.modifyCurrency(-task.getReward()*passNumber,task.getPostUserId());

        }

    }

    /**
     * 系统定时检查任务DDL方法
     * @describe 每天零点调用一次
     */
    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void endTask() {
        //获取所有未完成的任务
        List<Task> list = taskDao.getEveryUnFinishedTask();

        //获取当前时间
        Date ddl = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = format.format(ddl);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(ddl);
        Calendar calendar2 = Calendar.getInstance();

        //判断任务是否到达DDL
        for (Task aList : list) {
            calendar2.setTime(aList.getEndDate());
            if (calendar1.getTime().equals(calendar2.getTime())) {
                //到达DDL则结算任务
                finishTask(aList.getTaskId());
            }
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
            task.setReward(task.getReward() * 1.4);
            task.setUpRate("+40%");
        }else if(percent > 0.8) {
            task.setReward(task.getReward() * 1.6);
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
