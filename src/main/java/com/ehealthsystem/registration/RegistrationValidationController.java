package com.ehealthsystem.registration;

import com.ehealthsystem.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class RegistrationValidationController {

    String validation;

    @FXML
    TextField codeTextField;

    User newUser;

    public void start(User user) {
        newUser = user;
    }

    public void handleRegistrationButton(ActionEvent event) {
        validateCode();
    }

    public boolean validateCode() {
        if(codeTextField.getText().equals(validation)) {
            return true;
        } else {
            return false;
        }
    }

    public void getValidation() {

    }

}
