package com.ehealthsystem.admin;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.activation.UnsupportedDataTypeException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    Button editButton;

    @FXML
    Button deleteButton;

    @FXML
    GridPane userGridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUsersFromDatabase();
        } catch (SQLException | UnsupportedDataTypeException e) {
            e.printStackTrace();
        }
    }

    public void loadUsersFromDatabase() throws SQLException, UnsupportedDataTypeException {
        ArrayList<User> users = Database.getAllUsers();
        int row = 1;
        for (int i = 1; i < users.size(); i++) {
            loadCurrentUser(users.get(i), row);
            row++;
        }
    }

    public void loadCurrentUser(User user, int row) throws SQLException {
        Label username = new Label(user.getUsername());
        Label email = new Label(user.getMail());
        Label firstName = new Label(user.getFirstName());
        Label lastName = new Label(user.getLastName());
        Label street = new Label(user.getStreet());
        Label houseNo = new Label(user.getHouseNumber());
        Label zip = new Label(user.getZipCode());
        Label birthday = new Label(user.getBirthDate().toString());
        Label gender = new Label(user.getGender());
        Label password = new Label(user.getHashedPassword(user.getMail()));
        userGridPane.add(username, 0, row);
        userGridPane.add(email, 1, row);
        userGridPane.add(firstName, 2, row);
        userGridPane.add(lastName, 3, row);
        userGridPane.add(street, 4, row);
        userGridPane.add(houseNo, 5, row);
        userGridPane.add(zip, 6, row);
        userGridPane.add(birthday, 7, row);
        userGridPane.add(gender, 8, row);
        userGridPane.add(password, 9, row);
    }

    public void setUserButtons() {
        Button usernameButton = new Button();
        Button emailButton = new Button();
        Button firstNameButton = new Button();
        Button lastNameButton = new Button();
        Button streetButton = new Button();
        Button houseNoButton = new Button();
        Button zipButton = new Button();
        Button birthdayButton = new Button();
        Button genderButton = new Button();
        Button passwordButton = new Button();
    }
}
