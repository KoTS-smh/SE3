package com.sec.server.repository;

import com.sec.server.domain.Task;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
@Repository
public interface TaskDao {

    /**
     * 发布新的任务
     * @param task 任务
     */
    @Insert("insert into MRGSDB.task(postUserId, taskname, taskInfo, annotationType, classifiedInfo, beginDate, endDate, totalPoints, taskLevel, maxParticipator, state, viewedTimes,reward,taskTagString) " +
            "VALUES (#{postUserId},#{taskname},#{taskInfo}, #{annotationType}, #{classifiedInfo}, #{beginDate}, #{endDate}, #{totalPoints}, #{taskLevel}, #{maxParticipator}, #{state}, #{viewedTimes},#{reward},#{taskTagString})")
    @Options(useGeneratedKeys = true, keyProperty = "taskId", keyColumn = "taskId")
    void addTask(Task task);

    /**
     * 更新任务
     * @param task 任务
     */
    @Update("update MRGSDB.task set state = #{state},taskName = #{taskname}," +
            "taskInfo = #{taskInfo},endDate = #{endDate},totalPoints = #{totalPoints}," +
            "taskLevel = #{taskLevel},maxParticipator = #{maxParticipator},reward = #{reward}," +
            "taskTagString = #{taskTagString},quality = #{quality},upRate = #{upRate} where taskId = #{taskId}")
    void updateTask(Task task);

    /**
     * 删除任务
     * @param taskId 任务Id
     */
    @Delete("delete from MRGSDB.task where taskId = #{taskId}")
    void deleteTask(@Param("taskId") long taskId);

    /**
     * 增加任务观看人数
     * @param taskId 任务Id
     */
    @Update("update mrgsdb.task set viewedTimes = viewedTimes + 1 where taskId = #{taskId}")
    void increaseViewedTimes(@Param("taskId") long taskId);

    /**
     * 任务结算
     * @param taskId 任务Id
     */
    @Update("update mrgsdb.task set state = 2 where taskId = #{taskId}")
    void finishTask(@Param("taskId") long taskId);

    /**
     * 获得一个用户所有发布的任务
     * @param postUserId 用户Id
     * @return 任务列表 list
     */
    @Select("select * from MRGSDB.task where postUserId = #{postUserId}")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.repository.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> getAllPostTask(@Param("postUserId") long postUserId);

    /**
     * 获得由一个用户发布的所有完成的任务
     * @param postUserId 用户Id
     * @return 任务列表 list
     */
    @Select("select * from MRGSDB.task where state = 2 and postUserId = #{postUserId}")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.repository.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> getAllFinishedTask(@Param("postUserId") long postUserId);

    /**
     * 获得一个用户发布的所有正在进行中的任务
     * @param postUserId 用户Id
     * @return 任务列表 list
     */
    @Select("select * from MRGSDB.task where state = 1 and postUserId = #{postUserId}")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.repository.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> getAllunFinishedTask(@Param("postUserId") long postUserId);

    /**
     * 获得所有任务
     * @return 任务列表 list
     */
    @Select("select * from MRGSDB.task")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.repository.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> getAllTask();

    /**
     * 获得一个任务
     * @param taskId 任务Id
     * @return 任务 task
     */
    @Select("select * from MRGSDB.task where taskId = #{taskId}")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.repository.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    Task getTask(@Param("taskId") long taskId);

    /**
     * 获得所有未完成的任务
     * @return 任务列表 list
     */
    @Select("select * from MRGSDB.task where state <> 2")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.repository.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> getEveryUnFinishedTask();

    /**
     * 获得一个任务的名字
     * @param taskId 任务Id
     * @return 任务名字 taskName
     */
    @Select("select taskName from MRGSDB.task where taskId = #{taskId}")
    String getTaskName(long taskId);

    @Select("select * from MRGSDB.task where taskName like CONCAT('%',#{taskName},'%')")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.repository.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> searchForAllTasks(@Param("taskName") String taskName);

    /**
     * 搜索不同类型的任务
     * @param taskName 任务名称
     * @param annotationType 标注类型
     * @return 任务列表
     */
    @Select("select * from MRGSDB.task where taskName like CONCAT('%',#{taskName},'%') and annotationType = #{annotationType}")
    @Results({
            @Result(property = "imgUrls",column = "taskId", javaType = List.class,
                    many=@Many(select = "com.sec.server.repository.ImgUrlDao.getUrls")
            ),
            @Result(property = "taskId", column = "taskId")
    })
    List<Task> searchForTypedTasks(@Param("taskName") String taskName, @Param("annotationType") int annotationType);

    @Update("update mrgsdb.task set quality = #{quality} where taskId = #{taskId}")
    void setTaskQuality(@Param("taskId") long taskId,@Param("quality") double quality);


    /**
     * @return 标框标注任务数量
     */
    @Select("select count(*) from MRGSDB.task where annotationType = 0")
    int getNumOfRecTask();

    /**
     * @return 分类标注任务数量
     */
    @Select("select count(*) from MRGSDB.task where annotationType = 1")
    int getNumOfClassifyTask();

    /**
     * @return 区域标注任务数量
     */
    @Select("select count(*) from MRGSDB.task where annotationType = 2")
    int getNumOfRegionTask();

    /**
     * @return 整体标注任务数量
     */
    @Select("select count(*) from MRGSDB.task where annotationType = 3")
    int getNumOfWholeTask();

    /**
     * @return 所有任务数量
     */
    @Select("select count(*) from MRGSDB.task")
    int getNumOfAllTask();

    /**
     * 获取所有预约中未开始的任务
     * @return 预约中未开始的任务列表 list
     */
    @Select("select * from MRGSDB.task where state = 0")
    List<Task> getAllAppointTask();

    /**
     * 获取所有进行中未结算的任务
     * @return 进行中未结算的任务列表 list
     */
    @Select("select * from MRGSDB.task where state = 1")
    List<Task> getAllOngoingTask();

    @Select("select taskId from mrgsdb.task where state = 2")
    List<Long> getOngoingTaskId();
}
