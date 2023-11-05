package com.example.uberapp_tim22.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Passenger extends User{

    private List<FavoriteRoutes> favouriteRoutes;
    private List<Ride> rides;

    public Passenger() {
    }

    public Passenger(List<FavoriteRoutes> favouriteRoutes, List<Ride> rides) {
        this.favouriteRoutes = favouriteRoutes;
        this.rides = rides;
    }

    public Passenger(Long id, String firstName, String lastName, String picture, String phoneNumber, String email, String address, String password, boolean blocked, boolean active) {
        super(id, firstName, lastName, picture, phoneNumber, email, address, password, blocked, active);
    }

    public Passenger(Long id, String firstName, String lastName, String picture, String phoneNumber, String email, String address, String password, boolean blocked, boolean active, List<FavoriteRoutes> favouriteRoutes, List<Ride> rides) {
        super(id, firstName, lastName, picture, phoneNumber, email, address, password, blocked, active);
        this.favouriteRoutes = favouriteRoutes;
        this.rides = rides;
    }

    public List<FavoriteRoutes> getFavouriteRoutes() {
        return favouriteRoutes;
    }

    public void setFavouriteRoutes(List<FavoriteRoutes> favouriteRoutes) {
        this.favouriteRoutes = favouriteRoutes;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }
}
