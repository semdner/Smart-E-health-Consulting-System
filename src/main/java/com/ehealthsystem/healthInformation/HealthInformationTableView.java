package com.ehealthsystem.healthInformation;

import javafx.beans.property.SimpleStringProperty;

/**
 * Health information in a stored in an observable format
 */
public class HealthInformationTableView {
    private SimpleStringProperty ICD, healthProblem;

    /**
     * set ICD and health problem when creating an object
     * @param ICD
     * @param healthProblem
     */
    public HealthInformationTableView(String ICD, String healthProblem) {
        this.ICD = new SimpleStringProperty(ICD);
        this.healthProblem = new SimpleStringProperty(healthProblem);
    }

    /**
     * set the ICD
     * @param ICD
     */
    public void setICD(String ICD) {
        this.ICD.set(ICD);
    }

    /**
     * set the health problem
     * @param healthProblem
     */
    public void setHealthProblem(String healthProblem) {
        this.healthProblem.set(healthProblem);
    }

    /**
     * return the ICD
     * @return
     */
    public String getICD() {
        return ICD.get();
    }

    /**
     * return the health problem
     * @return
     */
    public String getHealthProblem() {
        return healthProblem.get();
    }
}
