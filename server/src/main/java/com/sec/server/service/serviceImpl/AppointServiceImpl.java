package com.sec.server.service.serviceImpl;

import com.sec.server.domain.HonorMessage;
import com.sec.server.domain.Message;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.AnnotationType;
import com.sec.server.enums.TaskOrderState;
import com.sec.server.enums.TaskState;
import com.sec.server.repository.*;
import com.sec.server.service.AppointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service(value = "appointService")
public class AppointServiceImpl implements AppointService {
    @Autowired
    private AppointDao appointDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private HonorDao honorDao;
    @Autowired
    private TaskOrderDao taskOrderDao;
    @Autowired
    private MessageDao messageDao;

    /**
     * 预约任务
     * @param taskId 任务Id
     * @param userId 工人Id
     */
    @Override
    public void appointTask(long taskId, long userId) {
        appointDao.insertAppointMessage(taskId,userId);
    }

    /**
     * 取消预约任务
     * @param taskId 任务Id
     * @param userId 工人Id
     */
    @Override
    public void cancelAppoint(long taskId, long userId) {
        appointDao.deleteAppointMessage(taskId,userId);
    }

    /**
     * 任务开始进行
     * @param taskId 任务Id
     * @describe 基本步骤：
     *                      1、从预约的人中挑选工人，其他人加入等待列表
     *                      2、给工人创建taskOrder
     *                      3、修改任务属性
     *                      4、通知发布者和工人
     */
    @Override
    public void endAppointment(long taskId) {
        //获取所有预约的用户
        List<Long> userList = appointDao.getAppointUser(taskId);
        //获取任务信息
        Task task = taskDao.getTask(taskId);
        //判断人数是否超过预设
        int number = task.getMaxParticipator();
        //工人预约通知
        Message messageToAppointSuccess = new Message();
        Message messageToWaitingList = new Message();
        //如果超过预设
        if(userList.size()>number) {
            //根据类型积分排序
            userList = sortListByHonorMessage(userList, task.getAnnotationType());
            for(int i = number;i<userList.size();i++){
                //通知工人被加入等待列表
                messageToWaitingList.setUserId(userList.get(i));
                messageToWaitingList.setMessageInfo("您已经被加入等待列表，请耐心等待替换或者预约新的任务。任务名称："+task.getTaskname());
                messageToWaitingList.setTitle("任务通知");
                messageToAppointSuccess.setRead(false);
                messageDao.insertMessage(messageToWaitingList);
            }
        }

        //获取工人
        for(int i = 0;i<number;i++){
            long userId = userList.get(i);
            //删除数据库中预约信息
            appointDao.deleteAppointMessage(taskId,userId);
            //创建taskOrder
            TaskOrder taskOrder = new TaskOrder();
            taskOrder.setTaskId(taskId);
            taskOrder.setAcceptUserId(userId);
            taskOrder.setBeginDate(task.getBeginDate());
            taskOrder.setEndDate(task.getEndDate());
            taskOrder.setSubmited(TaskOrderState.unSubmitted);
            taskOrderDao.insertTaskOrder(taskOrder);
            //通知工人任务开始
            messageToAppointSuccess.setUserId(userId);
            messageToAppointSuccess.setMessageInfo("您已经成功预约任务，祝您完成任务顺利。任务名称："+task.getTaskname());
            messageToAppointSuccess.setTitle("任务通知");
            messageToAppointSuccess.setRead(false);
            messageDao.insertMessage(messageToAppointSuccess);
        }

        //通知发布者任务正式开始
        Message message = new Message();
        message.setRead(false);
        message.setTitle("任务通知");
        message.setMessageInfo("您发布的任务已经成功开始。任务名称："+task.getTaskname());
        message.setUserId(task.getPostUserId());
        messageDao.insertMessage(message);

        //修改任务状态
        task.setState(TaskState.ongoing);
        taskDao.updateTask(task);

    }

    /**
     * 获取任务所有预约的工人Id
     * @param taskId 任务Id
     * @return 工人Id
     */
    @Override
    public List<Long> getAppointUser(long taskId) {
        return appointDao.getAppointUser(taskId);
    }

    /**
     * 获取工人预约的所有任务
     * @param userId 工人Id
     * @return 任务Id
     */
    @Override
    public List<Long> getAppointTask(long userId) {
        return appointDao.getAppointTask(userId);
    }

    /**
     * 工人荣誉度排序（高到低）
     * @param list 工人列表
     * @param annotationType 荣誉类型
     * @return 排序后工人列表 list
     */
    public List<Long> sortListByHonorMessage(List<Long> list,AnnotationType annotationType){
        List<HonorMessage> honorMessageList = new ArrayList<>();

        for (Long aList : list) {
            honorMessageList.add(honorDao.getHonorMessage(aList));
        }

        //从低到高排序
        switch (annotationType){
            case option1:
                honorMessageList.sort(Comparator.comparing(HonorMessage::getFrameTagPoint));
                break;
            case option2:
                honorMessageList.sort(Comparator.comparing(HonorMessage::getClassifyTagPoint));
                break;
            case option3:
                honorMessageList.sort(Comparator.comparing(HonorMessage::getRegionTagPoint));
                break;
            case option4:
                honorMessageList.sort(Comparator.comparing(HonorMessage::getWholeTagPoint));
                break;
        }

        //倒过来
        List<Long> newList = new ArrayList<>();
        for(int i = honorMessageList.size()-1;i>=0;i--){
            newList.add(honorMessageList.get(i).getUserId());
        }

        return newList;
    }
}
