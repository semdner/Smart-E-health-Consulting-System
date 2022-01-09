package com.ehealthsystem.registration;

import com.ehealthsystem.login.LoginController;
import com.ehealthsystem.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationController implements Initializable {

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
    TextField numberTextField;

    @FXML
    TextField zipTextField;

    @FXML
    DatePicker birthdayPicker;

    @FXML
    ChoiceBox<String> genderBox;

    @FXML
    PasswordField passwordField;

    @FXML
    PasswordField repeatPasswordField;

    @FXML
    CheckBox privateInsurancCheckBox;

    @FXML
    Label errorLabel;

    @FXML
    Label loginLabel;


    /**
     * fist method called when scene is switched.
     * Used to set the choices of the choice box
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] choices = {"male", "female", "other"};
        genderBox.getItems().addAll(choices);
    }

    /**
     * Method called when Button is pressed.
     * Used to check if values are wrong or
     * missing for registration
     * @param event event the Button reacts to
     */
    public void handleRegistrationButton(ActionEvent event) throws IOException, SQLException {
        if(validateUsername() && validateEmail() && validateFirstname() && validateLastname() && validateStreet() && validateNumber() && validateZip() && validateBirthday() && validateGender() && validatePasswords()) {
            User newUser = new User(usernameTextField.getText(),
                                    emailTextField.getText(),
                                    firstNameTextField.getText(),
                                    lastNameTextField.getText(),
                                    streetTextField.getText(),
                                    numberTextField.getText(),
                                    Integer.parseInt(zipTextField.getText()),
                                    birthdayPicker.getValue(),
                                    genderBox.getValue(),
                                    passwordField.getText(),
                                    privateInsurancCheckBox.isSelected(),
                                    true);
            switchToLoginView();
        } else {
            errorLabel.setText("Sign up information wrong or missing");
            errorLabel.setVisible(true);
        }
    }

    /**
     * Method called when Key is pressed and Username Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleUsername(KeyEvent event) {
        if(usernameTextField.getText().isBlank()) {
            errorLabel.setText("username cannot be blank");
            errorLabel.setVisible(true);
            usernameTextField.setStyle("-fx-control-inner-background: #ffb3b3;");
        }

        if(usernameTextField.getText().length() < 5) {
            errorLabel.setText("username needs to be at least 8 characters long");
            errorLabel.setVisible(true);
            usernameTextField.setStyle("-fx-control-inner-background: #ffb3b3;");
        } else {
            errorLabel.setVisible(false);
            usernameTextField.setStyle("-fx-control-inner-background: #cce6ff;");
        }
    }

    /**
     * Method called when Key is pressed and Email Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleEmail(KeyEvent event) {
        String email = emailTextField.getText();
        Pattern emailPat = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);

        if(matcher.find()) {
            errorLabel.setVisible(false);
            emailTextField.setStyle("-fx-control-inner-background: #cce6ff;");
        } else {
            errorLabel.setText("Invalid email format");
            errorLabel.setVisible(true);
            emailTextField.setStyle("-fx-control-inner-background: #ffb3b3;");
        }
    }

    /**
     * Method called when Key is pressed and First Name Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleFirstName(KeyEvent event) {
        if(firstNameTextField.getText().isBlank()) {
            errorLabel.setText("first name cannot be blank");
            errorLabel.setVisible(true);
            firstNameTextField.setStyle("-fx-control-inner-background: #ffb3b3;");
        } else {
            errorLabel.setVisible(false);
            firstNameTextField.setStyle("-fx-control-inner-background: #cce6ff;");
        }
    }

    /**
     * Method called when Key is pressed and Last Name Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleLastName(KeyEvent event) {
        if(lastNameTextField.getText().isBlank()) {
            errorLabel.setText("last name cannot be blank");
            errorLabel.setVisible(true);
            lastNameTextField.setStyle("-fx-control-inner-background: #ffb3b3;");
        } else {
            errorLabel.setVisible(false);
            lastNameTextField.setStyle("-fx-control-inner-background: #cce6ff;");
        }
    }

    /**
     * Method called when Key is pressed and Street Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleStreet(KeyEvent event) {
        if(streetTextField.getText().isBlank()) {
            errorLabel.setText("street cannot be blank");
            errorLabel.setVisible(true);
            streetTextField.setStyle("-fx-control-inner-background: #ffb3b3;");
        } else {
            errorLabel.setVisible(false);
            streetTextField.setStyle("-fx-control-inner-background: #cce6ff;");
        }
    }

    /**
     * Method called when Key is pressed and Number Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleNumber(KeyEvent event) {
        String number = numberTextField.getText();
        Pattern numberPat = Pattern.compile("^[0-9]{1,4}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = numberPat.matcher(number);

        if(numberTextField.getText().isBlank()) {
            errorLabel.setText("number cannot be blank");
            errorLabel.setVisible(true);
            numberTextField.setStyle("-fx-control-inner-background: #ffb3b3;");
        }

        if(matcher.find()) {
            errorLabel.setVisible(false);
            numberTextField.setStyle("-fx-control-inner-background: #cce6ff;");
        } else {
            errorLabel.setText("invalid house number");
            errorLabel.setVisible(true);
            numberTextField.setStyle("-fx-control-inner-background: #ffb3b3;");
        }
    }

    /**
     * Method called when Key is pressed and Zip Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleZip(KeyEvent event) {
        String zip = zipTextField.getText();
        Pattern zipPat = Pattern.compile("^[0-9]{5}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = zipPat.matcher(zip);

        if(matcher.find()) {
            errorLabel.setVisible(false);
            zipTextField.setStyle("-fx-control-inner-background: #cce6ff;");
        } else {
            errorLabel.setText("Invalid zip format");
            errorLabel.setVisible(true);
            zipTextField.setStyle("-fx-control-inner-background: #ffb3b3;");
        }
    }

    /**
     * Method called when Key is pressed and Password PasswordField is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handlePassword(KeyEvent event) {
        if(passwordField.getText().length() < 8) {
            errorLabel.setText("Password needs to be at least 8 characters");
            errorLabel.setVisible(true);
            passwordField.setStyle("-fx-control-inner-background: #ffb3b3;");
        } else {
            errorLabel.setVisible(false);
            passwordField.setStyle("-fx-control-inner-background: #cce6ff;");
        }
    }

    /**
     * Method called when Key is pressed and Reapted Password PasswordField is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleRepeatPassword(KeyEvent event) {
        if(repeatPasswordField.getText().equals(passwordField.getText())) {
            errorLabel.setVisible(false);
            repeatPasswordField.setStyle("-fx-control-inner-background: #cce6ff;");
        } else {
            errorLabel.setText("Passwords don't match");
            errorLabel.setVisible(true);
            repeatPasswordField.setStyle("-fx-control-inner-background: #ffb3b3;");
        }
    }

    /**
     * Method called when Login Label is pressed.
     * Used to check to switch the scene to the login screen.
     * @param event
     * @throws IOException
     */
    public void handleLoginLabel(MouseEvent event) throws IOException  {
        switchToLoginView();
    }

    private void switchToLoginView() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/login/login-view.fxml"));
        Stage stage = (Stage)loginLabel.getScene().getWindow();
        Scene loginScene = new Scene(root, 350, 450);
        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the username correctness.
     * @return true if username is entered correctly
     */
    public boolean validateUsername() {
        if(!usernameTextField.getText().isBlank() && usernameTextField.getText().length() >= 5) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the email correctness.
     * @return true if email is entered correctly
     */
    public boolean validateEmail() {
        String email = emailTextField.getText();
        Pattern emailPat = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);

        if(matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the first name correctness.
     * @return true if first name is entered correctly
     */
    public boolean validateFirstname() {
        if(!firstNameTextField.getText().isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the last name correctness.
     * @return true if last name is entered correctly
     */
    public boolean validateLastname() {
        if(!lastNameTextField.getText().isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the street correctness.
     * @return true if street is entered correctly
     */
    public boolean validateStreet() {
        if(!streetTextField.getText().isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the house numbers correctness.
     * @return true if house number is entered correctly
     */
    public boolean validateNumber() {
        if(!numberTextField.getText().isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the zip correctness.
     * @return true if zip is entered correctly
     */
    public boolean validateZip() {
        if(!zipTextField.getText().isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the birthday correctness.
     * @return true if birthday is entered correctly
     */
    public boolean validateBirthday() {
        LocalDate today = LocalDate.now();
        LocalDate birthday = birthdayPicker.getValue();
        Period period = Period.between(birthday, today);

        if(period.getYears() >= 18) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the gender correctness.
     * @return true if gender is entered correctly
     */
    public boolean validateGender() {
        if(!genderBox.getSelectionModel().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the password correctness.
     * @return true if password and repeated password match
     */
    public boolean validatePasswords() {
        if(!passwordField.getText().isBlank() && !repeatPasswordField.getText().isBlank() && repeatPasswordField.getText().equals(passwordField.getText())) {
            return true;
        } else {
            return false;
        }
    }

}