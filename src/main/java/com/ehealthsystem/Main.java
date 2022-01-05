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

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main/main-view.fxml"));
        Scene mainScene = new Scene(root, 350, 450);
        primaryStage.setTitle("welcome");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch(args);
    }
}
