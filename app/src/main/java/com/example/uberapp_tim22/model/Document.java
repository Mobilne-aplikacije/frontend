package com.example.uberapp_tim22.model;

public class Document {

    private Long id;
    private String name;
    private String image;
    private Driver driver;

    public Document() {
    }

    public Document(Long id, String name, String image, Driver driver) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.driver = driver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
