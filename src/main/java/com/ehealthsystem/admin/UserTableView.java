package com.ehealthsystem.admin;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class UserTableView {
    private SimpleStringProperty username, email, firstName, lastName, street, houseNo, gender;
    private SimpleStringProperty zipCode;
    private SimpleStringProperty birthDate;
    private SimpleStringProperty insuranceName;
    private SimpleBooleanProperty privateInsurance;
    private SimpleStringProperty password;

    public UserTableView(String username, String email,  String firstName, String lastName, String birthDate, String gender, String street, String houseNo, String zipCode, String insuranceName, Boolean privateInsurance, String password) {
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.birthDate = new SimpleStringProperty(birthDate);
        this.gender = new SimpleStringProperty(gender);
        this.street = new SimpleStringProperty(street);
        this.houseNo = new SimpleStringProperty(houseNo);
        this.zipCode = new SimpleStringProperty(zipCode);
        this.insuranceName = new SimpleStringProperty(insuranceName);
        this.privateInsurance = new SimpleBooleanProperty(privateInsurance);
        this.password = new SimpleStringProperty(password);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getUsername() {
        return username.get();
    }

    public String getEmail() {
        return email.get();
    }
    public String getFirstName() {
        return firstName.get();
    }
    public String getLastName() {
        return lastName.get();
    }
    public String getBirthDate() {
        return birthDate.get();
    }
    public String getGender() {
        return gender.get();
    }
    public String getStreet() {
        return street.get();
    }
    public String getHouseNo() {
        return houseNo.get();
    }
    public String getZipCode() {
        return zipCode.get();
    }
    public String getInsuranceName() {
        return insuranceName.get();
    }

    public Boolean getPrivateInsurance() {
        return privateInsurance.get();
    }

    public String getPassword() {
        return password.get();
    }

}
