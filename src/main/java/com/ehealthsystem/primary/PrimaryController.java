package com.ehealthsystem.primary;

import com.ehealthsystem.Main;
import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.appointment.AppointmentController;
import com.ehealthsystem.appointment.AppointmentInCreation;
import com.ehealthsystem.pdf.CreatePDF;
import com.ehealthsystem.pdf.fileChooserSave;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
    Label insuranceNameLabel;

    @FXML
    Label privateInsuranceLabel;

    @FXML
    Button makeAppointmentButton1;

    @FXML
    Button makeAppointmentButton2;

    @FXML
    GridPane upcomingAppointmentsGridPane;

    @FXML
    GridPane pastAppointmentsGridPane;

    @FXML
    Label upcomingAppointmentsErrorLabel;

    @FXML
    Label pastAppointmentsErrorLabel;

    /**
     * First method called when scene is switched.
     * Used to set the user information form the database.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUserDetails();
        try {
            loadUserAppointments();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        Platform.runLater( () -> makeAppointmentButton1.requestFocus() ); //https://stackoverflow.com/a/38374747/18039017
    }

    private void loadUserAppointments() throws SQLException, IOException {
        ArrayList<Appointment> appointments = Session.user.getAppointments();
        int pastRow = 0;
        int upcomingRow = 0;
        ArrayList<VBox> pastAppointmentBoxes = new ArrayList<>();
        for (int i = 0; i<appointments.size(); i++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("/com/ehealthsystem/appointment/appointment-view.fxml"));

            VBox vbox = fxmlloader.load();

            AppointmentController appointment = fxmlloader.getController();
            appointment.setData(appointments.get(i));
            if(appointments.get(i).isInThePast()) {
                pastAppointmentBoxes.add(vbox);
            } else {
                upcomingAppointmentsGridPane.add(vbox, 0, upcomingRow);
                upcomingRow++;
            }
        }

        Collections.reverse(pastAppointmentBoxes); //show past appointments in reverse order (i.e. the latest one first, the oldest one last)
        for (VBox pastAppointmentBox : pastAppointmentBoxes) {
            pastAppointmentsGridPane.add(pastAppointmentBox, 0, pastRow);
            pastRow++;
        }

        if (upcomingAppointmentsGridPane.getChildren().isEmpty()) {
            upcomingAppointmentsErrorLabel.setText("No upcoming appointments.");
            upcomingAppointmentsErrorLabel.setVisible(true);
        }
        if (pastAppointmentsGridPane.getChildren().isEmpty()) {
            pastAppointmentsErrorLabel.setText("No past appointments.");
            pastAppointmentsErrorLabel.setVisible(true);
        }

        System.out.println("Users appointments: " + appointments.size());
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
        setHouseNoLabel(Session.user.getHouseNumber());
        setZipLabel(Session.user.getZipCode());
        setBirthdayLabel(Session.user.getBirthDate());
        setGenderLabel(Session.user.getGender());
        setInsuranceNameLabel(Session.user.getInsuranceName());
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
        birthdayLabel.setText(birthday.format(Session.dateFormatter));
    }

    private void setGenderLabel(String gender) {
        genderLabel.setText(gender);
    }

    private void setInsuranceNameLabel(String insuranceName) {
        insuranceNameLabel.setText(insuranceName);
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
        Session.appointment = new AppointmentInCreation();
        SceneSwitch.switchTo(event, "appointment/appointmentUser-view.fxml", "Make appointment");
    }

    public void handleEditButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "primary/primary-edit-view.fxml", "Edit information");
    }
    
    public void handlepdfButton(ActionEvent event) throws SQLException, IOException {
        String dest = fileChooserSave.chooseDest(Main.getPrimaryStage());
        CreatePDF.create_Pdf(dest);
    }

    public void handleLogout(ActionEvent event) throws IOException {
        Session.logout();
    }

    public void handleHealthInformation(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(null, "healthInformation/healthInformation-view.fxml", "Health information");
    }
}
