package com.ehealthsystem.appointment;

import com.ehealthsystem.mail.SendEmail;
import com.ehealthsystem.reminder.ReminderScheduler;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.tools.StringEnumerator;
import com.google.maps.errors.ApiException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import javax.activation.UnsupportedDataTypeException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentShiftController extends ScheduleLoader {

    @FXML
    Label doctorLabel;

    @FXML
    Label addressLabel;

    @FXML
    Label specializationsLabel;

    @FXML
    Label dateLabel;

    @FXML
    Label timeLabel;

    @FXML
    Label healthProblemLabel;

    @FXML
    DatePicker datePicker;

    @FXML
    Label selectedDateLabel;

    @FXML
    Button shiftAppointmentButton;

    static Appointment loadedAppointment;

    public void start(Appointment appointment) throws SQLException, IOException, InterruptedException, ApiException {
        loadedAppointment = appointment;
        loadCurrentAppointment();
        loadShiftAppointment();
    }

    private void loadCurrentAppointment() throws SQLException, IOException, InterruptedException, ApiException {
        doctorLabel.setText(loadedAppointment.getDoctor().getLastName());
        addressLabel.setText(loadedAppointment.getDoctor().getFormattedAddressWithPlaceName());
        specializationsLabel.setText(StringEnumerator.enumerate(loadedAppointment.getDoctor().getSpecializations()));
        dateLabel.setText(loadedAppointment.getDate().format(Session.dateFormatter));
        timeLabel.setText(loadedAppointment.getTime().format(Session.timeFormatter));
        healthProblemLabel.setText(loadedAppointment.getHealthProblemDescription());
    }

    private void loadShiftAppointment() throws SQLException, UnsupportedDataTypeException {
        datePicker.setValue(loadedAppointment.getDate());
        loadSchedule();
    }

    private void loadSchedule() throws SQLException, UnsupportedDataTypeException {
        loadSchedule(datePicker.getValue(), loadedAppointment.getDoctor(), selectedDateLabel, shiftAppointmentButton);
    }

    public void handleShiftAppointmentButton(ActionEvent event) throws SQLException, IOException, MessagingException {
        if(selectedTime == null) {
            errorLabel.setText(NO_TIME_SELECTED);
            errorLabel.setVisible(true);
            return;
        } else {
            errorLabel.setVisible(false);
        }

        LocalDateTime oldDateTime = loadedAppointment.getDateTime();

        loadedAppointment.setDate(datePicker.getValue());
        loadedAppointment.setTime(selectedTime);

        if (loadedAppointment.getMinutesBeforeReminder() != 0) //has a reminder
            ReminderScheduler.updateReminder(loadedAppointment);

        SendEmail.sendMail(
                Session.user.getMail(),
                "Appointment shift confirmation: Doctor: %s: %s %s -> %s %s".formatted(
                        loadedAppointment.getDoctor().getFirstName(),
                        oldDateTime.toLocalDate().format(Session.dateFormatter),
                        oldDateTime.toLocalTime().format(Session.timeFormatter),
                        loadedAppointment.getDate().isEqual(oldDateTime.toLocalDate()) ? "" : loadedAppointment.getDate().format(Session.dateFormatter), //don't display date twice if only the time was changed
                        loadedAppointment.getTime().format(Session.timeFormatter)
                ),
                "This is to confirm that your appointment with the doctor %s was shifted.".formatted(loadedAppointment.getDoctor().getFirstName())
        );
        SceneSwitch.switchTo(event,"primary/primary-view.fxml", "E-Health-System");
    }

    public void handleBackButton(ActionEvent event) throws IOException, SQLException, InterruptedException, ApiException {
        SceneSwitch.loadAppointmentMadeView(event, loadedAppointment);
    }

    public void handleDateChoice(ActionEvent event) throws SQLException, UnsupportedDataTypeException { //not triggered when the same day is re-selected
        selectedTime = null;
        if (datePicker.getValue().isBefore(LocalDate.now())) {
            errorLabel.setText("Can't make an appointment in the past.");
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);
        }
        loadSchedule();
    }
}
