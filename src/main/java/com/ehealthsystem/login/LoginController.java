package com.ehealthsystem.login;

import com.ehealthsystem.database.DatabaseController;
import com.ehealthsystem.primary.PrimaryController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {

    @FXML
    public TextField emailTextField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Button loginButton;

    @FXML
    public Label registrationLabel;
    public Label errorLabel;

    /**
     *
     * @param event event that triggered the login button
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void handleLoginButton(ActionEvent event) throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("/com/ehealthsystem/primary/primary-view.fxml"));
        Stage stage = (Stage)loginButton.getScene().getWindow();
        Scene primaryScene = new Scene(root, 1000, 600);
        stage.setTitle("E-Health System");
        stage.setScene(primaryScene);
        stage.show();

        String email = emailTextField.getText();
        Pattern emailPat = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);

        if(matcher.find() && !passwordField.getText().isBlank()) {
            validateCredentials();
        } else {
            errorLabel.setText("Invalid email format. Please try again");
            errorLabel.setVisible(true);
        }

    }

    public void validateCredentials() throws IOException, SQLException {

        String sqlCommand = "SELECT count(1) FROM users WHERE email = '" + emailTextField.getText() + "' AND pw = '" + passwordField.getText() + "'";
        DatabaseController connection = new DatabaseController();
        Statement statement = connection.getConnection().createStatement();

        try (ResultSet queryResult = statement.executeQuery(sqlCommand)) {
            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    errorLabel.setText("User found");
                    errorLabel.setVisible(true);
                } else {
                    errorLabel.setText("User not found");
                    errorLabel.setVisible(true);
                }
            }
        }

    }

    @FXML
    public void handleRegistrationLabel(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ehealthsystem/registration/registration-view.fxml"));
        Stage stage = (Stage)registrationLabel.getScene().getWindow();
        Scene primaryScene = new Scene(root, 350, 450);
        stage.setTitle("Create Account");
        stage.setScene(primaryScene);
        stage.show();

    }

}
