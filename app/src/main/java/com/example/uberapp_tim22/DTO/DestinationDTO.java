package com.example.uberapp_tim22.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class DestinationDTO  implements Parcelable {

    private String address;
    private Double latitude;
    private Double longitude;

    public DestinationDTO(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected DestinationDTO(Parcel in) {
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<DestinationDTO> CREATOR = new Creator<DestinationDTO>() {
        @Override
        public DestinationDTO createFromParcel(Parcel in) {
            return new DestinationDTO(in);
        }

        @Override
        public DestinationDTO[] newArray(int size) {
            return new DestinationDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}