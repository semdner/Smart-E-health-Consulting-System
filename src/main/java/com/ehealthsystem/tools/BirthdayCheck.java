package com.ehealthsystem.tools;

import java.time.LocalDate;
import java.time.Period;

public class BirthdayCheck {
    public static final int MINIMUM_AGE = 18;

    public static boolean isOldEnough(LocalDate birthday) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthday, today);
        return period.getYears() >= MINIMUM_AGE;
    }
}
