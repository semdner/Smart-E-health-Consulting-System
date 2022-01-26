package com.ehealthsystem.tools;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitch {
    public static void switchTo(Event event, String file, String title) throws IOException {
        Parent root = FXMLLoader.load(SceneSwitch.class.getResource("/com/ehealthsystem/" + file));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public static void switchToCentered(Event event, String file, String title) throws IOException {
        switchTo(event, file, title);
        centerWindow(event);
    }

    public static void centerWindow(Event event) {
        // center window (works on Windows, not on Chrome OS)
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
