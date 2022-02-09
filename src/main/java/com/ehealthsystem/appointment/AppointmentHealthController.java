package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.healthInformation.HealthInformation;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Class for loading the health information
 */
public class AppointmentHealthController implements Initializable {

    /**
     *  attributes with a fx:id of the view
     */
    @FXML
    GridPane healthGridPane;

    @FXML
    Button continueButton;

    @FXML
    Button backButton;

    @FXML
    Label errorLabel;

    /**
     * stores all from the user selected health informaiton
     */
    private ArrayList<HealthInformation> selectedHealthInformation = new ArrayList<>();

    /**
     * stores all the from the database loaded health information
     */
    private ArrayList<HealthInformation> allHealthInformation = new ArrayList<>();

    /**
     * stores all the checkboxes to determine which is selected
     */
    private ArrayList<CheckBox> numberOfCheckBoxes = new ArrayList<>();

    /**
     * First method that is called when scene is switched.
     * Used to call the method for initalizing the health information
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadHealthInformation();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * load the next scene
     * @param event
     * @throws IOException
     */
    public void handleContinueButton(ActionEvent event) throws IOException {
        Session.appointment.setHealthInformation(selectedHealthInformation);
        SceneSwitch.switchTo(event, "appointment/appointmentInformation-view.fxml", "Make appointment");
    }

    /**
     * load the previous scene
     * @param event
     * @throws IOException
     */
    public void handleBackButton(ActionEvent event) throws IOException {
        Session.appointment.setHealthInformation(selectedHealthInformation);
        SceneSwitch.switchTo(event, "appointment/appointmentUser-view.fxml", "Make appointment");
    }

    /**
     * load the health information of the user from the database
     * @throws SQLException
     */
    private void loadHealthInformation() throws SQLException {
        allHealthInformation = Database.getHealthInformation(Session.user.getMail());
        if (allHealthInformation.isEmpty()) {
            errorLabel.setText("You don't have any saved health statuses.");
            errorLabel.setVisible(true);
            return;
        }
        fillHealthTable(allHealthInformation);
    }

    /**
     * fill the grid pane with the loaded health information
     * @param allHealthInformation
     */
    private void fillHealthTable(ArrayList<HealthInformation> allHealthInformation) {
        for(int i = 0; i<allHealthInformation.size(); i++) {
            Label ICD = new Label(allHealthInformation.get(i).getICD());
            Label health_problem  = new Label(allHealthInformation.get(i).getDisease());
            Label medication = new Label(allHealthInformation.get(i).getMedication());
            CheckBox selection = new CheckBox();
            setStyle(ICD, health_problem, medication, selection);
            // add eventhandler
            handleCheckBox(selection, allHealthInformation.get(i), i);

            //Restore CheckBox selections
            HealthInformation all_entry = allHealthInformation.get(i);
            for (HealthInformation selected : Session.appointment.getHealthInformation()) {
                if (healthInformationsMatch(all_entry, selected)) {
                    manuallySelect(selection, all_entry, true);
                    break; //can't do more than to check this entry
                }
            }

            int c = 0;
            healthGridPane.add(selection, c++, i+1);
            healthGridPane.add(ICD, c++, i+1);
            healthGridPane.add(health_problem, c++, i+1);
            //healthGridPane.add(medication, c++, i+1);
        }
    }

    /**
     * set the style for labels and buttons
     * @param ICD the parameter of the first column of the grid pane
     * @param health_problem the parameter of the second column of the grid pane
     * @param medication the third parameter of the grid pane
     * @param selection the created checkbox of a row
     */
    private void setStyle(Label ICD, Label health_problem, Label medication, CheckBox selection) {
        ICD.setStyle("-fx-font-size: 15px;");
        health_problem.setStyle("-fx-font-size: 15px;");
        medication.setStyle("-fx-font-size: 15px;");
        selection.setFocusTraversable(false);
    }

    /**
     * dynamically added eventhandler for checkbox
     * @param selection
     * @param selectedRow
     * @param row
     */
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


    /**
     * Compare the data instead of the object because the object is a different one when switching scenes
     * @param c
     */
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

    /**
     * select all the checkboxes/health information in the grid pane
     * @param event the event triggered the method
     */
    public void handleSelectAllButton(ActionEvent event) {
        for (int i = 0; i < numberOfCheckBoxes.size(); i++) {
            if(numberOfCheckBoxes.get(i).isSelected()) {
                continue;
            } else {
                manuallySelect(numberOfCheckBoxes.get(i), allHealthInformation.get(i), true);
            }
        }
    }

    /**
     * deselects all the checkboxes/health information in the grid pane
     * @param event the event triggered the method
     */
    public void handleDeselectAllButton(ActionEvent event) {
        for (int i = 0; i < numberOfCheckBoxes.size(); i++) {
            if(numberOfCheckBoxes.get(i).isSelected()) {
                manuallySelect(numberOfCheckBoxes.get(i), allHealthInformation.get(i), false);
            } else {
                continue;
            }
        }
    }

    /**
     * if a check box is selected and add the health information to the grid pane
     * @param c the checkbox selected
     * @param h the health information of the row
     * @param selected paramter to determine if checkbox is selected
     */
    private void manuallySelect(CheckBox c, HealthInformation h, boolean selected) {
        if (c.isSelected() == selected) return;
        c.setSelected(selected);
        if (selected)
            selectedHealthInformation.add(h);
        else
            removeMatchingHealthInformation(h);
    }
}
