package com.sec.server.service.serviceImpl;

import com.sec.server.dao.ImgUrlDao;
import com.sec.server.dao.TaskDao;
import com.sec.server.dao.TaskOrderDao;
import com.sec.server.dao.UserDao;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.domain.User;
import com.sec.server.enums.ResultCode;
import com.sec.server.enums.TaskTag;
import com.sec.server.enums.UserProfession;
import com.sec.server.exception.ResultException;
import com.sec.server.model.Picture_CardModel;
import com.sec.server.model.TaskModel;
import com.sec.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    @Override
    public void createTask(Task task) {
        List<String> urlLists = task.getImgUrls();

        taskDao.addTask(task);
        imgUrlDao.insertUrlList(urlLists, task.getTaskId());


    }

    @Override
    public void updataTask(Task task) {
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
            if(item.getSubmited()) {
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
            for(int i = 0;i < taskTags.size(); ++i) {
                tagCounts[taskTags.get(i).ordinal()]++;
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

    private Task checkTask(Task task) {
        Date beginDate = task.getBeginDate();
        Date endDate = task.getEndDate();
        Date today = new Date();

        //任务已经结束
        if(today.getTime() >= endDate.getTime()) {
            task.setFinished(true);
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
