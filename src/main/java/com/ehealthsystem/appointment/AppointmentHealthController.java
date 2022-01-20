package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.healthinformation.HealthInformation;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

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
        if (!Session.appointment.healthInformation.isEmpty()) {
            restoreCheckBoxSelections();
        }
    }

    private void restoreCheckBoxSelections() {
        for (int i = 0; i < healthGridPane.getChildren().size(); i++) {
            Node child = healthGridPane.getChildren().get(i);
            if (child.isManaged()) {
                Integer index = GridPane.getColumnIndex(child);
                if(index == null || index != 0) {
                    //System.out.println("Index is " + index + ", skipping");
                    continue;
                }
                if (!(child instanceof Label)) {
                    //System.out.println("Child not a label, skipping");
                    continue;
                }
                //ALL THIS ONLY TO GET THE ICD!
                String ICD = (((Label)child).getText());
                String healthProblem = ((Label)(healthGridPane.getChildren().get(i+1))).getText();
                String medication = ((Label)(healthGridPane.getChildren().get(i+2))).getText();
                //^ accessing GridPane children is ugly
                HealthInformation displayed = new HealthInformation(ICD, healthProblem, medication);

                for (HealthInformation selected : Session.appointment.healthInformation) {
                    if (healthInformationsMatch(displayed, selected)) {
                        manuallySelect(((CheckBox)(healthGridPane.getChildren().get(i+3))), selected, true);
                        break; //can't do more than to check this entry
                    }
                }
            }
        }
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
                    removeMatchingHealthInformation(selectedRow);
                }
            }
        });
    }

    //Compare the data instead of the object because the object is a different one when switching scenes
    private void removeMatchingHealthInformation(HealthInformation c) {
        for (int i = 0; i < selectedHealthInformation.size(); i++) {
            HealthInformation e = selectedHealthInformation.get(i);
            if (healthInformationsMatch(c, e))
                selectedHealthInformation.remove(i--);
        }
    }

    private boolean healthInformationsMatch(HealthInformation a, HealthInformation b) {
        return (a.getICD().equals(b.getICD()) &&
                a.getDisease().equals(b.getDisease()) &&
                (a.getMedication() == null && b.getMedication() == null || (a.getMedication() != null && b.getMedication() != null && a.getMedication().equals(b.getMedication())) )
        );
    }

    public void handleSelectAllButton(ActionEvent event) {
        for (int i = 0; i < numberOfCheckBoxes.size(); i++) {
            if(numberOfCheckBoxes.get(i).isSelected()) {
                continue;
            } else {
                manuallySelect(numberOfCheckBoxes.get(i), allHealthInformation.get(i), true);
            }
        }
    }

    public void handleDeselectAllButton(ActionEvent event) {
        for (int i = 0; i < numberOfCheckBoxes.size(); i++) {
            if(numberOfCheckBoxes.get(i).isSelected()) {
                manuallySelect(numberOfCheckBoxes.get(i), allHealthInformation.get(i), false);
            } else {
                continue;
            }
        }
    }

    private void manuallySelect(CheckBox c, HealthInformation h, boolean selected) {
        if (c.isSelected() == selected) return;
        c.setSelected(selected);
        if (selected)
            selectedHealthInformation.add(h);
        else
            removeMatchingHealthInformation(h);
    }
}
