package com.ehealthsystem.reminder;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.mail.SendEmail;
import com.ehealthsystem.tools.Session;

import javax.activation.UnsupportedDataTypeException;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.TimerTask;

public class ReminderTask extends TimerTask {
    Appointment appointment;

    public ReminderTask(Appointment appointment) {
        this.appointment = appointment;
    }

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
                    "This is to remind you of your upcoming appointment with the doctor %s.".formatted(appointment.getDoctor().getFirstName())
            );
        } catch (MessagingException | UnsupportedEncodingException | SQLException | UnsupportedDataTypeException e) {
            e.printStackTrace();
        }
    }
}
