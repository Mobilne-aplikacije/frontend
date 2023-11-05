package com.example.uberapp_tim22.model;

import com.example.uberapp_tim22.model.enums.VehicleName;

public class VehicleType {

    private Long id;
    private VehicleName vehicleName;
    private float pricePerKm;

    public VehicleType() {
    }

    public VehicleType(Long id, VehicleName vehicleName, float pricePerKm) {
        this.id = id;
        this.vehicleName = vehicleName;
        this.pricePerKm = pricePerKm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleName getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(VehicleName vehicleName) {
        this.vehicleName = vehicleName;
    }

    public float getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(float pricePerKm) {
        this.pricePerKm = pricePerKm;
    }
}
