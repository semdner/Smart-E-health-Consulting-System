package com.ehealthsystem.appointment;

import com.ehealthsystem.healthInformation.HealthInformation;
import com.ehealthsystem.map.DoctorDistance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class AppointmentInCreation {
    public ArrayList<DoctorDistance> doctorList = new ArrayList<>();
    //Data that will be stored in the final appointment
    private String healthProblem;
    private LocalDate date;
    private LocalTime time;

    //Data that is only used to save the creation progress
    private double distance = -1;
    private String specialization;
    private String healthProblemChoice;
    private ArrayList<HealthInformation> healthInformation = new ArrayList<>();

    public AppointmentInCreation() {
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

    public void setHealthProblemChoice(String healthProblemChoice){this.healthProblemChoice = healthProblemChoice;}

    public String getHealthProblemChoice(){return healthProblemChoice;}

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
