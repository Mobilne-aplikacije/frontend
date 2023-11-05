package com.example.uberapp_tim22.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class NewLocationDTO implements Parcelable {

    private NewLocationWithAddressDTO departure;
    private NewLocationWithAddressDTO destination;

    public NewLocationDTO(){

    }

    public NewLocationDTO(NewLocationWithAddressDTO departure, NewLocationWithAddressDTO destination) {
        this.departure = departure;
        this.destination = destination;
    }

    protected NewLocationDTO(Parcel in) {
    }

    public static final Creator<NewLocationDTO> CREATOR = new Creator<NewLocationDTO>() {
        @Override
        public NewLocationDTO createFromParcel(Parcel in) {
            return new NewLocationDTO(in);
        }

        @Override
        public NewLocationDTO[] newArray(int size) {
            return new NewLocationDTO[size];
        }
    };

    public NewLocationWithAddressDTO getDeparture() {
        return departure;
    }

    public void setDeparture(NewLocationWithAddressDTO departure) {
        this.departure = departure;
    }

    public NewLocationWithAddressDTO getDestination() {
        return destination;
    }

    public void setDestination(NewLocationWithAddressDTO destination) {
        this.destination = destination;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
