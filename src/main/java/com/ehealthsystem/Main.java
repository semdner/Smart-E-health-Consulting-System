package com.ehealthsystem;

import com.ehealthsystem.database.DatabaseController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import com.ehealthsystem.MainController;

public class Main extends Application {
    /**
     *  Required method to run and open the first JavaFX window of an application.
     *
     * @param primaryStage the main window/stage the scene is loaded into
     * @throws IOException needs to throw an IOException in case the .fxml file to open isn't found
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("main/main-view.fxml"));
        Scene mainScene = new Scene(root, 350, 450);
        primaryStage.setTitle("welcome");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch(args);
    }
}
