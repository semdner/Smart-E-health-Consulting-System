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

    public void handleCancelButton(ActionEvent event) throws IOException {
        PrimaryController.setEmail(email);
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/primary/primary-view.fxml"));
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("E-Health System");
        stage.setScene(primaryScene);
        stage.show();
    }

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

    public void loadScene(ActionEvent event) throws IOException {
        PrimaryController.setEmail(emailTextField.getText());
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/primary/primary-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("E-Health System");
        stage.setScene(primaryScene);
        stage.show();
    }

    private void updateUserInformation() throws SQLException {
        updateUsername(usernameTextField.getText());
        updateEmail(emailTextField.getText());
        updateFirstName(firstNameTextField.getText());
        updateLastName(lastNameTextField.getText());
        updateStreet(streetTextField.getText());
        updateNumber(houseNoTextField.getText());
        updateZip(zipTextField.getText());
        updateBirthday(birthdayPicker.getValue());
        updateGender((String) genderBox.getValue());
        updatePrivateInsurance(privateInsuranceCheckBox.isSelected());
    }

    private void updateUsername(String username) throws SQLException {
        if(!usernameTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setUsername(username);
        }
    }

    private void updateEmail(String updateEmail) throws SQLException {
        Pattern emailPat = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(updateEmail);

        if(matcher.find() && !emailTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setEmail(updateEmail);
            setEmail(updateEmail);
        }
    }

    private void updateFirstName(String fristName) throws SQLException {
        if(!firstNameTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setFirstName(fristName);
        }
    }

    private void updateLastName(String lastName) throws SQLException {
        if(!firstNameTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setLastName(lastName);
        }
    }

    private void updateStreet(String street) throws SQLException {
        if(!streetTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setStreet(street);
        }
    }

    private void updateNumber(String houseNo) throws SQLException {
        if(!streetTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setHouseNo(houseNo);
        }
    }

    private void updateZip(String zip) throws SQLException {
        if(!streetTextField.getText().isBlank()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setZipCode(Integer.parseInt(zip));
        }
    }

    private void updateBirthday(LocalDate birthday) throws SQLException {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthday, today);
        if(period.getYears() >= 18) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setBirthDate(birthday);
        }
    }

    private void updateGender(String gender) throws SQLException {
        if(!genderBox.getSelectionModel().isEmpty()) {
            User updateUser = Database.getUserFromEmail(email);
            updateUser.setGender(gender);
        }
    }

    private void updatePrivateInsurance(boolean privateInsurance) throws SQLException {
        User updateUser = Database.getUserFromEmail(email);
        updateUser.setPrivateInsurance(privateInsurance);
    }

    private void updatePassword(String oldPassword, String newPassword) throws SQLException {
        User updateUser = Database.getUserFromEmail(email);
        updateUser.changePassword(oldPassword, newPassword);
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

    public void handleOldPassword(KeyEvent event) throws SQLException {
        if(!Database.checkPassword(email, oldPasswordField.getText())) {
            errorLabel.setText("wrong password");
            errorLabel.setVisible(true);
        } else {
            errorLabel.setVisible(false);
        }
    }

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

    public void handleEmailTextField() {
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

    public static void setEmail(String loginEmail) {
        email = loginEmail;
    }
}
