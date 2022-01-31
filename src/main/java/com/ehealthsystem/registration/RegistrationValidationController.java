package com.ehealthsystem.registration;

import com.ehealthsystem.database.Database;
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

    static String validation;

    @FXML
    TextField codeTextField;

    @FXML
    Label errorLabel;

    User newUser;
    String password;

    public void start(User user, String password) {
        System.out.println(validation);
        this.password = password;
        newUser = user;
    }

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

    public boolean validateCode() {
        if(codeTextField.getText().equals(validation)) {
            return true;
        } else {
            return false;
        }
    }

    public void handleResendLabel(MouseEvent event) throws MessagingException {
        SendEmail.validateEmail(newUser.getMail());
    }

    public void handleBackLabel(ActionEvent event) throws IOException {
        SceneSwitch.switchToCentered(event, "registration/registration-view.fxml", "E-Health System");
    }

    public static void setValidation(String validation) {
        RegistrationValidationController.validation = validation;
    }

}
