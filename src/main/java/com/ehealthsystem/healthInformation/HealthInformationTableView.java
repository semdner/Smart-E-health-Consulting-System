package com.ehealthsystem.healthInformation;

import javafx.beans.property.SimpleStringProperty;

public class HealthInformationTableView {
    private SimpleStringProperty ICD, healthProblem;

    public HealthInformationTableView(String ICD, String healthProblem) {
        this.ICD = new SimpleStringProperty(ICD);
        this.healthProblem = new SimpleStringProperty(healthProblem);
    }

    public void setICD(String ICD) {
        this.ICD.set(ICD);
    }

    public void setHealthProblem(String healthProblem) {
        this.healthProblem.set(healthProblem);
    }

    public String getICD() {
        return ICD.get();
    }

    public String getHealthProblem() {
        return healthProblem.get();
    }


}
