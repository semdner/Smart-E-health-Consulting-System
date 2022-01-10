package com.ehealthsystem.primary;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PrimaryEditController implements Initializable {

    @FXML
    TextField usernameTextField;

    @FXML
    TextField emailTextField;

    @FXML
    TextField firstNameTextField;

    @FXML
    TextField lastNameTextField;

    @FXML
    TextField streetTextField;

    @FXML
    TextField houseNoTextField;

    @FXML
    TextField zipTextField;

    @FXML
    DatePicker birthdayPicker;

    @FXML
    ChoiceBox genderBox;

    @FXML
    CheckBox privateInsuranceCheckBox;

    static String email;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] choices = {"male", "female", "other"};
        genderBox.getItems().addAll(choices);
        try {
            loadUser();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadUser() throws SQLException {
        User currentUser = Database.getUserFromEmail(email);
        setUsernameTextField(currentUser.getUsername());
        setEmailTextField(currentUser.getMail());
        setFirstNameTextField(currentUser.getFirstName());
        setLastNameTextField(currentUser.getLastName());
        setStreetTextField(currentUser.getStreet());
        setHouseNoTextField(currentUser.getHouseNo());
        setZipTextField(currentUser.getZipCode());
        setBirthdayTextField(currentUser.getBirthDate());
        setGenderTextField(currentUser.getGender());
        setPrivateInsuranceTextField(currentUser.isPrivateInsurance());
    }

    private void setUsernameTextField(String username) {
        usernameTextField.setText(username);
    }

    private void setEmailTextField(String email) {
        emailTextField.setText(email);
    }

    private void setFirstNameTextField(String firstName) {
        firstNameTextField.setText(firstName);
    }

    private void setLastNameTextField(String lastName) {
        lastNameTextField.setText(lastName);
    }

    private void setStreetTextField(String street) {
        streetTextField.setText(street);
    }

    private void setHouseNoTextField(String houseNo) {
        houseNoTextField.setText(houseNo);
    }

    private void setZipTextField(int zip) {
        zipTextField.setText(String.valueOf(zip));
    }

    private void setBirthdayTextField(LocalDate birthday) {
        birthdayPicker.setValue((LocalDate)birthday);
    }

    private void setGenderTextField(String gender) {
        genderBox.setValue(gender);
    }

    private void setPrivateInsuranceTextField(boolean privateInsurance) {
        if(privateInsurance) {
            privateInsuranceCheckBox.setSelected(true);
        } else {
            privateInsuranceCheckBox.setSelected(false);
        }
    }

    public static void setEmail(String loginEmail) {
        email = loginEmail;
    }
}
