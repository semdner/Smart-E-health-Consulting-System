package com.ehealthsystem.appointment;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppointmentInformationController {

    static String email;

    public void handleContinueButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/appointmentFound-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("E-Health-System");
        stage.setScene(primaryScene);
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        AppointmentUserController.setEmail(email);
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/appointmentHealth-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("make appointment");
        stage.setScene(primaryScene);
        stage.show();
    }

    public static void setEmail(String loginEmail) {
        email = loginEmail;
    }

}
