package com.ehealthsystem.registration;

import com.ehealthsystem.mail.SendEmail;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;

public class RegistrationValidationController {

    /**
     * the generated validation code.
     * Used to compare the generated validation code with the validation code entered by the user
     */
    static String validation;

    /**
     * all attributes with a fx:id set
     */
    @FXML
    TextField codeTextField;

    @FXML
    Label errorLabel;

    User newUser;
    String password;

    /**
     * The first method called when switching scenes
     * @param user the user that wants to create a new account
     * @param password the new user's password (used to insert them later into the database)
     */
    public void start(User user, String password) {
        this.password = password;
        newUser = user;
    }

    /**
     * Scene switch, if validation was successful, to the primary scene
     * @param event the event that triggered the method
     * @throws SQLException Throws Exception during connection issues.
     * @throws IOException
     */
    public void handleRegistrationButton(ActionEvent event) throws SQLException, IOException {
        if(validateCode()) {
            Session.user = newUser;
            newUser.insertIntoDb(password);
            SceneSwitch.switchToCentered(event, "primary/primary-view.fxml", "E-Health System");
        } else {
            errorLabel.setText("Invalid code");
            errorLabel.setVisible(true);
        }
    }

    /**
     * Compare the generated code with the user input.
     * If they are equal the validation is successful.
     * @return True if the user input matches with the generated code that was sent to them.
     */
    public boolean validateCode() {
        if(codeTextField.getText().equals(validation)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * If the Label is pressed a new code is sent to the user
     * @param event the event that triggered the method
     * @throws MessagingException
     * @throws IOException
     * @throws SQLException Throws Exception during connection issues.
     */
    public void handleResendLabel(MouseEvent event) throws MessagingException, IOException, SQLException {
        SendEmail.validateEmail(newUser.getMail());
    }

    /**
     * switch to the registration
     * @param event the event that triggered the method
     * @throws IOException
     */
    public void handleBackButton(ActionEvent event) throws IOException {
        SceneSwitch.switchToCentered(event, "main/main-view.fxml", "Welcome");
    }

    /**
     * set the generated validation code for comparing them to the user input.
     * @param validation the generated validation code of the passed from the email class
     */
    public static void setValidation(String validation) {
        RegistrationValidationController.validation = validation;
    }

}
