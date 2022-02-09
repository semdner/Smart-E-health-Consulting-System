package com.ehealthsystem.tools;

import com.ehealthsystem.Main;
import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.appointment.AppointmentMadeController;
import com.google.maps.errors.ApiException;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class SceneSwitch {
    /**
     * Switch to another scene
     * @param event the JavaFX event from the controller's handler method for the trigger (e.g. button)
     * @param file the FXML file for the next scene
     * @param title the window title to display
     * @throws IOException if the file was not found
     */
    public static void switchTo(Event event, String file, String title) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneSwitch.class.getResource("/com/ehealthsystem/" + file)));
        Stage stage = event == null ? Main.getPrimaryStage() : (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch to another scene and centering the window
     * @param event the JavaFX event from the controller's handler method for the trigger (e.g. button)
     * @param file the FXML file for the next scene
     * @param title the window title to display
     * @throws IOException if the file was not found
     */
    public static void switchToCentered(Event event, String file, String title) throws IOException {
        Stage stage = event == null ? Main.getPrimaryStage() : (Stage)((Node)event.getSource()).getScene().getWindow(); //grab current stage before it's replaced at scene switch
        switchTo(event, file, title);
        centerWindow(stage);
    }

    /**
     * Center the window
     * @param stage the JavaFX window to center
     */
    public static void centerWindow(Stage stage) {
        // center window (works on Windows, not on Chrome OS)
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * Switches scene and displays the appointment the user has made.
     * @param event Trigger to invoke this method.
     * @param appointment The appointment made by the user.
     */
    public static void loadAppointmentMadeView(Event event, Appointment appointment) throws IOException, SQLException, InterruptedException, ApiException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/ehealthsystem/appointment/appointmentMade-view.fxml"));
        Parent root = loader.load();

        AppointmentMadeController controller = loader.getController();
        controller.start(appointment);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene primaryScene = new Scene(root);
        stage.setTitle("Appointment");
        stage.setScene(primaryScene);
    }
}
