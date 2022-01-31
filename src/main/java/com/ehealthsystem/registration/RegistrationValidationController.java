package com.ehealthsystem.registration;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class RegistrationValidationController {

    static String validation2;

    @FXML
    TextField codeTextField;

    @FXML
    Label errorLabel;

    User newUser;
    String password;

    public void start(User user, String password) {
        System.out.println(validation2);
        this.password = password;
        newUser = user;
    }

    public void handleRegistrationButton(ActionEvent event) throws SQLException, IOException {
        if(validateCode()) {
            Session.user = newUser;
            Object[][] parameters = {
                    {"username", Session.user.getUsername()},
                    {"email", Session.user.getMail()},
                    {"first_name", Session.user.getFirstName()},
                    {"last_name", Session.user.getLastName()},
                    {"street", Session.user.getStreet()},
                    {"number", Session.user.getHouseNo()},
                    {"zip", Session.user.getZipCode()},
                    {"birthday", Session.user.getBirthDate()},
                    {"sex", Session.user.getGender()},
                    {"password", Database.hashPassword(password)},
                    {"insurance_name", Session.user.getInsuranceName()},
                    {"private_insurance", Session.user.isPrivateInsurance()},
            };
            Database.insert("user", parameters);
            SceneSwitch.switchToCentered(event, "primary/primary-view.fxml", "E-Health System");
        } else {
            errorLabel.setText("Invalid code");
            errorLabel.setVisible(true);
        }
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
