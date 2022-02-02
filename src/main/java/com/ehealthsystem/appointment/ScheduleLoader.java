package com.ehealthsystem.appointment;

import java.time.LocalTime;

public abstract class ScheduleLoader {
    protected LocalTime selectedTime;

    public void setSelectedTime(LocalTime selectedTime) {
        this.selectedTime = selectedTime;
    }
}
