package com.ehealthsystem.tools;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.user.User;

import java.sql.SQLException;

public class Session {
    public static User user; //user that is currently logged in
    public static Appointment appointment; //an appointment that is currently created

    public static void loginUser(String email) throws SQLException {
        user = Database.getUserFromEmail(email);
    }
}
