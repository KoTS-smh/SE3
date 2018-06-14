package com.sec.server.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AppointDao {

    /**
     * 新建一个预约信息
     * @param taskId 任务Id
     * @param userId 工人Id
     */
    @Insert("insert into mrgsdb.appoint(taskId,userId) values (#{taskId},#{userId})")
    void insertAppointMessage(long taskId,long userId);

    /**
     * 删除一个预约信息
     * @param taskId 任务Id
     * @param userId 工人Id
     */
    @Delete("delete from mrgsdb.appoint where userId = #{userId} and taskId = #{taskId}")
    void deleteAppointMessage(long taskId,long userId);

    /**
     * 获取任务所有预约的工人Id
     * @param taskId 任务Id
     * @return 工人列表 list
     */
    @Select("select userId from mrgsdb.appoint where taskId = #{taskId}")
    List<Long> getAppointUser(long taskId);

    /**
     * 获取工人预约的所有任务Id
     * @param userId 工人Id
     * @return 任务列表 list
     */
    @Select("select taskId from mrgsdb.appoint where userId = #{userId}")
    List<Long> getAppointTask(long userId);
}
