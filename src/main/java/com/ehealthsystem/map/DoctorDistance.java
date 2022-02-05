package com.ehealthsystem.map;

import com.ehealthsystem.doctor.Doctor;

public class DoctorDistance {
    double distance;
    String geoData;
    Doctor doctor;

    public DoctorDistance() {

    }

    public DoctorDistance(double distance, String geodata, Doctor doctor) {
        this.distance = distance;
        this.geoData = geodata;
        this.doctor = doctor;
    }

    public double getDistance() {
        return distance;
    }

    public String getGeoData() {
        return geoData;
    }

    public Doctor getDoctor() {
        return doctor;
    }

}
