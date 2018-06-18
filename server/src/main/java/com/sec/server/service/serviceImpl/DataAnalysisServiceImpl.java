package com.sec.server.service.serviceImpl;

import com.sec.server.domain.Message;
import com.sec.server.model.PersonalDataModel;
import com.sec.server.model.TaskOrderMessage;
import com.sec.server.repository.AppointDao;
import com.sec.server.repository.TaskDao;
import com.sec.server.repository.TaskOrderDao;
import com.sec.server.repository.UserDao;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.domain.User;
import com.sec.server.enums.TaskOrderState;
import com.sec.server.service.AppointService;
import com.sec.server.service.DataAnalysisService;
import com.sec.server.service.MessageService;
import com.sec.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service(value="dataAnalysisService")
public class DataAnalysisServiceImpl implements DataAnalysisService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TaskOrderDao taskOrderDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private AppointDao appointDao;

    @Resource(name = "messageService")
    private MessageService messageService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "appointService")
    private AppointService appointService;

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
        double workerNumber = task.getMaxParticipator();
        //时间      权重：0.048
        double time = 0;
        //标注类型  权重：0.208
        double type = 0;
        //图片数量  权重：0.643
        double pictureNumber = task.getImgUrls().size();

        long difference = (task.getEndDate().getTime()-task.getBeginDate().getTime())/86400000;
        time = Math.abs(difference);

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
        User user;
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

        //获得任务名字
        Task task = taskDao.getTask(taskId);
        String taskName = task.getTaskname();

        //获得所有工人订单
        List<TaskOrder> list = taskOrderDao.getAllTaskOrderOfATask(taskId);

        //工人被替换信息
        Message messageToChangeWorked = new Message();
        Message messageToNewWorker = new Message();

        //从所有订单中找到被替换工人的订单
        for (TaskOrder aList : list) {
            if (replacedWorkerList.contains(aList.getAcceptUserId())) {
                //修改数据库中被替换工人的订单的状态
                aList.setSubmited(TaskOrderState.fail);
                taskOrderDao.updateTaskOrder(aList);
                //通知工人已经被替换
                messageToChangeWorked.setUserId(aList.getAcceptUserId());
                messageToChangeWorked.setMessageInfo("您因为完成评分太低等原因被替换出任务，感谢您曾经为此任务做出的贡献。"+"任务名称："+taskName);
                messageToChangeWorked.setTitle("任务通知");
                messageToChangeWorked.setRead(false);
                messageService.addMessage(messageToChangeWorked);
                //扣减信用积分
                userService.pointDrop(aList.getAcceptUserId());
            }
        }

        //获取等待中的工人列表
        List<Long> waitingList = appointDao.getAppointUser(taskId);
        //对工人进行排序
        waitingList = appointService.sortListByHonorMessage(waitingList,task.getAnnotationType());
        //从等待列表中选择等待工人补足
        for(int i = 0;i<replacedWorkerList.size();i++){
            //新建任务订单
            TaskOrder taskOrder = new TaskOrder();

            taskOrder.setAcceptUserId(waitingList.get(i));
            taskOrder.setBeginDate(new Date());
            taskOrder.setEndDate(list.get(0).getEndDate());
            taskOrder.setSubmited(TaskOrderState.unSubmitted);
            taskOrder.setTaskId(taskId);

            //通知工人已被选为新工人
            messageToNewWorker.setUserId(taskOrder.getAcceptUserId());
            messageToNewWorker.setMessageInfo("您被从等待中正式录用，感谢您的耐心等待，祝您完成任务顺利。任务名称："+taskName);
            messageToNewWorker.setTitle("任务通知");
            messageToNewWorker.setRead(false);
            messageService.addMessage(messageToNewWorker);

            //新建对应新工人的任务订单
            taskOrderDao.insertTaskOrder(taskOrder);
        }
    }

    @Override
    public PersonalDataModel getPersonalDataModel(long userId) {
        int point = userDao.getPoint(userId);
        int ongoingTaskNum = taskOrderDao.getAllOngoing(userId).size();
        String rank = null;
        List<Integer> pointList = userDao.getAllPoints();
        Collections.sort(pointList);

        int count = 0;
        for(int i = 0;i < pointList.size(); ++i) {
            if(point > pointList.get(i))
                count++;
        }

        if(count <= 100)
            rank = "第" + count + "名";

        else {
            double percentage = count / pointList.size();
            DecimalFormat df = new DecimalFormat("0.00");
            rank = "前" + df.format(percentage * 100) + " %";
        }

        PersonalDataModel personalDataModel = new PersonalDataModel(point, ongoingTaskNum, rank);

        return personalDataModel;
    }

    @Override
    public List<TaskOrderMessage> getTaskOrderMessage(long taskId) {
        Task task = taskDao.getTask(taskId);
        List<TaskOrderMessage> messageList = new ArrayList<>();
        if(task != null) {
            List<TaskOrder> taskOrderList = taskOrderDao.getTaskOrderByTaskId(taskId);
            for(TaskOrder tmp : taskOrderList) {
                TaskOrderMessage message = new TaskOrderMessage();
                DecimalFormat df = new DecimalFormat("#0.00");

                String percentage = df.format((tmp.getFinishedPics() * 1.0) / task.getImgUrls().size() );
                message.setPercentage(Double.parseDouble(percentage));

                String username = userDao.getUserById(tmp.getAcceptUserId()).getUsername();
                message.setAcceptUsername(username);

                message.setState(tmp.getSubmited());
                message.setTaskOrderId(tmp.getTaskOrderId());

                messageList.add(message);
            }
        }
        return messageList;
    }
}