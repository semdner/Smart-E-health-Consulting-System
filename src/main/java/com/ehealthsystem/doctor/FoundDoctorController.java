package com.ehealthsystem.doctor;

import com.ehealthsystem.map.DoctorDistance;
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

/**
 * manages all the found doctors of a distance search
 */
public class FoundDoctorController {

    /**
     * all attributes with a fx:id in the view
     */
    @FXML
    private Label doctorLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label zipLabel;

    @FXML
    private Label distanceLabel;

    @FXML
    private Button showMoreButton;

    /**
     * distance to the doctor
     */
    DoctorDistance doctor = new DoctorDistance();

    /**
     * the doctor's geo data in formatted form
     */
    String doctorGeoData;

    /**
     * Method to set the doctor's information when scene is loaded from outside
     * @param doctor the doctor selected
     * @param doctorGeoData the doctor's geo data
     * @param distance the distance from user to doctor
     */
    public void setData(DoctorDistance doctor, String doctorGeoData, double distance) {
        doctorLabel.setText(doctor.getDoctor().getLastName());
        addressLabel.setText(doctor.getDoctor().getStreet() + " " + doctor.getDoctor().getHouseNumber());
        zipLabel.setText(doctor.getDoctor().getZipCode());
        distanceLabel.setText("%.1f km".formatted(distance));
        this.doctor = doctor;
        this.doctorGeoData = doctorGeoData;
    }

    /**
     * Load the scene that shows detailed information for the selected doctor
     * @param event button click event
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     * @throws SQLException
     */
    public void handleShowMoreButton(ActionEvent event) throws IOException, InterruptedException, ApiException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ehealthsystem/doctor/foundDoctorFull-view.fxml"));
        Parent root = loader.load();

        FoundDoctorFullController controller = loader.getController();
        controller.setDoctor(doctor);
        controller.setDoctorGeoData(doctorGeoData);
        controller.setUserGeoData(Session.user.getFormattedAddress());
        controller.start();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root);
        stage.setTitle("Make appointment");
        stage.setScene(primaryScene);
    }
}
