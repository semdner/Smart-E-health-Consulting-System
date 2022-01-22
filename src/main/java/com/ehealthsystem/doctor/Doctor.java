package com.ehealthsystem.doctor;

public class Doctor {
    String firstName, lastName, street, number;
    String[] specilization;
    int zip;

    public Doctor() {

    }

    public Doctor(String firstName, String lastName, String street, String number, int zip) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.number = number;
        this.zip = zip;
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

    public int getZip() {
        return zip;
    }
}
