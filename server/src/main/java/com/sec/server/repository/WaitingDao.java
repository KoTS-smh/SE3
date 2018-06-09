package com.sec.server.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WaitingDao {

    /**
     * 获得一个任务的等待列表
     * @param taskId 任务Id
     * @return 工人等待列表 waitingList
     */
    @Select("select waitingList from MRGSDB.waiting where taskId =  #{taskId}")
    String getWaitingList(long taskId);

    /**
     * 新建一个等待信息
     * @param taskId 任务Id
     * @param waitingList 工人等待列表
     */
    @Insert("insertWaitingMessage into MRGSDB.waiting(taskId,waitingList) values (#{taskId},#{waitingList})")
    void insertWaitingMessage(long taskId,String waitingList);

    /**
     * 更改任务等待列表
     * @param taskId 任务Id
     * @param waitingList 工人等待列表
     */
    @Update("update MRGSDB.waiting set waitingList = #{waitingList} where taskId = #{taskId}")
    void updateWaitingMessage(long taskId,String waitingList);

    /**
     * 删除任务等待列表
     * @param taskId 任务Id
     */
    @Delete("delete MRGSDB.waiting where taskId = #{taskId}")
    void deleteWaitingMessage(long taskId);
}
