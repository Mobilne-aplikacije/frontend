package com.example.uberapp_tim22.DTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PanicRideDTO {
    private Integer id;
    private String startTime;
    private String endTime;
    private Integer totalCost;
    private PanicRideDriverDTO driver;
    private List<PanicRidePassengerDTO> passengers = null;
    private Integer estimatedTimeInMinutes;
    private String vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;
    private PanicRejectionDTO rejection;
    private List<LocationDTO> locations = null;

//    public PanicRideDTO(Long id, Date startTime, Date endTime, double totalCost, PanicRideDriverDTO driver, ArrayList<PanicRidePassengerDTO> passengers, int estimatedTimeInMinutes, VehicleType vehicleType, boolean babyTransport, boolean petTransport, PanicRejectionDTO rejection, ConcurrentHashMap<String, LocationDTO> locations) {
//        this.id = id;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.totalCost = totalCost;
//        this.driver = driver;
//        this.passengers = passengers;
//        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
//        this.vehicleType = vehicleType;
//        this.babyTransport = babyTransport;
//        this.petTransport = petTransport;
//        this.rejection = rejection;
//        this.locations = locations;
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    public PanicRideDriverDTO getDriver() {
        return driver;
    }

    public void setDriver(PanicRideDriverDTO driver) {
        this.driver = driver;
    }

    public List<PanicRidePassengerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PanicRidePassengerDTO> passengers) {
        this.passengers = passengers;
    }

    public Integer getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(Integer estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Boolean getBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(Boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public Boolean getPetTransport() {
        return petTransport;
    }

    public void setPetTransport(Boolean petTransport) {
        this.petTransport = petTransport;
    }

    public PanicRejectionDTO getRejection() {
        return rejection;
    }

    public void setRejection(PanicRejectionDTO rejection) {
        this.rejection = rejection;
    }

    public List<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDTO> locations) {
        this.locations = locations;
    }

}
