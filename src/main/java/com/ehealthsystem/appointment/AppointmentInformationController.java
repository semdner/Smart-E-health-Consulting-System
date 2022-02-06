package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.doctor.specialization.Specialization;
import com.ehealthsystem.map.GeoDistance;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.google.maps.errors.ApiException;
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
    ComboBox healthProblemChoiceBox;

    @FXML
    Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Specialization specialization = new Specialization();
            String[] categories = specialization.getSpecializationList().toArray(new String[0]);

            healthProblemChoiceBox.getItems().setAll(Database.loadProblems());

            if (Session.appointment.getDistance() != -1) {
                searchDistanceSlider.setValue(Session.appointment.getDistance());
            }

            healthProblemField.setText(Session.appointment.getHealthProblem());

            if(Session.appointment.getHealthProblemChoice() != null){
                healthProblemChoiceBox.setValue(Session.appointment.getHealthProblemChoice());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleContinueButton(ActionEvent event) throws IOException, InterruptedException, ApiException, SQLException {
        if (saveData()) {
            Session.appointment.doctorList = GeoDistance.filterDoctorsInRangeWithLocalCalculation(Database.getDoctorsBySpecialization(Session.appointment.getSpecialization()), Session.getUserGeo().geometry.location, Session.appointment.getDistance());
            if(Session.appointment.doctorList.isEmpty()) {
                errorLabel.setText("No doctors with that category in range.");
                errorLabel.setVisible(true);
            } else {
                SceneSwitch.switchTo(event, "appointment/appointmentFound-view.fxml", "Make appointment");
            }
        }
    }

    private boolean saveData() throws SQLException {
        boolean okay = true;
        Session.appointment.setDistance(searchDistanceSlider.getValue());

        if(validateHealthProblem()) {
            Session.appointment.setHealthProblem(healthProblemField.getText());

        } else {
            Session.appointment.setHealthProblem(null);
            okay = false;
        }

        if(validateHealthProblemChoiceBox()){
            Session.appointment.setHealthProblemChoice(healthProblemChoiceBox.getValue().toString());
            Session.appointment.setSpecialization(Database.problemToSuitableSpecialization(healthProblemChoiceBox.getValue().toString()));
        }else{
            okay = false;
        }

        return okay;
    }

    public void handleBackButton(ActionEvent event) throws IOException, SQLException {
        saveData();
        SceneSwitch.switchTo(event, "appointment/appointmentHealth-view.fxml", "Make appointment");
    }

    public boolean validateHealthProblem() {
        /*
        if(healthProblemField.getText() == null || healthProblemField.getText().isBlank()) {
            errorLabel.setText("Please describe your health problem.");
            errorLabel.setVisible(true);
            return false;
        } else {
            return true;
        }*/
        return true;
    }


    public boolean validateHealthProblemChoiceBox(){
        if(healthProblemChoiceBox.getValue() == null){
            errorLabel.setText("Please choose a Health Problem");
            errorLabel.setVisible(true);
            return false;
        }else{
            return true;
        }
    }
}
