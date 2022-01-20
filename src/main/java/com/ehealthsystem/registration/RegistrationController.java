package com.ehealthsystem.registration;

import com.ehealthsystem.login.LoginController;
import com.ehealthsystem.user.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    CheckBox privateInsuranceCheckBox;

    @FXML
    Label errorLabel;
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

    private void showError(String error) {
        errorLabel.setText(error);
        errorLabel.setVisible(true);
    }

    private void redBackground(TextField field) {
        field.setStyle("-fx-control-inner-background: #ffb3b3;");
    }

    private void normalBackground(TextField field) {
        field.setStyle("-fx-control-inner-background: #cce6ff;");
    }

    /**
     * Method called when Button is pressed.
     * Used to check if values are wrong or
     * missing for registration
     * @param event event the Button reacts to
     */
    public void handleRegistrationButton(ActionEvent event) throws IOException, SQLException {
        if(validateUsername() && validateEmail() && validateFirstname() && validateLastname() && validateStreet() && validateNumber() && validateZip() && validateBirthday() && validateGender() && validatePassword() && validateRepeatPassword()) {
            User newUser = new User(usernameTextField.getText(),
                                    emailTextField.getText(),
                                    firstNameTextField.getText(),
                                    lastNameTextField.getText(),
                                    streetTextField.getText(),
                                    numberTextField.getText(),
                                    zipTextField.getText(),
                                    birthdayPicker.getValue(),
                                    genderBox.getValue(),
                                    passwordField.getText(),
                                    privateInsuranceCheckBox.isSelected(),
                                    true);
            switchToLoginView(event);
        } else {
            //showError("Sign up information wrong or missing");
            // Don't show this generic, not helpful error message,
            // a more helpful, specific error message is set and shown by each validate function
        }
    }

    /**
     * Method called when Key is pressed and Username Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleUsername(KeyEvent event) {
        validateUsername();
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the username correctness.
     * @return true if username is entered correctly
     */
    public boolean validateUsername() {
        if (usernameTextField.getText().isBlank()) {
            showError("Username cannot be blank");
            redBackground(usernameTextField);
            return false;
        } else if (usernameTextField.getText().length() < 5) {
            showError("Username needs to be at least 5 characters long");
            redBackground(usernameTextField);
            return false;
        } else {
            errorLabel.setVisible(false);
            normalBackground(usernameTextField);
            return true;
        }
    }

    /**
     * Method called when Key is pressed and Email Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleEmail(KeyEvent event) {
        validateEmail();
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
            errorLabel.setVisible(false);
            normalBackground(emailTextField);
            return true;
        } else {
            showError("Invalid email format");
            redBackground(emailTextField);
            return false;
        }
    }

    /**
     * Method called when Key is pressed and First Name Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleFirstName(KeyEvent event) {
        validateFirstname();
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the first name correctness.
     * @return true if first name is entered correctly
     */
    public boolean validateFirstname() {
        if(!firstNameTextField.getText().isBlank()) {
            errorLabel.setVisible(false);
            normalBackground(firstNameTextField);
            return true;
        } else {
            showError("First name cannot be blank");
            redBackground(firstNameTextField);
            return false;
        }
    }

    /**
     * Method called when Key is pressed and Last Name Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleLastName(KeyEvent event) {
        validateLastname();
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the last name correctness.
     * @return true if last name is entered correctly
     */
    public boolean validateLastname() {
        if(!lastNameTextField.getText().isBlank()) {
            errorLabel.setVisible(false);
            normalBackground(lastNameTextField);
            return true;
        } else {
            showError("Last name cannot be blank");
            redBackground(lastNameTextField);
            return false;
        }
    }

    /**
     * Method called when Key is pressed and Street Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleStreet(KeyEvent event) {
        if(validateStreet()) {
            errorLabel.setVisible(false);
            normalBackground(streetTextField);
        } else {
            showError("Street cannot be blank");
            redBackground(streetTextField);
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
     * Method called when Key is pressed and Number Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleNumber(KeyEvent event) {
        validateNumber();
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the house numbers correctness.
     * @return true if house number is entered correctly
     */
    public boolean validateNumber() {
        if(numberTextField.getText().isBlank()) {
            showError("Number cannot be blank");
            redBackground(numberTextField);
            return false;
        } else {
            errorLabel.setVisible(false);
            normalBackground(numberTextField);
            return true;
        }
    }

    /**
     * Method called when Key is pressed and Zip Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleZip(KeyEvent event) {
        validateZip();
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the zip correctness.
     * @return true if zip is entered correctly
     */
    public boolean validateZip() {
        String zip = zipTextField.getText();

        if(zip.isBlank()) {
            showError("Zip code cannot be blank");
            redBackground(zipTextField);
            return false;
        }

        Pattern zipPat = Pattern.compile("^[0-9]{5}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = zipPat.matcher(zip);

        if(matcher.find()) {
            errorLabel.setVisible(false);
            normalBackground(zipTextField);
            return true;
        } else {
            showError("Invalid zip format");
            redBackground(zipTextField);
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
            errorLabel.setVisible(false);
            return true;
        } else {
            showError("You need to be at least 18 years old");
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
            errorLabel.setVisible(false);
            return true;
        } else {
            showError("Please select your gender");
            return false;
        }
    }

    /**
     * Method called when Key is pressed and Password PasswordField is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handlePassword(KeyEvent event) {
        validatePassword();
    }

    /**
     * Method called when Key is pressed and Reapted Password PasswordField is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleRepeatPassword(KeyEvent event) {
        validateRepeatPassword();
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the password correctness.
     * @return true if password and repeated password match
     */
    public boolean validatePassword() {
        if(passwordField.getText().length() < 8) {
            showError("Password needs to have at least 8 characters");
            redBackground(passwordField);
            return false;
        } else {
            errorLabel.setVisible(false);
            normalBackground(passwordField);
            return true;
        }
    }

    public boolean validateRepeatPassword() {
        if(repeatPasswordField.getText().equals(passwordField.getText())) {
            errorLabel.setVisible(false);
            normalBackground(repeatPasswordField);
            return true;
        } else {
            showError("Passwords don't match");
            redBackground(repeatPasswordField);
            return false;
        }
    }

    /**
     * Method called when Login Label is pressed.
     * Used to check to switch the scene to the login screen.
     * @param event
     * @throws IOException
     */
    public void handleLoginLabel(MouseEvent event) throws IOException  {
        switchToLoginView(event);
    }

    private void switchToLoginView(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/login/login-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene loginScene = new Scene(root, 350, 450);
        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
    }
}