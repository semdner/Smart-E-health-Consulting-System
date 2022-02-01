package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.doctor.DoctorTimeSlot;
import com.ehealthsystem.map.GeoCoder;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.tools.StringEnumerator;
import com.google.maps.errors.ApiException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AppointmentShiftController {

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
    private LocalTime selectedTime;

    static Appointment selectedAppointment;

    public void start(Appointment appointment) throws SQLException, IOException, InterruptedException, ApiException {
        selectedAppointment = appointment;
        loadCurrentAppointment();
        loadShiftAppointment();
        datePicker.valueProperty().addListener((observableValue, localDate, t1) -> {
            if (!datePicker.getValue().equals(selectedAppointment.getDate()) && datePicker.getValue().isAfter(LocalDate.now()) || datePicker.getValue().isEqual(LocalDate.now())) {
                try {
                    loadSchedule(datePicker.getValue());
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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateLabel.setText(selectedAppointment.getDate().format(dateFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        timeLabel.setText(selectedAppointment.getTime().format(timeFormatter));
    }

    private void loadShiftAppointment() throws SQLException {
        datePicker.setValue(selectedAppointment.getDate());
        loadSchedule(datePicker.getValue());
    }

    private void loadSchedule(LocalDate date) throws SQLException {
        ArrayList<DoctorTimeSlot> doctorTimeSlotList = getFreeTimeSlots(selectedAppointment.getDate());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        selectedDateLabel.setText(date.format(dateFormatter));
        int column = 0;
        int row = 1;
        if (doctorTimeSlotList.size() == 0) {
            //errorLabel.setText("This doctor has no free appointments during the specified time range");
            //errorLabel.setVisible(true);
            return;
        }
        for(int i = 0; i< doctorTimeSlotList.size(); i++, column++) {
            //Prepare UI
            Label time = new Label(doctorTimeSlotList.get(i).getTime().toString());
            Button timeButton = new Button();
            if(doctorTimeSlotList.get(i).getFree()) {
                handleTimeButton(time, timeButton);
                setStyle(time, timeButton);
            } else {
                setStyle(time);
            }

            if(i % 5 == 0 && i != 0) {
                //Go to next row
                column = 0;
                row++;
            }

            //Add to UI
            scheduleGridPane.add(time, column, row);
            if(doctorTimeSlotList.get(i).getFree()) {
                scheduleGridPane.add(timeButton, column, row);
            }
        }
    }

    private void handleTimeButton(Label time, Button timeButton) {
        timeLabelList.add(time);
        String timeStr = time.getText();
        // dynamically add the event handler for the buttons
        timeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (int i = 0; i<timeLabelList.size(); i++) {
                    timeLabelList.get(i).setTextFill(Color.web("#000000"));
                }
                time.setTextFill(Color.web("#FF0000"));
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Session.timePatternUI);
                selectedTime = LocalTime.parse(timeStr, timeFormatter);
            }
        });
    }

    /**
     * set the style for the not free appointment labels
     * @param time
     */
    private void setStyle(Label time) {
        time.setStyle("-fx-font-size: 15px;");
        time.setTextFill(Color.web("#999999"));
    }

    /**
     * set style for the free appointment labels and the buttons
     * @param time
     * @param timeButton
     */
    private void setStyle(Label time, Button timeButton) {
        time.setStyle("-fx-font-size: 15px;");
        timeButton.setStyle("-fx-opacity: 0%");
        timeButton.setPrefWidth(100);
    }

    private ArrayList<DoctorTimeSlot> getFreeTimeSlots(LocalDate date) throws SQLException {
        ArrayList<Appointment> appointments = Database.getDoctorsAppointments(selectedAppointment.getDoctor().getId(), date);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Database.datePattern);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Database.timePatternAppointment);

        //Generate times
        Set<String> times = new HashSet<>();
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(16, 0);
        for (LocalTime i = openingTime; i.isBefore(closingTime); i = i.plusMinutes(30)) {
            times.add("%s %s".formatted(
                    date.format(dateFormatter),
                    i.format(timeFormatter)
            ));
        }

        //Get busy times from database
        Set<String> busyTimes = new HashSet<>();
        for (Appointment a : appointments) {
            busyTimes.add("%s %s".formatted(
                    a.getDate().format(dateFormatter),
                    a.getTime().format(timeFormatter)
            ));
            System.out.println(busyTimes.size());
        }

        //Remove busy times (to not display them)
        times.removeAll(busyTimes); //reason why this is a string set: so that it's comparable (equality check)

        //Make set to list and order it (because set is unordered)
        ArrayList<String> timesList = new ArrayList<>(times);
        Collections.sort(timesList); //reason why this is a string list: so that it's comparable (sorting)

        //Create final list
        ArrayList<DoctorTimeSlot> timeSlots = new ArrayList<>();
        for (String time : timesList) {
            String dateString = time.split(" ")[0];
            String timeOfDay = time.split(" ")[1];

            //load them into LocalDate and LocalTime objects
            LocalDate d = LocalDate.parse(dateString, dateFormatter);
            LocalTime t = LocalTime.parse(timeOfDay, timeFormatter);

            //and finally into DoctorTimeSlot objects
            timeSlots.add(new DoctorTimeSlot(d, t, true));
        }

        return timeSlots;
    }

    public void handleShiftAppointmentButton(javafx.event.ActionEvent event) throws SQLException, IOException {
        selectedAppointment.setDate(datePicker.getValue());
        selectedAppointment.setTime(selectedTime);
        selectedAppointment.delete(selectedAppointment.getId());
        selectedAppointment.insertIntoDb();
        SceneSwitch.switchTo(event,"primary/primary-view.fxml", "E-Health-System");
    }
}
