package com.ehealthsystem.tools;

/**
 * Enumeration of all possible reminder times for an appointment.
 */
public enum ReminderTime { //https://stackoverflow.com/q/27801119/18039017
    /**
     * The user can choose the time they want to be reminded at for their appointment.
     */
    NO_REMINDER("No reminder", 0),
    TEN_MINUTES("10 minutes", 10),
    ONE_HOUR("1 hour", 60),
    THREE_DAYS("3 days", 3*24*60),
    ONE_WEEK("1 week", 7*24*60);

    private final String label;
    private final int minutes;

    /**
     * Constructor to set details for a reminder option
     * @param label Label for the reminder time
     * @param minutes The reminder time in minutes
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
     * Get the reminder time, written out as spoken.
     * @return string describing the time
     */
    public String getLabel() {
        return label;
    }
}
