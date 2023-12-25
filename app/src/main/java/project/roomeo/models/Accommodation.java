package project.roomeo.models;

import java.util.Date;
import java.util.List;

import project.roomeo.models.enums.AccommodationRequestStatus;
import project.roomeo.models.enums.AccommodationType;
import project.roomeo.models.enums.BookingMethod;
import project.roomeo.models.enums.Payment;

public class Accommodation {
    private Long id;

    private String name;
    private String description;
    private String location;
    private AccommodationType type;
    private boolean wifi;
    private boolean kitchen;
    private boolean airConditioner;
    private boolean parking;
    private List<String> availability;
    private Payment payment;
    private int price;
    private BookingMethod bookingMethod;
    private List<Rating> ratings;
    private List<String> photos;
    private int minGuest;
    private int maxGuest;
    private AccommodationRequestStatus status;
    private int hostId;

    public Accommodation(Long id, String name, String description, String location, AccommodationType type, boolean wifi, boolean kitchen, boolean airConditioner, boolean parking, List<String> availability, Payment payment, int price, BookingMethod bookingMethod, List<Rating> ratings, List<String> photos, int minGuest, int maxGuest, AccommodationRequestStatus status, int hostId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.type = type;
        this.wifi = wifi;
        this.kitchen = kitchen;
        this.airConditioner = airConditioner;
        this.parking = parking;
        this.availability = availability;
        this.payment = payment;
        this.price = price;
        this.bookingMethod = bookingMethod;
        this.ratings = ratings;
        this.photos = photos;
        this.minGuest = minGuest;
        this.maxGuest = maxGuest;
        this.status = status;
        this.hostId = hostId;
    }

    public Accommodation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public AccommodationType getType() {
        return type;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isKitchen() {
        return kitchen;
    }

    public void setKitchen(boolean kitchen) {
        this.kitchen = kitchen;
    }

    public boolean isAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(boolean airConditioner) {
        this.airConditioner = airConditioner;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public List<String> getAvailability() {
        return availability;
    }

    public void setAvailability(List<String> availability) {
        this.availability = availability;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public BookingMethod getBookingMethod() {
        return bookingMethod;
    }

    public void setBookingMethod(BookingMethod bookingMethod) {
        this.bookingMethod = bookingMethod;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public int getMinGuest() {
        return minGuest;
    }

    public void setMinGuest(int minGuest) {
        this.minGuest = minGuest;
    }

    public int getMaxGuest() {
        return maxGuest;
    }

    public void setMaxGuest(int maxGuest) {
        this.maxGuest = maxGuest;
    }

    public AccommodationRequestStatus getStatus() {
        return status;
    }

    public void setStatus(AccommodationRequestStatus status) {
        this.status = status;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }
}