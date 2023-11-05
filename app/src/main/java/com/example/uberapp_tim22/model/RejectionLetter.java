package com.example.uberapp_tim22.model;

import java.util.Date;

public class RejectionLetter {

    private Long id;
    private Ride ride;
    private String reason;
    private User user;
    private Date time;

    public RejectionLetter() {
    }

    public RejectionLetter(Long id, Ride ride, String reason, User user, Date time) {
        this.id = id;
        this.ride = ride;
        this.reason = reason;
        this.user = user;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
