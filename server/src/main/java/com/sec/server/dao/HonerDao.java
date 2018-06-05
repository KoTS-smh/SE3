package com.sec.server.dao;

import com.sec.server.domain.HonerMessage;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.AnnotationType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
