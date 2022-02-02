package com.ehealthsystem.appointment;

import com.ehealthsystem.map.GeoCoder;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.tools.StringEnumerator;
import com.google.maps.errors.ApiException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

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
    GridPane scheduleGridPane;

    private ArrayList<Label> timeLabelList = new ArrayList<>();

    static Appointment selectedAppointment;

    public void start(Appointment appointment) throws SQLException, IOException, InterruptedException, ApiException {
        selectedAppointment = appointment;
        loadCurrentAppointment();
        loadShiftAppointment();
        datePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
            if (!datePicker.getValue().equals(selectedAppointment.getDate()) && datePicker.getValue().isAfter(LocalDate.now()) || datePicker.getValue().isEqual(LocalDate.now())) {
                try {
                    loadSchedule();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                datePicker.setValue(LocalDate.now());
            }
        });
    }

    private void loadCurrentAppointment() throws SQLException, IOException, InterruptedException, ApiException {
        doctorLabel.setText(selectedAppointment.getDoctor().getLastName());
        addressLabel.setText(GeoCoder.geocode(selectedAppointment.getDoctor().getFormattedAddress()).formattedAddress);
        specializationsLabel.setText(StringEnumerator.enumerate(selectedAppointment.getDoctor().getSpecializations()));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Session.datePatternUI);
        dateLabel.setText(selectedAppointment.getDate().format(dateFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Session.timePatternUI);
        timeLabel.setText(selectedAppointment.getTime().format(timeFormatter));
    }

    private void loadShiftAppointment() throws SQLException {
        datePicker.setValue(selectedAppointment.getDate());
        loadSchedule();
    }

    private void loadSchedule() throws SQLException {
        loadSchedule(datePicker.getValue(), selectedAppointment.getDoctor(), scheduleGridPane, selectedDateLabel, timeLabelList, this);
    }

    public void handleShiftAppointmentButton(ActionEvent event) throws SQLException, IOException {
        selectedAppointment.setDate(datePicker.getValue());
        selectedAppointment.setTime(selectedTime);
        SceneSwitch.switchTo(event,"primary/primary-view.fxml", "E-Health-System");
    }
}
