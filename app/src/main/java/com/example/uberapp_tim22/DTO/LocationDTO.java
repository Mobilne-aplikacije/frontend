package com.example.uberapp_tim22.DTO;

public class LocationDTO {

    private DepartureDTO departure;
    private DestinationDTO destination;

    public DepartureDTO getDeparture() {
        return departure;
    }

    public void setDeparture(DepartureDTO departure) {
        this.departure = departure;
    }

    public DestinationDTO getDestination() {
        return destination;
    }

    public void setDestination(DestinationDTO destination) {
        this.destination = destination;
    }

}