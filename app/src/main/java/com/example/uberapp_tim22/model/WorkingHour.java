package com.example.uberapp_tim22.model;
;
import java.util.Date;
import java.util.Calendar;

public class WorkingHour {

    private Long id;
    private Date start;
    private Date endTime;
    private Driver driver;

    public WorkingHour(Date start, Date endTime, Driver driver) {
        this.start = start;
        this.endTime = endTime;
        this.driver = driver;
    }

    public WorkingHour(Long id, Date start, Date endTime) {
        this.id = id;
        this.start = start;
        this.endTime = endTime;
        this.driver = new Driver();
    }
}
