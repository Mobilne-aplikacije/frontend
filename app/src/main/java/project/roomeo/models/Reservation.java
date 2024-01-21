package project.roomeo.models;

import project.roomeo.models.enums.ReservationRequestStatus;

public class Reservation {
    private Long id;
    private int accommodationId;
    private String startDate;
    private String endDate;
    private ReservationRequestStatus status;
    private int guestId;
    private int price;

    public Reservation(int accommodationId, String start, String end, ReservationRequestStatus status, int guestId, int price) {
        this.accommodationId = accommodationId;
        this.startDate = start;
        this.endDate = end;
        this.status = status;
        this.guestId = guestId;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(int accommodationId) {
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
}
