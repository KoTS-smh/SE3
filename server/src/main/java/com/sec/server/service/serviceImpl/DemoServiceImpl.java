package com.sec.server.service.serviceImpl;

import com.sec.server.dao.UserDao;
import com.sec.server.dao.TaskOrderDao;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.domain.User;
import com.sec.server.enums.AnnotationType;
import com.sec.server.enums.TaskOrderState;
import com.sec.server.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.util.Date;
import java.util.List;

public class DemoServiceImpl implements DemoService {

    @Autowired
    private UserDao userDao;
    private TaskOrderDao taskOrderDao;

    /**
     * 计算任务的最小推荐金额
     * @param task 任务
     * @return 最小推荐金额
     * @describe 在发布者发布任务的时候系统会调用此方法
     *             计算出任务的最小推荐金额
     */
    @Override
    public double calculateMinimumMoneyOfTask(Task task){

        //工人数量  权重：0.101
        double workerNumber = 0;
        //时间      权重：0.048
        double time = 0;
        //标注类型  权重：0.208
        double type = 0;
        //图片数量  权重：0.643
        double pictureNumber = 0;

        switch (task.getAnnotationType()){
            case option1:
                type = 1;
                break;
            case option2:
                type = 2;
                break;
            case option3:
                type = 3;
                break;
            case option4:
                type = 4;
                break;
        }

        return workerNumber*0.101/10+time*0.048/7+type*0.208/4+pictureNumber*0.643/100;
    }

    /**
     * 修改数据库货币
     * @param changeCurrency 更改金额
     * @param userId 用户Id
     * @describe 在以下情况下会调用该方法：
     *             1、发布者发布任务缴纳订金
     *             2、发布者结算任务支付工人工资
     *             3、工人任务工资结算
     */
    @Override
    public void modifyCurrency(double changeCurrency, long userId) {
        //获取要修改的用户
        User user = new User();
        user = userDao.getUserById(userId);

        //修改余额
        user.setBalance(user.getBalance() + changeCurrency);

        //修改数据库
        userDao.updateUser(user);
    }

    /**
     * 工人替换算法
     * @param taskId 任务Id
     * @param replacedWorkerList 需要被替换的工人
     * @describe 在以下情况会调用该方法：
     *              （前提）
     *              1、任务存在等待接取任务的人
     *              2、在检查点有工人的质量不达标
     */
    @Override
    public void replaceWorker(long taskId,List<Long> replacedWorkerList) {

        //获得所有工人订单
        List<TaskOrder> list = taskOrderDao.getAllTaskOrderOfATask(taskId);

        //通知工人已经被替换 todo

        //修改数据库中任务订单的状态
        for (TaskOrder aList : list) {
            if (replacedWorkerList.contains(aList.getAcceptUserId()))
                aList.setSubmited(TaskOrderState.fail);
                taskOrderDao.updateTaskOrder(aList);
        }

        //从等待列表中选择等待工人补足
        List<Long> waitingWorkerList = taskOrderDao.getWaitingWorker(taskId);

        for(int i = 0;i<replacedWorkerList.size();i++){
            TaskOrder taskOrder = new TaskOrder();

            taskOrder.setAcceptUserId(waitingWorkerList.get(i));
            taskOrder.setBeginDate(new Date());
            taskOrder.setEndDate(list.get(0).getEndDate());
            taskOrder.setFinishedPics(0);
            taskOrder.setLastPic(0);
            taskOrder.setRate(0);
            taskOrder.setSubmited(TaskOrderState.unSubmitted);
            taskOrder.setTaskId(taskId);

            //新建对应新工人的任务订单
            taskOrderDao.insertTaskOrder(taskOrder);
        }

        //通知工人已被选为新工人 todo
    }

}