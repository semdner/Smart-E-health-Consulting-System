package com.ehealthsystem.tools;

import com.ehealthsystem.appointment.AppointmentInCreation;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.map.GeoCoder;
import com.ehealthsystem.user.User;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.sql.SQLException;

public class Session {
    public static User user; //user that is currently logged in
    public static AppointmentInCreation appointment; //an appointment that is currently created
    private static GeocodingResult userGeo;
    public static final String datePatternUI = "d.M.yyyy"; //date pattern used in the UI
    public static final String timePatternUI = "H:mm"; //time pattern used in the UI

    /**
     * To be called on login to load user object for the user to be called by further methods
     * @param email the email of the user to log in
     * @throws SQLException
     */
    public static void loginUser(String email) throws SQLException {
        user = Database.getUserFromEmail(email);
    }

    public static GeocodingResult getUserGeo() throws IOException, InterruptedException, ApiException {
        if (userGeo == null) //lazy-load, store
            userGeo = GeoCoder.geocode(Session.user.getFormattedAddress());
        return userGeo;
    }

    public static void invalidateUserGeo() {
        userGeo = null;
    }
}
