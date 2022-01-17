package com.ehealthsystem.map;

import com.ehealthsystem.doctor.Doctor;

public class DoctorDistance {
    double distance;
    String geoData;
    Doctor doctor;

    public DoctorDistance() {

    }

    public DoctorDistance(double distance, String geodata, String firstName, String lastName, String street, String number, int zip) {
        this.distance = distance;
        this.geoData = geodata;
        this.doctor = new Doctor(firstName, lastName, street, number, zip);
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
