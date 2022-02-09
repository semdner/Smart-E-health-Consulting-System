package com.ehealthsystem;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.reminder.ReminderScheduler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

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
        primaryStage.getIcons().add(new Image("file:src/main/resources/com/ehealthsystem/icon/icon.png"));
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * Main entry point to the application for the operating system.
     * @param args Command arguments
     */
    public static void main(String[] args) throws SQLException {
        launch(args);
    }

    /**
     * Set up database
     * @throws SQLException if a statement failed in the creation process
     */
    private static void initDB() throws SQLException {
        if (new File(Database.fileName).isFile()) //DB file exists
            Database.init();
        else
            Database.createDB(initialAdminPassword());
    }

    /**
     * Ask the admin that opens the application for the first time
     * in a text input dialog
     * for an initial admin password when creating the database
     * @return desired admin password entered by the admin
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

    /**
     * Get the application's primary stage.
     *
     * As the application only works with a single stage of which the content is only changed on scene switch,
     * this is a helper method to get the application's stage.
     * Used to switch scenes.
     * Implemented in Main as this class initially defines the primary stage.
     * @return the primary stage the application runs on.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
