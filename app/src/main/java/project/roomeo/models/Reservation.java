package project.roomeo.models;

import project.roomeo.models.enums.ReservationRequestStatus;

public class Reservation {
    private Long id;

    private Long accommodationId;
    private String startDate;
    private String endDate;
    private ReservationRequestStatus status;
    private int guestId;
    private int price;
    private  int numberOfPeople;
    private String accommodationName;


    public Reservation(long accommodationId, String start, String end, ReservationRequestStatus status, int guestId, int price, int numberOfPeople) {
        this.accommodationId = accommodationId;
        this.startDate = start;
        this.endDate = end;
        this.status = status;
        this.guestId = guestId;
        this.price = price;
        this.numberOfPeople = numberOfPeople;
    }


    // Getters and setters
    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ReservationRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationRequestStatus status) {
        this.status = status;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
