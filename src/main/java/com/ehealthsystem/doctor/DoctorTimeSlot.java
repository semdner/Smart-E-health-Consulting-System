package com.ehealthsystem.doctor;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;

import javax.activation.UnsupportedDataTypeException;
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

    public static ArrayList<DoctorTimeSlot> getFreeTimeSlots(LocalDate date, Doctor doctor) throws SQLException, UnsupportedDataTypeException {
        ArrayList<Appointment> appointments = Database.getDoctorsAppointments(doctor.getId(), date);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Database.datePattern);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Database.timePatternAppointment);

        //Generate times
        Set<String> times = new HashSet<>();
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(16, 0);
        for (LocalTime i = openingTime; i.isBefore(closingTime); i = i.plusMinutes(30)) {
            times.add("%s %s".formatted(
                    date.format(dateFormatter),
                    i.format(timeFormatter)
            ));
        }

        //Get busy times from database
        Set<String> busyTimes = new HashSet<>();
        for (Appointment a : appointments) {
            busyTimes.add("%s %s".formatted(
                    a.getDate().format(dateFormatter),
                    a.getTime().format(timeFormatter)
            ));
            System.out.println(busyTimes.size());
        }

        //Remove busy times (to not display them)
        times.removeAll(busyTimes); //reason why this is a string set: so that it's comparable (equality check)

        //Make set to list and order it (because set is unordered)
        ArrayList<String> timesList = new ArrayList<>(times);
        Collections.sort(timesList); //reason why this is a string list: so that it's comparable (sorting)

        //Create final list
        ArrayList<DoctorTimeSlot> timeSlots = new ArrayList<>();
        for (String time : timesList) {
            String dateString = time.split(" ")[0];
            String timeOfDay = time.split(" ")[1];

            //load them into LocalDate and LocalTime objects
            LocalDate d = LocalDate.parse(dateString, dateFormatter);
            LocalTime t = LocalTime.parse(timeOfDay, timeFormatter);

            //and finally into DoctorTimeSlot objects
            timeSlots.add(new DoctorTimeSlot(d, t, true));
        }

        return timeSlots;
    }
}
