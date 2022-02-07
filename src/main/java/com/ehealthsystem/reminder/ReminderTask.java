package com.ehealthsystem.reminder;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.mail.SendEmail;
import com.ehealthsystem.tools.Session;

import javax.activation.UnsupportedDataTypeException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.TimerTask;

/**
 * An object of this class represents a task to send a reminder mail at a certain point in the future.
 */
public class ReminderTask extends TimerTask {
    Appointment appointment;

    /**
     * Create a new ReminderTask for an appointment.
     * The reminder time is automatically read out from the provided appointment.
     * @param appointment the appointment for which a reminder is to be sent
     */
    public ReminderTask(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * The method to be executed at the reminder time.
     * Responsible for sending the reminder email.
     */
    @Override
    public void run() {
        try {
            SendEmail.sendMail(
                    Database.getUserByUsername(appointment.getUser()).getMail(),
                    "Appointment reminder: Doctor: %s @ %s %s".formatted(
                            appointment.getDoctor().getLastName(),
                            appointment.getDate().format(Session.dateFormatter),
                            appointment.getTime().format(Session.timeFormatter)
                    ),
                    "This is to remind you of your upcoming appointment with the doctor %s.".formatted(appointment.getDoctor().getLastName()),false
            );
        } catch (MessagingException | UnsupportedEncodingException | SQLException | UnsupportedDataTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
