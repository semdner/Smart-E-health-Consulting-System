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
    TableView<User> userTableView;

    @FXML
    TableColumn<User, String> username;

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
        users = Database.getAllUsers();
        ObservableList<User> list = FXCollections.observableArrayList();
            for (User user : users) {
                list.add(user);
            }
        userTableView.setItems(list);
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
