package project.roomeo.models;


import project.roomeo.models.enums.RatingStatus;
import project.roomeo.models.enums.RatingType;

public class Rating {
    private Long id;
    private int rating;
    private String comment;
    private RatingStatus status;
    private RatingType type;
    private int accommodationId;
    private int guestId;
    private String ratingDate;

    public Rating(int rating, String comment, RatingStatus status, RatingType type, int accommodationId, int guestId,String ratingDate) {
        this.rating = rating;
        this.comment = comment;
        this.status = status;
        this.type = type;
        this.accommodationId = accommodationId;
        this.guestId = guestId;
        this.ratingDate = ratingDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RatingStatus getStatus() {
        return status;
    }

    public void setStatus(RatingStatus status) {
        this.status = status;
    }

    public RatingType getType() {
        return type;
    }

    public void setType(RatingType type) {
        this.type = type;
    }

    public int getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(int accommodationId) {
        this.accommodationId = accommodationId;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(String ratingDate) {
        this.ratingDate = ratingDate;
    }
}
