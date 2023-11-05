package com.example.uberapp_tim22.DTO;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.uberapp_tim22.model.RejectionLetter;

import java.time.LocalDateTime;

public class RejectionDTO implements Parcelable {

    private String reason;
    private LocalDateTime timeOfRejection;

    public RejectionDTO() {

    }
    public RejectionDTO(String reason, LocalDateTime timeOfRejection){
        this.reason = reason;
        this.timeOfRejection = timeOfRejection;
    }

    protected RejectionDTO(Parcel in) {
        reason = in.readString();
    }

    public static final Creator<RejectionDTO> CREATOR = new Creator<RejectionDTO>() {
        @Override
        public RejectionDTO createFromParcel(Parcel in) {
            return new RejectionDTO(in);
        }

        @Override
        public RejectionDTO[] newArray(int size) {
            return new RejectionDTO[size];
        }
    };

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimeOfRejection() {
        return this.timeOfRejection;
    }

    public void setTimeOfRejection(LocalDateTime deductionTime) {
        this.timeOfRejection = deductionTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reason);
    }
}
