package com.ehealthsystem;

import com.ehealthsystem.tools.SceneSwitch;
import com.google.maps.errors.ApiException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Controller for manging the first/"main" scene
 */
public class MainController {

    /**
     * attributes with a fx:id assigned in the view
     */
    @FXML
    public Button loginButton;

    @FXML
    public Button registrationButton;

    /**
     * Handle event when login button is pressed.
     * Used to switch to the login scene.
     * @param event the event that triggers the execution of the method
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    public void handleLoginButton(ActionEvent event) throws IOException, InterruptedException, ApiException {
        SceneSwitch.switchTo(event, "login/login-view.fxml", "Login");
    }

    /**
     * Handle event when registration button is pressed
     * Used to switch to the registration scene.
     * @param event the event that triggers the execution of the method
     * @throws IOException
     */
    @FXML
    public void handleRegistrationButton(ActionEvent event) throws IOException {
        SceneSwitch.switchToCentered(event, "registration/registration-view.fxml", "Create Account");
    }

}
