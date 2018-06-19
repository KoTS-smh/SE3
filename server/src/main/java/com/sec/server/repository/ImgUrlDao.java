package com.sec.server.repository;

import com.sec.server.domain.ImgUrl;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ImgUrlDao {
    @Insert("insert into MRGSDB.imgUrl(taskId, url) values (#{taskId}, #{url})")
    void insertUrl(ImgUrl imgUrl);

    @Select("select url from MRGSDB.imgUrl where taskId = #{taskId}")
    List<String> getUrls(long taskId);

    @Insert("<script>"
            + "insert into MRGSDB.imgUrl(taskId, url) values " +
            "<foreach collection=\"urlList\" item= \"url\" separator=\",\"> " +
            "(#{taskId}, #{url})" +
            "</foreach>" + "</script>")
    void insertUrlList(@Param("urlList") List<String> urlList, @Param("taskId") long taskId);

    @Select("select count(taskId) from mrgsdb.imgUrl where taskId = #{taskId}")
    int getImageNum(long taskId);

    /**
     * 删除一个任务对应的图片url信息
     * @param taskId 任务Id
     * @describe 此方法只有在任务可以被删除的时候才会调用
     */
    @Delete("delete from MRGSDB.imgUrl where taskId = #{taskId}")
    void deleteUrl(long taskId);
}
