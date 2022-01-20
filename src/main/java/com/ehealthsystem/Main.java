package com.ehealthsystem;

import com.ehealthsystem.database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main extends Application {
    /**
     *  Required method to run and open the first JavaFX window of an application.
     *
     * @param primaryStage the main window/stage the scene is loaded into
     * @throws IOException needs to throw an IOException in case the .fxml file to open isn't found
     */
    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        initDB();

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
     * TODO To be replaced with GUI dialog
     * @return desired admin password entered by the user
     */
    private static String initialAdminPassword()
    {
        System.out.println("Please enter your desired admin password: ");

        Scanner in = new Scanner(System.in);
        String password = in.nextLine();
        in.close();

        return password;
    }
}
