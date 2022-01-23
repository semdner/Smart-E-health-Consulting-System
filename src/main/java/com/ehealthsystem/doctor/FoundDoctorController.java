package com.ehealthsystem.doctor;

import com.ehealthsystem.map.DoctorDistance;
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

    public void handleShowMoreButton(ActionEvent event) throws IOException, InterruptedException, ApiException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ehealthsystem/doctor/foundDoctorFull-view.fxml"));
        Parent root = loader.load();

        FoundDoctorFullController controller = loader.getController();
        controller.setDoctor(doctor);
        controller.setDoctorGeoData(doctorGeoData);
        controller.setUserGeoData(userGeoData);
        controller.start();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root);
        stage.setTitle("Make appointment");
        stage.setScene(primaryScene);
    }
}
