package com.ehealthsystem.doctor;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.Session;

import javax.activation.UnsupportedDataTypeException;
import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Class used to represent a time slot (for whatever doctor that is currently shown in the UI)
 */
public class DoctorTimeSlot {
    private LocalDate date;
    private LocalTime time;
    private boolean free;

    /**
     * Constructor which sets the attributes when creating a new object (Doctor time slot).
     * @param date set the date of the time slot.
     * @param time set the time of the time slot.
     * @param free set the availability of the doctor.
     */
    public DoctorTimeSlot(LocalDate date, LocalTime time, boolean free) {
        this.date = date;
        this.time = time;
        this.free = free;
    }

    /**
     * Return the Date of the Time slot.
     * @return The date as an object of the type LocalDate.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Return the time of the time slot.
     * @return The time as an object of the type LocalTime.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Return the date and time of the time slot.
     * @return the date and time as an object of the type LocalDateTime
     */
    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time);
    }

    /**
     * Return the availability of the Doctor.
     * @return The availability as a Boolean.
     */
    public boolean getFree() {
        return free;
    }

    /**
     * Get a list of possible time slots, including whether they are free or not
     * @param date The date for which time slots shall be returned.
     * @param doctor The doctor whose schedule shall be taken.
     * @return list of time slots
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public static ArrayList<DoctorTimeSlot> getFreeTimeSlots(LocalDate date, Doctor doctor) throws SQLException, UnsupportedDataTypeException {
        //Generate times
        ArrayList<LocalDateTime> times = new ArrayList<>();
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(16, 0);
        for (LocalTime i = openingTime; i.isBefore(closingTime); i = i.plusMinutes(30)) {
            times.add(LocalDateTime.of(date, i));
        }

        //Get busy times (i.e. already reserved by another patient) from database
        ArrayList<Appointment> appointments = Database.getDoctorsAppointments(doctor.getId(), date);
        ArrayList<LocalDateTime> busyTimes = new ArrayList<>();
        for (Appointment a : appointments) {
            busyTimes.add(a.getDateTime());
        }

        //Create final list
        ArrayList<DoctorTimeSlot> timeSlots = new ArrayList<>();
        for (LocalDateTime time : times) {
            //and finally into DoctorTimeSlot objects
            boolean timeIsInPast = time.isBefore(LocalDateTime.now());
            boolean timeIsBusy = busyTimes.contains(time);
            boolean free = !timeIsInPast && !timeIsBusy; //free if not in the past and not busy
            timeSlots.add(new DoctorTimeSlot(time.toLocalDate(), time.toLocalTime(), free));
        }

        return timeSlots;
    }
}
