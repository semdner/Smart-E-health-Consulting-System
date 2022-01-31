package com.ehealthsystem.registration;

import com.ehealthsystem.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class RegistrationValidationController {

    static String validation2;

    @FXML
    TextField codeTextField;

    User newUser;

    public void start(User user) {
        System.out.println(validation2);

        newUser = user;
    }

    public void handleRegistrationButton(ActionEvent event) {
        validateCode();
    }

    public boolean validateCode() {
        if(codeTextField.getText().equals(validation2)) {
            return true;
        } else {
            return false;
        }
    }

    public static void setValidation(String validation) {
        validation2 = validation;
    }

}
