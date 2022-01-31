package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.doctor.Doctor;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.google.maps.errors.ApiException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class AppointmentController {

    @FXML
    Label dateLabel;

    @FXML
    Label timeLabel;

    @FXML
    Label doctorLabel;

    @FXML
    Button showMoreButton;

    Appointment loadedAppointment;

    public void setData(Appointment appointment) throws SQLException {
        loadedAppointment = appointment; // used later for passing to the next scene;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateLabel.setText(loadedAppointment.getDate().format(dateTimeFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        timeLabel.setText(loadedAppointment.getTime().format(timeFormatter));
        doctorLabel.setText(loadedAppointment.getDoctor().getLastName());
    }

    public void handleShowMoreButton(ActionEvent event) throws IOException, SQLException, InterruptedException, ApiException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ehealthsystem/appointment/appointmentMade-view.fxml"));
        Parent root = loader.load();

        AppointmentMadeController controller = loader.getController();
        controller.start(loadedAppointment);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root);
        stage.setTitle("Appointment");
        stage.setScene(primaryScene);
    }
}
