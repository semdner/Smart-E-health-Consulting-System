package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.doctor.Doctor;
import com.ehealthsystem.reminder.ReminderScheduler;

import javax.activation.UnsupportedDataTypeException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents an appointment with all the attributes.
 *
 */
public class Appointment {
    public final String tableName = "appointment";
    private int id, doctor, minutesBeforeReminder, duration;
    private String user, healthProblemDescription;
    private LocalDate date;
    private LocalTime time;

    /**
     *
     * @param insertIntoDb true if this is a new appointment that is inserted to the database, false if the object shall solely represent an appointment that is already saved in the database
     * @param id ignored when insertToDb is true (to be precise, it is saved but overwritten after successful insert)
     */
    public Appointment(boolean insertIntoDb, int id, String user, int doctor, String healthProblemDescription, LocalDate date, LocalTime time, int minutesBeforeReminder, int duration) throws SQLException, UnsupportedDataTypeException {
        this.id = id;
        this.user = user;
        this.doctor = doctor;
        this.healthProblemDescription = healthProblemDescription;
        this.date = date;
        this.time = time;
        this.minutesBeforeReminder = minutesBeforeReminder;
        this.duration = duration;
        if (insertIntoDb) {
            insertIntoDb();
            ReminderScheduler.createReminder(this);
        }
    }

    /**
     * Inserts this appointment into the database.
     * Only to be used if a new appointment is created.
     */
    private void insertIntoDb() throws SQLException, UnsupportedDataTypeException {
        if(healthProblemDescription == null) {
            healthProblemDescription = "";
        }
        Object[][] parameters = {
                {"user", user},
                {"doctor_id", doctor},
                {"healthProblemDescription", healthProblemDescription},
                {"date", date},
                {"time", time},
                {"minutesBeforeReminder", minutesBeforeReminder},
                {"duration", duration},
        };
        id = Database.insert(tableName, parameters);
    }

    /**
     * Get the appointment id returned
     * @return return id as integer
     */
    public int getId() {
        return id;
    }

    /**
     * return the doctor selected for the appointment
     * @return return doctor as object of the class Doctor
     * @throws SQLException if the sql query wouldn't find a doctor in the database it throws an exception
     */
    public Doctor getDoctor() throws SQLException {
        return Database.loadDoctorFromId(doctor); //lazy-load and also don't store
    }

    /**
     * get the set date of the appointment
     * @return return the date as an object of the type LocalDate
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * get the set time of the appointment
     * @return return the time as an object of the type LocalTime
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * get the set date and time of the appointment
     * @return return the date and time as an object of the type LocalDateTime
     */
    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time);
    }

    /**
     * get the appointment lies in the past.
     * Used for the sorting in upcoming and past appointments
     * @return return true or false depending on the date
     */
    public boolean isInThePast() {
        return getDateTime().isBefore(LocalDateTime.now());
    }

    /**
     * get the time of the reminder set in minutes
     * @return return time as int
     */
    public int getMinutesBeforeReminder() {
        return minutesBeforeReminder;
    }

    /**
     * For future use: Get the appointment's duration
     * @return the duration of the appointment in minutes
     */
    public int getDuration() {
        return duration;
    }

    /**
     * get the username of the that made the appointment
     * @return return the username as a string
     */
    public String getUser() {
        return user;
    }

    /**
     * get the health problem description that was set
     * @return return the description as a String
     */
    public String getHealthProblemDescription() {
        return healthProblemDescription;
    }

    /**
     * Updates the entry for this appointment in the database
     * @param newValues
     * @throws SQLException
     */
    private void update(Object[][] newValues) throws SQLException, UnsupportedDataTypeException {
        Database.update(
                tableName,
                newValues,
                new Object[][]{{"id", id}}
        );
    }

    /**
     * Deletes an appointment from the database.
     * Use to cancel an appointment.
     * @return whether deletion succeeded
     */
    public boolean delete() throws SQLException {
        String query = "DELETE FROM appointment WHERE id = ?";
        PreparedStatement statement = Database.connection.prepareStatement(query);
        statement.setInt(1, id);
        return statement.executeUpdate() == 1;
    }

    /**
     * Set the date appointment when shifting it.
     * @param date the new selected date
     * @throws SQLException if the datebase class throws a exception
     * @throws UnsupportedDataTypeException
     */
    public void setDate(LocalDate date) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"date", date}});
        this.date = date;
    }

    /**
     * Set the time appointment when shifting it.
     * @param time the new selected time
     * @throws SQLException if the datebase class throws a exception
     * @throws UnsupportedDataTypeException
     */
    public void setTime(LocalTime time) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"time", time}});
        this.time = time;
    }
}
