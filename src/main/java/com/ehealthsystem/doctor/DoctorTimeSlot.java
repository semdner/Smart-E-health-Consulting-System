package com.ehealthsystem.doctor;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DoctorTimeSlot {
    private LocalDate date;
    private LocalTime time;
    private boolean free;

    public DoctorTimeSlot(LocalDate date, LocalTime time, boolean free) {
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
