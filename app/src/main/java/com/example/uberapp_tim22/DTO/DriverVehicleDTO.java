package com.example.uberapp_tim22.DTO;

import com.example.uberapp_tim22.model.Location;
import com.example.uberapp_tim22.model.enums.VehicleName;

public class DriverVehicleDTO {

    private Long id;
    private Long driverId;
    private VehicleName vehicleVehicleName;
    private String model;
    private String licenseNumber;
    private CurrentLocationDTO currentLocation;
    private int passengerSeats;
    private boolean babyTransport;
    private boolean petTransport;

    public DriverVehicleDTO(Long id, Long driverId, VehicleName vehicleVehicleName, String model, String licenseNumber, CurrentLocationDTO currentLocation, int passengerSeats, boolean babyTransport, boolean petTransport) {
        this.id = id;
        this.driverId = driverId;
        this.vehicleVehicleName = vehicleVehicleName;
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.currentLocation = currentLocation;
        this.passengerSeats = passengerSeats;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public VehicleName getVehicleType() {
        return vehicleVehicleName;
    }

    public void setVehicleType(VehicleName vehicleVehicleName) {
        this.vehicleVehicleName = vehicleVehicleName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public CurrentLocationDTO getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(CurrentLocationDTO currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getPassengerSeats() {
        return passengerSeats;
    }

    public void setPassengerSeats(int passengerSeats) {
        this.passengerSeats = passengerSeats;
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
}
