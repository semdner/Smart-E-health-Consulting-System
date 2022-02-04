package com.ehealthsystem.appointment;

import com.ehealthsystem.map.GeoCoder;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.tools.StringEnumerator;
import com.google.maps.errors.ApiException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
        datePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
            if (!datePicker.getValue().isBefore(LocalDate.now())) { //>= today
                try {
                    loadSchedule();
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            } else {
                datePicker.setValue(LocalDate.now());
            }
        });
    }

    private void loadCurrentAppointment() throws SQLException, IOException, InterruptedException, ApiException {
        doctorLabel.setText(loadedAppointment.getDoctor().getLastName());
        addressLabel.setText(loadedAppointment.getDoctor().getFormattedAddressWithPlaceName());
        specializationsLabel.setText(StringEnumerator.enumerate(loadedAppointment.getDoctor().getSpecializations()));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Session.datePatternUI);
        dateLabel.setText(loadedAppointment.getDate().format(dateFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Session.timePatternUI);
        timeLabel.setText(loadedAppointment.getTime().format(timeFormatter));
    }

    private void loadShiftAppointment() throws SQLException, UnsupportedDataTypeException {
        datePicker.setValue(loadedAppointment.getDate());
        loadSchedule();
    }

    private void loadSchedule() throws SQLException, UnsupportedDataTypeException {
        loadSchedule(datePicker.getValue(), loadedAppointment.getDoctor(), selectedDateLabel, shiftAppointmentButton);
    }

    public void handleShiftAppointmentButton(ActionEvent event) throws SQLException, IOException {
        if(selectedTime == null) {
            errorLabel.setVisible(true);
            return;
        }

        loadedAppointment.setDate(datePicker.getValue());
        loadedAppointment.setTime(selectedTime);
        SceneSwitch.switchTo(event,"primary/primary-view.fxml", "E-Health-System");
    }

    public void handleBackButton(ActionEvent event) throws IOException, SQLException, InterruptedException, ApiException {
        SceneSwitch.loadAppointmentMadeView(event, loadedAppointment);
    }
}
