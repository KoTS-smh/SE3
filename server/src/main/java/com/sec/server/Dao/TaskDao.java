package com.sec.server.dao;

import com.sec.server.domain.Task;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TaskDao {
    @Insert("insert into MRGSDB.task(postUserId, taskname, taskInfo, annotationType, classifiedInfo, beginDate, endDate, totalPoints, taskLevel, maxParticipator, isFinished, viewedTimes,reward,taskTagString,quality) " +
            "VALUES (#{postUserId},#{taskname},#{taskInfo}, #{annotationType}, #{classifiedInfo}, #{beginDate}, #{endDate}, #{totalPoints}, #{taskLevel}, #{maxParticipator}, #{isFinished}, #{viewedTimes},#{reward},#{taskTagString},#{quality})")
    @Options(useGeneratedKeys = true, keyProperty = "taskId", keyColumn = "taskId")
    int addTask(Task task);

    @Update("update MRGSDB.task set taskName = #{taskName},taskInfo = #{taskInfo},endDate = #{endDate},totalPoints = #{totalPoints},taskLevel = #{taskLevel},maxParticipator = #{maxParticipator},reward = #{reward},taskTagString = #{taskTagString},quality = #{quality} where taskId = #{taskId}")
    void updateTask(Task task);

    @Delete("delete from MRGSDB.task where taskId = #{taskId}")
    void deleteTask(@Param("taskId") long taskId);

    @Select("select * from mrgsdb.task where taskId = #{taskId}")
    Task getById(@Param("taskId") long taskId);

    @Update("update mrgsdb.task set viewedTimes = viewedTimes + 1 where taskId = #{taskId}")
    void increaseViewedTimes(@Param("taskId") long taskId);

    @Update("update mrgsdb.task set isFinished = true")
    void finishTask(@Param("taskId") long taskId);

    @Select("select * from MRGSDB.task where postUserId = #{postUserId}")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.dao.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> getAllPostTask(@Param("postUserId") long postUserId);

    @Select("select * from MRGSDB.task where isFinished = 1 and postUserId = #{postUserId}")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.dao.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> getAllFinishedTask(@Param("postUserId") long postUserId);

    @Select("select * from MRGSDB.task where isFinished = 0 and postUserId = #{postUserId}")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.dao.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> getAllunFinishedTask(@Param("postUserId") long postUserId);

    @Select("select * from MRGSDB.task")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.dao.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> getAllTask();

    @Select("select * from MRGSDB.task where taskId = #{taskId}")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.dao.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    Task getTask(@Param("taskId") long taskId);

    @Select("select * from MRGSDB.task where isFinished = 0")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.dao.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> getEveryUnFinishedTask();

    @Select("select taskName from MRGSDB.task where taskId = #{taskId}")
    String getTaskName(long taskId);
}
