package com.ehealthsystem.healthInformation;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * managed the deleting and creation of the user health information
 */
public class HealthInformationController implements Initializable {

    /**
     * all attributes with a fx:id assigned in the view
     */
    @FXML
    TableView<HealthInformationTableView> healthTableView;

    @FXML
    TableColumn<HealthInformationTableView, String> ICD;

    @FXML
    TableColumn<HealthInformationTableView, String> healthProblem;

    @FXML
    Button deleteButton;

    @FXML
    ChoiceBox healthChoiceBox;

    /**
     * represents the selected row to determine which row needs to be deleted when event is triggered
     */
    HealthInformationTableView selectedRow = new HealthInformationTableView(null, null);

    /**
     * The first method that is called when scene is switched.
     * Used to fill the choice box and load the users health problems.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ArrayList<String> choices = Database.getDisease();
            healthChoiceBox.getItems().addAll(choices);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // genderBox.getItems().addAll(choices);
        try {
            loadHealthProblem();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the users health problems from the database and store them als Observable
     * @throws SQLException
     */
    private void loadHealthProblem() throws SQLException {
        ObservableList<HealthInformationTableView> healthProblems = Database.getHealthInformationForTableView(Session.user.getUsername());
        ICD.setCellValueFactory(new PropertyValueFactory<HealthInformationTableView, String>("ICD"));
        healthProblem.setCellValueFactory(new PropertyValueFactory<HealthInformationTableView, String>("healthProblem"));
        healthTableView.setItems(healthProblems);
        healthTableView.setOnMousePressed((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedRow = healthTableView.getSelectionModel().getSelectedItem();
            }
        });
    }

    /**
     * deletes a users health problem (a row) in the Tableview
     * @param event the event that triggered the method
     * @throws SQLException
     * @throws IOException
     */
    public void handleDeleteButton(ActionEvent event) throws SQLException, IOException {
        if(selectedRow.getICD() != null && selectedRow.getHealthProblem() != null) {
            Database.deleteHealthInformation(selectedRow, Session.user.getUsername());
            SceneSwitch.switchTo(null, "healthInformation/healthInformation-view.fxml", "Health information");
        }
    }

    /**
     * Adds a health problem to the user
     * @param event the event that triggered the method
     * @throws SQLException
     * @throws IOException
     */
    public void handleAddButton(ActionEvent event) throws SQLException, IOException {
        if((String)healthChoiceBox.getValue() != null) {
            Database.addHealthInformation((String) healthChoiceBox.getValue(), Session.user.getUsername());
            SceneSwitch.switchTo(null, "healthInformation/healthInformation-view.fxml", "Health information");
        }
    }

    /**
     * switches back to the primary view
     * @param event the event that triggered the method
     * @throws IOException
     */
    public void handleBackButton(ActionEvent event) throws IOException {
        SceneSwitch.switchTo(event, "primary/primary-view.fxml", "E-Health System");
    }
}
