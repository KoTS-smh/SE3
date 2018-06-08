package com.sec.server.service.serviceImpl;

import com.sec.server.dao.TaskDao;
import com.sec.server.dao.TaskOrderDao;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.model.TaskOrderModel;
import com.sec.server.service.TaskOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service(value = "taskOrderService")
public class TaskOrderServiceImpl implements TaskOrderService {
    @Autowired
    TaskOrderDao taskOrderDao;
    @Autowired
    TaskDao taskDao;

    @Override
    public TaskOrder getTaskOrderById(long taskOrderId) {
        return taskOrderDao.getTaskOrder(taskOrderId);
    }

    @Override
    public List<TaskOrderModel> getAllTaskOrder(long userId) {
        DecimalFormat fmt = new DecimalFormat("##0.0");
        List<TaskOrder> taskOrderList = taskOrderDao.getAllTaskOrder(userId);
        List<TaskOrderModel> taskOrderModels = new ArrayList<>();
        for(TaskOrder tmp : taskOrderList) {
            TaskOrderModel taskOrderModel = new TaskOrderModel(tmp);
            Task task = taskDao.getTask(tmp.getTaskId());
            taskOrderModel.setTaskName(task.getTaskname());
            double completion = (tmp.getFinishedPics() * 1.0) / task.getImgUrls().size();
            taskOrderModel.setDegreeOfCompletion(fmt.format(completion));

            taskOrderModels.add(taskOrderModel);
        }
        return taskOrderModels;
    }

    @Override
    public void createTaskOrder(TaskOrder taskOrder) {
        Task task = taskDao.getTask(taskOrder.getTaskId());
        taskOrder.setBeginDate(task.getBeginDate());
        taskOrder.setEndDate(task.getEndDate());
        taskOrderDao.insertTaskOrder(taskOrder);
    }

    @Override
    public void updateTaskOrder(TaskOrder taskOrder) {
        taskOrderDao.updateTaskOrder(taskOrder);
    }

    @Override
    public void deleteTaskOrder(long taskOrderId) {
        taskOrderDao.deleteTaskOrder(taskOrderId);
    }

    @Override
    public List<TaskOrder> getAllSubmited(long userId) {
        List<TaskOrder> list = taskOrderDao.getAllSubmited(userId);
        return list;
    }
}
