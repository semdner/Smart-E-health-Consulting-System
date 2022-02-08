package com.ehealthsystem.map;

import com.ehealthsystem.doctor.Doctor;

public class DoctorDistance {
    double distance;
    String geoData;
    Doctor doctor;

    /**
     * Needed for the FoundDoctorController the doctor doesn't have any values at the beginning assigned
     */
    public DoctorDistance() {

    }

    /**
     * set the set distance from the doctor to the user, the geodata and the indication of which doctor is meant
     * @param distance distance from doctor to user
     * @param geodata geo data of the doctor
     * @param doctor doctor object which indicates which doctor
     */
    public DoctorDistance(double distance, String geodata, Doctor doctor) {
        this.distance = distance;
        this.geoData = geodata;
        this.doctor = doctor;
    }

    /**
     * get the distance from doctor to user
     * @return
     */
    public double getDistance() {
        return distance;
    }

    /**
     * get the doctors geodata
     * @return
     */
    public String getGeoData() {
        return geoData;
    }

    /**
     * get the doctor
     * @return
     */
    public Doctor getDoctor() {
        return doctor;
    }

}
