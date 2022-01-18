package com.ehealthsystem.healthinformation;

import java.time.LocalDate;

public class HealthInformation {

    private String ICD, disease, medication;

    public HealthInformation(String ICD, String disease, String medication) {
        this.ICD = ICD;
        this.disease = disease;
        this.medication = medication;
    }

    public String getICD() {
        return ICD;
    }

    public String getDisease() {
        return disease;
    }

    public String getMedication() {
        return medication;
    }

    public void setICD(String ICD) {
        this.ICD = ICD;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

}
