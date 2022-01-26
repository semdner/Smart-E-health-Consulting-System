package com.ehealthsystem.appointment;

import com.ehealthsystem.doctor.FoundDoctorController;
import com.ehealthsystem.map.DoctorDistance;
import com.ehealthsystem.map.GeoCoder;
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

public class AppointmentFoundController implements Initializable {

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
            Session.userGeo = GeoCoder.geocode(Session.user.getStreet() + " " + Session.user.getHouseNo(), Session.user.getZipCode());
            String userGeoData = Session.userGeo.formattedAddress;
            ArrayList<DoctorDistance> doctorList = GeoDistance.getDoctorsInRange(userGeoData, Session.appointment.getDistance());
            if(doctorList.isEmpty()) {
                errorLabel.setVisible(true);
            } else {
                loadDoctors(doctorList, userGeoData);
            }
        } catch (SQLException | ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * dynamically load the doctors in the gridpane
     * @param doctorList the list of all doctors which matches the distance
     * @param userGeoData
     * @throws IOException
     */
    private void loadDoctors(ArrayList<DoctorDistance> doctorList, String userGeoData) throws IOException {
        int row = 0;
        int column = 0;
        for(int i = 0; i<doctorList.size(); i++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("/com/ehealthsystem/doctor/foundDoctor-view.fxml"));

            VBox vbox = fxmlloader.load();

            FoundDoctorController doctor = fxmlloader.getController();
            doctor.setData(doctorList.get(i), doctorList.get(i).getGeoData(), userGeoData);

            if(column == 3) {
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