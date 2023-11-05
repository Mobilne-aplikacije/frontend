package com.example.uberapp_tim22.model;

public class Location {

    private Long id;
    private String address;
    private double latitude;
    private double longitude;

    public Location() {
    }

    public Location(Long id, String address, double latitude, double longitude) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
