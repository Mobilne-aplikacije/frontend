package com.example.uberapp_tim22.model;

public class Route {
    private Long id;
    private Location departure;
    private Location destination;
    private double km;

    public Route() {
    }

    public Route(Long id, Location departure, Location destination, double km) {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.km = km;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getDeparture() {
        return departure;
    }

    public void setDeparture(Location departure) {
        this.departure = departure;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }
}
