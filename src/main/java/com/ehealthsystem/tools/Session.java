package com.ehealthsystem.tools;

import com.ehealthsystem.appointment.AppointmentInCreation;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.user.User;
import com.google.maps.model.GeocodingResult;

import java.sql.SQLException;

public class Session {
    public static User user; //user that is currently logged in
    public static AppointmentInCreation appointment; //an appointment that is currently created
    public static GeocodingResult userGeo;

    /**
     * To be called on login to load user object for the user to be called by further methods
     * @param email the email of the user to log in
     * @throws SQLException
     */
    public static void loginUser(String email) throws SQLException {
        user = Database.getUserFromEmail(email);
    }
}
