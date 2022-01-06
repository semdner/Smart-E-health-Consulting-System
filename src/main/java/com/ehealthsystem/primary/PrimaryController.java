package com.ehealthsystem.primary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PrimaryController {

    @FXML
    public Button makeAppointmentButton1;
    public Button makeAppointmentButton2;

    public void handleMakeAppointmentButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/appointment1-view.fxml"));
        Stage stage = (Stage)makeAppointmentButton1.getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("make appointment");
        stage.setScene(primaryScene);
        stage.show();
    }

}
