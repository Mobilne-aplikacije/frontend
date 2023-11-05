package com.example.uberapp_tim22.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HopInInboxReturnedDTO implements Serializable {
    private Long id;
    private HopInUserReturnedDTO firstUser;
    private HopInUserReturnedDTO secondUser;
    List<HopInMessageReturnedDTO> messages = new ArrayList<HopInMessageReturnedDTO>();
    private String lastMessage;
    private Long rideId;

    public HopInInboxReturnedDTO(Long id, HopInUserReturnedDTO firstUser, HopInUserReturnedDTO secondUser, String type, List<HopInMessageReturnedDTO> messages, String lastMessage) {
        this.id = id;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.messages = messages;
        this.lastMessage = lastMessage;
    }

    public HopInInboxReturnedDTO(Long id, HopInUserReturnedDTO firstUser, HopInUserReturnedDTO secondUser, String type, List<HopInMessageReturnedDTO> messages, String lastMessage, Long rideId) {
        this.id = id;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.messages = messages;
        this.lastMessage = lastMessage;
        this.rideId = rideId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<HopInMessageReturnedDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<HopInMessageReturnedDTO> messages) {
        this.messages = messages;
    }

    public HopInUserReturnedDTO getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(HopInUserReturnedDTO firstUser) {
        this.firstUser = firstUser;
    }

    public HopInUserReturnedDTO getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(HopInUserReturnedDTO secondUser) {
        this.secondUser = secondUser;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }
}
