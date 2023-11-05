package com.example.uberapp_tim22.model;

import com.example.uberapp_tim22.model.enums.VehicleName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoriteRoutes {


    private Long id;
    private String favoriteName;
    private List<Route> routes;
    private List<Passenger> passengers;
    private VehicleName vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public FavoriteRoutes() {
    }

    public FavoriteRoutes(Long id, String favoriteName, List<Route> routes, List<Passenger> passengers, VehicleName vehicleType, boolean babyTransport, boolean petTransport) {
        this.id = id;
        this.favoriteName = favoriteName;
        this.routes = routes;
        this.passengers = passengers;
        this.vehicleType = vehicleType;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public VehicleName getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleName vehicleType) {
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
}
