package com.example.uberapp_tim22.DTO;

import java.util.List;

public class UnregisteredUserDTO {
    private List<NewLocationDTO> locations;
    private String vehicleType;
    private Boolean babyTransport;
    private Boolean petTransport;

    public UnregisteredUserDTO(List<NewLocationDTO> locations, String vehicleType, Boolean babyTransport, Boolean petTransport) {
        this.locations = locations;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public List<NewLocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<NewLocationDTO> locations) {
        this.locations = locations;
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

}