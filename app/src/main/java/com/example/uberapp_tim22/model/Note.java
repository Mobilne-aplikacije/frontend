package com.example.uberapp_tim22.model;

import java.util.Date;

public class Note {

    private Long id;
    private String message;
    private Date date;
    private User user;

    public Note() {
    }

    public Note(Long id, String message, Date date, User user) {
        this.id = id;
        this.message = message;
        this.date = date;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
