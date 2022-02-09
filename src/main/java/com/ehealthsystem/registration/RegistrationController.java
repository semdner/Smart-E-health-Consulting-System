package com.ehealthsystem.registration;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.mail.SendEmail;
import com.ehealthsystem.map.Geocoder;
import com.ehealthsystem.tools.BirthdayCheck;
import com.ehealthsystem.tools.EmailCheck;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.user.User;
import com.google.maps.errors.ApiException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Method for managing the Registration of a new user
 */
public class RegistrationController implements Initializable {

    /**
     * all attributes with a fx:id assigned in the view
     */
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
    RadioButton genderChoiceMale;
    @FXML
    RadioButton genderChoiceFemale;
    @FXML
    RadioButton genderChoiceOther;

    List<RadioButton> genderRadioButtons;

    @FXML
    PasswordField passwordField;

    @FXML
    PasswordField repeatPasswordField;

    @FXML
    TextField insuranceNameTextField;

    @FXML
    CheckBox privateInsuranceCheckBox;

    @FXML
    Label errorLabel;

    @FXML
    TextField birthdayEditor;
    private String gender;

    /**
     * fist method called when scene is switched.
     * Used to set the choices of the choice box
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderRadioButtons = Arrays.asList(genderChoiceMale, genderChoiceFemale, genderChoiceOther);
        Platform.runLater(() -> firstNameTextField.requestFocus()); //https://stackoverflow.com/a/38374747/1803901

        birthdayPicker.valueProperty().addListener((observable, oldDate, newDate) -> {
            birthdayEditor.setText(birthdayPicker.getValue().format(Session.dateFormatter));
            validateBirthday();
        });
    }

    /**
     * Shows an individual error label when the input is invalid.
     * @param error The error text.
     */
    private void showError(String error) {
        errorLabel.setText(error);
        errorLabel.setVisible(true);
    }

    /**
     * Set a fields background red when the input is invalid.
     * @param field The text field in the registration form.
     */
    private void redBackground(TextField field) {
        field.setStyle("-fx-control-inner-background: #ffb3b3;");
    }

    /**
     * Set an error on a field. Field will be shown in red and the error will be displayed.
     * @param field the field to show in red
     * @param error the error to display
     */
    private void fieldError(TextField field, String error) {
        redBackground(field);
        showError(error);
    }

    /**
     * Hide an error on a field. Field's color will be reset and error message will not be displayed.
     * @param field The text field in the registration.
     */
    private void hideError(TextField field) {
        errorLabel.setVisible(false);
        normalBackground(field);
    }

    /**
     * Reset a fields background
     * @param field The text field in the registration.
     */
    private void normalBackground(TextField field) {
        field.setStyle("-fx-control-inner-background: #cce6ff;");
    }

    /**
     * Method called when Button is pressed.
     * Used to check if values are wrong or
     * missing for registration
     * @param event event the Button reacts to
     */
    public void handleRegistrationButton(ActionEvent event) throws IOException, SQLException, MessagingException, InterruptedException, ApiException {
        if(validateFirstname() && validateLastname() && validateEmail() && validateUsername() && validatePassword() && validateRepeatPassword() && validateStreet() && validateNumber() && validateAddress() && validateZip() && validateBirthday() && validateGender() && validateInsuranceName()) {
            errorLabel.setVisible(false);
            User newUser = new User(usernameTextField.getText(),
                                    emailTextField.getText(),
                                    firstNameTextField.getText(),
                                    lastNameTextField.getText(),
                                    streetTextField.getText(),
                                    numberTextField.getText(),
                                    zipTextField.getText(),
                                    birthdayPicker.getValue(),
                                    gender,
                                    passwordField.getText(),
                                    insuranceNameTextField.getText(),
                                    privateInsuranceCheckBox.isSelected(),
                                    false);
            SendEmail.validateEmail(newUser.getMail());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ehealthsystem/registration/registrationValidation-view.fxml"));
            Parent root = loader.load();

            RegistrationValidationController controller = loader.getController();
            controller.start(newUser, passwordField.getText());

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene primaryScene = new Scene(root);
            stage.setTitle("E-Health-System");
            stage.setScene(primaryScene);
            SceneSwitch.centerWindow(stage);
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
    public void handleUsername(KeyEvent event) throws SQLException {
        validateUsername();
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the username correctness.
     * @return true if username is entered correctly
     */
    public boolean validateUsername() throws SQLException {
        if (usernameTextField.getText().isBlank()) {
            fieldError(usernameTextField, "Username cannot be blank");
            return false;
        } else if (usernameTextField.getText().length() < 5) {
            fieldError(usernameTextField, "Username needs to be at least 5 characters long");
            return false;
        } else if (Database.isUsernameTaken(usernameTextField.getText())) {
            fieldError(usernameTextField, "Username already in use");
            return false;
        } else {
            hideError(usernameTextField);
            return true;
        }
    }

    /**
     * Used to validate the Birthday when user inputs the date into the text field.
     * @param event the event that triggered the methods execution
     */
    public void handleBirthdayEditor(KeyEvent event){
        validateBirthday();
    }

    /**
     * Method called when Key is pressed and Email Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleEmail(KeyEvent event) throws SQLException {
        validateEmail();
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the email correctness.
     * @return true if email is entered correctly
     */
    public boolean validateEmail() throws SQLException {
        if(!EmailCheck.isValidEmailAddress(emailTextField.getText())) {
            fieldError(emailTextField, "Invalid email format");
            return false;
        } else if (Database.isEmailTaken(emailTextField.getText())) {
            fieldError(emailTextField, "Email already used for another account");
            return false;
        } else {
            hideError(emailTextField);
            return true;
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
            hideError(firstNameTextField);
            return true;
        } else {
            fieldError(firstNameTextField, "First name cannot be blank");
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
            hideError(lastNameTextField);
            return true;
        } else {
            fieldError(lastNameTextField, "Last name cannot be blank");
            return false;
        }
    }

    /**
     * Method called when Key is pressed and Street Textbox is selected.
     * Used to check if information are right/wrong while typing.
     * @param event KeyEvent the Textbox reacts to
     */
    public void handleStreet(KeyEvent event) {
        validateStreet();
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the street correctness.
     * @return true if street is entered correctly
     */
    public boolean validateStreet() {
        if(!streetTextField.getText().isBlank()) {
            hideError(streetTextField);
            return true;
        } else {
            fieldError(streetTextField, "Street cannot be blank");
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
            fieldError(numberTextField, "Number cannot be blank");
            return false;
        } else {
            hideError(numberTextField);
            return true;
        }
    }

    /**
     * check if the address exist to validate the users input.
     * @return True if the address exist, else false in addition to showing an error message.
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    private boolean validateAddress() throws IOException, InterruptedException, ApiException {
        try {
            Geocoder.geocode("%s %s, %s".formatted(streetTextField.getText(), numberTextField.getText(), zipTextField.getText()));
            return true;
        } catch (ArrayIndexOutOfBoundsException e) { //[0] in geocode()
            showError("Unknown address, did you enter everything correctly?");
            return false;
        }
    }

    /**
     * Method called when the insurance name is typed in.
     * Used to validate that the user input is not empty
     * @param keyEvent the event that triggers the method execution
     */
    public void handleInsuranceName(KeyEvent keyEvent) {
        validateInsuranceName();
    }

    /**
     * Check if the insurance name is empty.
     * If yes throw an error and return false and if not the opposite.
     * @return if the insurance name is valid
     */
    private boolean validateInsuranceName() {
        if(insuranceNameTextField.getText().isBlank()) {
            fieldError(insuranceNameTextField, "Insurance name cannot be blank");
            return false;
        } else {
            hideError(insuranceNameTextField);
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
            fieldError(zipTextField, "Zip code cannot be blank");
            return false;
        }

        Pattern zipPat = Pattern.compile("^[0-9]{5}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = zipPat.matcher(zip);

        if(matcher.find()) {
            hideError(zipTextField);
            return true;
        } else {
            fieldError(zipTextField, "Invalid zip format");
            return false;
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the birthday correctness.
     * @return true if birthday is entered correctly
     */
    public boolean validateBirthday() {
        if(birthdayEditor.getText().isBlank()){
            fieldError(birthdayEditor,"Birthday has to be entered");
            return false;
        }else {
            LocalDate date;
            try {
                date = LocalDate.parse(birthdayEditor.getText(), DateTimeFormatter.ofPattern("d.M.yyyy")); //allows user to leave ot leading 0s in day and month (e.g. 1.1.2000)
            } catch (DateTimeParseException e) {
                fieldError(birthdayEditor,"Invalid date format for birthday");
                return false;
            }
            hideError(birthdayEditor);
            birthdayPicker.setValue(date);
        }

        if(!BirthdayCheck.isOldEnough(birthdayPicker.getValue())) {
            fieldError(birthdayEditor, "You need to be at least " + BirthdayCheck.MINIMUM_AGE + " years old");
            return false;
        }

        errorLabel.setVisible(false);
        return true;
    }

    /**
     * Method to handle the proper functioning of the radio buttons for gender selection
     * @param event ActionEvent triggered when pressing a radio button
     */
    public void handleGenderChoice(ActionEvent event) {
        RadioButton source = ((RadioButton) event.getSource());
        gender = source.getText();
        for (RadioButton choice : genderRadioButtons) {
            choice.setSelected(choice == source); //deselect all other choices
        }
    }

    /**
     * Helper method for the handleRegistrationButton method.
     * Used to validate the gender correctness.
     * @return true if gender is entered correctly
     */
    public boolean validateGender() {
        if(gender != null) {
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
     * Method called when Key is pressed and Repeated Password PasswordField is selected.
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
            fieldError(passwordField, "Password needs to have at least 8 characters");
            return false;
        } else {
            hideError(passwordField);
            return true;
        }
    }

    public boolean validateRepeatPassword() {
        if(repeatPasswordField.getText().equals(passwordField.getText())) {
            hideError(repeatPasswordField);
            return true;
        } else {
            fieldError(repeatPasswordField, "Passwords don't match");
            return false;
        }
    }

    /**
     * Method called when back button is pressed.
     * @param event Trigger to invoke this method.
     */
    public void handleBackButton(ActionEvent event) throws IOException {
        SceneSwitch.switchToCentered(event, "main/main-view.fxml", "Welcome");
    }
}