package com.ehealthsystem.tools;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EmailCheck {
    /**
     * Check with a regular expression an email address for validity
     * @param email the email address to check
     * @return whether the email address is valid
     */
    public static boolean isValidEmailAddress(String email) {
        Pattern emailPat = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);
        return matcher.find();
    }
}
