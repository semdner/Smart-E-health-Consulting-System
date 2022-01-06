package com.ehealthsystem.appointment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AppointmentController {

    @FXML
    public Label firstNameLabel;
    public Label lastNameLabel;
    public Label emailLabel;
    public Label streetLabel;
    public Label houseNumberLabel;
    public Label zipCodeLabel;
    public Label birthdayLabel;

    @FXML
    public Button cancelButton;
    public Button editButton;
    public Button continueButton;

    /**
     *
     * @param event event triggered when button is pressed
     * @throws IOException if the file can't be found.
     */

    public void handleCancelButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/primary/primary-view.fxml"));
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("E-Health-System");
        stage.setScene(primaryScene);
    }

}
