package com.ehealthsystem;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.reminder.ReminderScheduler;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class Main extends Application {
    static Stage primaryStage;

    /**
     *  Required method to run and open the first JavaFX window of an application.
     *
     * @param primaryStage the main window/stage the scene is loaded into
     * @throws IOException needs to throw an IOException in case the .fxml file to open isn't found
     */
    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        this.primaryStage = primaryStage;
        initDB();
        ReminderScheduler.setupReminders();

        Parent root = FXMLLoader.load(getClass().getResource("main/main-view.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    @Override
    public void stop(){
        Database.close();
    }

    public static void main(String[] args) throws SQLException {
        launch(args);
    }

    /**
     * Set up database
     * @throws SQLException
     */
    private static void initDB() throws SQLException {
        if (new File(Database.fileName).isFile()) //DB file exists
            Database.init();
        else
            Database.createDB(initialAdminPassword());
    }

    /**
     * Ask the user for an initial admin password when creating the database
     * @return desired admin password entered by the user
     */
    private static String initialAdminPassword()
    {
        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle("Setup");
        dialog.setHeaderText("Please enter your desired administrator password");
        dialog.setContentText("Password:");

        Optional<String> result = dialog.showAndWait();

        var input = new Object() {
            String value = "";
        };
        result.ifPresentOrElse(password -> input.value = password, () -> System.exit(0));

        return input.value;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
