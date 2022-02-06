package com.ehealthsystem.admin;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    Button editButton;

    @FXML
    Button cancelButton;

    @FXML
    Button saveButton;

    @FXML
    Button deleteButton;

    @FXML
    Label errorLabel;

    @FXML
    TableView<UserTableView> userTableView;

    @FXML
    TableColumn<UserTableView, String> username;

    @FXML
    TableColumn<UserTableView, String> email;

    @FXML
    TableColumn<UserTableView, String> firstName;

    @FXML
    TableColumn<UserTableView, String> lastName;

    @FXML
    TableColumn<UserTableView, String> birthday;

    @FXML
    TableColumn<UserTableView, String> gender;

    @FXML
    TableColumn<UserTableView, String> street;

    @FXML
    TableColumn<UserTableView, String> houseNo;

    @FXML
    TableColumn<UserTableView, String> zip;

    @FXML
    TableColumn<UserTableView, String> insuranceName;

    @FXML
    TableColumn<UserTableView, Boolean> privateInsurance;

    @FXML
    TableColumn<UserTableView, String> password;


    ArrayList<User> users;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUsersFromDatabase();
        } catch (SQLException | UnsupportedDataTypeException e) {
            e.printStackTrace();
        }
    }

    private void loadUsersFromDatabase() throws SQLException, UnsupportedDataTypeException {
        ObservableList<UserTableView> users = Database.getUserForTableView();
        username.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("username"));
        email.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("email"));
        firstName.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("lastName"));
        birthday.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("birthDate"));
        gender.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("gender"));
        street.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("street"));
        houseNo.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("houseNo"));
        zip.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("zipCode"));
        insuranceName.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("insuranceName"));
        privateInsurance.setCellValueFactory(new PropertyValueFactory<UserTableView, Boolean>("privateInsurance"));
        password.setCellValueFactory(new PropertyValueFactory<UserTableView, String>("password"));

        userTableView.setItems(users);
    }

    public void handleEditButton(ActionEvent event) {

    }

    public void handleCancelButton(ActionEvent event) throws IOException {

    }

    public void handleSaveButton(ActionEvent event) throws SQLException, IOException {

    }

    public void handleLogout(ActionEvent event) throws IOException {
        Session.logout();
    }
}
