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
            loadUser(users.get(i), row);
            row++;
        }
    }

    private void loadUser(User user, int row) throws SQLException {
        ArrayList<Label> labels = createLabel(user);
        ArrayList<Button> buttons = createButton(user);
        loadTable(labels, buttons, row);
    }

    private ArrayList<Label> createLabel(User user) throws SQLException {
        ArrayList labels = new ArrayList<>();
        labels.add(new Label(user.getUsername()));
        labels.add(new Label(user.getMail()));
        labels.add( new Label(user.getFirstName()));
        labels.add(new Label(user.getLastName()));
        labels.add(new Label(user.getStreet()));
        labels.add(new Label(user.getHouseNumber()));
        labels.add(new Label(user.getZipCode()));
        labels.add(new Label(user.getBirthDate().toString()));
        labels.add(new Label(user.getGender()));
        labels.add(new Label(user.getHashedPassword(user.getMail())));
        return labels;
    }

    private ArrayList<Button> createButton(User user) {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(new Button());
        buttons.add(new Button());
        buttons.add(new Button());
        buttons.add(new Button());
        buttons.add(new Button());
        buttons.add(new Button());
        buttons.add(new Button());
        buttons.add(new Button());
        buttons.add(new Button());
        buttons.add(new Button());
        return buttons;
    }

    public void loadTable(ArrayList<Label> labels, ArrayList<Button> buttons, int row) {
        for (int i = 0; i < 10; i++) {
            userGridPane.add(labels.get(i), i, row);
        }
    }
}
