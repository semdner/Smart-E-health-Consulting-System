package com.ehealthsystem.primary;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.BirthdayCheck;
import com.ehealthsystem.tools.EmailCheck;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
    TextField insuranceNameTextField;

    @FXML
    CheckBox privateInsuranceCheckBox;

    @FXML
    PasswordField oldPasswordField;

    @FXML
    PasswordField newPasswordField;

    @FXML
    Label errorLabel;

    @FXML
    Button cancelButton;

    @FXML
    Button saveButton;

    /**
     * Method called when scene is switched.
     * Used to set the ChoiceBox choices and to load the user information
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] choices = {"male", "female", "other"};
        genderBox.getItems().addAll(choices);
        loadUserDetails();
    }

    /**
     * Method called when cancel button is pressed.
     * Return to the primary window without saving edited user information
     * @param event
     * @throws IOException
     */
    public void handleCancelButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "primary/primary-view.fxml", "E-Health System");
    }

    /**
     * Method called when save button is pressed.
     * Updates the edited user information in the DB
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void handleSaveButton(ActionEvent event) throws IOException, SQLException {
        updateUserInformation();
        if(oldPasswordField.getText().isBlank() && newPasswordField.getText().isBlank())
            updatePassword(oldPasswordField.getText(), newPasswordField.getText());
        loadScene(event);
    }

    /**
     * scene switch to primary scene
     * @param event
     * @throws IOException
     */
    public void loadScene(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "primary/primary-view.fxml", "E-Health System");
    }

    /**
     * update user information when save button is pressed
     * @throws SQLException
     */
    private void updateUserInformation() throws SQLException {
        updateUsername(usernameTextField.getText());
        updateEmail(emailTextField.getText());
        if(!firstNameTextField.getText().isBlank()) {
            updateFirstName(firstNameTextField.getText());
            updateLastName(lastNameTextField.getText());
        }
        if(!streetTextField.getText().isBlank()) {
            updateStreet(streetTextField.getText());
            updateNumber(houseNoTextField.getText());
            updateZip(zipTextField.getText());
            Session.userGeo = null;
        }
        updateBirthday(birthdayPicker.getValue());
        updateGender((String) genderBox.getValue());
        updateInsuranceName(insuranceNameTextField.getText());
        updatePrivateInsurance(privateInsuranceCheckBox.isSelected());
    }

    /**
     * update username when save button is pressed
     * @param username
     * @throws SQLException
     */
    private void updateUsername(String username) throws SQLException {
        if(!usernameTextField.getText().isBlank()) {
            Session.user.setUsername(username);
        }
    }

    /**
     * update email when save button is pressed
     * @param updateEmail
     * @throws SQLException
     */
    private void updateEmail(String updateEmail) throws SQLException {
        if(EmailCheck.isValidEmailAddress(emailTextField.getText())) {
            Session.user.setEmail(updateEmail);
        }
    }

    /**
     * Update first name when save button is pressed
     * @param firstName
     * @throws SQLException
     */
    private void updateFirstName(String firstName) throws SQLException {
        Session.user.setFirstName(firstName);
    }

    /**
     * Update last name when save button is pressed
     * @param lastName
     * @throws SQLException
     */
    private void updateLastName(String lastName) throws SQLException {
        Session.user.setLastName(lastName);
    }

    /**
     * Update street when save button is pressed
     * @param street
     * @throws SQLException
     */
    private void updateStreet(String street) throws SQLException {
        Session.user.setStreet(street);
    }

    /**
     * Update house number when save button is pressed
     * @param houseNo
     * @throws SQLException
     */
    private void updateNumber(String houseNo) throws SQLException {
        Session.user.setHouseNo(houseNo);
    }

    /**
     * Update zip when save button is pressed
     * @param zip
     * @throws SQLException
     */
    private void updateZip(String zip) throws SQLException {
        Session.user.setZipCode(zip);
    }

    /**
     * Update birthday when save button is pressed
     * @param birthday
     * @throws SQLException
     */
    private void updateBirthday(LocalDate birthday) throws SQLException {
        if(BirthdayCheck.isOldEnough(birthday)) {
            Session.user.setBirthDate(birthday);
        }
    }

    /**
     * Update gender when save button is pressed
     * @param gender
     * @throws SQLException
     */
    private void updateGender(String gender) throws SQLException {
        if(!genderBox.getSelectionModel().isEmpty()) {
            Session.user.setGender(gender);
        }
    }

    private void updateInsuranceName(String text) throws SQLException {
        Session.user.setInsuranceName(text);
    }

    /**
     * Update private insurance when save button is pressed
     * @param privateInsurance
     * @throws SQLException
     */
    private void updatePrivateInsurance(boolean privateInsurance) throws SQLException {
        Session.user.setPrivateInsurance(privateInsurance);
    }

    /**
     * Update password when save button is pressed
     * @param oldPassword
     * @param newPassword
     * @throws SQLException
     */
    private void updatePassword(String oldPassword, String newPassword) throws SQLException {
        Session.user.changePassword(oldPassword, newPassword);
    }

    /**
     * Load user information when button is pressed
     * @throws SQLException
     */
    public void loadUserDetails() {
        User currentUser = Session.user;
        setUsernameTextField(currentUser.getUsername());
        setEmailTextField(currentUser.getMail());
        setFirstNameTextField(currentUser.getFirstName());
        setLastNameTextField(currentUser.getLastName());
        setStreetTextField(currentUser.getStreet());
        setHouseNoTextField(currentUser.getHouseNo());
        setZipTextField(currentUser.getZipCode());
        setBirthdayTextField(currentUser.getBirthDate());
        setGenderTextField(currentUser.getGender());
        setInsuranceNameTextField(currentUser.getInsuranceName());
        setPrivateInsuranceTextField(currentUser.isPrivateInsurance());
    }

    /**
     * Set Username Text Field to user information in DB
     * @param username
     */
    private void setUsernameTextField(String username) {
        usernameTextField.setText(username);
    }

    /**
     * Set Email Text Field to user information in DB
     * @param email
     */
    private void setEmailTextField(String email) {
        emailTextField.setText(email);
    }

    /**
     * Set First Name Text Field to user information in DB
     * @param firstName
     */
    private void setFirstNameTextField(String firstName) {
        firstNameTextField.setText(firstName);
    }

    /**
     * Set Last Name Text Field to user information in DB
     * @param lastName
     */
    private void setLastNameTextField(String lastName) {
        lastNameTextField.setText(lastName);
    }

    /**
     * Set Street Text Field to user information in DB
     * @param street
     */
    private void setStreetTextField(String street) {
        streetTextField.setText(street);
    }

    /**
     * Set House Number Text Field to user information in DB
     * @param houseNo
     */
    private void setHouseNoTextField(String houseNo) {
        houseNoTextField.setText(houseNo);
    }

    /**
     * Set Zip Text Field to user information in DB
     * @param zip
     */
    private void setZipTextField(String zip) {
        zipTextField.setText(zip);
    }

    /**
     * Set Date Picker to birthday in DB
     * @param birthday
     */
    private void setBirthdayTextField(LocalDate birthday) {
        birthdayPicker.setValue(birthday);
    }

    /**
     * Set Gender Text Field to user information in DB
     * @param gender
     */
    private void setGenderTextField(String gender) {
        genderBox.setValue(gender);
    }

    private void setInsuranceNameTextField(String insuranceName) {
        insuranceNameTextField.setText(insuranceName);
    }

    /**
     * Set private insurance Check Box to user information in DB
     * @param privateInsurance
     */
    private void setPrivateInsuranceTextField(boolean privateInsurance) {
        privateInsuranceCheckBox.setSelected(privateInsurance);
    }

    /**
     * Check if old password is correct
     * @param event
     * @throws SQLException
     */
    public void handleOldPassword(KeyEvent event) throws SQLException {
        if(!Database.checkPassword(Session.user.getMail(), oldPasswordField.getText())) {
            errorLabel.setText("wrong password");
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);
        }
    }

    /**
     * Checks if new password is correct while typing
     * @param event
     * @throws SQLException
     */
    public void handleNewPassword(KeyEvent event) throws SQLException {
        if(!newPasswordField.getText().isBlank() && oldPasswordField.getText().isBlank()) {
            errorLabel.setText("type in old password");
            errorLabel.setVisible(true);
        } else if(Database.checkPassword(Session.user.getMail(), newPasswordField.getText())) {
            errorLabel.setText("old password cannot be new password");
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);
        }
    }

    /**
     * handle Keyboard input for Email Text Field.
     * Check if Email format matches
     */
    public void handleEmailTextField(KeyEvent event) {
        if(EmailCheck.isValidEmailAddress(emailTextField.getText())) {
           errorLabel.setVisible(false);
        } else {
            errorLabel.setText("Invalid email format");
            errorLabel.setVisible(true);
        }
    }
}
