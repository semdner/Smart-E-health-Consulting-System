package com.ehealthsystem.appointment;

import com.ehealthsystem.healthinformation.HealthInformation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Appointment {
    String healthProblem;
    LocalDate date;
    LocalTime time;
    double distance;
    String specialization;
    ArrayList<HealthInformation> healthInformation = new ArrayList<>();

    public Appointment() {
    }

    public Appointment(ArrayList<HealthInformation> healthInformation) {
        this.healthInformation = healthInformation;
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
        this.healthInformation = healthInformation;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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
