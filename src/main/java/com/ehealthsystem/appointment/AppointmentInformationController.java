package com.ehealthsystem.appointment;

import com.ehealthsystem.doctor.specialization.Specialization;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AppointmentInformationController implements Initializable {

    @FXML
    Slider searchDistanceSlider;

    @FXML
    TextField healthProblemField;

    @FXML
    ComboBox doctorChoiceBox;

    @FXML
    Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Specialization specialization = new Specialization();
            String[] categories = specialization.getSpecializationList().toArray(new String[0]);
            doctorChoiceBox.getItems().setAll(categories);

            if (Session.appointment.getDistance() != -1) {
                searchDistanceSlider.setValue(Session.appointment.getDistance());
            }

            healthProblemField.setText(Session.appointment.getHealthProblem());

            if (Session.appointment.getSpecialization() != null) {
                doctorChoiceBox.setValue(Session.appointment.getSpecialization());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleContinueButton(ActionEvent event) throws IOException {
        if (saveData()) {
            SceneSwitch.switchTo(event, "appointment/appointmentFound-view.fxml", "Make appointment");
        }
    }

    private boolean saveData() {
        boolean okay = true;
        Session.appointment.setDistance(searchDistanceSlider.getValue());

        if(validateSpecialization()) {
            Session.appointment.setSpecialization(doctorChoiceBox.getValue().toString());
        } else {
            okay = false;
        }

        if(validateHealthProblem()) {
            Session.appointment.setHealthProblem(healthProblemField.getText());
        } else {
            Session.appointment.setHealthProblem(null);
            okay = false;
        }

        return okay;
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        saveData();
        SceneSwitch.switchTo(event, "appointment/appointmentHealth-view.fxml", "Make appointment");
    }

    public boolean validateHealthProblem() {
        if(healthProblemField.getText() == null || healthProblemField.getText().isBlank()) {
            errorLabel.setText("Please describe your health problem.");
            errorLabel.setVisible(true);
            return false;
        } else {
            return true;
        }
    }

    public boolean validateSpecialization() {
        if(doctorChoiceBox.getValue() == null) {
            errorLabel.setText("Please choose a doctor specialization.");
            errorLabel.setVisible(true);
            return false;
        } else {
            return true;
        }
    }
}
