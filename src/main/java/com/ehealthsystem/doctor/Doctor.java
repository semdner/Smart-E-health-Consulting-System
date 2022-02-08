package com.ehealthsystem.doctor;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.HasAddress;
import com.google.maps.model.LatLng;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Represents the Doctor and his attributes
 */
public class Doctor implements HasAddress {
    int id;
    String firstName, lastName, street, number;
    String zip;
    LatLng location;

    /**
     * ???
     */
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

    /**
     * Returns the ID of the Doctor.
     * @return The ID as an integer.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the first name of the Doctor.
     * @return The first name as a String.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the Doctor.
     * @return The last name as a String.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the address of the doctor´s office.
     * @return The address as a String.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Returns the house no. of the doctor´s office.
     * @return The house no. as a String.
     */
    public String getHouseNumber() {
        return number;
    }

    /**
     * Returns the zip code of the doctor´s office.
     * @return The zip code as a String.
     */
    public String getZipCode() {
        return zip;
    }

    /**
     * Returns the location of doctor´s office from google maps.
     * @return The location as a coordinate.
     */
    public LatLng getLocation() {
        return location;
    }

    /**
     * Returns a List of the specialization of a doctor.
     * @return The specialization as an ArrayList.
     * @throws SQLException If connection issues with the Database.
     */
    public ArrayList<String> getSpecializations() throws SQLException {
        return Database.loadDoctorSpecializations(id); //lazy-load and also don't store
    }
}
