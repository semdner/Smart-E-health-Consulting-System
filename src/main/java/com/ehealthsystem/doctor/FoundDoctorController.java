package com.ehealthsystem.doctor;

import com.ehealthsystem.appointment.AppointmentInformationController;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.map.DoctorDistance;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class FoundDoctorController {

    @FXML
    private Label doctorLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label zipLabel;

    @FXML
    private Button showMoreButton;

    DoctorDistance doctor = new DoctorDistance();
    String userGeoData;
    String doctorGeoData;

    public void setData(DoctorDistance doctor, String doctorGeoData, String userGeoData) throws IOException {
        doctorLabel.setText(doctor.getDoctor().getLastName());
        addressLabel.setText(doctor.getDoctor().getStreet() + " " + doctor.getDoctor().getNumber());
        zipLabel.setText(Integer.toString(doctor.getDoctor().getZip()));
        this.doctor = doctor;
        this.userGeoData = userGeoData;
        this.doctorGeoData = doctorGeoData;
    }

    public void handleShowMoreButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ehealthsystem/doctor/foundDoctorFull-view.fxml"));
        Parent root = loader.load();
        FoundDoctorFullController controller = loader.getController();
        controller.setDoctor(doctor);
        controller.setDoctorGeoData(doctorGeoData);
        controller.setUserGeoData(userGeoData);
        controller.start();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("make appointment");
        stage.setScene(primaryScene);
    }
}
