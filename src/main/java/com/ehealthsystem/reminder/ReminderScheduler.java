package com.ehealthsystem.reminder;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;

import javax.activation.UnsupportedDataTypeException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class ReminderScheduler {
    static Map<Integer, Timer> map = new HashMap<>();
    private final static boolean logging = false;

    private static void log(String message) {
        if (logging) {
            System.out.println(message);
        }
    }

    public static void setupReminders() throws SQLException, UnsupportedDataTypeException {
        log("Setting up reminders");
        for (Appointment a : Database.getUpcomingAppointmentsWithReminder()) {
            createReminder(a);
        }
    }

    /**
     * To be called for creating reminders (on application load and creation of a new appointment)
     * @param appointment
     */
    public static void createReminder(Appointment appointment) {
        //determine reminder time
        LocalDateTime reminderTime = appointment.getDateTime().minusMinutes(appointment.getMinutesBeforeReminder());
        if (reminderTime.isBefore(LocalDateTime.now())) { //e.g. appointment tomorrow with reminder time = 3 days
            return;
        }

        Date reminderDate = Date.from(reminderTime.atZone(ZoneId.systemDefault()).toInstant()); //https://www.baeldung.com/java-timer-and-timertask
        log("Creating reminder: " + reminderDate + ", " + appointment.getId());
        Timer t = new Timer();
        t.schedule(new ReminderTask(appointment), reminderDate);
        map.put(appointment.getId(), t);
    }

    /**
     * To remove the reminder when the appointment was cancelled
     * @param appointment
     */
    public static void deleteReminder(Appointment appointment) {
        log("Deleting appointment");
        try {
            map.get(appointment.getId()).cancel();
        } catch (NullPointerException e) {
            //no reminder as reminder time was already in the past (e.g. on application load) -> no reminder created in the first place
        }
        map.remove(appointment.getId());
    }

    /**
     * To adjust a reminder when an appointment's time was shifted
     * @param appointment
     */
    public static void updateReminder(Appointment appointment) {
        log("SHIFT START");
        deleteReminder(appointment);
        createReminder(appointment);
        log("SHIFT END");
    }
}
