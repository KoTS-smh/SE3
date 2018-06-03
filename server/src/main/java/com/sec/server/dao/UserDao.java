package com.sec.server.dao;

import com.sec.server.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {
    @Insert("insertUser into MRGSDB.users(userId,email,description,education,password," +
            "point,sex,telPhone,userLevel,username,profession) values (#{userId},#{email}," +
            "#{description},#{education},#{password},#{point},#{sex},#{telPhone}," +
            "#{userLevel},#{username},#{profession})")
    void insertUser(User user);

    @Select("select * from MRGSDB.users where username=#{username}")
    User getUser(String username);

    @Select("select * from MRGSDB.users where userId=#{userId}")
    User getUserById(long userId);

    @Select("select * from MRGSDB.users")
    List<User> getAllUsers();

    @Update("update MRGSDB.users set sex=#{sex},education=#{education},telPhone=#{telPhone}," +
            "email=#{email},description=#{description},profession=#{profession} WHERE userId=#{userId}")
    void updateUser(User user);

    @Delete("delete from MRGSDB.users where userId=#{userId}")
    void deleteUser(long userId);
}
