package com.ehealthsystem.appointment;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.healthinformation.HealthInformation;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Appointment {
    String email, healthProblem;
    LocalDate date;
    LocalTime time;
    double distance;
    String specialization;
    ArrayList<HealthInformation> healthInformation = new ArrayList<>();

    public Appointment(String email) {
        this.email = email;
    }

    public Appointment(ArrayList<HealthInformation> healthInformation) {
        this.healthInformation.addAll(healthInformation);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setHealthProblem(String healthProblem) {
        this.healthProblem = healthProblem;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setHealthInformation(ArrayList<HealthInformation> healthInformation) {
        this.healthInformation.addAll(healthInformation);
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public double getDistance() {
        return distance;
    }
}
