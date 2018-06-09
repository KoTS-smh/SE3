package com.sec.server.repository;

import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.AnnotationType;
import com.sec.server.enums.TaskOrderState;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TaskOrderDao {
    /**
     * 工人接取任务
     * @param taskOrder 任务订单
     * @describe 任务订单创建之时就是预约状态
     */
    @Insert("insertUser into MRGSDB.taskOrder(taskId,acceptUserId,submited,lastPic," +
            "finishedPics,rate,beginDate,endDate) values (#{taskId},#{acceptUserId},#{submited},0,0,0," +
            "#{beginDate},#{endDate})")
    @Options(useGeneratedKeys = true, keyProperty = "taskOrderId", keyColumn = "taskOrderId")
    void insertTaskOrder(TaskOrder taskOrder);

    /**
     * 由Id获取任务订单
     * @param taskOrderId 任务订单Id
     * @return 任务订单 taskOrder
     */
    @Select("select * from MRGSDB.taskOrder where taskOrderId = #{taskOrderId}")
    TaskOrder getTaskOrder(long taskOrderId);

    /**
     * 获取一个工人所有的任务订单
     * @param acceptUserId 工人Id
     * @return 任务订单列表 list
     */
    @Select("select * from MRGSDB.taskOrder where acceptUserId = #{acceptUserId}")
    List<TaskOrder> getAllTaskOrder(long acceptUserId);

    /**
     * 获取一个工人一个标注类型中所有完成的任务订单
     * @param acceptUserId 工人Id
     * @return 任务订单列表 list
     */
    @Select("select * from MRGSDB.taskOrder " +
            "where acceptUserId = #{acceptUserId} and submited = 3 " +
            "and taskId not in (select taskId from MRGSDB.task where annotationType != type )")
    List<TaskOrder> getAllFinishedTaskOrderOfAType(long acceptUserId, AnnotationType type);

    /**
     * 获取一个工人所有提交的任务订单
     * @param acceptUserId 工人Id
     * @return 已提交的任务订单列表 list
     */
    @Select("select * from MRGSDB.taskOrder where acceptUserId = #{acceptUserId} and submited = 0")
    List<TaskOrder> getAllSubmited(long acceptUserId);

    /**
     * 更新任务订单
     * @param taskOrder 任务订单Id
     */
    @Update("update MRGSDB.taskOrder set submited = #{submited}, lastPic = #{lastPic}, finishedPics = #{finishedPics}," +
            "beginDate = #{beginDate}, endDate = #{endDate}, rate = #{rate} where taskOrderId = #{taskOrderId}")
    void updateTaskOrder(TaskOrder taskOrder);

    /**
     * 删除任务订单
     * @param taskOrderId 任务订单Id
     */
    @Delete("delete from MRGSDB.taskOrder where taskOrderId = #{taskOrderId}")
    void deleteTaskOrder(long taskOrderId);

    /**
     * 删除预约工人的订单
     *
     */
    @Delete("delete from MRGSDB.taskOrder where taskId = {#taskId} and userId = {#userId}")
    void deleteAppointTaskOrder(long taskId,long userId);

    /**
     * 获取接取一个任务的所有工人Id
     * @param taskId 任务Id
     * @return 工人Id列表 list
     */
    @Select("select acceptUserId from MRGSDB.taskOrder where taskId = #{taskId}")
    List<Long> getAcceptUserIds(long taskId);

    /**
     * 获取接取一个任务的工人数目
     * @param taskId 任务Id
     * @return 工人数目 number
     */
    @Select("select count(*) from MRGSDB.taskOrder where taskId = #{taskId}")
    int getAcceptNum(long taskId);

    /**
     * 获取一个任务所有的任务订单
     * @param taskId 任务Id
     * @return 任务订单列表 list
     */
    @Select("select * from MRGSDB.taskOrder where taskId = # {taskId} and state != 2 ")
    List<TaskOrder> getAllTaskOrderOfATask(long taskId);

    /**
     * 获取一个任务的等待工人列表
     */
    List<Long> getWaitingWorker(long taskId);

    /**
     * 获得工人接受的所有任务
     * @param acceptUserId 工人Id
     * @return 任务列表 list
     */
    @Select("select taskId from MRGSDB.taskOrder where acceptUserId = #{acceptUserId}")
    List<Long> getAcceptedTasks(long acceptUserId);

    /**
     * 更改任务订单状态
     * @param taskOrderId 任务订单Id
     * @describe 包括：
     *                  预约结束 appoint     -> unSubmitted
     *                  提交订单 unSubmitted -> submitted
     *                  审批通过 submitted   -> finish
     *                  被替换   ...         -> fail
     */
    @Update("update MRGSDB.taskOrder set submited = #{submited} where taskOrderId = #{taskOrderId}")
    void changeTaskOrderState(long taskOrderId ,TaskOrderState submited);
}