package com.sec.server.repository;

import com.sec.server.domain.ImgUrl;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
}
