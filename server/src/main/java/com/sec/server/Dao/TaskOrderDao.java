package com.sec.server.dao;

import com.sec.server.domain.TaskOrder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TaskOrderDao {
    @Insert("insert into MRGSDB.taskOrder(taskId,acceptUserId,submited,lastPic," +
            "finishedPics,rate) values (#{taskId},#{acceptUserId},0,0,0,0)")
    @Options(useGeneratedKeys = true, keyProperty = "taskOrderId", keyColumn = "taskOrderId")
    int insertTaskOrder(TaskOrder taskOrder);

    @Select("select * from MRGSDB.taskOrder where taskOrderId = #{taskOrderId}")
    TaskOrder getTaskOrder(long taskOrderId);

    @Select("select * from MRGSDB.taskOrder where acceptUserId = #{acceptUserId}")
    List<TaskOrder> getAllTaskOrder(long acceptUserId);

    @Select("select * from MRGSDB.taskOrder where acceptUserId = #{acceptUserId} and submited = 1")
    List<TaskOrder> getAllSubmited(long acceptUserId);

    @Update("update MRGSDB.taskOrder set submited = #{submited}, lastPic = #{lastPic}, finishedPics = #{finishedPics}," +
            "beginDate = #{beginDate}, endDate = #{endDate}, rate = #{rate} where taskOrderId = #{taskOrderId}")
    void updateTaskOrder(TaskOrder taskOrder);

    @Delete("delete from MRGSDB.taskOrder where taskOrderId = #{taskOrderId}")
    void deleteTaskOrder(long taskOrderId);

    @Select("select acceptUserId from MRGSDB.taskOrder where taskId = #{taskId}")
    List<Long> getAcceptUserIds(long taskId);
}
