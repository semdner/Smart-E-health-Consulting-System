package com.ehealthsystem;

import com.ehealthsystem.map.GeoCoder;
import com.ehealthsystem.tools.SceneSwitch;
import com.google.maps.errors.ApiException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    public Button loginButton;
    public Button registrationButton;

    @FXML
    public void handleLoginButton(ActionEvent event) throws IOException, InterruptedException, ApiException {
        SceneSwitch.switchTo(event, "login/login-view.fxml", "Login");
    }

    @FXML
    public void handleRegistrationButton(ActionEvent event) throws IOException {
        SceneSwitch.switchToCentered(event, "registration/registration-view.fxml", "Create Account");
    }

}
