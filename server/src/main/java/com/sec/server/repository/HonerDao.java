package com.sec.server.repository;

import com.sec.server.domain.HonerMessage;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HonerDao {
    /**
     * 获取一个工人荣誉等级情况
     * @param userId 工人Id
     * @return 工人的等级信息 message
     */
    @Select("select * from MRGSDB.honer where userId = #{userId}")
    HonerMessage getTagLevel(long userId);

    /**
     * 修改一个工人荣誉等级情况
     * @param honerMessage 工人荣誉等级信息
     */
    @Update("update MRGSDB.honer set frameTagLevel = #{frameTagLevel},classifyTagLevel = #{classifyTagLevel}," +
            "wholeTagLevel = #{wholeTagLevel},regionTagLevel = #{regionTagLevel},totalLevel = #{totalLevel} WHERE userId = #{userId}")
    void setTagLevel(HonerMessage honerMessage);

    /**
     * 新建一个工人的荣誉
     * @param userId 工人Id
     */
    @Insert("insertUserHoner into MRGSDB.honer(userId,frameTagLevel,frameTagPoint," +
            "classifyTagLevel,classifyTagPoint,wholeTagLevel,wholeTagPoint," +
            "regionTagLevel,regionTagPoint,totalLevel) " +
            "values (#{userId},0,0,0,0,0,0,0,0,0)")
    void insertUserHoner(long userId);

    /**
     * 删除一个工人的荣誉
     * @param userId 工人Id
     */
    @Delete("delete MGRSDB.honer where userId = #{userId}")
    void deleteUserHoner(long userId);
}
