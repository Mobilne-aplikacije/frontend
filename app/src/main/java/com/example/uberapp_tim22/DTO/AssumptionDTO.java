package com.example.uberapp_tim22.DTO;

public class AssumptionDTO {

    private int estimatedTimeInMinutes;
    private int estimatedCost;

    public AssumptionDTO() {
    }

    public AssumptionDTO(int estimatedTimeInMinutes, int estimatedCost) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
        this.estimatedCost = estimatedCost;
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public int getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(int estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

}
