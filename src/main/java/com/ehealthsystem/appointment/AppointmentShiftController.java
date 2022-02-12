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

/**
 * Class for managing the appointment shifting
 */
public class AppointmentShiftController extends ScheduleLoader {

    /**
     * attributes with a fx:id assigned in the view
     */
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

    /**
     * the appointment to shift which is loaded in the scene
     */
    static Appointment loadedAppointment;

    /**
     * first method called when switching scenes
     * @param appointment the apponitment selected to shift
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    public void start(Appointment appointment) throws SQLException, IOException, InterruptedException, ApiException {
        loadedAppointment = appointment;
        loadCurrentAppointment();
        loadShiftAppointment();
    }

    /**
     * set the data to current appointment informaton section
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    private void loadCurrentAppointment() throws SQLException, IOException, InterruptedException, ApiException {
        doctorLabel.setText(loadedAppointment.getDoctor().getLastName());
        addressLabel.setText(loadedAppointment.getDoctor().getFormattedAddressWithPlaceName());
        specializationsLabel.setText(StringEnumerator.enumerate(loadedAppointment.getDoctor().getSpecializations()));
        dateLabel.setText(loadedAppointment.getDate().format(Session.dateFormatter));
        timeLabel.setText(loadedAppointment.getTime().format(Session.timeFormatter));
        healthProblemLabel.setText(loadedAppointment.getHealthProblemDescription());
    }

    /**
     * set the current data to the appointment shift section
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    private void loadShiftAppointment() throws SQLException, UnsupportedDataTypeException {
        datePicker.setValue(loadedAppointment.getDate());
        loadSchedule();
    }

    /**
     * load the doctors schedule
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    private void loadSchedule() throws SQLException, UnsupportedDataTypeException {
        loadSchedule(datePicker.getValue(), loadedAppointment.getDoctor(), selectedDateLabel, shiftAppointmentButton);
    }

    /**
     * Shift the appointment when the button was pressed
     * @param event event that triggered the method call
     * @throws SQLException
     * @throws IOException
     * @throws MessagingException
     */
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
                        loadedAppointment.getDoctor().getLastName(),
                        oldDateTime.toLocalDate().format(Session.dateFormatter),
                        oldDateTime.toLocalTime().format(Session.timeFormatter),
                        loadedAppointment.getDate().isEqual(oldDateTime.toLocalDate()) ? "" : loadedAppointment.getDate().format(Session.dateFormatter), //don't display date twice if only the time was changed
                        loadedAppointment.getTime().format(Session.timeFormatter)
                ),
                "This is to confirm that your appointment with the doctor %s was shifted.".formatted(loadedAppointment.getDoctor().getLastName()),false
        );
        SendEmail.sendMail(
                Session.user.getMail(),
                "Appointment shift confirmation: Patient: %s %s: %s %s -> %s %s".formatted(
                        Session.user.getFirstName(),
                        Session.user.getLastName(),
                        oldDateTime.toLocalDate().format(Session.dateFormatter),
                        oldDateTime.toLocalTime().format(Session.timeFormatter),
                        loadedAppointment.getDate().isEqual(oldDateTime.toLocalDate()) ? "" : loadedAppointment.getDate().format(Session.dateFormatter), //don't display date twice if only the time was changed
                        loadedAppointment.getTime().format(Session.timeFormatter)
        ),
                "Your patient %s %s shifted their appointment with you.".formatted(
                        Session.user.getFirstName(),
                        Session.user.getLastName()), true);
        SceneSwitch.switchTo(event,"primary/primary-view.fxml", "E-Health-System");
    }

    /**
     * Go back to the last scene without shifting the appointment
     * @param event event that triggered the method call
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     * @throws ApiException
     */
    public void handleBackButton(ActionEvent event) throws IOException, SQLException, InterruptedException, ApiException {
        SceneSwitch.loadAppointmentMadeView(event, loadedAppointment);
    }

    /**
     * Check if date is not in the past
     * @param event event that triggered the method call
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void handleDateChoice(ActionEvent event) throws SQLException, UnsupportedDataTypeException { //not triggered when the same day is re-selected
        selectedTime = null;
        loadSchedule();
    }
}
