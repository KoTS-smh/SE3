package com.sec.server.dao;

import com.sec.server.domain.Task;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.MappedTypes;

@Mapper
public interface TaskDao {
    @Insert("insert into mrgsdb.task(taskId, postUserId, taskName, taskInfo, annotationType, classifiedInfo, beginDate, endDate, totalPoints, taskLevel, maxParticipator, isFinished, viewedTimes,reward) " +
            "VALUES (#{taskId},#{postUserId},#{taskName},#{taskInfo}, #{annotationType}, #{classifiedInfo}, #{beginDate}, #{endDate}, #{totalPoints}, #{taskLevel}, #{maxParticipator}, #{isFinished}, #{viewedTimes},#{reward})")
    void addTask(Task task);

    @Update("update mrgsdb.task set taskName = #{taskName},taskInfo = #{taskInfo},endDate = #{endDate},totalPoints = #{totalPoints},taskLevel = #{taskLevel},maxParticipator = #{maxParticipator},reward = #{reward}")
    void updateTask(Task task);

    @Delete("delete from mrgsdb.task where taskId = #{taskId}")
    void deleteTask(@Param("taskId") long taskId);

    @Select("select * from mrgsdb.task where taskId = #{taskId}")
    Task getById(@Param("taskId") long taskId);

    @Update("update mrgsdb.task set viewedTimes = viewedTimes + 1 where taskId = #{taskId}")
    void increaseViewedTimes(@Param("taskId") long taskId);

    @Update("update mrgsdb.task set isFinished = true")
    void finishTask(@Param("taskId") long taskId);
}
