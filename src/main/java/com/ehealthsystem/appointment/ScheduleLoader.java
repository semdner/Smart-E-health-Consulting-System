package com.ehealthsystem.appointment;

import com.ehealthsystem.doctor.Doctor;
import com.ehealthsystem.doctor.DoctorTimeSlot;
import com.ehealthsystem.tools.Session;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.activation.UnsupportedDataTypeException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Abstract class
 * extended by scene controllers that need a schedule of appointment slots displayed
 * (FoundDoctorFullController and AppointmentShiftController)
 */
public abstract class ScheduleLoader {
    @FXML
    GridPane scheduleGridPane;

    @FXML
    protected
    Label errorLabel;

    private ArrayList<Label> timeLabelList = new ArrayList<>();
    protected LocalTime selectedTime;

    protected static final String NO_TIME_SELECTED = "Please select a time.";

    /**
     * @param date
     * @param doctor
     * @param dateLabel
     * @param primaryActionButton
     * @return true if at least one free time slot was loaded (=is displayed to the user)
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    protected boolean loadSchedule(LocalDate date, Doctor doctor, Label dateLabel, Button primaryActionButton) throws SQLException, UnsupportedDataTypeException {
        scheduleGridPane.getChildren().remove(1, scheduleGridPane.getChildren().size()); //clear grid pane, except for date label
        dateLabel.setText(date.format(Session.dateFormatter));

        ArrayList<DoctorTimeSlot> doctorTimeSlotList = DoctorTimeSlot.getFreeTimeSlots(date, doctor);
        int column = 0;
        int row = 1;
        int freeTimeslots = 0;
        for(int i = 0; i< doctorTimeSlotList.size(); i++, column++) {
            //Prepare UI
            Label time = new Label(doctorTimeSlotList.get(i).getTime().format(Session.timeFormatterForSchedule));
            Button timeButton = new Button();
            boolean free = doctorTimeSlotList.get(i).getFree();
            if(free) {
                freeTimeslots++;
                handleTimeButton(time, timeButton);
            }
            setStyle(time, timeButton, free);

            if(i % 5 == 0 && i != 0) {
                //Go to next row
                column = 0;
                row++;
            }

            //Add to UI
            scheduleGridPane.add(time, column, row);
            scheduleGridPane.add(timeButton, column, row);
        }

        if (freeTimeslots <= 0) {
            primaryActionButton.setDisable(true);

            if (doctorTimeSlotList.get(doctorTimeSlotList.size() - 1).getDateTime().isBefore(LocalDateTime.now())) { //last timeslot is already in the past
                errorLabel.setText("Can't make an appointment in the past.");
            } else {
                errorLabel.setText("No free appointments that day. How about another day?"); //may have many patients
            }
            errorLabel.setVisible(true);

            return false;
        } else { //initial state, or another date was selected => undo
            errorLabel.setVisible(false);
            primaryActionButton.setDisable(false);
            return true;
        }
    }

    /**
     * Sets up a clickable button that handles the click on an appointment time
     * @param time the time label for the associated button
     * @param timeButton the button to set up
     */
    private void handleTimeButton(Label time, Button timeButton) {
        timeLabelList.add(time);
        String timeStr = time.getText();
        // dynamically add the event handler for the buttons
        timeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (Label label : timeLabelList) {
                    label.setTextFill(Color.web("#000000"));
                }
                time.setTextFill(Color.web("#FF0000"));
                selectedTime = LocalTime.parse(timeStr, Session.timeFormatterForSchedule);
                selectedTimeChanged();
            }
        });
    }

    /**
     * Handler that triggers whenever the user selected another time.
     * Empty in this abstract class, but can be overwritten in inheriting/extending classes to easily add functionality
     */
    protected void selectedTimeChanged() {

    }

    /**
     * set style for the free appointment labels and the buttons
     * @param time
     * @param timeButton
     */
    private static void setStyle(Label time, Button timeButton, boolean free) {
        if (!free) time.setTextFill(Color.web("#999999"));
        time.setStyle("-fx-font-size: 15px;");
        timeButton.setStyle("-fx-opacity: 0%");
        timeButton.setPrefWidth(100);
    }
}
