package com.example.uberapp_tim22.DTO;

import java.util.Date;
import java.util.List;

public class RideDTO {

    private List<NewLocationDTO> locations;
    private List<IdAndEmailDTO> passengers;
    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;
    private String scheduledTime;

    public RideDTO(){}

    public RideDTO(List<NewLocationDTO> locations, List<IdAndEmailDTO> passengers, String vehicleType, boolean babyTransport, boolean petTransport, String scheduledTime) {
        this.locations = locations;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.scheduledTime = scheduledTime;
    }

    public List<NewLocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<NewLocationDTO> locations) {
        this.locations = locations;
    }

    public List<IdAndEmailDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<IdAndEmailDTO> passengers) {
        this.passengers = passengers;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
