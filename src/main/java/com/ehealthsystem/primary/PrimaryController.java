package com.ehealthsystem.primary;

import com.ehealthsystem.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import com.ehealthsystem.database.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {

    @FXML
    Label usernameLabel;

    @FXML
    Label emailLabel;

    @FXML
    Label firstNameLabel;

    @FXML
    Label lastNameLabel;

    @FXML
    Label streetLabel;

    @FXML
    Label houseNoLabel;

    @FXML
    Label zipLabel;

    @FXML
    Label birthdayLabel;

    @FXML
    Label genderLabel;

    @FXML
    Label privateInsuranceLabel;

    @FXML
    Button makeAppointmentButton1;

    @FXML
    Button makeAppointmentButton2;

    static String email;

    /**
     * First method called when scene is switched.
     * Used to set the user information form the database.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUser(email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param email
     * @throws SQLException
     */
    private void loadUser(String email) throws SQLException {
        User currentUser = Database.getUserFromEmail(email);
        setUsernameLabel(currentUser.getUsername());
        setEmailLabel(currentUser.getMail());
        setFirstNameLabel(currentUser.getFirstName());
        setLastNameLabel(currentUser.getLastName());
        setStreetLabel(currentUser.getStreet());
        setHouseNoLabel(currentUser.getHouseNo());
        setZipLabel(currentUser.getZipCode());
        setBirthdayLabel(currentUser.getBirthDate());
        setGenderLabel(currentUser.getGender());
        setPrivateInsuranceLabel(currentUser.isPrivateInsurance());
    }

    private void setUsernameLabel(String username) {
        usernameLabel.setText(username);
    }

    private void setEmailLabel(String email) {
        emailLabel.setText(email);
    }

    private void setFirstNameLabel(String firstName) {
        firstNameLabel.setText(firstName);
    }

    private void setLastNameLabel(String lastName) {
        lastNameLabel.setText(lastName);
    }

    private void setStreetLabel(String street) {
        streetLabel.setText(street);
    }

    private void setHouseNoLabel(String houseNo) {
        houseNoLabel.setText(houseNo);
    }

    private void setZipLabel(int zip) {
        zipLabel.setText(String.valueOf(zip));
    }

    private void setBirthdayLabel(LocalDate birthday) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        birthdayLabel.setText(birthday.format(formatter));
    }

    private void setGenderLabel(String gender) {
        genderLabel.setText(gender);
    }

    private void setPrivateInsuranceLabel(boolean privateInsurance) {
        if(privateInsurance) {
            privateInsuranceLabel.setText("yes");
        } else {
            privateInsuranceLabel.setText("no");
        }
    }

    /**
     * Method executed when one of the make appointment Buttons are pressed.
     * Used to switch scene to appointment form.
     * @param event
     * @throws IOException
     */
    public void handleMakeAppointmentButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/appointment1-view.fxml"));
        Stage stage = (Stage)makeAppointmentButton1.getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("make appointment");
        stage.setScene(primaryScene);
        stage.show();
    }

    public void handleEditButton(ActionEvent event) throws IOException {
        PrimaryEditController.setEmail(email);
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/primary/primary-edit-view.fxml"));
        Stage stage = (Stage)makeAppointmentButton1.getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("make appointment");
        stage.setScene(primaryScene);
        stage.show();
    }

    public static void setEmail(String loginEmail) {
        email = loginEmail;
    }
}