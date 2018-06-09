package com.sec.server.controller;

import com.sec.server.domain.Message;
import com.sec.server.service.MessageService;
import com.sec.server.utils.Result;
import com.sec.server.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MessageController {
    @Resource(name = "messageService")
    MessageService messageService;

    @RequestMapping("/testAdd")
    public Result testAdd() {
        Message message = new Message(6, "测试内容", "测试标题" );

        messageService.addMessage(message);
        System.out.println(message.getMessageId());

        return ResultUtils.success();
    }
}
