package com.sec.server.service.serviceImpl;

import com.sec.server.enums.TaskState;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.AppointDao;
import com.sec.server.enums.AnnotationType;
import com.sec.server.repository.TaskDao;
import com.sec.server.repository.TaskOrderDao;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.TaskOrderState;
import com.sec.server.model.TaskOrderModel;
import com.sec.server.service.TaskOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "taskOrderService")
public class TaskOrderServiceImpl implements TaskOrderService {
    @Autowired
    private TaskOrderDao taskOrderDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private AppointDao appointDao;

    /**
     * 通过Id获取任务订单
     * @param taskOrderId 任务订单Id
     * @return 任务订单 taskOrder
     */
    @Override
    public TaskOrder getTaskOrderById(long taskOrderId) {
        return taskOrderDao.getTaskOrder(taskOrderId);
    }

    /**
     * 获取工人所有的任务订单
     * @param userId 工人Id
     * @return 任务订单列表 list
     * @describe
     *              需要在这里组装
     *              传回去的任务订单列表包括：
     *              1、taskOrder表中已经创建的任务订单
     *              2、appoint表中预约的任务(如果task还在预约期)
     *              3、appoint表中等待的任务(如果task不在预约期)
     */
    @Override
    public List<TaskOrderModel> getAllTaskOrder(long userId) {
        //返回的数据
        List<TaskOrderModel> taskOrderModels = new ArrayList<>();
        //设置完成度为一位小数
        DecimalFormat fmt = new DecimalFormat("##0.0");
        //获取taskOrder表中所有任务
        List<TaskOrder> taskOrderList = taskOrderDao.getAllTaskOrder(userId);
        //操作taskOrder表的信息
        for(TaskOrder tmp : taskOrderList) {
            TaskOrderModel taskOrderModel = new TaskOrderModel(tmp);
            Task task = taskDao.getTask(tmp.getTaskId());
            taskOrderModel.setTaskName(task.getTaskname());
            double completion = (tmp.getFinishedPics() * 1.0) / task.getImgUrls().size();
            taskOrderModel.setDegreeOfCompletion(fmt.format(completion));

            taskOrderModels.add(taskOrderModel);
        }
        //获取appoint表中的任务
        List<Long> taskIdList = appointDao.getAppointTask(userId);
        //创建对应的任务信息
        for (Long aTaskId:taskIdList) {
            Task task = taskDao.getTask(aTaskId);
            TaskOrderModel taskOrderModel = new TaskOrderModel();
            taskOrderModel.setAcceptUserId(userId);
            taskOrderModel.setBeginDate(task.getBeginDate());
            taskOrderModel.setEndDate(task.getEndDate());
            taskOrderModel.setDegreeOfCompletion("0.0");
            taskOrderModel.setLastPic(0);
            taskOrderModel.setRate(0);
            taskOrderModel.setTaskId(task.getTaskId());
            taskOrderModel.setTaskName(task.getTaskname());
            taskOrderModel.setTaskOrderId(0);
            //判断任务是否已经开始
            switch (task.getState()){
                case appoint:
                    taskOrderModel.setSubmited(TaskOrderState.appoint);
                    break;
                case ongoing:
                    taskOrderModel.setSubmited(TaskOrderState.waiting);
                    break;
            }
            taskOrderModels.add(taskOrderModel);
        }


        return taskOrderModels;
    }

    /**
     * 创建任务订单
     * @param taskOrder 任务订单信息
     */
    @Override
    public void createTaskOrder(TaskOrder taskOrder) {
        Task task = taskDao.getTask(taskOrder.getTaskId());
        taskOrder.setBeginDate(new Date());
        //taskOrder.setEndDate(task.getEndDate());
        taskOrder.setSubmited(TaskOrderState.unSubmitted);
        taskOrderDao.insertTaskOrder(taskOrder);
    }

    /**
     * 更新任务订单
     * @param taskOrder 新的任务订单信息
     */
    @Override
    public void updateTaskOrder(TaskOrder taskOrder) {
        taskOrderDao.updateTaskOrder(taskOrder);
    }

    /**
     * 删除任务订单或取消预约任务
     * @param taskOrderId 任务订单Id
     */
    @Override
    public void deleteTaskOrder(long taskOrderId) {
        taskOrderDao.deleteTaskOrder(taskOrderId);
    }

    /**
     * 获得工人所有提交的任务订单
     * @param userId 工人Id
     * @return 任务订单列表 list
     */
    @Override
    public List<TaskOrder> getAllSubmited(long userId) {
        return taskOrderDao.getAllSubmited(userId);
    }

    /**
     * 工人提交任务订单
     * @param taskOrderId 任务订单Id
     */
    @Override
    public void submittedTaskOrder(long taskOrderId) {
        taskOrderDao.changeTaskOrderState(taskOrderId,TaskOrderState.submitted);
    }

    @Override
    public int getNumOfDiffenentType(long userId, AnnotationType annotationType) {
        int num = taskOrderDao.getAllFinishedTaskOrderOfAType(userId, annotationType).size();

        return num;
    }


    @Override
    public void appointTask(long taskId, long userId) {
        Task task = taskDao.getTask(taskId);
        if(task.getState()==TaskState.finish){
            throw new ResultException("任务已经结束了！",13333);
        }
        if(taskOrderDao.getAcceptNum(taskId)<task.getMaxParticipator()&&task.getState()==TaskState.ongoing){
            TaskOrder taskOrder = new TaskOrder();
            taskOrder.setLastPic(1);
            taskOrder.setTaskId(taskId);
            taskOrder.setAcceptUserId(userId);
            taskOrder.setBeginDate(new Date());
            taskOrder.setFinishedPics(0);
            taskOrder.setSubmited(TaskOrderState.unSubmitted);
            taskOrderDao.insertTaskOrder(taskOrder);
        }else{
            appointDao.insertAppointMessage(taskId,userId);
        }
    }

}
