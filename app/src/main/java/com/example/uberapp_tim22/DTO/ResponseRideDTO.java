package com.example.uberapp_tim22.DTO;

import com.example.uberapp_tim22.model.enums.VehicleName;

import java.util.ArrayList;
import java.util.Date;

public class ResponseRideDTO {

    private Long id;
    private Date startTime;
    private Date endTime;
    private int totalCost;
    private IdAndEmailDTO driver;
    private ArrayList<IdAndEmailDTO> passengers;
    private int estimatedTimeInMinutes;
    private VehicleName vehicleVehicleName;
    private boolean babyTransport;
    private boolean petTransport;
    private RejectionDTO rejection;
    private ArrayList<LocationDTO> locations;

    public ResponseRideDTO() {
    }

    public ResponseRideDTO(Long id, Date startTime, Date endTime, int totalCost, IdAndEmailDTO driver, ArrayList<IdAndEmailDTO> passengers, int estimatedTimeInMinutes, VehicleName vehicleVehicleName, boolean babyTransport, boolean petTransport, RejectionDTO rejection, ArrayList<LocationDTO> locations) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.driver = driver;
        this.passengers = passengers;
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.vehicleVehicleName = vehicleVehicleName;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.rejection = rejection;
        this.locations = locations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public IdAndEmailDTO getDriver() {
        return driver;
    }

    public void setDriver(IdAndEmailDTO driver) {
        this.driver = driver;
    }

    public ArrayList<IdAndEmailDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<IdAndEmailDTO> passengers) {
        this.passengers = passengers;
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public VehicleName getVehicleVehicleName() {
        return vehicleVehicleName;
    }

    public void setVehicleVehicleName(VehicleName vehicleVehicleName) {
        this.vehicleVehicleName = vehicleVehicleName;
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

    public RejectionDTO getRejection() {
        return rejection;
    }

    public void setRejection(RejectionDTO rejection) {
        this.rejection = rejection;
    }

    public ArrayList<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationDTO> locations) {
        this.locations = locations;
    }
}
