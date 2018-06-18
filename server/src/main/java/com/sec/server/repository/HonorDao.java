package com.sec.server.repository;

import com.sec.server.domain.HonorMessage;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HonorDao {
    /**
     * 获取一个工人荣誉等级情况
     * @param userId 工人Id
     * @return 工人的等级信息 message
     */
    @Select("select * from MRGSDB.honor where userId = #{userId}")
    HonorMessage getHonorMessage(@Param("userId") long userId);

    /**
     * 修改一个工人荣誉等级情况
     * @param honorMessage 工人荣誉等级信息
     */
    @Update("update MRGSDB.honor set frameTagLevel = #{frameTagLevel},classifyTagLevel = #{classifyTagLevel}," +
            "wholeTagLevel = #{wholeTagLevel},regionTagLevel = #{regionTagLevel},totalLevel = #{totalLevel} WHERE userId = #{userId}")
    void setTagLevel(HonorMessage honorMessage);

    /**
     * 新建一个工人的荣誉
     * @param userId 工人Id
     */
    @Insert("insert into MRGSDB.honor(userId,frameTagLevel,frameTagPoint," +
            "classifyTagLevel,classifyTagPoint,wholeTagLevel,wholeTagPoint," +
            "regionTagLevel,regionTagPoint,totalLevel) " +
            "values (#{userId},0,0,0,0,0,0,0,0,0)")
    void insertUserHonor(@Param("userId") long userId);

    /**
     * 删除一个工人的荣誉
     * @param userId 工人Id
     */
    @Delete("delete MGRSDB.honor where userId = #{userId}")
    void deleteUserHonor(@Param("userId") long userId);

    /**
     * 获取一个工人标框标注积分
     * @param userId 用户ID
     * @return 积分
     */
    @Select("select frameTagPoint from MRGSDB.honor where userId = #{userId}")
    Double getFrameTagPoint(@Param("userId") long userId);

    /**
     * 获取一个工人分类标注积分
     * @param userId 用户ID
     * @return 积分
     */
    @Select("select classifyTagPoint from MRGSDB.honor where userId = #{userId}")
    Double getClassifyTagPoint(@Param("userId") long userId);

    /**
     * 获取一个工人整体标注积分
     * @param userId 用户ID
     * @return 积分
     */
    @Select("select wholeTagPoint from MRGSDB.honor where userId = #{userId}")
    Double getWholeTagPoint(@Param("userId") long userId);

    /**
     * 获取一个工人区域标注积分
     * @param userId 用户ID
     * @return 积分
     */
    @Select("select regionTagPoint from MRGSDB.honor where userId = #{userId}")
    Double getRegionTagPoint(@Param("userId") long userId);
}
