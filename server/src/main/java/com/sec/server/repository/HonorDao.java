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
    HonorMessage getHonorMessage(long userId);

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
    void insertUserHonor(long userId);

    /**
     * 删除一个工人的荣誉
     * @param userId 工人Id
     */
    @Delete("delete MGRSDB.honor where userId = #{userId}")
    void deleteUserHonor(long userId);
}
