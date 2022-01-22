package com.ehealthsystem.appointment;

import com.ehealthsystem.healthinformation.HealthInformation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AppointmentInCreation {
    private String healthProblem;
    private LocalDate date;
    private LocalTime time;
    private double distance = -1;
    private String specialization;
    private ArrayList<HealthInformation> healthInformation = new ArrayList<>();

    public AppointmentInCreation() {
    }

    public AppointmentInCreation(ArrayList<HealthInformation> healthInformation) {
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

    public String getHealthProblem() {
        return healthProblem;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setHealthInformation(ArrayList<HealthInformation> healthInformation) {
        this.healthInformation = healthInformation;
    }

    public ArrayList<HealthInformation> getHealthInformation() {
        return healthInformation;
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
