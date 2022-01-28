package com.ehealthsystem.tools;

public enum ReminderTime { //https://stackoverflow.com/q/27801119/18039017
    NO_REMINDER("No reminder", 0),
    TEN_MINUTES("10 minutes", 10),
    ONE_HOUR("1 hour", 60),
    THREE_DAYS("3 days", 3*24*60),
    ONE_WEEK("1 week", 7*24*60);

    private String label;
    private int minutes;

    ReminderTime(String label, int minutes) {
        this.label = label;
        this.minutes = minutes;
    }

    public String toString() {
        return label;
    }

    public int getMinutes() {
        return minutes;
    }
}
