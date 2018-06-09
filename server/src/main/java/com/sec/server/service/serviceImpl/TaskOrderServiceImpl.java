package com.sec.server.service.serviceImpl;

import com.sec.server.dao.TaskDao;
import com.sec.server.dao.TaskOrderDao;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.TaskOrderState;
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
     */
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

    /**
     * 创建任务订单
     * @param taskOrder 任务订单信息
     */
    @Override
    public void createTaskOrder(TaskOrder taskOrder) {
        Task task = taskDao.getTask(taskOrder.getTaskId());
        taskOrder.setBeginDate(task.getBeginDate());
        taskOrder.setEndDate(task.getEndDate());
        taskOrder.setSubmited(TaskOrderState.appoint);
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

    @Override
    public void endAppointment(long taskId) {
        List<TaskOrder> list = taskOrderDao.getAllTaskOrderOfATask(taskId);
        for (TaskOrder aList : list) {
            taskOrderDao.changeTaskOrderState(aList.getTaskOrderId(),TaskOrderState.unSubmitted);
        }
    }
}
