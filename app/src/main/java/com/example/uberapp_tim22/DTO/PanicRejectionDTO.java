package com.example.uberapp_tim22.DTO;

import java.util.Date;

public class PanicRejectionDTO {
    private String reason;
    private Date timeOfRejection;

    public PanicRejectionDTO(String reason, Date timeOfRejection) {
        this.reason = reason;
        this.timeOfRejection = timeOfRejection;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(Date timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }
}
