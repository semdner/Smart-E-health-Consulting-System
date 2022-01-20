package com.ehealthsystem.tools;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
}
