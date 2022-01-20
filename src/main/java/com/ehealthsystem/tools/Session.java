package com.ehealthsystem.tools;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.user.User;

import java.sql.SQLException;

public class Session {
    public static User user;

    public static void loginUser(String email) throws SQLException {
        user = Database.getUserFromEmail(email);
    }
}
