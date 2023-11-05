package com.example.uberapp_tim22.DTO;

public class SendMessageDTO {
    private String message;
    private String type;
    private String rideId;

    public SendMessageDTO() {
    }

    public SendMessageDTO(String message, String type, String rideId) {
        this.message = message;
        this.type = type;
        this.rideId = rideId;
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

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }
}
