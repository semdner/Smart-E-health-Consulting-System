package com.ehealthsystem.tools;

/**
 * Method for reminding the user about his appointment.
 */
public enum ReminderTime { //https://stackoverflow.com/q/27801119/18039017
    /**
     * The user can choose the time he wants to be reminded before his appointment
     */
    NO_REMINDER("No reminder", 0),
    TEN_MINUTES("10 minutes", 10),
    ONE_HOUR("1 hour", 60),
    THREE_DAYS("3 days", 3*24*60),
    ONE_WEEK("1 week", 7*24*60);

    private String label;
    private int minutes;

    /**
     * Constructor to set the time for the reminding
     * @param label The time as a String/Enum constant.
     * @param minutes The time in minutes
     */
    ReminderTime(String label, int minutes) {
        this.label = label;
        this.minutes = minutes;
    }

    /**
     * Gets the name of this enum constant.
     * @return Name of the enum constant.
     */
    public String toString() {
        return label;
    }

    /**
     * Gets the minutes.
     * @return The time in minutes.
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Gets the name of this enum constant.
     * @return Name of the enum constant.
     */
    public String getLabel() {
        return label;
    }
}
