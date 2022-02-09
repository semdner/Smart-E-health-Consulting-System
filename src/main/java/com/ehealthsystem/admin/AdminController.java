package com.ehealthsystem.admin;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.BirthdayCheck;
import com.ehealthsystem.tools.EmailCheck;
import com.ehealthsystem.tools.SceneSwitch;
import com.ehealthsystem.tools.Session;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.BooleanStringConverter;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

/**
 * Controller class for admin-view.fxml.
 * Used to handle the interaction between user and program for the scene
 */
public class AdminController implements Initializable {

    /**
     * The defined with fx:id defined components of the admin-view
     */
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

    @FXML
    Button deleteButton;

    /**
     * The selected row to determine if and which row is currently selected
     */
    UserTableView selectedRow = new UserTableView(null, null, null, null, null, null, null, null, null, null, false, null);

    /**
     * First method when scene is switches used to call the needed function for loading user inforamtion
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUsersFromDatabase();
        } catch (SQLException  e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the users from the data base and store them in an observable.
     * Define which Column of the Tableview corresponds to which value of the attributes of the UserTableView class and fill the table
     * @throws SQLException throws sql exception if the method of causes one
     */
    private void loadUsersFromDatabase() throws SQLException {
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
        handleEdit();
        userTableView.setItems(users);
        userTableView.setOnMousePressed((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                selectedRow = userTableView.getSelectionModel().getSelectedItem();
            }
        });
    }

    /**
     * Add the Event Handler for dynamically for the columns
     */
    private void handleEdit() {
        username.setCellFactory(TextFieldTableCell.forTableColumn());
        username.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                user.setUsername(userTableViewStringCellEditEvent.getNewValue());
                Object[][] parameters = {{"username", user.getUsername()}};
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });


        email.setCellFactory(TextFieldTableCell.forTableColumn());
        email.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                if(EmailCheck.isValidEmailAddress(userTableViewStringCellEditEvent.getNewValue())) {
                    user.setEmail(userTableViewStringCellEditEvent.getNewValue());
                }
                Object[][] parameters = {{"email", user.getEmail()}};
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });

        firstName.setCellFactory(TextFieldTableCell.forTableColumn());
        firstName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                user.setFirstName(userTableViewStringCellEditEvent.getNewValue());
                Object[][] parameters = {{"first_name", user.getFirstName()}};
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });

        lastName.setCellFactory(TextFieldTableCell.forTableColumn());
        lastName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                user.setLastName(userTableViewStringCellEditEvent.getNewValue());
                Object[][] parameters = {{"last_name", user.getLastName()}};
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });

        birthday.setCellFactory(TextFieldTableCell.forTableColumn());
        birthday.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                LocalDate date = null;
                if(!userTableViewStringCellEditEvent.getNewValue().isBlank()){
                    try {
                        date = LocalDate.parse(userTableViewStringCellEditEvent.getNewValue(), DateTimeFormatter.ofPattern("d.M.yyyy"));
                    } catch (DateTimeParseException e) {
                        return;
                    }
                }
                
                if(!BirthdayCheck.isOldEnough(date)) {
                    return;
                }

                user.setBirthDate(userTableViewStringCellEditEvent.getNewValue());
                Object[][] parameters = {{"birthday", date.format(Database.dateFormatter)}};
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });

        gender.setCellFactory(TextFieldTableCell.forTableColumn());
        gender.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                switch(userTableViewStringCellEditEvent.getNewValue()) {
                    case "male" : user.setGender(userTableViewStringCellEditEvent.getNewValue()); break;
                    case "female": user.setGender(userTableViewStringCellEditEvent.getNewValue()); break;
                    case "other": user.setGender(userTableViewStringCellEditEvent.getNewValue()); break;
                    default: user.setGender(user.getGender()); break;
                }
                Object[][] parameters = {{"sex", user.getGender()}};
                System.out.println(user.getGender());
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });

        street.setCellFactory(TextFieldTableCell.forTableColumn());
        street.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                user.setStreet(userTableViewStringCellEditEvent.getNewValue());
                Object[][] parameters = {{"street", user.getStreet()}};
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });

        houseNo.setCellFactory(TextFieldTableCell.forTableColumn());
        houseNo.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                user.setHouseNo(userTableViewStringCellEditEvent.getNewValue());
                Object[][] parameters = {{"number", user.getHouseNo()}};
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });

        zip.setCellFactory(TextFieldTableCell.forTableColumn());
        zip.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                user.setZipCode(userTableViewStringCellEditEvent.getNewValue());
                Object[][] parameters = {{"zip", user.getZipCode()}};
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });

        insuranceName.setCellFactory(TextFieldTableCell.forTableColumn());
        insuranceName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                user.setInsuranceName(userTableViewStringCellEditEvent.getNewValue());
                Object[][] parameters = {{"insurance_name", user.getInsuranceName()}};
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });

        privateInsurance.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        privateInsurance.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, Boolean> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                String identifier = user.getUsername();
                user.setPrivateInsurance(userTableViewStringCellEditEvent.getNewValue());
                Object[][] parameters = {{"private_insurance", user.getPrivateInsurance()}};
                try {
                    Database.update("user", parameters, new Object[][]{{"username", identifier}});
                } catch (SQLException | UnsupportedDataTypeException e) {
                    e.printStackTrace();
                }
            }
        });

        password.setCellFactory(TextFieldTableCell.forTableColumn());
        password.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserTableView, String> userTableViewStringCellEditEvent) {
                UserTableView user = userTableViewStringCellEditEvent.getRowValue();
                try {
                    user.setPassword(userTableViewStringCellEditEvent.getNewValue());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Deltes a selected user from the database if the delete button is clicked
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void handleDeleteButton(ActionEvent event) throws IOException, SQLException {
        if(selectedRow.getUsername() != null) {
            Database.deleteUserInformation(selectedRow, selectedRow.getUsername());
            SceneSwitch.switchToCentered(event, "admin/admin-view.fxml", "Admin Panel");
        }
    }

    /**
     * logs the admin out
     * @param event
     * @throws IOException
     */
    public void handleLogout(ActionEvent event) throws IOException {
        Session.logout();
    }
}
