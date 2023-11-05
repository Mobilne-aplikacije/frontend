package com.example.uberapp_tim22.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UserActivation {

    private Long id;
    private User user;
    private LocalDateTime date;
    private int lifespan;

    public UserActivation() {
    }

    public UserActivation(Long id, User user, LocalDateTime date, int lifespan) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.lifespan = lifespan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }
}
