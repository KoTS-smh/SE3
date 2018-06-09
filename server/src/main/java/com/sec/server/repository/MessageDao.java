package com.sec.server.repository;

import com.sec.server.domain.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageDao {
    @Insert("insert into MRGSDB.message(userId, messageInfo, title, isRead) values " +
            "(#{userId}, #{messageInfo}, #{title}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "messageId", keyColumn = "messageId")
    void insertMessage(Message message);

    @Select("select * from MRGSDB.message where userId = #{userId} and isRead = 0")
    List<Message> getMessages(long userId);

    @Update("update MRGSDB.message set isRead = 1 where messageId = #{messageId}")
    void setAsReaded(long messageId);

    @Delete("delete from MRGSDB.message where messageId = #{messageId}")
    void deleteMessage(long messageId);
}
