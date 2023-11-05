package com.example.uberapp_tim22.DTO;

import java.io.Serializable;

public class HopInMessageDTO implements Serializable {

    Long receiverId;
    String message;
    Long rideId;

    public HopInMessageDTO(Long receiverId, String message, Long rideId) {
        super();
        this.receiverId = receiverId;
        this.message = message;
        this.rideId = rideId;
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
}
