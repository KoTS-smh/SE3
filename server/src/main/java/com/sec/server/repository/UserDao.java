package com.sec.server.repository;

import com.sec.server.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {

    /**
     * 新建一个用户
     * @param user 用户
     */
    @Insert("insert into MRGSDB.users(email,description,education,password," +
            "point,sex,telPhone,userLevel,username,profession,balance) values (#{email}," +
            "#{description},#{education},#{password},#{point},#{sex},#{telPhone}," +
            "#{userLevel},#{username},#{profession},#{balance})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "userId")
    void insertUser(User user);

    /**
     * 通过名字获取用户
     * @param username 用户名
     * @return 用户信息 user
     */
    @Select("select * from MRGSDB.users where username=#{username}")
    User getUser(String username);

    /**
     * 由用户Id获得一个用户
     * @param userId 用户Id
     * @return 用户 user
     */
    @Select("select * from MRGSDB.users where userId=#{userId}")
    User getUserById(long userId);

    /**
     * 获得所有用户
     * @return 用户列表 list
     */
    @Select("select * from MRGSDB.users")
    List<User> getAllUsers();

    /**
     * 修改用户信息
     * @param user 用户信息
     */
    @Update("update MRGSDB.users set sex=#{sex},education=#{education},telPhone=#{telPhone}," +
            "email=#{email},description=#{description},profession=#{profession} WHERE userId=#{userId}")
    void updateUser(User user);

    /**
     * 删除用户
     * @param userId 用户Id
     */
    @Delete("delete from MRGSDB.users where userId=#{userId}")
    void deleteUser(long userId);

    @Update("update MRGSDB.users set balance = balance + #{num} where userId = #{userId}")
    void recharge(@Param("num") int num,@Param("userId") long userId);

    @Select("select balance from MRGSDB.users where userId = #{userId}")
    double getBalance(long userId);

    @Update("update MRGSDB.users set balance = balance - #{cost} where userId = #{userId}")
    void consume(@Param("cost") double cost,@Param("userId") long userId);

    /**
     * 修改用户的信用积分
     * @param userId 用户Id
     * @param point 修改的积分
     */
    @Update("update MRGSDB.users set point=point + #{point} where userId = #{userId}")
    void increaseUserPoint(@Param("userId") long userId,@Param("point") int point);
}
