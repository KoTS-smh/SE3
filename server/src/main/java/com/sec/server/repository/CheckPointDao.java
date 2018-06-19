package com.sec.server.repository;

import com.sec.server.domain.CheckPoint;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface CheckPointDao {

    /**
     * 创建新的检查点
     * @param taskId 任务Id
     * @param date 检查点日期
     * @param isEnd 是否是任务中最后一个检查点
     */
    @Insert("insert into MRGSDB.checkpoint(taskId,date,isEnd) values (#{taskId},#{date},#{isEnd})")
    void createCheckPoint(@Param("taskId") long taskId,@Param("date") Date date,@Param("isEnd") boolean isEnd);

    /**
     * 获取这个时间需要检查的任务Id
     * @param date 当前时间
     * @return 需要检查的任务列表 list
     */
    @Select("select taskId from MRGSDB.checkpoint where date = #{date}")
    List<Long> getTaskIdByDate(@Param("date") Date date);

    /**
     * 获取一个任务所有的检查点信息
     * @param taskId 任务Id
     * @return 检查点列表 list
     */
    @Select("select date from MRGSDB.checkpoint where taskId = #{taskId}")
    List<Date> getDate(@Param("taskId") long taskId);

    /**
     * 删除一个任务所有检查点
     * @param taskId 任务Id
     * @describe 这个方法只有当任务可以被删除的时候才会调用
     */
    @Delete("delete from MRGSDB.checkpoint where taskId = #{taskId}")
    void deleteCheckPoint(@Param("taskId")long taskId);
}
