package com.ehealthsystem.tools;

import java.time.LocalDate;
import java.time.Period;

public class BirthdayCheck {
    public static final int MINIMUM_AGE = 18;

    /**
     * Check if a date of birth fulfills system's certain defined minimum age requirement
     * @param birthday the date of birth to check
     * @return if the date of birth fulfills the requirement
     */
    public static boolean isOldEnough(LocalDate birthday) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthday, today);
        return period.getYears() >= MINIMUM_AGE;
    }
}
