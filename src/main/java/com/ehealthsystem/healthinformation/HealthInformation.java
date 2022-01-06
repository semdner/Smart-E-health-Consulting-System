package com.ehealthsystem.healthinformation;

public class HealthInformation {

    private String preExistingConditions, allergies, pastTreatments, currentTreatments, medications;


    /**
     *
     * @param preExistingConditions
     * @param allergies
     * @param pastTreatments
     * @param currentTreatments
     * @param medications
     */
    public HealthInformation(String preExistingConditions, String allergies, String pastTreatments, String currentTreatments, String medications) {
        this.preExistingConditions = preExistingConditions;
        this.allergies = allergies;
        this.pastTreatments = pastTreatments;
        this.currentTreatments = currentTreatments;
        this.medications = medications;
    }
}
