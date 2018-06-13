package com.sec.server.service.serviceImpl;

import com.sec.server.domain.HonorMessage;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.AnnotationType;
import com.sec.server.repository.HonorDao;
import com.sec.server.repository.TaskOrderDao;
import com.sec.server.repository.UserDao;
import com.sec.server.service.HonorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class HonorServiceImpl implements HonorService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TaskOrderDao taskOrderDao;

    @Autowired
    private HonorDao honorDao;

    /**
     * 计算用户在标框标注上的荣誉
     * @param userId 用户Id
     * @describe
     *          荣誉分级：
     *              1级：白色   任务数>=0      评分>=0
     *              2级：蓝色   任务数>=10     评分>=60
     *              3级：紫色   任务数>=50     评分>=65
     *              4级：红色   任务数>=100    评分>=70
     *              5级：黄色   任务数>=200    评分>=75
     *
     */
    @Override
    public void honorOfFrameTag(long userId) {
        //获取工人所有完成的标框任务订单
        List<TaskOrder> list = taskOrderDao.getAllFinishedTaskOrderOfAType(userId,AnnotationType.option1);
        //获取工人当前荣誉等级
        HonorMessage honorMessage = honorDao.getHonorMessage(userId);
        int frameTagLevel = honorMessage.getFrameTagLevel();
        //计算当前评分
        double point = calculatePoint(list);
        honorMessage.setFrameTagPoint(point);
        //判断要怎么处理等级
        //      0   降级
        //      1   不变
        //      2   升级
        int handle = judgePoint(frameTagLevel,point,list.size());
        switch (handle){
            case 0:
                if(frameTagLevel>1)
                    honorMessage.setFrameTagLevel(frameTagLevel-1);
                break;
            case 1:
                break;
            case 2:
                if(frameTagLevel<5)
                    honorMessage.setFrameTagLevel(frameTagLevel+1);
                break;
        }
        //修改工人数据库的荣誉等级
        honorDao.setTagLevel(honorMessage);
    }

    @Override
    public void honorOfClassifyTag(long userId) {
        //获取工人所有完成的标框任务订单
        List<TaskOrder> list = taskOrderDao.getAllFinishedTaskOrderOfAType(userId,AnnotationType.option2);
        //获取工人当前荣誉等级
        HonorMessage honorMessage = honorDao.getHonorMessage(userId);
        int classifyTagLevel = honorMessage.getClassifyTagLevel();
        //计算当前评分
        double point = calculatePoint(list);
        honorMessage.setClassifyTagPoint(point);
        //判断要怎么处理等级
        //      0   降级
        //      1   不变
        //      2   升级
        int handle = judgePoint(classifyTagLevel,point,list.size());
        switch (handle){
            case 0:
                if(classifyTagLevel>1)
                    honorMessage.setClassifyTagLevel(classifyTagLevel-1);
                break;
            case 1:
                break;
            case 2:
                if(classifyTagLevel<5)
                    honorMessage.setClassifyTagLevel(classifyTagLevel+1);
                break;
        }
        //修改工人数据库的荣誉等级
        honorDao.setTagLevel(honorMessage);
    }

    @Override
    public void honorOfWholeTag(long userId) {
        //获取工人所有完成的标框任务订单
        List<TaskOrder> list = taskOrderDao.getAllFinishedTaskOrderOfAType(userId,AnnotationType.option4);
        //获取工人当前荣誉等级
        HonorMessage honorMessage = honorDao.getHonorMessage(userId);
        int wholeTagLevel = honorMessage.getWholeTagLevel();
        //计算当前评分
        double point = calculatePoint(list);
        honorMessage.setWholeTagPoint(point);
        //判断要怎么处理等级
        //      0   降级
        //      1   不变
        //      2   升级
        int handle = judgePoint(wholeTagLevel,point,list.size());
        switch (handle){
            case 1:
                break;
            case 0:
                if(wholeTagLevel>1)
                    honorMessage.setWholeTagLevel(wholeTagLevel-1);
                break;
            case 2:
                if(wholeTagLevel<5)
                    honorMessage.setWholeTagLevel(wholeTagLevel+1);
                break;
        }
        //修改工人数据库的荣誉等级
        honorDao.setTagLevel(honorMessage);
    }

    @Override
    public void honorRegionTag(long userId) {
        //获取工人所有完成的标框任务订单
        List<TaskOrder> list = taskOrderDao.getAllFinishedTaskOrderOfAType(userId,AnnotationType.option3);
        //获取工人当前荣誉等级
        HonorMessage honorMessage = honorDao.getHonorMessage(userId);
        int regionTagLevel = honorMessage.getRegionTagLevel();
        //计算当前评分
        double point = calculatePoint(list);
        honorMessage.setRegionTagPoint(point);
        //判断要怎么处理等级
        //      0   降级
        //      1   不变
        //      2   升级
        int handle = judgePoint(regionTagLevel,point,list.size());
        switch (handle){
            case 0:
                if(regionTagLevel>1)
                    honorMessage.setRegionTagLevel(regionTagLevel-1);
                break;
            case 1:
                break;
            case 2:
                if(regionTagLevel<5)
                    honorMessage.setRegionTagLevel(regionTagLevel+1);
                break;
        }
        //修改工人数据库的荣誉等级
        honorDao.setTagLevel(honorMessage);
    }

    @Override
    public void honorTotal(long userId) {

        //获取工人当前荣誉等级
        HonorMessage honorMessage = honorDao.getHonorMessage(userId);
        int totalLevel = honorMessage.getTotalLevel();

        //综合四个荣誉等级给予评定
        ArrayList<Integer> list = new ArrayList<>();
        list.add(honorMessage.getClassifyTagLevel());
        list.add(honorMessage.getFrameTagLevel());
        list.add(honorMessage.getRegionTagLevel());
        list.add(honorMessage.getWholeTagLevel());

        //计数器，如果超过两个等级超过当前总体荣誉等级，则更改总体荣誉等级
        int count = 0;
        for (Integer aList : list) {
            if (aList > totalLevel)
                count++;
        }

        if (count>=2) {
            totalLevel++;
            honorMessage.setTotalLevel(totalLevel);
        }

        //修改工人数据库的荣誉等级
        honorDao.setTagLevel(honorMessage);
    }

    @Override
    public double calculateTypePoint(long userId, AnnotationType annotationType) {
        HonorMessage honorMessage = honorDao.getHonorMessage(userId);

        switch (annotationType){
            case option1:
                return honorMessage.getFrameTagPoint();
            case option2:
                return honorMessage.getClassifyTagPoint();
            case option3:
                return honorMessage.getRegionTagPoint();
            case option4:
                return honorMessage.getWholeTagPoint();
        }

        return 0;
    }

    /**
     * 创建荣誉信息
     * @param userId 用户Id
     * @describe 每注册一个用户就新建一个对应的荣誉信息
     */
    @Override
    public void createHonorMessage(long userId) {
        honorDao.insertUserHonor(userId);
        System.out.print(userId+"success");
    }

    /**
     * 统计分数信息
     * @param list 任务订单列表
     * @return 评分 point
     */
     private double calculatePoint(List<TaskOrder> list){
        int count = 0;
        for (TaskOrder aList : list) {
            count += aList.getRate();
        }
        return count/list.size();
    }

    /**
     * 判断分数是否
     * @param level 用户原等级
     * @param point 用户当前分数
     * @return 等级判断
     *              0   降级
     *              1   不变
     *              2   升级
     * @describe 可能出现的情况
     *              1、等级不变
     *              2、评分下降，降级
     *              3、评分上升，任务数满足，升级
     */
    private int judgePoint(int level,double point,double taskNumber){
        //获得当前等级对应的分数区间
        int minPoint = 0;
        int maxPoint = 100;
        int compareTaskNumber = 0;

        switch (level){
            case 1:
                maxPoint = 59;
                break;
            case 2:
                minPoint = 60;
                maxPoint = 64;
                compareTaskNumber = 10;
                break;
            case 3:
                minPoint = 65;
                maxPoint = 69;
                compareTaskNumber = 50;
                break;
            case 4:
                minPoint = 70;
                maxPoint = 74;
                compareTaskNumber = 100;
                break;
            case 5:
                minPoint = 75;
                compareTaskNumber = 200;
                break;
        }

        //判断评分是否在这个区间
        if(point<minPoint)
            return 0;
        else if(point>maxPoint) {
            if(taskNumber>=compareTaskNumber)
                return 2;
            else
                return 1;
        }
        else
            return 1;
    }

}
