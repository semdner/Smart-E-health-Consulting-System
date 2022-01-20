package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.healthinformation.HealthInformation;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppointmentHealthController implements Initializable {

    @FXML
    GridPane healthGridPane;

    @FXML
    Button continueButton;

    @FXML
    Button backButton;

    private ArrayList<HealthInformation> selectedHealthInformation = new ArrayList<>();
    private ArrayList<HealthInformation> allHealthInformation = new ArrayList<>();
    private ArrayList<CheckBox> numberOfCheckBoxes = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadHealthInformation();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleContinueButton(ActionEvent event) throws IOException {
        Session.appointment.setHealthInformation(selectedHealthInformation);
        SceneSwitch.switchTo(event, "appointment/appointmentInformation-view.fxml", "Make appointment");
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        Session.appointment.setHealthInformation(selectedHealthInformation);
        SceneSwitch.switchTo(event, "appointment/appointmentUser-view.fxml", "Make appointment");
    }

    private void loadHealthInformation() throws SQLException {
        allHealthInformation = Database.getHealthInformation(Session.user.getMail());
        fillHealthTable(allHealthInformation);
    }

    private void fillHealthTable(ArrayList<HealthInformation> allHealthInformation) {
        for(int i = 0; i<allHealthInformation.size(); i++) {
            Label ICD = new Label(allHealthInformation.get(i).getICD());
            Label health_problem  = new Label(allHealthInformation.get(i).getDisease());
            Label medication = new Label(allHealthInformation.get(i).getMedication());
            CheckBox selection = new CheckBox();
            setStyle(ICD, health_problem, medication, selection);
            handleCheckBox(selection, allHealthInformation.get(i), i);

            healthGridPane.add(ICD, 0, i+1);
            healthGridPane.add(health_problem, 1, i+1);
            healthGridPane.add(medication, 2, i+1);
            healthGridPane.add(selection, 3, i+1);
        }
    }

    private void setStyle(Label ICD, Label health_problem, Label medication, CheckBox selection) {
        ICD.setStyle("-fx-font-size: 15px;");
        health_problem.setStyle("-fx-font-size: 15px;");
        medication.setStyle("-fx-font-size: 15px;");
        selection.setFocusTraversable(false);
    }

    private void handleCheckBox(CheckBox selection, HealthInformation selectedRow, int row) {
        numberOfCheckBoxes.add(selection);
        selection.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(selection.isSelected()) {
                    selectedHealthInformation.add(selectedRow);
                } else {
                    selectedHealthInformation.remove(selectedRow);
                }
            }
        });
    }

    public void handleSelectAllButton(ActionEvent event) {
        for (int i = 0; i < numberOfCheckBoxes.size(); i++) {
            if(numberOfCheckBoxes.get(i).isSelected()) {
                continue;
            } else {
                numberOfCheckBoxes.get(i).setSelected(true);
                selectedHealthInformation.add(allHealthInformation.get(i));
            }
        }
    }

    public void handleDeselectAllButton(ActionEvent event) {
        for (int i = 0; i < numberOfCheckBoxes.size(); i++) {
            if(numberOfCheckBoxes.get(i).isSelected()) {
                numberOfCheckBoxes.get(i).setSelected(false);
                selectedHealthInformation.remove(allHealthInformation.get(i));
            } else {
                continue;
            }
        }
    }
}
