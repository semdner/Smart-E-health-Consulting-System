package com.ehealthsystem.primary;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    static String email;

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
        try {
            loadUser();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method called when cancel button is pressed.
     * Return to the primary window without saving edited user information
     * @param event
     * @throws IOException
     */
    public void handleCancelButton(ActionEvent event) throws IOException {
        PrimaryController.setEmail(email);
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/primary/primary-view.fxml"));
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("E-Health System");
        stage.setScene(primaryScene);
        stage.show();
    }

    /**
     * Method called when save button is pressed.
     * Updates the edited user information in the DB
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void handleSaveButton(ActionEvent event) throws IOException, SQLException {
        if(oldPasswordField.getText().isBlank() && newPasswordField.getText().isBlank()) {
            updateUserInformation();
            loadScene(event);
        } else {
            updateUserInformation();
            updatePassword(oldPasswordField.getText(), newPasswordField.getText());
            loadScene(event);
        }
    }

    /**
     * scene switch to primary scene
     * @param event
     * @throws IOException
     */
    public void loadScene(ActionEvent event) throws IOException {
        PrimaryController.setEmail(emailTextField.getText());
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/primary/primary-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("E-Health System");
        stage.setScene(primaryScene);
        stage.show();
    }

    /**
     * update user information when save button is pressed
     * @throws SQLException
     */
    private void updateUserInformation() throws SQLException {
        updateUsername(usernameTextField.getText());
        updateEmail(emailTextField.getText());
        updateFirstName(firstNameTextField.getText());
        updateLastName(lastNameTextField.getText());
        if(!streetTextField.getText().isBlank()) {
            updateStreet(streetTextField.getText());
            updateNumber(houseNoTextField.getText());
            updateZip(zipTextField.getText());
        }
        updateBirthday(birthdayPicker.getValue());
        updateGender((String) genderBox.getValue());
        updatePrivateInsurance(privateInsuranceCheckBox.isSelected());
    }

    /**
     * update username when save button is pressed
     * @param username
     * @throws SQLException
     */
    private void updateUsername(String username) throws SQLException {
        if(!usernameTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setUsername(username);
        }
    }

    /**
     * update email when save button is pressed
     * @param updateEmail
     * @throws SQLException
     */
    private void updateEmail(String updateEmail) throws SQLException {
        Pattern emailPat = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(updateEmail);

        if(matcher.find() && !emailTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setEmail(updateEmail);
            setEmail(updateEmail);
        }
    }

    /**
     * Update first name when save button is pressed
     * @param fristName
     * @throws SQLException
     */
    private void updateFirstName(String fristName) throws SQLException {
        if(!firstNameTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setFirstName(fristName);
        }
    }

    /**
     * Update last name when save button is pressed
     * @param lastName
     * @throws SQLException
     */
    private void updateLastName(String lastName) throws SQLException {
        if(!firstNameTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setLastName(lastName);
        }
    }

    /**
     * Update street when save button is pressed
     * @param street
     * @throws SQLException
     */
    private void updateStreet(String street) throws SQLException {
        User updateUser = Database.getUserFromEmail(email);
        updateUser.setStreet(street);
    }

    /**
     * Update house number when save button is pressed
     * @param houseNo
     * @throws SQLException
     */
    private void updateNumber(String houseNo) throws SQLException {
        User updateUser = Database.getUserFromEmail(email);
        updateUser.setHouseNo(houseNo);
    }

    /**
     * Update zip when save button is pressed
     * @param zip
     * @throws SQLException
     */
    private void updateZip(String zip) throws SQLException {
        User updateUser = Database.getUserFromEmail(email);
        updateUser.setZipCode(zip);
    }

    /**
     * Update birthday when save button is pressed
     * @param birthday
     * @throws SQLException
     */
    private void updateBirthday(LocalDate birthday) throws SQLException {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthday, today);
        if(period.getYears() >= 18) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setBirthDate(birthday);
        }
    }

    /**
     * Update gender when save button is pressed
     * @param gender
     * @throws SQLException
     */
    private void updateGender(String gender) throws SQLException {
        if(!genderBox.getSelectionModel().isEmpty()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setGender(gender);
        }
    }

    /**
     * Update private insurance when save button is pressed
     * @param privateInsurance
     * @throws SQLException
     */
    private void updatePrivateInsurance(boolean privateInsurance) throws SQLException {
        User updateUser = Database.getUserFromEmail(email);
        updateUser.setPrivateInsurance(privateInsurance);
    }

    /**
     * Update password when save button is pressed
     * @param oldPassword
     * @param newPassword
     * @throws SQLException
     */
    private void updatePassword(String oldPassword, String newPassword) throws SQLException {
        User updateUser = Database.getUserFromEmail(email);
        updateUser.changePassword(oldPassword, newPassword);
    }

    /**
     * Load user information when button is pressed
     * @throws SQLException
     */
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
        birthdayPicker.setValue((LocalDate)birthday);
    }

    /**
     * Set Gender Text Field to user information in DB
     * @param gender
     */
    private void setGenderTextField(String gender) {
        genderBox.setValue(gender);
    }

    /**
     * Set private insurance Check Box to user information in DB
     * @param privateInsurance
     */
    private void setPrivateInsuranceTextField(boolean privateInsurance) {
        if(privateInsurance) {
            privateInsuranceCheckBox.setSelected(true);
        } else {
            privateInsuranceCheckBox.setSelected(false);
        }
    }

    /**
     * Check if old password is correct
     * @param event
     * @throws SQLException
     */
    public void handleOldPassword(KeyEvent event) throws SQLException {
        if(!Database.checkPassword(email, oldPasswordField.getText())) {
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
        } else if(Database.checkPassword(email, newPasswordField.getText())) {
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
        String email = emailTextField.getText();
        Pattern emailPat = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);

        if(matcher.find()) {
           errorLabel.setVisible(false);
        } else {
            errorLabel.setText("Invalid email format");
            errorLabel.setVisible(true);
        }
    }

    /**
     * set mail when scene is changed
     * @param loginEmail emailed used for login
     */
    public static void setEmail(String loginEmail) {
        email = loginEmail;
    }
}
