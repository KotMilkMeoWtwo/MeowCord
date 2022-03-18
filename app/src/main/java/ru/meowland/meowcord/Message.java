package ru.meowland.meowcord;

import java.util.Date;

public class Message {
    public String nickName;
    public String textMessage;
    private long messageTime;

    public Message(){}
    public Message(String nickName, String textMessage){
        this.nickName = nickName;
        this.textMessage = textMessage;
        this.messageTime = new Date().getTime();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
