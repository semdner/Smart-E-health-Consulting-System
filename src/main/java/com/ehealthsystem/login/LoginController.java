package com.ehealthsystem.login;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.EmailCheck;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;

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
        if(validateAdmin() && validatePasswordField() && validateCredentials()) {
            loadAdminWindow(event);
        } else if (validateUsernameOrEmail() && validatePasswordField() && validateCredentials())
            loadPrimaryWindow(event);
    }

    /**
     * Check if the user that is currently logging in is the system's administrator
     * @return whether the admin is logging in
     */
    private boolean validateAdmin() {
        return emailTextField.getText().equals("admin");
    }

    /**
     * Method to validate the email format.
     * @return true if email format filled in matches pattern
     */
    private boolean validateUsernameOrEmail() {
        if(emailTextField.getText().contains("@")) {
            if(EmailCheck.isValidEmailAddress(emailTextField.getText())) {
                return true;
            } else {
                errorLabel.setText("Invalid username or email. Please try again");
                errorLabel.setVisible(true);
                return false;
            }
        } else {
            return true;
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
        SceneSwitch.switchToCentered(event, "primary/primary-view.fxml", "E-Health System");
    }

    private void loadAdminWindow(ActionEvent event) throws SQLException, IOException {
        Session.loginAdmin(emailTextField.getText());
        SceneSwitch.switchToCentered(event, "admin/admin-view.fxml", "Admin Panel");
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
     * @param event MouseEvent that triggered the method
     * @throws IOException if the FXMLLoader can't find the scene
     */
    public void handleRegistrationLabel(MouseEvent event) throws IOException {
        SceneSwitch.switchToCentered(event, "registration/registration-view.fxml", "Create Account");
    }

}
