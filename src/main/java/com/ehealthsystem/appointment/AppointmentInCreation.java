package com.ehealthsystem.appointment;

import com.ehealthsystem.healthInformation.HealthInformation;
import com.ehealthsystem.map.DoctorDistance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Class for storing the creation progress of an Appointment by the user.
 */
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

    /**
     * Sets the date of the appointment.
     * @param date the date of the appointment.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the date of the appointment.
     * @return The time as an object of the type LocalDate.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the time of the appointment
     * @param time the time of the appointment
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Returns the time of the appointment.
     * @return The time as an object of the type LocalTime.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the health problem of the user.
     * @param healthProblem the health problem of the user as a string.
     */
    public void setHealthProblem(String healthProblem) {
        this.healthProblem = healthProblem;
    }

    /**
     * Returns the healthproblem of the user.
     * @return The healthproblem as a String.
     */
    public String getHealthProblem() {
        return healthProblem;
    }

    /**
     * Sets the specialization of the doctor.
     * @param specialization the specialization of the doctor.
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * Return the specialization of the doctor.
     * @return The specialization as a String.
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Sets the maximum distance between doctor and user.
     * @param distance the distance in km between doctor and user.
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Returns the saved maximum distance to a Doctor.
     * @return The distance as double.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Sets the health information of a user.
     * @param healthInformation the list in which the health information of a user is stored.
     */
    public void setHealthInformation(ArrayList<HealthInformation> healthInformation) {
        this.healthInformation = healthInformation;
    }

    /**
     * Returns the health information of the user as a List.
     * @return The health information as an Arraylist.
     */
    public ArrayList<HealthInformation> getHealthInformation() {
        return healthInformation;
    }

    /**
     * Sets the healthproblem of the user.
     * @param healthProblemChoice the healthproblem of the user.
     */
    public void setHealthProblemChoice(String healthProblemChoice){this.healthProblemChoice = healthProblemChoice;}

    /**
     * Returns the healthproblem chosen by the user.
     * @return The healthproblemchoice as a String.
     */
    public String getHealthProblemChoice(){return healthProblemChoice;}

}
