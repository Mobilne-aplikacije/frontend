package project.roomeo.models;

public class Report {
    private Long id;

    private Integer ratingId;

    private Integer hostId;

    private Integer guestId;

    public Report(Integer ratingId, Integer hostId, Integer guestId) {
        this.ratingId = ratingId;
        this.hostId = hostId;
        this.guestId = guestId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public Integer getGuestId() {
        return guestId;
    }

    public void setGuestId(Integer guestId) {
        this.guestId = guestId;
    }
}