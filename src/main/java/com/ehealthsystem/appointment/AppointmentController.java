package com.ehealthsystem.appointment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
     * Switch scene to primary
     *
     * @param event event triggered when cancel button is pressed
     * @throws IOException if the FXMLLoader cant find .fxml file
     */

    public void handleCancelButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/primary/primary-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("E-Health-System");
        stage.setScene(primaryScene);
    }

    /**
     * Open new window to edit the information.
     *
     * @param event event triggered when edit button is pressed
     * @throws IOException if the FXMLLoader cant find .fxml file
     */
    public void handleEditButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/edit/editAppointment1-view.fxml"));
        Stage stage = new Stage();
        Scene dialogScene = new Scene(root, 500, 400);
        stage.setTitle("edit user information");
        stage.setScene(dialogScene);
        stage.show();
    }

    /**
     * Switch scene to next appointment view
     *
     * @param event
     * @throws IOException
     */
    public void handleContinueButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/appointment2-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("make appointment");
        stage.setScene(primaryScene);
    }

}

