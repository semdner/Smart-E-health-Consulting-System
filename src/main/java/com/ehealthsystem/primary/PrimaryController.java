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

/**
 * Used to manage the primary scene
 */
public class PrimaryController implements Initializable {

    /**
     * All attributes with a fx:id assigned in the view
     */
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

    /**
     * load the user appointments to show them in a list for managing them.
     * @throws SQLException Throws Exception during connection issues.
     * @throws IOException
     */
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

        //System.out.println("Users appointments: " + appointments.size());
    }

    /**
     * Call the setter methods for setting the user data to show them his user information
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

    /**
     * set the username on the users profile
     * @param username the username of the user logged-in in the session
     */
    private void setUsernameLabel(String username) {
        usernameLabel.setText(username);
    }

    /**
     * set the email on the users profile
     * @param email the email of the user logged-in in the session
     */
    private void setEmailLabel(String email) {
        emailLabel.setText(email);
    }

    /**
     * set the first name on the users profile
     * @param firstName the first name of the user logged-in in the session
     */
    private void setFirstNameLabel(String firstName) {
        firstNameLabel.setText(firstName);
    }

    /**
     * set the last name on the users profile
     * @param lastName last name of the user logged-in in the session
     */
    private void setLastNameLabel(String lastName) {
        lastNameLabel.setText(lastName);
    }

    /**
     * set the street label on the users profile
     * @param street street of the user logged-in in the session
     */
    private void setStreetLabel(String street) {
        streetLabel.setText(street);
    }

    /**
     * set the houseNoLabel on the users profile
     * @param houseNo house number of the user logged-in in the session
     */
    private void setHouseNoLabel(String houseNo) {
        houseNoLabel.setText(houseNo);
    }

    /**
     * set the zip code on the users profile
     * @param zip zip code of the user logged-in in the session
     */
    private void setZipLabel(String zip) {
        zipLabel.setText(zip);
    }

    /**
     * set the birthday on the users profile
     * @param birthday birthday of the user logged-in in the session
     */
    private void setBirthdayLabel(LocalDate birthday) {
        birthdayLabel.setText(birthday.format(Session.dateFormatter));
    }

    /**
     * set the gender on the users profile
     * @param gender gender of the user logged-in in the session
     */
    private void setGenderLabel(String gender) {
        genderLabel.setText(gender);
    }

    /**
     * set the insurance name on the users profile
     * @param insuranceName insurance name of the user logged-in in the session
     */
    private void setInsuranceNameLabel(String insuranceName) {
        insuranceNameLabel.setText(insuranceName);
    }

    /**
     * set the insurance type on the users profile
     * @param privateInsurance insurance type of the user logged-in in the session
     */
    private void setPrivateInsuranceLabel(boolean privateInsurance) {
        privateInsuranceLabel.setText(privateInsurance ? "Yes" : "No");
    }

    /**
     * Method executed when one of the make appointment Buttons are pressed.
     * Used to switch scene to appointment form.
     * @param event Trigger to invoke this method.
     * @throws IOException
     */
    public void handleMakeAppointmentButton(ActionEvent event) throws IOException {
        Session.appointment = new AppointmentInCreation();
        SceneSwitch.switchTo(event, "appointment/appointmentUser-view.fxml", "Make appointment");
    }

    /**
     * Switch to the edit of the users account information when button is pressed
     * @param event the event that causes the execution of the method
     * @throws IOException
     */
    public void handleEditButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "primary/primary-edit-view.fxml", "Edit information");
    }

    /**
     * Handle the pdf generation menu item and opens the file chooser if it is pressed
     * @param event the event that causes the execution of the method
     * @throws SQLException Throws Exception during connection issues.
     * @throws IOException
     * @see CreatePDF for explanation of the exception
     */
    public void handlepdfButton(ActionEvent event) throws SQLException, IOException {
        String dest = fileChooserSave.chooseDest(Main.getPrimaryStage());
        CreatePDF.create_Pdf(dest, false);
    }

    /**
     * Call the logout method of the Session class to log out the user when the logout menu item is pressed
     * @param event the event that causes the execution of the method
     * @throws IOException
     */
    public void handleLogout(ActionEvent event) throws IOException {
        Session.logout();
    }

    /**
     * Switch the scene to the management of the health information when the menu item is pressed
     * @param event the event that causes the execution of the method
     * @throws IOException
     */
    public void handleHealthInformation(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(null, "healthInformation/healthInformation-view.fxml", "Health information");
    }
}
