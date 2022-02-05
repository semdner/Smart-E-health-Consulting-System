package com.ehealthsystem.admin;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.Session;
import com.ehealthsystem.user.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.activation.UnsupportedDataTypeException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalTime;
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
    GridPane userGridPane;

    @FXML
    Label errorLabel;

    Label selectedLabel;
    int[] selectedCell = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUsersFromDatabase();
        } catch (SQLException | UnsupportedDataTypeException e) {
            e.printStackTrace();
        }
    }

    private void loadUsersFromDatabase() throws SQLException, UnsupportedDataTypeException {
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

    private void loadTable(ArrayList<Label> labels, ArrayList<Button> buttons, int row) {
        for (int i = 0; i < 10; i++) {
            userGridPane.add(labels.get(i), i, row);
            userGridPane.add(buttons.get(i), i, row);
            setStyle(buttons.get(i));
            handleButton(labels.get(i), buttons.get(i), i, row);
        }
    }

    private void handleButton(Label label, Button button, int column, int row) {
        // dynamically add the event handler for the buttons
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(selectedLabel != null) {
                    selectedLabel.setTextFill(Color.web("#000000"));
                }
                selectedLabel = label;
                selectedLabel.setTextFill(Color.web("#FF0000"));
                selectedCell = new int[]{column, row};
            }
        });
    }

    private static void setStyle(Button button) {
        button.setStyle("-fx-opacity: 0%");
        button.setPrefWidth(100);
    }

    public void handleEditButton(ActionEvent event) {
        if(selectedCell == null) {
            errorLabel.setVisible(true);
            return;
        } else {
            errorLabel.setVisible(false);
            cancelButton.setDisable(false);
            saveButton.setDisable(false);
            TextField editTextField = new TextField();
            editTextField.setText(selectedLabel.getText());
            userGridPane.add(editTextField, selectedCell[0], selectedCell[1]);
        }
    }
}
