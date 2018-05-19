package com.sec.server.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestDao {

    int getTest(int num);

    @Insert("insert into testdb.test(id,num) values (#{id},#{num})")
    void insert(@Param("id") int id, @Param("num") int num);


}
