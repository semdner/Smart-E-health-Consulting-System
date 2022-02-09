package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.doctor.FoundDoctorController;
import com.ehealthsystem.map.DoctorDistance;
import com.ehealthsystem.map.GeoDistance;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.google.maps.errors.ApiException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Class which contains loads all the founds doctors based on the search distance
 */
public class AppointmentFoundController implements Initializable {

    /**
     *  attributes with a fx:id of the view
     */
    @FXML
    Label errorLabel;

    @FXML
    GridPane doctorGridPane;

    /**
     * get the doctors in the selected distance in to an arraylist
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadDoctors(Session.appointment.doctorList); //from Session
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * dynamically load the doctors in the grid pane
     * @param doctorList the list of all doctors which matches the distance
     * @throws IOException
     */
    private void loadDoctors(ArrayList<DoctorDistance> doctorList) throws IOException {
        int row = 0;
        int column = 0;
        for (DoctorDistance doctorDistance : doctorList) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("/com/ehealthsystem/doctor/foundDoctor-view.fxml"));

            VBox vbox = fxmlloader.load();

            FoundDoctorController doctor = fxmlloader.getController();
            doctor.setData(doctorDistance, doctorDistance.getGeoData(), doctorDistance.getDistance());

            if (column == 3) {
                column = 0;
                row++;
            }

            doctorGridPane.add(vbox, column++, row);
        }
    }

    /**
     * event handler for back button
     * @param event
     * @throws IOException
     */
    public void handleBackButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "appointment/appointmentInformation-view.fxml", "Make appointment");
    }
}