package com.example.uberapp_tim22.DTO;

import java.io.Serializable;
import java.util.Date;

public class ChatMessagesDTO implements Serializable {

    private Long senderId;
    private Long receiverId;
    private String message;
    private String sendingTime;
    private Long rideId;

    public ChatMessagesDTO() {
    }

    public ChatMessagesDTO(Long senderId, Long receiverId, String message, String sendingTime, Long rideId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.sendingTime = sendingTime;
        this.rideId = rideId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }

}
