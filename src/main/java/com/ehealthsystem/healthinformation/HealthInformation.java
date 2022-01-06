package com.ehealthsystem.healthinformation;

public class HealthInformation {

    private String preExistingConditions, allergies, medications;


    /**
     *
     * @param preExistingConditions
     * @param allergies
     * @param medications
     */
    public HealthInformation(String preExistingConditions, String allergies, String medications) {
        this.preExistingConditions = preExistingConditions;
        this.allergies = allergies;
        this.medications = medications;
    }

    String getPreExistingConditions() {
        return preExistingConditions;
    }

    String getAllergies() {
        return allergies;
    }

    String getMedications() {
        return medications;
    }
}
