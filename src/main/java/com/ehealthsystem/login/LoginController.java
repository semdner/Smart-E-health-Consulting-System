package com.ehealthsystem.login;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.EmailCheck;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {

    @FXML
    public TextField emailTextField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Button loginButton;

    @FXML
    public Label registrationLabel;
    public Label errorLabel;

    /**
     * Method called when Login Button is pressed.
     * Used to validate the filled in information.
     * @param event event that triggered the login button
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void handleLoginButton(ActionEvent event) throws IOException, SQLException {
        if (validateEmail() && validatePasswordField() && validateCredentials())
            loadPrimaryWindow(event);
    }

    /**
     * Method to validate the email format.
     * @return true if email format filled in matches pattern
     */
    private boolean validateEmail() {
        if(EmailCheck.isValidEmailAddress(emailTextField.getText())) {
            return true;
        } else {
            errorLabel.setText("Invalid email format. Please try again");
            errorLabel.setVisible(true);
            return false;
        }
    }

    /**
     * Method to validate the Password typed in.
     * @return true if the password field is filled
     */
    private boolean validatePasswordField() {
        if (passwordField.getText().isBlank()) {
            errorLabel.setText("Password cannot be blank.");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    /**
     * Method called to switch scene to primary window after successfully logged in.
     * @throws IOException FXMLLoader can't find file for switching scene
     */
    private void loadPrimaryWindow(Event event) throws IOException, SQLException {
        Session.loginUser(emailTextField.getText());

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneSwitch.switchTo(event, "primary/primary-view.fxml", "E-Health System");
        // center window
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * Method to validate the email and password matches with the email and password in the database
     * @return true if credentials match the credentials in the database
     * @throws SQLException
     */
    public boolean validateCredentials() throws SQLException {
        if(Database.checkPassword(emailTextField.getText(), passwordField.getText())) {
            return true;
        } else {
            errorLabel.setText("Invalid username or password");
            errorLabel.setVisible(true);
            return false;
        }
    }

    /**
     * Method called to switch scene to registration form.
     * @param event MouseEvent trigged the method
     * @throws IOException if the FXMLLoader can't find the scene
     */
    public void handleRegistrationLabel(MouseEvent event) throws IOException {
        SceneSwitch.switchTo(event, "registration/registration-view.fxml", "Create Account");
    }

}
