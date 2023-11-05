package com.example.uberapp_tim22.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewPassengerDTO implements Parcelable {
    private Long id;
    private int rating;
    private String comment;
    private RideUserDTO passenger;

    public ReviewPassengerDTO() {}

    public ReviewPassengerDTO(Long id, int rating, String comment, RideUserDTO passenger) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.passenger = passenger;
    }

    protected ReviewPassengerDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        rating = in.readInt();
        comment = in.readString();
        passenger = in.readParcelable(RideUserDTO.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeInt(rating);
        dest.writeString(comment);
        dest.writeParcelable(passenger, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReviewPassengerDTO> CREATOR = new Creator<ReviewPassengerDTO>() {
        @Override
        public ReviewPassengerDTO createFromParcel(Parcel in) {
            return new ReviewPassengerDTO(in);
        }

        @Override
        public ReviewPassengerDTO[] newArray(int size) {
            return new ReviewPassengerDTO[size];
        }
    };

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

    public RideUserDTO getPassenger() {
        return passenger;
    }

    public void setPassenger(RideUserDTO passenger) {
        this.passenger = passenger;
    }

}
