package com.example.uberapp_tim22.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class IdAndEmailDTO implements Parcelable {

    private Long id;
    private String email;

    public IdAndEmailDTO(){

    }

    public IdAndEmailDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    protected IdAndEmailDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        email = in.readString();
    }

    public static final Creator<IdAndEmailDTO> CREATOR = new Creator<IdAndEmailDTO>() {
        @Override
        public IdAndEmailDTO createFromParcel(Parcel in) {
            return new IdAndEmailDTO(in);
        }

        @Override
        public IdAndEmailDTO[] newArray(int size) {
            return new IdAndEmailDTO[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(email);
    }
}
