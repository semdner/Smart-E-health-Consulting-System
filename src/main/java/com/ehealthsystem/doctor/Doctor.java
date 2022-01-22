package com.ehealthsystem.doctor;

public class Doctor {
    int id;
    String firstName, lastName, street, number;
    String[] specialization;
    int zip;

    public Doctor() {

    }

    public Doctor(int id, String firstName, String lastName, String street, String number, int zip) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.number = number;
        this.zip = zip;
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

    public int getZip() {
        return zip;
    }
}
