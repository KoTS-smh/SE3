package com.sec.server.service.serviceImpl;

import com.sec.server.dao.MessageDao;
import com.sec.server.domain.Message;
import com.sec.server.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "messageService")
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageDao messageDao;

    @Override
    public void addMessage(Message message) {
        messageDao.insertMessage(message);
    }
}
