package com.ehealthsystem.reminder;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;

import javax.activation.UnsupportedDataTypeException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Manager for ReminderTasks (using java.util.TimerTask)
 * Sets up reminders on application start.
 * Gets notified by the UI controllers whenever a reminder time changes.
 * Checks the reminder time for validity by itself. Does not schedule reminders that should have already been sent.
 * Application's process keeps running in the background even though UI was closed, if there are still reminders scheduled.
 */
public class ReminderScheduler {
    static Map<Integer, Timer> map = new HashMap<>();
    private final static boolean logging = false;

    /**
     * Display messages to the console for verbosity purposes.
     * @param message The message to display to the console.
     */
    private static void log(String message) {
        if (logging) {
            System.out.println(message);
        }
    }

    /**
     * Set up (schedule) all reminders on application start.
     * Do not call from Main before the database file is created on initial setup.
     */
    public static void setupReminders() throws SQLException, UnsupportedDataTypeException {
        log("Setting up reminders");
        for (Appointment a : Database.getUpcomingAppointmentsWithReminder()) {
            createReminder(a);
        }
    }

    /**
     * Create a reminder for a given appointment.
     * Called on application load and creation of a new appointment.
     * @param appointment the appointment in question to create a reminder for
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
     * Remove a reminder for an appointment.
     * To be called before an appointment gets cancelled/deleted (e.g. patient cancels appointment).
     * Also used as helper method for updateReminder.
     * @param appointment the appointment to delete the reminder for
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
     * Update the reminder time when an appointment's time was changed (appointment shifted)
     * AppointmentShiftController is responsible for triggering this method
     * to keep the reminder time in sync with the appointment's time
     *
     * Works by deleting a potentially existing reminder
     * and then creating a new reminder for that appointment.
     * Has effectively no own code because it only calls the according functions.
     * @param appointment the appointment to update the reminder for
     */
    public static void updateReminder(Appointment appointment) {
        log("SHIFT START");
        deleteReminder(appointment);
        createReminder(appointment);
        log("SHIFT END");
    }
}
