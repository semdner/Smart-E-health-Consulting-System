package com.ehealthsystem.appointment;

import com.ehealthsystem.doctor.specialization.Specialization;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AppointmentInformationController implements Initializable {

    @FXML
    DatePicker datePicker;

    @FXML
    Slider searchDistanceSlider;

    @FXML
    TextField healthProblemField;

    @FXML
    ComboBox doctorChoiceBox;

    @FXML
    Label errorLabel;

    static Appointment newAppointment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Specialization specialization = new Specialization();
            String[] categories = specialization.getSpecializationList().toArray(new String[0]);
            doctorChoiceBox.getItems().setAll(categories);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleContinueButton(ActionEvent event) throws IOException {
        if(validateDate() && validateHealthProblem() && validateSpecialization()) {
            newAppointment.setDate(datePicker.getValue());
            newAppointment.setDistance(searchDistanceSlider.getValue());
            newAppointment.setSpecialization(doctorChoiceBox.getValue().toString());
            AppointmentFoundController.setAppointment(newAppointment);
            Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/appointmentFound-view.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene primaryScene = new Scene(root, 1000, 600);
            stage.setTitle("make appointment");
            stage.setScene(primaryScene);
        } else {
            errorLabel.setVisible(true);
        }
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        AppointmentHealthController.setAppointment(newAppointment);
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/appointmentHealth-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("make appointment");
        stage.setScene(primaryScene);
        stage.show();
    }

    public boolean validateDate() {
        LocalDate date;
        try {
            date = datePicker.getValue();
            if (date.isBefore(LocalDate.now())) {
                return false;
            } else {
                return true;
            }
        } catch (NullPointerException e) {
            return false;

        }
    }

    public boolean validateHealthProblem() {
        if(healthProblemField.getText().isBlank()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validateSpecialization() {
        if(doctorChoiceBox.getValue() == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void setAppointment(Appointment passedAppointment) {
        newAppointment = passedAppointment;
    }
}
