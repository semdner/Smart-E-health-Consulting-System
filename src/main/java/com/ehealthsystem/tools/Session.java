package com.ehealthsystem.tools;

import com.ehealthsystem.admin.Admin;
import com.ehealthsystem.appointment.AppointmentInCreation;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.map.Geocoder;
import com.ehealthsystem.user.User;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 * Class to manage a Session of a logged-in user/admin.
 */
public class Session {
    //Attributes required for a session of a user
    /**
     * User that is currently logged in
     */
    public static User user;
    public static Admin admin;
    /** An appointment that is currently created */
    public static AppointmentInCreation appointment;
    private static GeocodingResult userGeo;
    /** date pattern used in the UI */
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    /** time pattern used in the UI */
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
    /** time pattern used for the schedule grid pane */
    public static final DateTimeFormatter timeFormatterForSchedule = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * To be called on login to load user object for the user to be called by further methods
     * @param email the email of the user to log in
     * @throws SQLException Throws Exception during connection issues.
     */
    public static void loginUser(String email) throws SQLException, UnsupportedDataTypeException {
        user = Database.getUserByUsernameOrEmail(email);
    }

    /**
     * Called when the admin logs-in.
     * A new object of the type admin is created,that represents the logged in admin.
     * @param name Name of the admin.
     */
    public static void loginAdmin(String name) /* throws SQLException, UnsupportedDataTypeException */{
        admin = new Admin("admin");
    }

    /**
     * return the user´s geolocation, determined using Google Maps
     * @return GeoCodingResult of user
     * @throws ApiException if the Google Maps API couldn't be connected to
     */
    public static GeocodingResult getUserGeo() throws IOException, InterruptedException, ApiException {
        if (userGeo == null) //lazy-load, store
            userGeo = Geocoder.geocode(Session.user.getFormattedAddress());
        return userGeo;
    }

    /**
     * Delete cached information of user's geolocation.
     * Called for example when they change their address in their personal information section.
     */
    public static void invalidateUserGeo() {
        userGeo = null;
    }

    /**
     * Logout the session's user by setting the attribute to null and showing the main view (login, register)
     */
    public static void logout() throws IOException {
        user = null;
        admin = null;
        appointment = null;
        invalidateUserGeo();
        SceneSwitch.switchToCentered(null, "main/main-view.fxml", "Welcome"); //hack with custom event
    }
}
