package com.sec.server.domain;

public class Message {
    //信息Id
    private long messageId;
    //接受信息的用户Id
    private long userId;
    //信息正文
    private String messageInfo;
    //信息标题
    private String title;
    private boolean isRead;

    public Message(){}

    public Message(long userId, String messageInfo, String title) {
        this.userId = userId;
        this.messageInfo = messageInfo;
        this.title = title;
        this.isRead = false;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
