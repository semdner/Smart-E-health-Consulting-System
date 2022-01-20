package com.ehealthsystem.primary;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.appointment.AppointmentUserController;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
        loadUserDetails();
    }

    /**
     *
     * @throws SQLException
     */
    private void loadUserDetails() {
        setUsernameLabel(Session.user.getUsername());
        setEmailLabel(Session.user.getMail());
        setFirstNameLabel(Session.user.getFirstName());
        setLastNameLabel(Session.user.getLastName());
        setStreetLabel(Session.user.getStreet());
        setHouseNoLabel(Session.user.getHouseNo());
        setZipLabel(Session.user.getZipCode());
        setBirthdayLabel(Session.user.getBirthDate());
        setGenderLabel(Session.user.getGender());
        setPrivateInsuranceLabel(Session.user.isPrivateInsurance());
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

    private void setZipLabel(String zip) {
        zipLabel.setText(zip);
    }

    private void setBirthdayLabel(LocalDate birthday) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        birthdayLabel.setText(birthday.format(formatter));
    }

    private void setGenderLabel(String gender) {
        genderLabel.setText(gender);
    }

    private void setPrivateInsuranceLabel(boolean privateInsurance) {
        privateInsuranceLabel.setText(privateInsurance ? "Yes" : "No");
    }

    /**
     * Method executed when one of the make appointment Buttons are pressed.
     * Used to switch scene to appointment form.
     * @param event
     * @throws IOException
     */
    public void handleMakeAppointmentButton(ActionEvent event) throws IOException {
        AppointmentUserController.setAppointment(new Appointment(email));
        SceneSwitch.switchTo(event, "appointment/appointmentUser-view.fxml", "Make appointment");
    }

    public void handleEditButton(ActionEvent event) throws IOException {
        PrimaryEditController.setEmail(email);
        SceneSwitch.switchTo(event, "primary/primary-edit-view.fxml", "Edit information");
    }

    public static void setEmail(String loginEmail) {
        email = loginEmail;
    }
}