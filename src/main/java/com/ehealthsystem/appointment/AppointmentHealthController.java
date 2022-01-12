package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.healthinformation.HealthInformation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.ehealthsystem.appointment.AppointmentUserController.email;

public class AppointmentHealthController implements Initializable {

    @FXML
    GridPane healthGridPane;

    @FXML
    Button continueButton;

    @FXML
    Button backButton;

    static String email;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadHealthInformation();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleContinueButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/appointmentInformation-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("E-Health-System");
        stage.setScene(primaryScene);
    }

    public void handleBackButton(ActionEvent event) throws IOException {
        AppointmentUserController.setEmail(email);
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/appointment/appointmentUser-view.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("make appointment");
        stage.setScene(primaryScene);
        stage.show();
    }

    private void loadHealthInformation() throws SQLException {
        ArrayList<HealthInformation> healthInformation = Database.getHealthInformation(email);
        fillHealthTable(healthInformation);
    }

    private void fillHealthTable(ArrayList<HealthInformation> healthInformation) {
        for(int i = 0; i<healthInformation.size(); i++) {
            Label ICD = new Label(healthInformation.get(i).getICD());
            Label health_problem  = new Label(healthInformation.get(i).getDisease());
            Label medication = new Label(healthInformation.get(i).getMedication());
            CheckBox selection = new CheckBox();
            setStyle(ICD, health_problem, medication, selection);

            healthGridPane.add(ICD, 0, i+1);
            healthGridPane.add(health_problem, 1, i+1);
            healthGridPane.add(medication, 2, i+1);
            healthGridPane.add(selection, 3, i+1);
        }
    }

    private void setStyle(Label ICD, Label health_problem, Label medication, CheckBox selection) {
        ICD.setStyle("-fx-font-size: 15px;");
        health_problem.setStyle("-fx-font-size: 15px;");
        medication.setStyle("-fx-font-size: 15px;");
        selection.setFocusTraversable(false);
    }

    public void handleSelectAllButton() {

    }

    public static void setEmail(String loginEmail) {
        email = loginEmail;
    }

}
