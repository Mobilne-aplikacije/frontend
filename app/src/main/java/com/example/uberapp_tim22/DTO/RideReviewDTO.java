package com.example.uberapp_tim22.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class RideReviewDTO implements Parcelable {

    private ReviewPassengerDTO vehicleReview;
    private ReviewPassengerDTO driverReview;

    public RideReviewDTO(ReviewPassengerDTO vehicleReview, ReviewPassengerDTO driverReview) {
        this.vehicleReview = vehicleReview;
        this.driverReview = driverReview;
    }

    protected RideReviewDTO(Parcel in) {
        vehicleReview = in.readParcelable(ReviewPassengerDTO.class.getClassLoader());
        driverReview = in.readParcelable(ReviewPassengerDTO.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(vehicleReview, flags);
        dest.writeParcelable(driverReview, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RideReviewDTO> CREATOR = new Creator<RideReviewDTO>() {
        @Override
        public RideReviewDTO createFromParcel(Parcel in) {
            return new RideReviewDTO(in);
        }

        @Override
        public RideReviewDTO[] newArray(int size) {
            return new RideReviewDTO[size];
        }
    };

    public ReviewPassengerDTO getVehicleReview() {
        return vehicleReview;
    }

    public void setVehicleReview(ReviewPassengerDTO vehicleReview) {
        this.vehicleReview = vehicleReview;
    }

    public ReviewPassengerDTO getDriverReview() {
        return driverReview;
    }

    public void setDriverReview(ReviewPassengerDTO driverReview) {
        this.driverReview = driverReview;
    }
}
