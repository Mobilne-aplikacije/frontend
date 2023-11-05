package com.example.uberapp_tim22.model;

import java.util.List;

public class Vehicle {

    private Long id;
    private Driver driver;
    private String vehicleModel;
    private VehicleType vehicleType;
    private String licenseNumber;
    private int seats;
    private Location currentLocation;
    private boolean babyFriendly;
    private boolean petFriendly;
    private List<Review> reviews;

    public Vehicle() {
    }

    public Vehicle(Long id, Driver driver, String vehicleModel, VehicleType vehicleType, String licenseNumber, int seats, Location currentLocation, boolean babyFriendly, boolean petFriendly, List<Review> reviews) {
        this.id = id;
        this.driver = driver;
        this.vehicleModel = vehicleModel;
        this.vehicleType = vehicleType;
        this.licenseNumber = licenseNumber;
        this.seats = seats;
        this.currentLocation = currentLocation;
        this.babyFriendly = babyFriendly;
        this.petFriendly = petFriendly;
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isBabyFriendly() {
        return babyFriendly;
    }

    public void setBabyFriendly(boolean babyFriendly) {
        this.babyFriendly = babyFriendly;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
