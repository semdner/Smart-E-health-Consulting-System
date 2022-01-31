package com.ehealthsystem.doctor;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.HasAddress;
import com.google.maps.model.LatLng;

import java.sql.SQLException;
import java.util.ArrayList;

public class Doctor implements HasAddress {
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

    public String getHouseNumber() {
        return number;
    }

    public String getZipCode() {
        return zip;
    }

    public LatLng getLocation() {
        return location;
    }

    public ArrayList<String> getSpecializations() throws SQLException {
        return Database.loadDoctorSpecializations(id); //lazy-load and also don't store
    }
}
