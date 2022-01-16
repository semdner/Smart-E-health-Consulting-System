package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.doctor.FoundDoctorController;
import com.ehealthsystem.map.DoctorDistance;
import com.ehealthsystem.map.GeoCoder;
import com.google.maps.errors.ApiException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    static Appointment newAppointment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] choices = {"10 minutes", "1 hour", "3 days", "1 week"};
        reminderComboBox.getItems().addAll(choices);
        try {
            String userGeoData = GeoCoder.geocode(Database.getAddress(newAppointment.getEmail()), Database.getZip(newAppointment.getEmail()));
            ArrayList<DoctorDistance> doctorList = Database.getDoctorFromDistance(userGeoData, newAppointment.getDistance());
            if(doctorList.isEmpty()) {
                errorLabel.setVisible(true);
            } else {
                loadDoctors(doctorList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void loadDoctors(ArrayList<DoctorDistance> doctorList) throws IOException {
        int row = 0;
        int column = 0;
        for(int i = 0; i<doctorList.size(); i++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("/com/ehealthsystem/doctor/foundDoctor-view.fxml"));

            VBox vbox = fxmlloader.load();

            FoundDoctorController doctor = fxmlloader.getController();
            doctor.setData(doctorList.get(i));

            if(column == 3) {
                column = 0;
                row++;
            }

            doctorGridPane.add(vbox, column++, row);

        }
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        AppointmentInformationController.setAppointment(newAppointment);
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/appointmentInformation-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("make appointment");
        stage.setScene(primaryScene);
    }

    public static void setAppointment(Appointment passedAppointment) {
        newAppointment = passedAppointment;
    }
}