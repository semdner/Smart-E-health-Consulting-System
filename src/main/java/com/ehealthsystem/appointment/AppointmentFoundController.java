package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.doctor.FoundDoctorController;
import com.ehealthsystem.map.DoctorDistance;
import com.ehealthsystem.map.GeoCoder;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.google.maps.errors.ApiException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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
    ComboBox reminderComboBox;

    @FXML
    Label errorLabel;

    @FXML
    GridPane doctorGridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] choices = {"10 minutes", "1 hour", "3 days", "1 week"};
        reminderComboBox.getItems().addAll(choices);
        try {
            String userGeoData = GeoCoder.geocode(Session.user.getStreet() + " " + Session.user.getHouseNo(), Session.user.getZipCode());
            ArrayList<DoctorDistance> doctorList = Database.getDoctorFromDistance(userGeoData, Session.appointment.getDistance());
            if(doctorList.isEmpty()) {
                errorLabel.setVisible(true);
            } else {
                loadDoctors(doctorList, userGeoData);
            }
        } catch (SQLException | ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

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

    public void handleBackButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "appointment/appointmentInformation-view.fxml", "Make appointment");
    }
}