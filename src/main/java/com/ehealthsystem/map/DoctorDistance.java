package com.ehealthsystem.map;

import com.ehealthsystem.doctor.Doctor;
import com.google.maps.model.LatLng;

public class DoctorDistance {
    double distance;
    LatLng location;
    String geoData;
    Doctor doctor;

    public DoctorDistance() {

    }

    public DoctorDistance(double distance, LatLng location, String geodata, int doctorId, String firstName, String lastName, String street, String number, int zip) {
        this.distance = distance;
        this.location = location;
        this.geoData = geodata;
        this.doctor = new Doctor(doctorId, firstName, lastName, street, number, zip);
    }

    public double getDistance() {
        return distance;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getGeoData() {
        return geoData;
    }

    public Doctor getDoctor() {
        return doctor;
    }

}
