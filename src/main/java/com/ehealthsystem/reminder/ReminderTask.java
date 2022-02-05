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
                    Database.getUser(appointment.getUser()).getMail(),
                    "Appointment reminder: Dr. %s @ %s %s".formatted(
                            appointment.getDoctor().getLastName(),
                            appointment.getDate().format(Session.dateFormatter),
                            appointment.getTime().format(Session.timeFormatter)
                    ),
                    "This is to remind you of your upcoming appointment with Dr. %s.".formatted(appointment.getDoctor().getFirstName())
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedDataTypeException e) {
            e.printStackTrace();
        }
    }
}
