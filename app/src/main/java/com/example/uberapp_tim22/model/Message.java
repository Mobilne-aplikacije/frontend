package com.example.uberapp_tim22.model;

import com.example.uberapp_tim22.model.enums.MessageType;
import java.util.Date;

public class Message {

    private Long id;
    private User sender;
    private User reciever;
    private String message;
    private Date sendingTime;
    private MessageType type;
    private int driveId;

    public Message() {
    }

    public Message(Long id, User sender, User reciever, String message, Date sendingTime, MessageType type, int driveId) {
        this.id = id;
        this.sender = sender;
        this.reciever = reciever;
        this.message = message;
        this.sendingTime = sendingTime;
        this.type = type;
        this.driveId = driveId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(Date sendingTime) {
        this.sendingTime = sendingTime;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getDriveId() {
        return driveId;
    }

    public void setDriveId(int driveId) {
        this.driveId = driveId;
    }
}
