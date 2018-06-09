package com.sec.server.repository;

import com.sec.server.domain.Annotation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AnnotationDao {

    @Insert("insert into mrgsdb.annotation(taskOrderId, pictureNum, sentence, coordinates) VALUES " +
            "(#{taskOrderId},#{pictureNum},#{sentence},#{coordinates})")
    void addAnnotation(Annotation annotation);

    @Delete("delete from mrgsdb.annotation where taskOrderId = #{taskOrderId} and pictureNum = #{pictureNum}")
    void deleteOneAnnotation(@Param("taskOrderId") long taskOrderId, @Param("pictureNum") int pictureNum);

    @Delete("delete from mrgsdb.annotation where taskOrderId = #{taskOrderId}")
    void deleteAllAnnotation(@Param("taskOrderId") long taskOrderId);

    @Update("update mrgsdb.annotation set sentence = #{sentence},coordinates = #{coordinates} " +
            "where taskOrderId = #{taskOrderId} and pictureNum = #{pictureNum}")
    void updateAnnotation(Annotation annotation);

    @Select("select * from mrgsdb.annotation where taskOrderId = #{taskOrderId} and pictureNum = #{pictureNum}")
    Annotation getOne(@Param("taskOrderId") long taskOrderId, @Param("pictureNum") int pictureNum);

    @Select("selsct * from mrgsdb.annotation where taskOrderId = #{taskOrderId}")
    List<Annotation> getAll(@Param("taskOrderId") long taskOrderId);
}
