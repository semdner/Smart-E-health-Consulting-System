package com.ehealthsystem.doctor;

import com.google.maps.model.LatLng;

public class Doctor {
    int id;
    String firstName, lastName, street, number;
    String zip;
    LatLng location;

    public Doctor() {

    }

    public Doctor(int id, String firstName, String lastName, String street, String number, String zip, LatLng location) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.number = number;
        this.zip = zip;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getZip() {
        return zip;
    }

    public LatLng getLocation() {
        return location;
    }
}
