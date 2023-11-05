package com.example.uberapp_tim22.model;

import java.util.List;


public class Driver extends User{

    private List<Document> documents;
    private List<Ride> rides;
    private Vehicle vehicle;

    public Driver() {
    }

    public Driver(List<Document> documents, List<Ride> rides, Vehicle vehicle) {
        this.documents = documents;
        this.rides = rides;
        this.vehicle = vehicle;
    }

    public Driver(Long id, String firstName, String lastName, String picture, String phoneNumber, String email, String address, String password, boolean blocked, boolean active) {
        super(id, firstName, lastName, picture, phoneNumber, email, address, password, blocked, active);
    }


    public Driver(Driver body) {
        super(body.getId(), body.getFirstName(), body.getLastName(), body.getPicture(), body.getPhoneNumber(), body.getEmail(), body.getAddress(), body.getPassword(), body.isBlocked(), body.isActive());
        this.documents = body.getDocuments();
        this.rides = body.getRides();
        this.vehicle = body.getVehicle();
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
