package com.ehealthsystem.tools;

import com.ehealthsystem.admin.Admin;
import com.ehealthsystem.appointment.AppointmentInCreation;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.map.GeoCoder;
import com.ehealthsystem.user.User;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class Session {
    public static User user; //user that is currently logged in
    public static Admin admin;
    public static AppointmentInCreation appointment; //an appointment that is currently created
    private static GeocodingResult userGeo;
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy"); //date pattern used in the UI
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm"); //time pattern used in the UI
    public static final DateTimeFormatter timeFormatterForSchedule = DateTimeFormatter.ofPattern("HH:mm"); //time pattern used for the schedule grid pane

    /**
     * To be called on login to load user object for the user to be called by further methods
     * @param email the email of the user to log in
     * @throws SQLException
     */
    public static void loginUser(String email) throws SQLException, UnsupportedDataTypeException {
        user = Database.getUserFromEmail(email);
    }

    public static void loginAdmin(String name) throws SQLException, UnsupportedDataTypeException {
        admin = Database.getAdmin(name);
    }

    public static GeocodingResult getUserGeo() throws IOException, InterruptedException, ApiException {
        if (userGeo == null) //lazy-load, store
            userGeo = GeoCoder.geocode(Session.user.getFormattedAddress());
        return userGeo;
    }

    public static void invalidateUserGeo() {
        userGeo = null;
    }

    public static void logout() throws IOException {
        user = null;
        admin = null;
        appointment = null;
        invalidateUserGeo();
        SceneSwitch.switchToCentered(null, "main/main-view.fxml", "Welcome"); //hack with custom event
    }
}
