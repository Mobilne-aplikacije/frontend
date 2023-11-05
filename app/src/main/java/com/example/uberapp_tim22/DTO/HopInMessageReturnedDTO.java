package com.example.uberapp_tim22.DTO;

import java.io.Serializable;

public class HopInMessageReturnedDTO implements Serializable {

    int id;
    String timeOfSending;
    int senderId;
    int receiverId;
    String message;
    String type;
    int rideId;
    int inboxId;

    public HopInMessageReturnedDTO(int id, int senderId, int receiverId, String timeOfSending, String message,
                                   String type, int rideId) {
        super();
        this.id = id;
        this.timeOfSending = timeOfSending;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.type = type;
        this.rideId = rideId;
    }

//	public HopInMessageReturnedDTO(Message message) {
//		super();
//		this.id = message.getId();
//		this.timeOfSending = message.getTimeOfSending();
//		this.senderId = message.getSenderId();
//		this.receiverId = message.getReceiverId();
//		this.message = message.getMessage();
//		this.type = message.getType();
//		this.rideId = message.getRideId();
//	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeOfSending() {
        return timeOfSending;
    }

    public void setTimeOfSending(String timeOfSending) {
        this.timeOfSending = timeOfSending;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public int getInboxId() {
        return inboxId;
    }

    public void setInboxId(int inboxId) {
        this.inboxId = inboxId;
    }
}
