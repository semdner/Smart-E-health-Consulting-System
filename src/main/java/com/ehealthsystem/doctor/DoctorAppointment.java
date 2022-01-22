package com.ehealthsystem.doctor;

import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorAppointment {
    private LocalDate date;
    private LocalTime time;
    private boolean free;

    public DoctorAppointment(LocalDate date, LocalTime time, boolean free) {
        this.date = date;
        this.time = time;
        this.free = free;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public boolean getFree() {
        return free;
    }
}
